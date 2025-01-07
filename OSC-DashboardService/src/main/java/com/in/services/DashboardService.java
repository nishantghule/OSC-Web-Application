package com.in.services;

import com.in.dtos.*;

public interface DashboardService {
    FetchProductResponseDTO getProductDetails(FetchProductRequestDTO getProductsDTO);
    GenericResponseDTO getDashboardProducts(DashboardRequestDTO dashboardRequestDTO);
    GenericResponseDTO getFilteredProducts(FilterProductsDTO filterProductsDTO);
}
