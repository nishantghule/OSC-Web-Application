package com.in.mappers;

import com.in.dtos.*;
import com.in.proto.product.Category;
import com.in.proto.product.DashboardRequest;
import com.in.proto.product.DashboardResponse;
import com.in.proto.product.FilterProductsRequest;
import com.in.proto.product.FilterProductsResponse;
import com.in.proto.product.Product;
import com.in.proto.product.ProductData;
import com.in.proto.product.ProductDataRequest;
import com.in.proto.product.ProductDataResponse;
import com.in.proto.product.RecentlyViewedProductsRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataMapper {
    public static ProductDataRequest dtoToRequest(FetchProductRequestDTO dto){
        return ProductDataRequest.newBuilder()
                .setProductId(dto.getProdId())
                .setUserId(dto.getUserId())
                .setCategoryId(dto.getCatId())
                .build();
    }
    public static FetchProductResponseDTO responseToDto(ProductDataResponse response){
        ProductData product = response.getProduct();
        return new FetchProductResponseDTO(product.getProductId(), product.getCategoryId(), product.getProductName(), product.getProductDetails(), product.getProductPrice(), protoToDtoList(response.getProductsList()));
    }
    public static SimilarProductsDTO protoToDto(ProductData product) {
        return new SimilarProductsDTO(
                product.getProductId(),
                product.getCategoryId(),
                product.getProductName(),
                product.getProductPrice()
        );
    }
    // Method to convert list of proto Products to list of DTOs
    public static List<SimilarProductsDTO> protoToDtoList(List<ProductData> productList) {
        return productList.stream()
                .map(DataMapper::protoToDto)
                .collect(Collectors.toList());
    }



    public static DashboardResponseDTO responseToDto(DashboardResponse response) {
        // Convert Proto lists to DTO lists
        List<CategoryDetailsDTO> categories = protoToCategoryList(response.getDashboardDetails().getCategoriesList());
        List<ProductDetailsDTO> featuredProducts = protoToProductList(response.getDashboardDetails().getFeaturedProductsList());
        List<ProductDetailsDTO> recentlyViewedProducts = protoToProductList(response.getDashboardDetails().getRecentlyViewedProductsList());
        List<ProductDetailsDTO> similarProducts = protoToProductList(response.getDashboardDetails().getSimilarProductsList());

        // return the DashboardResponseDTO using the mapped categories and products
        return new DashboardResponseDTO(categories, featuredProducts, recentlyViewedProducts, similarProducts);
    }


    public static List<CategoryDetailsDTO> protoToCategoryList(List<Category> categories) {
        return categories.stream()
                .map(DataMapper::protoToCategoryDto)
                .collect(Collectors.toList());
    }

    public static List<ProductDetailsDTO> protoToProductList(List<Product> products) {
        return products.stream()
                .map(DataMapper::protoToProductDto)
                .collect(Collectors.toList());
    }

    public static ProductDetailsDTO protoToProductDto(Product product) {
        return new ProductDetailsDTO(
                product.getProductId(),
                product.getCategoryId(),
                product.getProductName(),
                product.getProductDetails(),
                product.getProductPrice(),
                product.getImagePath(),
                0);
    }

    public static CategoryDetailsDTO protoToCategoryDto(Category category) {
        return new CategoryDetailsDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getImagePath()
        );
    }

    public static RecentlyViewedProductsRequest dtoToRequest(DashboardRequestDTO dto){
        return RecentlyViewedProductsRequest.newBuilder()
                .setUserId(dto.getUserId())
                .build();
    }
    public static DashboardRequest dtoToRequest(List<String> productIds){
        return DashboardRequest.newBuilder()
                .addAllProductId(productIds)
                .build();
    }
    public static FilterProductsRequest dtoToRequest(FilterProductsDTO dto){
        return FilterProductsRequest.newBuilder()
                .setCatId(dto.getCatId())
                .setFilter(dto.getFilter())
                .build();
    }
    public static GenericResponseDTO dashboardProductsResponse(DashboardResponseDTO dashboardResponse) {
        List<Map<String, Object>> dataObject = new ArrayList<>();

        if (dashboardResponse != null) {

            // Featured products
            if (!dashboardResponse.getFeaturedProducts().isEmpty()) {
                Map<String, Object> featuredProductsMap = new HashMap<>();
                featuredProductsMap.put("TYPE", "Featured Products");
                featuredProductsMap.put("Featured Products", dashboardResponse.getFeaturedProducts());
                dataObject.add(featuredProductsMap);
            }

            // Recently Viewed Products
            if (!dashboardResponse.getRecentlyViewedProducts().isEmpty()) {
                Map<String, Object> recentlyViewedMap = new HashMap<>();
                recentlyViewedMap.put("TYPE", "Recently Viewed Products");
                recentlyViewedMap.put("Recently Viewed Products", dashboardResponse.getRecentlyViewedProducts());
                dataObject.add(recentlyViewedMap);
            }

            // Similar Products
            if (!dashboardResponse.getSimilarProducts().isEmpty()) {
                Map<String, Object> similarProductsMap = new HashMap<>();
                similarProductsMap.put("TYPE", "Similar Products");
                similarProductsMap.put("Similar Products", dashboardResponse.getSimilarProducts());
                dataObject.add(similarProductsMap);
            }
            // Categorised products
            if (!dashboardResponse.getCategories().isEmpty()) {
                Map<String, Object> categoriesMap = new HashMap<>();
                categoriesMap.put("TYPE", "Categories");
                categoriesMap.put("Categories", dashboardResponse.getCategories());
                dataObject.add(categoriesMap);
            }
        }
        // Final Response
        Map<String, Object> finalData = new HashMap<>();
        finalData.put("data", dataObject);
        return new GenericResponseDTO(200, finalData);
    }
    public static SimilarProductsDTO protoToDto(Product product) {
        return new SimilarProductsDTO(
                product.getProductId(),
                product.getCategoryId(),
                product.getProductName(),
                product.getProductPrice()
        );
    }

    public static GenericResponseDTO responseToDto(FilterProductsResponse response) {
        Map<String, Object> map = new HashMap<>();
        List<SimilarProductsDTO> list = response.getFilteredProductsList()
                .stream()
                .map(DataMapper::protoToDto)
                .toList();
        map.put("products", list);
        return new GenericResponseDTO(200, map);
    }


}
