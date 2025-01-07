package com.in.services.serviceimpls;

import com.in.dtos.*;
import com.in.mappers.CartDataMapper;
import com.in.proto.cart.CartDataServiceGrpc;
import com.in.proto.cart.ViewCartProductResponse;
import com.in.services.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final Logger log = LogManager.getLogger(CartServiceImpl.class);
    private final CartDataServiceGrpc.CartDataServiceBlockingStub cartDataServiceStub;

    @Override
    public GenericResponseDTO viewCartService(ViewCartRequestDTO dto) {
        ViewCartProductResponse response = cartDataServiceStub.fetchProductsOfCart(CartDataMapper.dtoToRequest(dto));
        if (response == null || response.getCartProductsList().isEmpty()) {
            log.info("user {} having no product in cart",dto.getUserId());
            return new GenericResponseDTO(200,"Cart is empty..sorry!!");
        }
        List<CartProductDataDTO> cartProductsList = response.getCartProductsList().stream().map(CartDataMapper::protoToDto).toList();
        int productsCartCount = cartProductsList.size();
        log.info("Total products in cart: {}", productsCartCount);
        Double totalPrice = 0.0;
        for(CartProductDataDTO cartProduct: cartProductsList){
            totalPrice += cartProduct.getProductPrice() * cartProduct.getQuantity();
            log.info("total price: {}", totalPrice);
        }

        ViewCartResponseDTO viewCartResponseDTO = new ViewCartResponseDTO(cartProductsList, productsCartCount, totalPrice);
        return new GenericResponseDTO(200,viewCartResponseDTO);
    }

    @Override
    public GenericResponseDTO addProductToCart(CartDataRequestDTO dto) {
        cartDataServiceStub.addProductToCart(CartDataMapper.dtoToRequest(dto));
        return new GenericResponseDTO(200, "Product added to cart successfully");
    }

    @Override
    public GenericResponseDTO decreaseCartProductQuantity(CartDataRequestDTO dto) {
        cartDataServiceStub.decreaseCartProductQuantity(CartDataMapper.dtoToRequest(dto));
        return new GenericResponseDTO(200, "Product quantity decreased from cart successfully");
    }

    @Override
    public GenericResponseDTO removeProductFromCart(RemoveCartProductDTO dto) {
        cartDataServiceStub.removeProductFromCart(CartDataMapper.dtoToRequest(dto));
        return new GenericResponseDTO(200, "Product removed from cart successfully");
    }
}
