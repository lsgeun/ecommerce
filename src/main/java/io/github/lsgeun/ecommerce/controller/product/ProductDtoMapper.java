package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    // Read
    ProductDto.ReadResponse toReadResponse(Product product);

    // Create
    Product toProduct(ProductDto.CreateRequest createRequest);

    ProductDto.CreateResponse toCreateResponse(Product product);

    // Update
    Product toProduct(ProductDto.UpdateRequest updateRequest);

    ProductDto.UpdateResponse toUpdateResponse(Product product);

    // Delete
    ProductDto.DeleteResponse toDeleteResponse(Product product);
}
