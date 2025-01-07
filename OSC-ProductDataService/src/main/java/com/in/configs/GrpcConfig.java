package com.in.configs;

import com.in.proto.product.ProductsViewHistoryGrpc;
import com.in.proto.session.SessionServiceGrpc;
import com.in.proto.user.UserDataServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9097).usePlaintext().build();
    }

    @Bean
    public ProductsViewHistoryGrpc.ProductsViewHistoryBlockingStub recentlyViewedBlockingStub(){
        return ProductsViewHistoryGrpc.newBlockingStub(managedChannel());
    }

}
