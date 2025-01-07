package com.in.mappers;

import com.in.dtos.RecentlyViewedProductDTO;
import com.in.dtos.UpdateRecentlyViewedDTO;
import com.in.entities.RecentlyViewedEntity;
import com.in.kafka.avro.RecentlyViewedHistory;
import com.in.proto.product.UpdateRecentlyViewedProductsRequest;

import java.util.List;

public class DataMapper {
    public static RecentlyViewedHistory dtoToAvro(List<String> productIds){
        return RecentlyViewedHistory.newBuilder()
                //.setProductIds(new ArrayList<>(productIds))
                .setProductIds(productIds)
                .build();
    }
    public static UpdateRecentlyViewedProductsRequest dtoToRequest(UpdateRecentlyViewedDTO dto){
        return UpdateRecentlyViewedProductsRequest.newBuilder()
                .setUserId(dto.getUserId())
                .setProductId(dto.getProductId())
                .build();
    }
    public static UpdateRecentlyViewedDTO requestToDto(UpdateRecentlyViewedProductsRequest request){
        return new UpdateRecentlyViewedDTO(request.getUserId(), request.getProductId());
    }
    public static RecentlyViewedEntity dtoToEntity(RecentlyViewedProductDTO dto){
        return new RecentlyViewedEntity(dto.getUserId(), dto.getProductIds());
    }
}
