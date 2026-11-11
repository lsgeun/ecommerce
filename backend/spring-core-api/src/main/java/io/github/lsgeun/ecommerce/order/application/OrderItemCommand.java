package io.github.lsgeun.ecommerce.order.application;

public record OrderItemCommand(String productNumber, int quantity) {
}
