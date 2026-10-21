package org.example.orderservice.mapper;

import org.example.orderservice.dto.OrderItemResponseDto;
import org.example.orderservice.dto.OrderRequestDto;
import org.example.orderservice.dto.OrderResponseDto;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto toDto(Order product);

    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderRequestDto request);


    OrderItemResponseDto toItemDto(OrderItem product);



}
