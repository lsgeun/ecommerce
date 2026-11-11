package io.github.lsgeun.ecommerce.product.domain;

public interface ProductRepository {

    Product getByNumberForUpdate(String number);
}
