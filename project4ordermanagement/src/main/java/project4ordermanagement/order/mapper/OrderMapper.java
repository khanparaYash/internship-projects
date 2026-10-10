package project4ordermanagement.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project4ordermanagement.order.dto.OrderRequestDto;
import project4ordermanagement.order.dto.OrderItemResponseDto;
import project4ordermanagement.order.dto.OrderResponseDto;
import project4ordermanagement.order.entity.Order;
import project4ordermanagement.order.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto toDto(Order product);

    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderRequestDto request);


    OrderItemResponseDto  toItemDto(OrderItem product);



}
