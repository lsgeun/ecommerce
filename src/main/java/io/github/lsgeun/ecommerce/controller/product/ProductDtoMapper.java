package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    // Read
    ProductDto.ReadResponse toReadResponse(
        Product product
    );

    // Create
    default Product toProduct(
        ProductDto.CreateRequest createRequest
    ) {
        return Product.create(
            createRequest.getNumber(),
            createRequest.getName(),
            createRequest.getPrice(),
            createRequest.getStock(),
            createRequest.getStatus()
        );
    }

    ProductDto.CreateResponse toCreateResponse(
        Product product
    );

    // Update
    ProductDto.UpdateResponse toUpdateResponse(
        Product product
    );

    void updateFromDto(
        ProductDto.UpdateRequest updateRequest,
        @MappingTarget Product product
    );

    // Delete
    ProductDto.DeleteResponse toDeleteResponse(
        Product product
    );
}
