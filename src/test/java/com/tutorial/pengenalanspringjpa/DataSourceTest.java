package com.tutorial.pengenalanspringjpa;

public class DataSourceTest {

    /**
     * DataSource
     * ● Salah satu keuntungan menggunakan Spring Data JPA dan Spring Boot adalah, semua upacara yang
     *   biasa kita lakukan ketika menggunakan JPA, sudah dilakukan oleh Spring Boot
     * ● Jadi kita tidak perlu membuat DataSource secara manual, karena sudah otomatis dibuat oleh Spring Boot
     * ● https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration.java
     * ● Untuk mengubah konfigurasi DataSource, kita cukup menggunakan resources/application.properties saja
     * ● Kita bisa lihat semua konfigurasinya dengan prefix spring.datasource.*
     * ● https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.data
     *
     * Konfigurasi JPA
     * ● Untuk melakukan konfigurasi JPA, kita juga tidak perlu melakukannya secara manual lagi di file persistence.xml
     * ● Secara otomatis JPA akan menggunakan DataSource di Spring, dan jika kita butuh mengubah
     *   konfigurasi, kita bisa menggunakan properties dengan prefix spring.jpa.*
     * ● https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html
     *
     * code konfigurasi jpa di application.properties
     * # set database
     * spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
     * spring.datasource.username=root
     * spring.datasource.password=root
     * spring.datasource.url=jdbc:mysql://localhost:3306/belajar_spring_data_jpa_ver_latest
     *
     * # pooling connection db
     * spring.datasource.type=com.zaxxer.hikari.HikariDataSource
     * spring.datasource.hikari.minimum-idle=5
     * spring.datasource.hikari.maximum-pool-size=10
     *
     * # show debug sql saat development
     * spring.jpa.properties.hibernate.show_sql=true
     * spring.jpa.properties.hibernate.format_sql=true
     *
     */

}
