package com.in.grpcservices;


import com.in.dtos.DashboardResponseDTO;
import com.in.dtos.FetchProductResponseDTO;
import com.in.dtos.ProductDetailsDTO;
import com.in.mappers.ProductDataMapper;
import com.in.proto.product.*;
import com.in.proto.product.DashboardProductsGrpc;
import com.in.proto.product.DashboardRequest;
import com.in.proto.product.DashboardResponse;
import com.in.proto.product.FilterProductsRequest;
import com.in.proto.product.FilterProductsResponse;
import com.in.proto.product.ProductDataRequest;
import com.in.proto.product.ProductDataResponse;
import com.in.services.ProductDataService;
import com.in.services.serviceimpls.DashboardProductsService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@RequiredArgsConstructor
@GrpcService
public class ProductDataGrpcService extends DashboardProductsGrpc.DashboardProductsImplBase {
    private final ProductDataService productDataService;
    private final DashboardProductsService dashboardProductsService;
    @Override
    public void fetchProductDetails(ProductDataRequest request, StreamObserver<ProductDataResponse> responseObserver) {
        FetchProductResponseDTO fetchProductResponseDTO = productDataService.fetchProductDetails(ProductDataMapper.requestToDto(request));
        ProductDataResponse response = ProductDataMapper.dtoToResponse(fetchProductResponseDTO);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void filterProducts(FilterProductsRequest request, StreamObserver<FilterProductsResponse> responseObserver) {
        List<ProductDetailsDTO> productDetailsDTOS = productDataService.filterProducts(ProductDataMapper.requestToDto(request));
        FilterProductsResponse response = ProductDataMapper.dtoToResponse(productDetailsDTOS);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void fetchDashboardProducts(DashboardRequest request, StreamObserver<DashboardResponse> responseObserver) {
        DashboardResponseDTO dashboardProducts = productDataService.getDashboardProducts(request.getProductIdList());
        DashboardResponse response = ProductDataMapper.dtoToResponse(dashboardProducts);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductDetails(GetProductDetailsRequest request, StreamObserver<GetProductDetailsResponse> responseObserver) {
        List<ProductDetailsDTO> productDetails = productDataService.getProductDetails(request.getProductIdsList());
        GetProductDetailsResponse response = GetProductDetailsResponse.newBuilder()
                        .addAllProducts(ProductDataMapper.dtoToProductList(productDetails)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
