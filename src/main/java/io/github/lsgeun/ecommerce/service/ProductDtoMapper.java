package io.github.lsgeun.ecommerce.service;

import io.github.lsgeun.ecommerce.domain.Product;
import io.github.lsgeun.ecommerce.domain.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    ProductDto.ReadResponse toReadResponse(Product product);
}
