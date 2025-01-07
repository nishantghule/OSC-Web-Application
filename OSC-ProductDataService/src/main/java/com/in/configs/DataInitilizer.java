package com.in.configs;

import com.in.dtos.CategoryDetailsDTO;
import com.in.dtos.ProductDetailsDTO;
import com.in.kafka.avro.category.CategoryDetails;
import com.in.kafka.avro.product.ProductDetails;
import com.in.kafka.avro.product.ProductViewCount;
import com.in.kafka.producer.CategoryDataProducer;
import com.in.kafka.producer.ProductViewCountProducer;
import com.in.kafka.producer.ProductsDataProducer;
import com.in.mappers.CategoryDataMapper;
import com.in.mappers.ProductDataMapper;
import com.in.services.CategoryDataService;
import com.in.services.ProductDataService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "data.initialize", havingValue = "true")
public class DataInitilizer {
    private final ProductDataService productDataService;
    private final CategoryDataService categoryDataService;
    private final ProductsDataProducer productsDataProducer;
    private final CategoryDataProducer categoryDataProducer;
    private final ProductViewCountProducer productViewCountProducer;
    private final Logger log = LogManager.getLogger(DataInitilizer.class);

    @EventListener(ApplicationReadyEvent.class)
    public void publishData(){
        log.info("publishing products and categories data to kafka topic");
        List<ProductDetailsDTO> productsList = productDataService.getAllProducts();
        List<CategoryDetailsDTO> categoryList = categoryDataService.getAllCategories();

        //publish products data to products topic
        for(ProductDetailsDTO product: productsList){
            ProductDetails avroProduct = ProductDataMapper.dtoToAvro(product);
            productsDataProducer.publishProductsData(product.getProductId(), avroProduct);
        }
        //publish categories data to category topic
        for(CategoryDetailsDTO category: categoryList){
            CategoryDetails avroCategory = CategoryDataMapper.dtoToAvro(category);
            categoryDataProducer.publishCategoryData(category.getCategoryId(), avroCategory);
        }

        for(ProductDetailsDTO product: productsList){
            ProductViewCount avroProductViewCount = ProductViewCount.newBuilder()
                    .setCategoryId(product.getCategoryId())
                    .setViewCount(0)
                    .build();
            productViewCountProducer.publishProductViewCount(product.getProductId(), avroProductViewCount);
        }
        log.info("published products and categories data to kafka topic successfully");

    }
}
