CREATE DATABASE belajar_spring_data_jpa_ver_latest;

SHOW DATABASES;

USE belajar_spring_data_jpa_ver_latest;

CREATE TABLE categories
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

show tables;

select * from categories;

CREATE TABLE products
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    price       BIGINT       NOT NULL,
    category_id BIGINT      NOT NULL ,
    primary key (id),
    foreign key fk_products_categories (category_id) REFERENCES categories (id)
) ENGINE = InnoDB;

select * from categories;
select * from products;

