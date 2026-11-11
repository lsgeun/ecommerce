package io.github.lsgeun.ecommerce.order.presentation;

import io.github.lsgeun.ecommerce.order.application.OrderItemCommand;
import io.github.lsgeun.ecommerce.order.domain.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    OrderItemCommand toItemCommand(OrderDto.Create.Request.Item item);

    List<OrderItemCommand> toItemCommands(List<OrderDto.Create.Request.Item> items);

    OrderDto.Response toResponse(Order order);
}
