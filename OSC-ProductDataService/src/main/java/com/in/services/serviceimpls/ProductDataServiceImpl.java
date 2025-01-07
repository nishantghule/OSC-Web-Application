package com.in.services.serviceimpls;

import com.in.dtos.*;
import com.in.entities.ProductEntity;
import com.in.kafka.avro.product.ProductDetails;
import com.in.kafka.avro.product.ProductViewCount;
import com.in.kafka.producer.ProductViewCountProducer;
import com.in.mappers.ProductDataMapper;
import com.in.proto.product.ProductsViewHistoryGrpc;
import com.in.proto.product.UpdateRecentlyViewedProductsRequest;
import com.in.repositories.ProductDataRepository;
import com.in.repositories.ProductViewCountRepository;
import com.in.services.ProductDataService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductDataServiceImpl implements ProductDataService {
    private final Logger log = LogManager.getLogger(ProductDataServiceImpl.class);
    private final ProductDataRepository productDataRepository;
    private final ModelMapper modelMapper;
    private final ReadOnlyKeyValueStore<String, ProductDetails> productDataStore;
    private final ReadOnlyKeyValueStore<String, ProductViewCount> productViewCountStore;
    private final ProductViewCountProducer productViewCountProducer;
    private final ProductsViewHistoryGrpc.ProductsViewHistoryBlockingStub recentlyViewedStub;
    private final ProductViewCountRepository productViewCountRepository;
    private final DashboardProductsService dashboardProductsService;

    @Override
    public List<ProductDetailsDTO> getAllProducts() {
        log.info("Fetching all products from the repository...");
        List<ProductEntity> productEntities = productDataRepository.findAll();
        log.info("Found {} products in the repository.", productEntities.size());
        return productEntities.stream()
                .map((product) -> modelMapper.map(product, ProductDetailsDTO.class))
                .toList();
    }

    public DashboardResponseDTO getDashboardProducts(List<String> recentlyViewedProductIds) {
        log.info("Fetching dashboard products for recently viewed product IDs: {}", recentlyViewedProductIds);

        List<CategoryDetailsDTO> topCategoriesList = dashboardProductsService.getTopCategoriesList(12);
        log.debug("Top categories list: {}", topCategoriesList);

        List<ProductDetailsDTO> similarProducts = new ArrayList<>();
        List<ProductDetailsDTO> recentlyViewedProducts = new ArrayList<>();
        List<ProductDetailsDTO> featuredProducts = new ArrayList<>();

        if (recentlyViewedProductIds.isEmpty()) {
            log.info("No recently viewed products found. Returning featured products only.");
            featuredProducts = dashboardProductsService.getFeaturedProducts();
        } else {
            log.info("User has history. Returning similar and recently viewed products.");
            similarProducts = dashboardProductsService.getSimilarProducts(recentlyViewedProductIds);
            recentlyViewedProducts = dashboardProductsService.getRecentlyViewedProducts(recentlyViewedProductIds);
        }

        return new DashboardResponseDTO(
                topCategoriesList,
                featuredProducts,
                recentlyViewedProducts,
                similarProducts
        );
    }

    @Override
    public List<ProductDetailsDTO> filterProducts(FilterProductsDTO filterProductsDTO) {
        log.info("Filtering products with filter criteria: {}", filterProductsDTO);

        KeyValueIterator<String, ProductDetails> allProducts = productDataStore.all();
        List<ProductDetailsDTO> categorisedProducts = StreamSupport.stream(Spliterators.spliteratorUnknownSize(allProducts, Spliterator.ORDERED), false)
                .filter(entry -> entry.value.getCategoryId().toString().equals(filterProductsDTO.getCatId()))
                .map(entry -> ProductDataMapper.avroToDto(
                        entry.key,
                        productViewCountStore.get(entry.key).getViewCount(),
                        entry.value
                ))
                .toList();

        log.info("Product details filtered: {}", categorisedProducts);

        Comparator<ProductDetailsDTO> comparator = switch (filterProductsDTO.getFilter()) {
            case "LH" -> // Low to High Price
                    Comparator.comparing(ProductDetailsDTO::getProdMarketPrice);
            case "HL" -> // High to Low Price
                    Comparator.comparing(ProductDetailsDTO::getProdMarketPrice).reversed();
            case "NF" -> // Newest First
                    Comparator.comparing(ProductDetailsDTO::getDate).reversed();
            case "P" -> // Popularity
                    Comparator.comparing(ProductDetailsDTO::getCounts).reversed();
            default -> throw new IllegalArgumentException("Invalid filter type: " + filterProductsDTO.getFilter());
        };

        return categorisedProducts.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public FetchProductResponseDTO fetchProductDetails(FetchProductRequestDTO request) {
        log.info("Fetching product details for product ID: {}", request.getProdId());

        // Getting product details
        ProductDetails productDetails = productDataStore.get(request.getProdId());
        if (productDetails == null) {
            log.error("Product with ID {} not found.", request.getProdId());
            throw new NullPointerException();
        }

        log.info("Product details for {} fetched successfully.", request.getProdId());

        // Update the product view count
        updateProductViewCount(request, productDetails);

        // Update the recently view history
        updateRecentlyViewedHistory(request);

        // Get the list for similar products
        List<SimilarProductsDTO> similarProductsDTOList = getSimilarProductOfCategory(request.getCatId(), request.getProdId());
        log.info("Similar products fetched for product {}: {}", request.getProdId(), similarProductsDTOList);

        // Response with the product details and similar products
        return new FetchProductResponseDTO(
                request.getProdId(),
                request.getCatId(),
                productDetails.getProductName().toString(),
                productDetails.getProductDescription().toString(),
                productDetails.getProductPrice(),
                similarProductsDTOList
        );
    }

    private void updateRecentlyViewedHistory(FetchProductRequestDTO request) {
        log.info("Updating recently viewed history for user {} and product {}", request.getUserId(), request.getProdId());

        UpdateRecentlyViewedProductsRequest updateRequest = UpdateRecentlyViewedProductsRequest.newBuilder()
                .setUserId(request.getUserId())
                .setProductId(request.getProdId())
                .build();

        recentlyViewedStub.updateRecentlyViewedProducts(updateRequest);
        log.info("Recently viewed history updated for user {} and product {}", request.getUserId(), request.getProdId());
    }

    private void updateProductViewCount(FetchProductRequestDTO request, ProductDetails productDetails) {
        log.info("Updating product view count for product {}.", request.getProdId());

        ProductViewCount productViewCount = productViewCountStore.get(request.getProdId());
        int initialCount = productViewCount.getViewCount();
        ProductViewCount updatedProductViewCount = ProductViewCount.newBuilder()
                .setCategoryId(productDetails.getCategoryId())
                .setViewCount(initialCount + 1)
                .build();

        productViewCountProducer.publishProductViewCount(request.getProdId(), updatedProductViewCount);
        log.info("Product view count updated for product {}", request.getProdId());
    }

    // Utility method to get product of same category
    private List<SimilarProductsDTO> getSimilarProductOfCategory(String categoryId, String productId) {
        log.info("Fetching similar products for category {} excluding product {}", categoryId, productId);

        List<SimilarProductsDTO> similarProductsDTOList = new ArrayList<>();
        KeyValueIterator<String, ProductDetails> allProducts = productDataStore.all();
        int similarProductCount = 0;

        if (!allProducts.hasNext()) {
            log.warn("No products found in the product data store.");
            return similarProductsDTOList;
        }

        while (allProducts.hasNext() && similarProductCount < 6) {
            KeyValue<String, ProductDetails> entry = allProducts.next();
            ProductDetails currentProduct = entry.value;
            if (currentProduct.getCategoryId().toString().equals(categoryId) && !entry.key.equals(productId)) {
                SimilarProductsDTO similarProductDTO = new SimilarProductsDTO(
                        entry.key,
                        categoryId,
                        currentProduct.getProductName().toString(),
                        currentProduct.getProductPrice()
                );
                similarProductsDTOList.add(similarProductDTO);
                similarProductCount++;
            }
        }

        log.info("Total similar products found: {}", similarProductsDTOList.size());
        return similarProductsDTOList;
    }

    public List<ProductDetailsDTO> getProductDetails(List<String> productIds){
        log.info("Fetching product details for product IDs: {}", productIds);

        List<ProductDetailsDTO> productDetailsList = new ArrayList<>();
        for(String prodId: productIds){
            ProductDetails productDetails = productDataStore.get(prodId);
            ProductDetailsDTO productDetailsDTO = ProductDataMapper.avroToDTO(prodId, productDetails);
            productDetailsList.add(productDetailsDTO);
        }
        log.info("Product details fetched for {} products.", productIds.size());
        return productDetailsList;
    }
}
