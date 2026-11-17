-- H2(local, test)와 MySQL(dev)에서 모두 실행되도록 두 DB가 공통으로 지원하는 문법만 사용

CREATE TABLE product_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    number VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    price BIGINT NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_product_number UNIQUE (number)
);

CREATE TABLE order_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    status ENUM ('CREATED') NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE order_item_tb (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_number VARCHAR(50) NOT NULL,
    product_name VARCHAR(50) NOT NULL,
    unit_price BIGINT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES order_tb (id)
);
