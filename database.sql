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