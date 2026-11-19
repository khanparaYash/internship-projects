package org.example.orderservice.mapper;


import com.example.dto.OrderItemResponseDto;
import com.example.dto.OrderRequestDto;
import com.example.dto.OrderResponseDto;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto toDto(Order product);

    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderRequestDto request);


    OrderItemResponseDto toItemDto(OrderItem product);

    default OffsetDateTime map(LocalDateTime value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default LocalDateTime map(OffsetDateTime value) {
        return value == null ? null : value.toLocalDateTime();
    }

    default OrderStatus mapDtoToEntity(com.example.dto.OrderStatus status) {
        return status == null ? null : OrderStatus.valueOf(status.name());
    }

    default com.example.dto.OrderStatus mapEntityToDto(OrderStatus status) {
        return status == null ? null : com.example.dto.OrderStatus.valueOf(status.name());
    }


}
