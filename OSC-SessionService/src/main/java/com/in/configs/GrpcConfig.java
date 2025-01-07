package com.in.configs;

import com.in.grpc.proto.user.UserDataServiceGrpc;
import com.in.proto.session.SessionServiceGrpc;
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
    public ManagedChannel userManagedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
    }
    @Bean
    public ManagedChannel sessionManagedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9091).usePlaintext().build();
    }

    @Bean
    public UserDataServiceGrpc.UserDataServiceBlockingStub userServiceBlockingStub(){
        return UserDataServiceGrpc.newBlockingStub(userManagedChannel());
    }
    @Bean
    public SessionServiceGrpc.SessionServiceBlockingStub sessionServiceBlockingStub(){
        return SessionServiceGrpc.newBlockingStub(sessionManagedChannel());
    }
}
