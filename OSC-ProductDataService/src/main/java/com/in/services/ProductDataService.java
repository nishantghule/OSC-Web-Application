package com.in.services;

import com.in.dtos.*;

import java.util.List;

public interface ProductDataService {
    List<ProductDetailsDTO> getAllProducts();

    FetchProductResponseDTO fetchProductDetails(FetchProductRequestDTO request);

    DashboardResponseDTO getDashboardProducts(List<String> recentlyViewedProductIds);

    List<ProductDetailsDTO> filterProducts(FilterProductsDTO filterProductsDTO);

    List<ProductDetailsDTO> getProductDetails(List<String> productIds);
}
