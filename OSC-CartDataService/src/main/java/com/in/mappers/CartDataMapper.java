package com.in.mappers;

import com.in.dtos.CartDataDTO;
import com.in.dtos.CartDataRequestDTO;
import com.in.dtos.ProductDataDTO;
import com.in.dtos.RemoveCartProductDTO;
import com.in.kafka.avro.cart.CartProduct;
import com.in.proto.cart.CartProductDataRequest;
import com.in.proto.cart.RemoveProductDataRequest;
import com.in.proto.cart.ViewCartProductResponse;
import com.in.proto.product.GetProductDetailsRequest;
import com.in.proto.product.GetProductDetailsResponse;
import com.in.proto.product.Product;

import java.util.ArrayList;
import java.util.List;

public class CartDataMapper {
    public static List<ProductDataDTO> responseToProductList(GetProductDetailsResponse response, List<CartProduct> cartProductList) {
        List<ProductDataDTO> productDataList = new ArrayList<>();

        if (response != null && cartProductList != null) {
            for (Product product : response.getProductsList()) {
                CartProduct cartProduct = cartProductList.stream()
                        .filter(prod -> prod.getProductId().equals(product.getProductId()))
                        .findFirst()
                        .orElse(null);

                if (cartProduct != null) {
                    ProductDataDTO productDataDto = getProductDataDTO(product, cartProduct);
                    productDataList.add(productDataDto);
                }
            }
        }
        return productDataList;
    }

    private static ProductDataDTO getProductDataDTO(Product product, CartProduct cartProduct) {
        ProductDataDTO productDataDto = new ProductDataDTO();
        productDataDto.setProductId(product.getProductId());
        productDataDto.setCategoryId(product.getCategoryId());
        productDataDto.setProdName(product.getProductName());
        productDataDto.setProdMarketPrice(product.getProductPrice());
        productDataDto.setProductDescription(product.getProductDetails());
        productDataDto.setImagePath(product.getImagePath());
        productDataDto.setQuantity(cartProduct.getQuantity());
        return productDataDto;
    }

    public static GetProductDetailsRequest dtoToRequest(List<String> productIds) {
        return GetProductDetailsRequest.newBuilder().addAllProductIds(productIds).build();
    }
    public static CartDataRequestDTO requestToDto(CartProductDataRequest request){
        return CartDataRequestDTO.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
    }
    public static RemoveCartProductDTO requestToDto(RemoveProductDataRequest request){
        return RemoveCartProductDTO.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .build();
    }

    public static com.in.proto.cart.CartProduct dtoToProto(ProductDataDTO dto){
        return com.in.proto.cart.CartProduct.newBuilder()
                .setProductId(dto.getProductId())
                .setProductName(dto.getProdName())
                .setCategoryId(dto.getCategoryId())
                .setProductDescription(dto.getProductDescription())
                .setProductPrice(dto.getProdMarketPrice())
                .setImagePath(dto.getImagePath())
                .setQuantity(dto.getQuantity())
                .build();
    }
    public static ViewCartProductResponse dtoToResponse(List<ProductDataDTO> dtoList) {
        List<com.in.proto.cart.CartProduct> list = dtoList.stream().map(CartDataMapper::dtoToProto).toList();
        return ViewCartProductResponse.newBuilder()
                .addAllCartProducts(list)
                .build();
    }
    public static CartDataDTO avroToDto(CartProduct cartProduct){
        return CartDataDTO.builder()
                .productId(cartProduct.getProductId())
                .quantity(cartProduct.getQuantity())
                .build();
    }
}
