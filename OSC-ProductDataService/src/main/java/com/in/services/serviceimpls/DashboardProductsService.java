package com.in.services.serviceimpls;

import com.in.dtos.CategoryDetailsDTO;
import com.in.dtos.ProductDetailsDTO;
import com.in.kafka.avro.category.CategoryDetails;
import com.in.kafka.avro.product.ProductDetails;
import com.in.kafka.avro.product.ProductViewCount;
import com.in.mappers.CategoryDataMapper;
import com.in.mappers.ProductDataMapper;
import com.in.proto.product.ProductsViewHistoryGrpc;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardProductsService {
    private static final Logger log = LogManager.getLogger(DashboardProductsService.class);

    private final ProductsViewHistoryGrpc.ProductsViewHistoryBlockingStub recentlyViewedHistoryStub;
    private final ReadOnlyKeyValueStore<String, ProductViewCount> viewCountStore;
    private final ReadOnlyKeyValueStore<String, CategoryDetails> categoryStore;
    private final ReadOnlyKeyValueStore<String, ProductDetails> productStore;

    public List<ProductDetailsDTO> getRecentlyViewedProducts(List<String> recentlyViewedProductIds) {
        log.info("Fetching recently viewed products for productIds: {}", recentlyViewedProductIds);
        return recentlyViewedProductIds.stream()
                .map(productId -> {
                    ProductDetails productDetails = productStore.get(productId);
                    if (productDetails != null) {
                        log.debug("Product found: {}", productId);
                        return ProductDataMapper.avroToDto(productId, 0, productDetails);
                    } else {
                        log.warn("Product not found for productId: {}", productId);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<ProductDetailsDTO> getFeaturedProducts() {
        log.info("Fetching featured products based on view count...");
        KeyValueIterator<String, ProductViewCount> viewCountIterator = viewCountStore.all();
        Map<String, ProductViewCount> viewCountMap = new HashMap<>();

        while (viewCountIterator.hasNext()) {
            KeyValue<String, ProductViewCount> entry = viewCountIterator.next();
            viewCountMap.put(entry.key, entry.value);
        }

        //Sorting the map entries by view count in descending order
        log.debug("Sorting view count data...");
        return viewCountMap.entrySet().stream()
                .sorted((e1, e2) -> {
                    // Comparing by total view count in descending order
                    int countComparison = Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount());
                    if (countComparison != 0) {
                        return countComparison;
                    }
                    // If counts are equal, compare by category ID in ascending alphabetical order
                    return e1.getValue().getCategoryId().toString().compareTo(e2.getValue().getCategoryId().toString());
                })
                .map(entry -> {
                    ProductDetails product = productStore.get(entry.getKey());
                    if (product != null) {
                        log.debug("Featured product found: {}", entry.getKey());
                        return ProductDataMapper.avroToDto(entry.getKey(), entry.getValue().getViewCount(), product);
                    }
                    log.warn("No product found for productId: {}", entry.getKey());
                    return null;
                })
                .collect(Collectors.toList());
    }

    public List<CategoryDetailsDTO> getTopCategoriesList(int count) {
        log.info("Fetching top categories based on product view counts...");
        Map<String, Long> categoryCountMap = new HashMap<>();
        KeyValueIterator<String, ProductViewCount> productViewCountList = viewCountStore.all();

        // Calculate total view count for each category
        while (productViewCountList.hasNext()) {
            KeyValue<String, ProductViewCount> productViewCount = productViewCountList.next();
            String categoryId = productViewCount.value.getCategoryId().toString();
            categoryCountMap.put(categoryId, categoryCountMap.getOrDefault(categoryId, 0L) + productViewCount.value.getViewCount());
        }

        log.debug("Sorting categories by view count...");
        // Sorting categories first by total view count (descending)
        return categoryCountMap.entrySet().stream()
                .sorted((e1, e2) -> {
                    // Compare by total view count in descending order
                    int countComparison = Long.compare(e2.getValue(), e1.getValue());
                    if (countComparison != 0) {
                        return countComparison;
                    }
                    // If counts are equal, compare by category ID (ascending)
                    return e1.getKey().compareTo(e2.getKey());
                })
                .limit(count)
                .map(entry -> {
                    CategoryDetails categoryDetails = categoryStore.get(entry.getKey());
                    return CategoryDataMapper.avroToDto(entry.getKey(), categoryDetails);
                })
                .collect(Collectors.toList());
    }

    public List<ProductDetailsDTO> getSimilarProducts(List<String> lastViewedProductIds) {
        log.info("Fetching similar products based on last viewed products: {}", lastViewedProductIds);
        Map<String, ProductViewCount> allViewCounts = new HashMap<>();
        KeyValueIterator<String, ProductViewCount> viewCountIterator = viewCountStore.all();

        while (viewCountIterator.hasNext()) {
            KeyValue<String, ProductViewCount> entry = viewCountIterator.next();
            allViewCounts.put(entry.key, entry.value);
        }

        // Get categories of last viewed products
        List<String> categoryOrder = lastViewedProductIds.stream()
                .map(productId -> {
                    ProductViewCount productViewCount = allViewCounts.get(productId);
                    return (productViewCount != null) ? productViewCount.getCategoryId().toString() : null;
                })
                .filter(Objects::nonNull)
                .toList();

        Set<String> addedProducts = new HashSet<>();
        List<ProductDetailsDTO> similarProducts = new ArrayList<>();

        log.debug("Fetching similar products...");
        for (String categoryId : categoryOrder) {
            similarProducts.addAll(
                    allViewCounts.entrySet().stream()
                            .filter(entry -> entry.getValue() != null
                                    && entry.getValue().getCategoryId().toString().equals(categoryId)
                                    && !addedProducts.contains(entry.getKey())
                                    && !lastViewedProductIds.contains(entry.getKey()))
                            .sorted((e1, e2) -> Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount()))
                            .map(entry -> {
                                String productId = entry.getKey();
                                ProductDetails product = productStore.get(productId);
                                if (product != null) {
                                    ProductDetailsDTO dto = ProductDataMapper.avroToDto(productId, entry.getValue().getViewCount(), product);
                                    addedProducts.add(productId);
                                    return dto;
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .limit(1)
                            .toList()
            );
        }

        if (similarProducts.size() < 6) {
            log.debug("Adding more similar products to meet the limit...");
            for (String categoryId : categoryOrder) {
                similarProducts.addAll(
                        allViewCounts.entrySet().stream()
                                .filter(entry -> entry.getValue() != null
                                        && entry.getValue().getCategoryId().toString().equals(categoryId)
                                        && !addedProducts.contains(entry.getKey())
                                        && !lastViewedProductIds.contains(entry.getKey()))
                                .sorted((e1, e2) -> Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount()))
                                .map(entry -> {
                                    String productId = entry.getKey();
                                    ProductDetails product = productStore.get(productId);
                                    if (product != null) {
                                        ProductDetailsDTO dto = ProductDataMapper.avroToDto(productId, entry.getValue().getViewCount(), product);
                                        addedProducts.add(productId);
                                        return dto;
                                    }
                                    return null;
                                })
                                .filter(Objects::nonNull)
                                .limit(6 - similarProducts.size())
                                .toList()
                );

                if (similarProducts.size() >= 6) {
                    break;
                }
            }
        }

        
/*
        // Filling remaining slots if not filled
        if (similarProducts.size() < 6) {
            similarProducts.addAll(
                    allViewCounts.entrySet().stream()
                            .filter(entry -> !lastViewedProductIds.contains(entry.getKey())
                                    && !addedProducts.contains(entry.getKey()))
                            .sorted((e1, e2) -> Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount()))
                            .map(entry -> {
                                String productId = entry.getKey();
                                ProductDetails product = productStore.get(productId);
                                if (product != null) {
                                    ProductDetailsDTO dto = ProductDataMapper.avroToDto(productId, entry.getValue().getViewCount(), product);
                                    addedProducts.add(productId);
                                    return dto;
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .limit(6 - similarProducts.size())
                            .toList()
            );
        }*/
        //returning exactly 6 unique products
        log.debug("Returning final list of similar products...");
        return similarProducts.stream()
                .distinct()
                .limit(6)
                .collect(Collectors.toList());
    }
    /*

    public List<ProductDetailsDTO> getSimilarProducts(List<String> lastViewedProductIds) {
        log.info("Fetching similar products based on last viewed products: {}", lastViewedProductIds);

        // Fetch all view counts
        Map<String, ProductViewCount> allViewCounts = getAllViewCounts();

        // Get the categories of the last viewed products
        List<String> categoryOrder = getCategoryOrder(lastViewedProductIds, allViewCounts);

        // Set to track already added products
        Set<String> addedProducts = new HashSet<>();
        List<ProductDetailsDTO> similarProducts = new ArrayList<>();

        // Fetch similar products
        fetchSimilarProducts(categoryOrder, allViewCounts, lastViewedProductIds, addedProducts, similarProducts);

        // Add more products if there are less than 6 similar products
        if (similarProducts.size() < 6) {
            log.debug("Adding more similar products to meet the limit...");
            addMoreSimilarProducts(categoryOrder, allViewCounts, lastViewedProductIds, addedProducts, similarProducts);
        }

        // Fill remaining slots if not filled
        if (similarProducts.size() < 6) {
            fillRemainingSlots(allViewCounts, lastViewedProductIds, addedProducts, similarProducts);
        }

        // Return the final list of 6 unique products
        log.debug("Returning final list of similar products...");
        return getFinalListOfProducts(similarProducts);
    }

    private Map<String, ProductViewCount> getAllViewCounts() {
        Map<String, ProductViewCount> allViewCounts = new HashMap<>();
        KeyValueIterator<String, ProductViewCount> viewCountIterator = viewCountStore.all();

        while (viewCountIterator.hasNext()) {
            KeyValue<String, ProductViewCount> entry = viewCountIterator.next();
            allViewCounts.put(entry.key, entry.value);
        }

        return allViewCounts;
    }

    private List<String> getCategoryOrder(List<String> lastViewedProductIds, Map<String, ProductViewCount> allViewCounts) {
        return lastViewedProductIds.stream()
                .map(productId -> {
                    ProductViewCount productViewCount = allViewCounts.get(productId);
                    return (productViewCount != null) ? productViewCount.getCategoryId().toString() : null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private void fetchSimilarProducts(List<String> categoryOrder, Map<String, ProductViewCount> allViewCounts,
                                      List<String> lastViewedProductIds, Set<String> addedProducts,
                                      List<ProductDetailsDTO> similarProducts) {
        log.debug("Fetching similar products...");
        for (String categoryId : categoryOrder) {
            similarProducts.addAll(
                    allViewCounts.entrySet().stream()
                            .filter(entry -> entry.getValue() != null
                                    && entry.getValue().getCategoryId().toString().equals(categoryId)
                                    && !addedProducts.contains(entry.getKey())
                                    && !lastViewedProductIds.contains(entry.getKey()))
                            .sorted((e1, e2) -> Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount()))
                            .map(entry -> {
                                String productId = entry.getKey();
                                ProductDetails product = productStore.get(productId);
                                if (product != null) {
                                    ProductDetailsDTO dto = ProductDataMapper.avroToDto(productId, entry.getValue().getViewCount(), product);
                                    addedProducts.add(productId);
                                    return dto;
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .limit(1)
                            .toList()
            );
        }
    }

    private void addMoreSimilarProducts(List<String> categoryOrder, Map<String, ProductViewCount> allViewCounts,
                                        List<String> lastViewedProductIds, Set<String> addedProducts,
                                        List<ProductDetailsDTO> similarProducts) {
        for (String categoryId : categoryOrder) {
            similarProducts.addAll(
                    allViewCounts.entrySet().stream()
                            .filter(entry -> entry.getValue() != null
                                    && entry.getValue().getCategoryId().toString().equals(categoryId)
                                    && !addedProducts.contains(entry.getKey())
                                    && !lastViewedProductIds.contains(entry.getKey()))
                            .sorted((e1, e2) -> Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount()))
                            .map(entry -> {
                                String productId = entry.getKey();
                                ProductDetails product = productStore.get(productId);
                                if (product != null) {
                                    ProductDetailsDTO dto = ProductDataMapper.avroToDto(productId, entry.getValue().getViewCount(), product);
                                    addedProducts.add(productId);
                                    return dto;
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .limit(6 - similarProducts.size())
                            .toList()
            );

            if (similarProducts.size() >= 6) {
                break;
            }
        }
    }

    private void fillRemainingSlots(Map<String, ProductViewCount> allViewCounts, List<String> lastViewedProductIds,
                                    Set<String> addedProducts, List<ProductDetailsDTO> similarProducts) {
        if (similarProducts.size() < 6) {
            similarProducts.addAll(
                    allViewCounts.entrySet().stream()
                            .filter(entry -> !lastViewedProductIds.contains(entry.getKey())
                                    && !addedProducts.contains(entry.getKey()))
                            .sorted((e1, e2) -> Long.compare(e2.getValue().getViewCount(), e1.getValue().getViewCount()))
                            .map(entry -> {
                                String productId = entry.getKey();
                                ProductDetails product = productStore.get(productId);
                                if (product != null) {
                                    ProductDetailsDTO dto = ProductDataMapper.avroToDto(productId, entry.getValue().getViewCount(), product);
                                    addedProducts.add(productId);
                                    return dto;
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .limit(6 - similarProducts.size())
                            .toList()
            );
        }
    }

    private List<ProductDetailsDTO> getFinalListOfProducts(List<ProductDetailsDTO> similarProducts) {
        return similarProducts.stream()
                .distinct()
                .limit(6)
                .collect(Collectors.toList());
    }
}
*/
}
