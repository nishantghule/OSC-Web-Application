package com.in.configs;

import com.in.proto.cart.CartDataServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    @Bean
    public ManagedChannel managedChannel(){
        return ManagedChannelBuilder.forAddress("localhost",9099).usePlaintext().build();
    }
    @Bean
    public CartDataServiceGrpc.CartDataServiceBlockingStub cartDataServiceBlockingStub(){
        return CartDataServiceGrpc.newBlockingStub(managedChannel());
    }
}
