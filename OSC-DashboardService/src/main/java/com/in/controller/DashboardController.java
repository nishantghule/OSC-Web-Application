package com.in.controller;

import com.in.dtos.*;
import com.in.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/product/details")
    public ResponseEntity<?> fetchProduct(@RequestBody FetchProductRequestDTO dto) {
        FetchProductResponseDTO response = dashboardService.getProductDetails(dto);
        return ResponseEntity.status(200).body(new GenericResponseDTO(200, response));
    }
    @GetMapping("/get")
    public ResponseEntity<?> get(){
        return ResponseEntity.ok("working");
    }

    @PostMapping("/user/dashboard")
    public ResponseEntity<GenericResponseDTO> getDashboard(@RequestBody DashboardRequestDTO dto){
        GenericResponseDTO response = dashboardService.getDashboardProducts(dto);
        return ResponseEntity.status(200).body(response);
    }
    @PostMapping("/filter/product")
    public ResponseEntity<GenericResponseDTO> getFilteredProducts(@RequestBody FilterProductsDTO dto){
        GenericResponseDTO response = dashboardService.getFilteredProducts(dto);
        return ResponseEntity.status(200).body(response);
    }
}


