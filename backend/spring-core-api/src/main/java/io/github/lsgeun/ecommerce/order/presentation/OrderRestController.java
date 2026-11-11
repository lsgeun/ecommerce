package io.github.lsgeun.ecommerce.order.presentation;

import io.github.lsgeun.ecommerce.order.application.OrderItemCommand;
import io.github.lsgeun.ecommerce.order.application.OrderSimpleService;
import io.github.lsgeun.ecommerce.order.domain.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/orders")
@RestController
public class OrderRestController {

    private final OrderSimpleService orderSimpleService;
    private final OrderDtoMapper orderDtoMapper;

    @PostMapping
    public ResponseEntity<OrderDto.Response> createOrder(
        @RequestBody @Valid OrderDto.Create.Request createRequest
    ) {
        List<OrderItemCommand> commands = orderDtoMapper.toItemCommands(createRequest.getItems());

        Order createdOrder = orderSimpleService.createOrder(createRequest.getUserId(), commands);

        return ResponseEntity.ok(orderDtoMapper.toResponse(createdOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> getOrder(@PathVariable Long id) {
        Order order = orderSimpleService.getOrder(id);

        return ResponseEntity.ok(orderDtoMapper.toResponse(order));
    }
}
