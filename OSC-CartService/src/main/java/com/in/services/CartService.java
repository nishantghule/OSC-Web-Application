package com.in.services;

import com.in.dtos.CartDataRequestDTO;
import com.in.dtos.GenericResponseDTO;
import com.in.dtos.RemoveCartProductDTO;
import com.in.dtos.ViewCartRequestDTO;

public interface CartService {
    GenericResponseDTO viewCartService(ViewCartRequestDTO dto);
    GenericResponseDTO addProductToCart(CartDataRequestDTO dto);
    GenericResponseDTO decreaseCartProductQuantity(CartDataRequestDTO dto);
    GenericResponseDTO removeProductFromCart(RemoveCartProductDTO dto);
}
