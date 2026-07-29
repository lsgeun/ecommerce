package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    // Read
    ProductDto.Read.Response toReadResponse(Product product);

    // Create
    Product toProduct(ProductDto.Create.Request createRequest);

    ProductDto.Create.Response toCreateResponse(Product product);

    // Update
    Product toProduct(ProductDto.Update.Request updateRequest);

    ProductDto.Update.Response toUpdateResponse(Product product);

    // Delete
    ProductDto.Delete.Response toDeleteResponse(Product product);
}
