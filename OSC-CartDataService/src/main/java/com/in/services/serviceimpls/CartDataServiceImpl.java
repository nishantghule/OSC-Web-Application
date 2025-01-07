package com.in.services.serviceimpls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.in.dtos.CartDataDTO;
import com.in.dtos.CartDataRequestDTO;
import com.in.dtos.ProductDataDTO;
import com.in.dtos.RemoveCartProductDTO;
import com.in.entities.CartDataEntity;
import com.in.kafka.avro.cart.CartData;
import com.in.kafka.avro.cart.CartProduct;
import com.in.kafka.producers.CartDataProducer;
import com.in.mappers.CartDataMapper;
import com.in.proto.product.DashboardProductsGrpc;
import com.in.proto.product.GetProductDetailsResponse;
import com.in.repositories.CartDataRepository;
import com.in.services.CartDataService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDataServiceImpl implements CartDataService {
    private final ReadOnlyKeyValueStore<String, CartData> cartDataStore;
    private final CartDataProducer cartDataProducer;
    private final DashboardProductsGrpc.DashboardProductsBlockingStub productDataStub;
    private final CartDataRepository cartDataRepository;
    private final Logger log = LogManager.getLogger(CartDataServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void addProductToCart(CartDataRequestDTO dto) {
        log.info("Adding product to cart for user: {}, productId: {}, quantity: {}", dto.getUserId(), dto.getProductId(), dto.getQuantity());
        CartData cartData = cartDataStore.get(dto.getUserId());
        if (cartData == null) {
            cartData = new CartData(new ArrayList<>());
        }

        List<CartProduct> cartProducts = cartData.getCartProducts();
        boolean productExists = false;

        if (!cartProducts.isEmpty()) {
            for (CartProduct product : cartProducts) {
                if (product.getProductId().equals(dto.getProductId())) {
                    log.debug("Product already in cart. Updating quantity for productId: {}", dto.getProductId());
                    product.setQuantity(product.getQuantity() + dto.getQuantity());
                    productExists = true;
                    break;
                }
            }
        }

        if (!productExists) {
            CartProduct newCartProduct = CartProduct.newBuilder()
                    .setProductId(dto.getProductId())
                    .setQuantity(dto.getQuantity())
                    .build();
            cartProducts.add(newCartProduct);
            log.debug("Added new product to cart: {}", newCartProduct);
        }

        cartDataProducer.publishCartProductData(dto.getUserId(), cartData);
        log.info("Cart updated and published for user: {}. CartData: {}", dto.getUserId(), cartData);
    }

    @Override
    public void decreaseCartProductQuantity(CartDataRequestDTO dto) {
        log.info("Decreasing product quantity in cart for user: {}, productId: {}, quantity: {}", dto.getUserId(), dto.getProductId(), dto.getQuantity());
        CartData cartData = cartDataStore.get(dto.getUserId());
        if (cartData == null) {
            log.warn("No cart data found for user: {}", dto.getUserId());
            return;
        }

        List<CartProduct> modifiableProductsList = new ArrayList<>(cartData.getCartProducts());
        Iterator<CartProduct> iterator = modifiableProductsList.iterator();
        boolean productUpdated = false;

        while (iterator.hasNext()) {
            CartProduct product = iterator.next();
            if (product.getProductId().equals(dto.getProductId())) {
                log.debug("Found productId: {} in cart. Current quantity: {}", product.getProductId(), product.getQuantity());
                if (product.getQuantity() > dto.getQuantity()) {
                    product.setQuantity(product.getQuantity() - dto.getQuantity());
                    productUpdated = true;
                    log.debug("Reduced quantity of productId: {}. New quantity: {}", product.getProductId(), product.getQuantity());
                } else {
                    iterator.remove();
                    log.info("Removed productId: {} from cart as its quantity is now zero.", product.getProductId());
                }
                break;
            }
        }

        if (!productUpdated && modifiableProductsList.isEmpty()) {
            log.info("Cart is now empty for user: {}", dto.getUserId());
            cartDataProducer.publishCartProductData(dto.getUserId(), null);
        } else {
            cartData.setCartProducts(modifiableProductsList);
            cartDataProducer.publishCartProductData(dto.getUserId(), cartData);
            log.info("Updated cart data for user: {}", dto.getUserId());
        }
    }

    @Override
    public void removeProductFromCart(RemoveCartProductDTO dto) {
        log.info("Removing productId: {} from cart for userId: {}", dto.getProductId(), dto.getUserId());
        CartData cartData = cartDataStore.get(dto.getUserId());
        if (cartData == null) {
            log.warn("Cart is empty for userId: {}", dto.getUserId());
            return;
        }

        List<CartProduct> products = new ArrayList<>(cartData.getCartProducts());
        boolean productRemoved = products.removeIf(product -> product.getProductId().equals(dto.getProductId()));

        if (productRemoved) {
            log.info("ProductId: {} removed from cart for user: {}", dto.getProductId(), dto.getUserId());
        } else {
            log.warn("ProductId: {} not found in cart for user: {}", dto.getProductId(), dto.getUserId());
        }

        if (products.isEmpty()) {
            cartDataProducer.publishCartProductData(dto.getUserId(), null);
            log.info("Cart is now empty for userId: {}", dto.getUserId());
        } else {
            cartData.setCartProducts(products);
            cartDataProducer.publishCartProductData(dto.getUserId(), cartData);
            log.info("Updated cart after product removal for userId: {}", dto.getUserId());
        }
    }

    @Override
    public List<ProductDataDTO> viewCartProducts(String userId) {
        log.info("Fetching cart products for userId: {}", userId);
        CartData cartData = cartDataStore.get(userId);
        if (cartData == null) {
            log.info("No cart data found for userId: {}", userId);
            return new ArrayList<>();
        }
        List<String> productIds = cartData.getCartProducts().stream()
                .map(CartProduct::getProductId)
                .toList();
        log.debug("Fetching product details for productIds: {}", productIds);
        GetProductDetailsResponse response = productDataStub.getProductDetails(CartDataMapper.dtoToRequest(productIds));
        List<ProductDataDTO> products = CartDataMapper.responseToProductList(response, cartData.getCartProducts());
        log.info("Fetched cart products: {}", products);
        return products;
    }

    @Override
    public void saveCartData(String userId) {
        log.info("Saving cart data to database for userId: {}", userId);
        try {
            CartData cartData = cartDataStore.get(userId);
            if (cartData == null) {
                log.warn("No cart data found for userId: {}", userId);
                cartDataRepository.deleteById(userId);
                return;
            }
            List<CartDataDTO> products = cartData.getCartProducts().stream()
                    .map(CartDataMapper::avroToDto)
                    .toList();
            String productsJson = objectMapper.writeValueAsString(products);
            CartDataEntity entity = CartDataEntity.builder()
                    .userId(userId)
                    .cartProducts(productsJson)
                    .build();
            cartDataRepository.save(entity);
            log.info("Cart data for userId: {} saved successfully to database.", userId);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for cart data of userId: {}", userId, e);
            throw new RuntimeException("Error processing cart data JSON", e);
        }
    }
}
