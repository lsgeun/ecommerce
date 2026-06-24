package io.github.lsgeun.ecommerce.service.product;

import io.github.lsgeun.ecommerce.controller.product.ProductDto;
import io.github.lsgeun.ecommerce.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    ProductDto.ReadResponse toReadResponse(Product product);

    Product toProduct(ProductDto.CreateRequest createRequest);
}
