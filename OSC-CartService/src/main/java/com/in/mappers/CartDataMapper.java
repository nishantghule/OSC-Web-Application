package com.in.mappers;

import com.in.dtos.CartDataRequestDTO;
import com.in.dtos.CartProductDataDTO;
import com.in.dtos.RemoveCartProductDTO;
import com.in.dtos.ViewCartRequestDTO;
import com.in.proto.cart.CartProduct;
import com.in.proto.cart.CartProductDataRequest;
import com.in.proto.cart.RemoveProductDataRequest;
import com.in.proto.cart.ViewCartProductRequest;

public class CartDataMapper {
    public static ViewCartProductRequest dtoToRequest(ViewCartRequestDTO dto){
        return ViewCartProductRequest.newBuilder()
                .setUserId(dto.getUserId()).
                build();
    }
    public static CartProductDataDTO protoToDto(CartProduct cartProduct){
        return CartProductDataDTO.builder()
                .productId(cartProduct.getProductId())
                .productName(cartProduct.getProductName())
                .categoryId(cartProduct.getCategoryId())
                .productPrice(cartProduct.getProductPrice())
                .quantity(cartProduct.getQuantity())
                .build();
    }
    public static CartProductDataRequest dtoToRequest(CartDataRequestDTO dto){
        return CartProductDataRequest.newBuilder()
                .setUserId(dto.getUserId())
                .setProductId(dto.getProdId())
                .setQuantity(dto.getCount())
                .build();
    }
    public static RemoveProductDataRequest dtoToRequest(RemoveCartProductDTO dto){
        return RemoveProductDataRequest.newBuilder()
                .setUserId(dto.getUserId())
                .setProductId(dto.getProdId())
                .build();
    }
}
