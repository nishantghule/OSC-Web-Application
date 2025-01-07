package com.in.grpcservices;

import com.google.protobuf.Empty;
import com.in.dtos.ProductDataDTO;
import com.in.mappers.CartDataMapper;
import com.in.proto.cart.*;
import com.in.proto.cart.CartDataServiceGrpc;
import com.in.services.CartDataService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class CartDataGrpcService extends CartDataServiceGrpc.CartDataServiceImplBase {
    private final CartDataService cartDataService;
    @Override
    public void addProductToCart(CartProductDataRequest request, StreamObserver<Empty> responseObserver) {
        cartDataService.addProductToCart(CartDataMapper.requestToDto(request));
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void decreaseCartProductQuantity(CartProductDataRequest request, StreamObserver<Empty> responseObserver) {
        cartDataService.decreaseCartProductQuantity(CartDataMapper.requestToDto(request));
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void removeProductFromCart(RemoveProductDataRequest request, StreamObserver<Empty> responseObserver) {
        cartDataService.removeProductFromCart(CartDataMapper.requestToDto(request));
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void fetchProductsOfCart(ViewCartProductRequest request, StreamObserver<ViewCartProductResponse> responseObserver) {
        List<ProductDataDTO> cartProducts = cartDataService.viewCartProducts(request.getUserId());
        responseObserver.onNext(CartDataMapper.dtoToResponse(cartProducts));
        responseObserver.onCompleted();
    }

    @Override
    public void saveCartProductsToDB(ViewCartProductRequest request, StreamObserver<Empty> responseObserver) {
        cartDataService.saveCartData(request.getUserId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
