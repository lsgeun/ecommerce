package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    // Read
    ProductDto.ReadResponse toReadResponse(Product product);

    // Create
    default Product toProduct(ProductDto.CreateRequest createRequest) {
        return Product.create(
            createRequest.getNumber(),
            createRequest.getName(),
            createRequest.getPrice(),
            createRequest.getStock(),
            createRequest.getStatus()
        );
    }

    ProductDto.CreateResponse toCreateResponse(Product product);

    // Update
    ProductDto.UpdateResponse toUpdateResponse(Product product);

    default Product toProduct(ProductDto.UpdateRequest updateRequest) {
        return Product.create(
            updateRequest.getNumber(),
            updateRequest.getName(),
            updateRequest.getPrice(),
            updateRequest.getStock(),
            updateRequest.getStatus()
        );
    }

    // Delete
    ProductDto.DeleteResponse toDeleteResponse(Product product);
}
