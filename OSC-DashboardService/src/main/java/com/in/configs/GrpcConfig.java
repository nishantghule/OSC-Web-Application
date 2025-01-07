package com.in.configs;

import com.in.proto.product.DashboardProductsGrpc;
import com.in.proto.product.ProductsViewHistoryGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel managedChannel1() {
        return ManagedChannelBuilder.forAddress("localhost", 9098).usePlaintext().build();
    }

    @Bean
    public DashboardProductsGrpc.DashboardProductsBlockingStub productDataServiceStub(){
        return DashboardProductsGrpc.newBlockingStub(managedChannel1());
    }
    @Bean
    public ManagedChannel managedChannel2() {
        return ManagedChannelBuilder.forAddress("localhost", 9097).usePlaintext().build();
    }

    @Bean
    public ProductsViewHistoryGrpc.ProductsViewHistoryBlockingStub recentlyViewedBlockingStub(){
        return ProductsViewHistoryGrpc.newBlockingStub(managedChannel2());
    }
}
