package io.github.lsgeun.ecommerce.domain.product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findByNumber(String number);
    Product getByNumber(String Number);
    Product create(Product product);
    Product delete(Product product);
    Product deleteByNumber(String number);
    boolean validateNotExistsByNumber(String Number);
}
