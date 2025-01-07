package com.in.grpcservices;

import com.in.mappers.DataMapper;
import com.in.proto.product.*;
import com.in.proto.product.ProductsViewHistoryGrpc;
import com.in.services.RecentlyViewedHistoryService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class RecentlyViewedGrpcService extends ProductsViewHistoryGrpc.ProductsViewHistoryImplBase {
    private final RecentlyViewedHistoryService recentlyViewedHistoryService;

    @Override
    public void updateRecentlyViewedProducts(UpdateRecentlyViewedProductsRequest request, StreamObserver<UpdateRecentViewedProductsResponse> responseObserver) {
        Boolean success = recentlyViewedHistoryService.updateRecentlyViewedProducts(DataMapper.requestToDto(request));
        UpdateRecentViewedProductsResponse response = UpdateRecentViewedProductsResponse.newBuilder().setSuccess(success).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void fetchRecentlyViewedHistory(RecentlyViewedProductsRequest request, StreamObserver<RecentlyViewedProductsResponse> responseObserver) {
        List<String> productsList = recentlyViewedHistoryService.fetchRecentlyViewedProducts(request.getUserId());
        RecentlyViewedProductsResponse response = RecentlyViewedProductsResponse.newBuilder().addAllProductIds(productsList).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
