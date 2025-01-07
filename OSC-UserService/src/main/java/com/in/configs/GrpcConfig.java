package com.in.configs;

import com.in.proto.cart.CartDataServiceGrpc;
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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9094).usePlaintext().build();
    }

    @Bean
    public UserDataServiceGrpc.UserDataServiceBlockingStub userServiceBlockingStub(){
        return UserDataServiceGrpc.newBlockingStub(managedChannel());
    }

    @Bean
    public ManagedChannel sessionManagedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9091).usePlaintext().build();
    }

    @Bean
    public SessionServiceGrpc.SessionServiceBlockingStub sessionServiceBlockingStub(){
        return SessionServiceGrpc.newBlockingStub(sessionManagedChannel());
    }

    @Bean
    public ManagedChannel cartDataManagedChannel(){
        return ManagedChannelBuilder.forAddress("localhost",9099).usePlaintext().build();
    }
    @Bean
    public CartDataServiceGrpc.CartDataServiceBlockingStub cartDataServiceBlockingStub(){
        return CartDataServiceGrpc.newBlockingStub(cartDataManagedChannel());
    }
}
