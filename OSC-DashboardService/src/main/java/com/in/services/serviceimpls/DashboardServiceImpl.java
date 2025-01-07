package com.in.services.serviceimpls;

import com.in.dtos.*;
import com.in.mappers.DataMapper;
import com.in.proto.product.*;
import com.in.proto.product.DashboardProductsGrpc;
import com.in.proto.product.ProductsViewHistoryGrpc;
import com.in.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardProductsGrpc.DashboardProductsBlockingStub productDataStub;
    private final ProductsViewHistoryGrpc.ProductsViewHistoryBlockingStub productsViewHistoryStub;
    private final Logger log = LogManager.getLogger(DashboardServiceImpl.class);
    @Override
    public FetchProductResponseDTO getProductDetails(FetchProductRequestDTO fetchProductRequestDTO) {
        ProductDataResponse response = productDataStub.fetchProductDetails(DataMapper.dtoToRequest(fetchProductRequestDTO));
        return DataMapper.responseToDto(response);
    }

    @Override
    public GenericResponseDTO getDashboardProducts(DashboardRequestDTO dashboardRequestDTO) {
        RecentlyViewedProductsResponse recentlyViewedProductsResponse = productsViewHistoryStub.fetchRecentlyViewedHistory(DataMapper.dtoToRequest(dashboardRequestDTO));
        if (recentlyViewedProductsResponse.getProductIdsList().isEmpty()) {
            log.info("no recently view product for userId: {}", dashboardRequestDTO.getUserId());
        } else {
            log.info("list<productId>: {} ", recentlyViewedProductsResponse.getProductIdsList());
        }
        DashboardResponse response = productDataStub.fetchDashboardProducts(DataMapper.dtoToRequest(recentlyViewedProductsResponse.getProductIdsList()));
        DashboardResponseDTO responseDTO = DataMapper.responseToDto(response);

        return DataMapper.dashboardProductsResponse(responseDTO);
    }

    @Override
    public GenericResponseDTO getFilteredProducts(FilterProductsDTO filterProductsDTO) {
        FilterProductsResponse response = productDataStub.filterProducts(DataMapper.dtoToRequest(filterProductsDTO));
        return DataMapper.responseToDto(response);
    }


}
