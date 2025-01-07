package com.in.services;

import com.in.dtos.CartDataRequestDTO;
import com.in.dtos.ProductDataDTO;
import com.in.dtos.RemoveCartProductDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CartDataService {
    void addProductToCart(CartDataRequestDTO dto);
    void decreaseCartProductQuantity(CartDataRequestDTO dto);
    void removeProductFromCart(RemoveCartProductDTO dto);
    List<ProductDataDTO> viewCartProducts(String userId);
    void saveCartData(String userId);
}
