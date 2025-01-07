package com.in.controller;

import com.in.dtos.CartDataRequestDTO;
import com.in.dtos.GenericResponseDTO;
import com.in.dtos.RemoveCartProductDTO;
import com.in.dtos.ViewCartRequestDTO;
import com.in.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/cart")
public class CartServiceController {
    private final CartService cartService;
    @PostMapping("/view")
    public ResponseEntity<GenericResponseDTO> viewCartProducts(@RequestBody ViewCartRequestDTO dto){
        GenericResponseDTO genericResponseDTO = cartService.viewCartService(dto);
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }
    @PostMapping("/increase")
    public ResponseEntity<GenericResponseDTO> addProductToCart(@RequestBody CartDataRequestDTO dto){
        GenericResponseDTO genericResponseDTO = cartService.addProductToCart(dto);
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }
    @PostMapping("/decrease")
    public ResponseEntity<GenericResponseDTO> decreaseCartProductQuantity(@RequestBody CartDataRequestDTO dto){
        GenericResponseDTO genericResponseDTO = cartService.decreaseCartProductQuantity(dto);
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }
    @PostMapping("/remove")
    public ResponseEntity<GenericResponseDTO> removeProductFromCart(@RequestBody RemoveCartProductDTO dto){
        GenericResponseDTO genericResponseDTO = cartService.removeProductFromCart(dto);
        return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
    }

}
