package com.in.mappers;

import com.in.dtos.*;
import com.in.kafka.avro.product.ProductDetails;
import com.in.proto.product.Category;
import com.in.proto.product.DashboardDetails;
import com.in.proto.product.DashboardResponse;
import com.in.proto.product.FilterProductsResponse;
import com.in.proto.product.Product;
import com.in.proto.product.ProductData;
import com.in.proto.product.ProductDataRequest;
import com.in.proto.product.ProductDataResponse;
import com.in.proto.product.FilterProductsRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDataMapper {
    public static ProductDetails dtoToAvro(ProductDetailsDTO dto) {
        return ProductDetails.newBuilder()
                .setProductName(dto.getProdName())
                .setCategoryId(dto.getCategoryId())
                .setProductPrice(dto.getProdMarketPrice())
                .setProductDescription(dto.getProductDescription())
                .setImagePath(dto.getImagePath())
                .setDate(LocalDateTime.now().toString())
                .build();
    }

    public static FetchProductRequestDTO requestToDto(ProductDataRequest request) {
        return new FetchProductRequestDTO(request.getUserId(), request.getProductId(), request.getCategoryId());
    }

    public static ProductDataResponse dtoToResponse(FetchProductResponseDTO response) {
        ProductData product = ProductData.newBuilder()
                .setProductId(response.getProdId())
                .setCategoryId(response.getCatId())
                .setProductName(response.getProdName())
                .setProductDetails(response.getProdDesc())
                .setProductPrice(response.getProdPrice())
                .build();
        return ProductDataResponse.newBuilder()
                .setProduct(product)
                .addAllProducts(dtoToAvroList(response.getSimilarProducts()))
                .build();
    }

    public static ProductData dtoToAvro(SimilarProductsDTO dto) {
        return ProductData.newBuilder()
                .setProductId(dto.getProductId())
                .setCategoryId(dto.getCategoryId())
                .setProductName(dto.getProdName())
                .setProductPrice(dto.getProdMarketPrice())
                .build();
    }
    public static SimilarProductsDTO dtoToDTO(ProductDetailsDTO dto) {
        return new SimilarProductsDTO(
                dto.getProductId(),
                dto.getCategoryId(),
                dto.getProdName(),
                dto.getProdMarketPrice());
    }

    public static List<ProductData> dtoToAvroList(List<SimilarProductsDTO> list) {
        return list.stream().map(ProductDataMapper::dtoToAvro).toList();
    }

    public static ProductDetailsDTO avroToDto(String productId, int viewCount, ProductDetails productDetails) {
        return ProductDetailsDTO.builder()
                .productId(productId)
                .categoryId(productDetails.getCategoryId().toString())
                .prodName(productDetails.getProductName().toString())
                .productDescription(productDetails.getProductDescription().toString())
                .prodMarketPrice(productDetails.getProductPrice())
                .imagePath(productDetails.getImagePath().toString())
                .date(LocalDateTime.parse(productDetails.getDate()))
                .counts(viewCount)
                .build();
    }
    public static ProductDetailsDTO avroToDTO(String productId,ProductDetails productDetails) {
        return ProductDetailsDTO.builder()
                .productId(productId)
                .categoryId(productDetails.getCategoryId().toString())
                .prodName(productDetails.getProductName().toString())
                .productDescription(productDetails.getProductDescription().toString())
                .prodMarketPrice(productDetails.getProductPrice())
                .imagePath(productDetails.getImagePath().toString())
                .build();
    }


    public static DashboardResponse dtoToResponse(DashboardResponseDTO dto) {
        List<Category> categories = dtoToCategoryList(dto.getCategories());
        List<Product> featuredProducts = dtoToProductList(dto.getFeaturedProducts());
        List<Product> recentlyViewedProducts = dtoToProductList(dto.getRecentlyViewedProducts());
        List<Product> similarProducts = dtoToProductList(dto.getSimilarProducts());

        DashboardDetails dashboardDetails = DashboardDetails.newBuilder()
                .addAllCategories(categories)
                .addAllFeaturedProducts(featuredProducts)
                .addAllRecentlyViewedProducts(recentlyViewedProducts)
                .addAllSimilarProducts(similarProducts)
                .build();

        return DashboardResponse.newBuilder()
                .setDashboardDetails(dashboardDetails)
                .build();
    }

    public static List<Product> dtoToProductList(List<ProductDetailsDTO> list) {
        return list.stream().map(ProductDataMapper::dtoToProto).toList();
    }
    public static List<Category> dtoToCategoryList(List<CategoryDetailsDTO> list) {
        return list.stream().map(ProductDataMapper::dtoToProto).toList();
    }

    public static Product dtoToProto(ProductDetailsDTO dto) {
        return Product.newBuilder()
                .setProductId(dto.getProductId())
                .setCategoryId(dto.getCategoryId())
                .setProductName(dto.getProdName())
                .setProductDetails(dto.getProductDescription())
                .setProductPrice(dto.getProdMarketPrice())
                .setImagePath(dto.getImagePath())
                .build();
    }

    public static Category dtoToProto(CategoryDetailsDTO dto) {
        return Category.newBuilder()
                .setCategoryId(dto.getCategoryId())
                .setCategoryName(dto.getCategoryName())
                .setImagePath(dto.getImagePath())
                .build();
    }
    public static FilterProductsDTO requestToDto(FilterProductsRequest dto) {
        return FilterProductsDTO.builder()
                .catId(dto.getCatId())
                .filter(dto.getFilter())
                .build();
    }
    public static FilterProductsResponse dtoToResponse(List<ProductDetailsDTO> list){
        return FilterProductsResponse.newBuilder()
                .addAllFilteredProducts(dtoToProductList(list))
                .build();
    }

}
