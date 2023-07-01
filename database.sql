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

select *
from categories;

CREATE TABLE products
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    price       BIGINT       NOT NULL,
    category_id BIGINT       NOT NULL,
    primary key (id),
    foreign key fk_products_categories (category_id) REFERENCES categories (id)
) ENGINE = InnoDB;

select *
from categories;

select *
from products;

delete
from products
where id = 4;

select p.id,
       p.category_id,
       p.name,
       p.price
from products p
join categories c
     on c.id = p.category_id
where p.name like ? escape ''
    or c.name like ? escape '';

# query anotation product
SELECT p.* FROM products p join categories c on (c.id = p.category_id) WHERE p.name LIKE '%komik%' ESCAPE '' OR p.name LIKE '%BUKU%' ESCAPE '';


ALTER TABLE categories
    ADD COLUMN created_date TIMESTAMP;

ALTER TABLE categories
    ADD COLUMN last_modified_data TIMESTAMP;

ALTER TABLE categories
    RENAME COLUMN last_modified_data TO last_modified_date;

select * from categories;

delete from categories c where c.id = 49;

