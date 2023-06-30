package com.tutorial.declarativetransaction;

import com.tutorial.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeclarativeTransactionTest {

    /**
     * Declarative Transaction
     * ● Saat kita menggunakan JPA secara manual, kita harus melakukan management transaction secara
     *   manual menggunakan EntityManager
     * ● Spring menyediakan fitur Declarative Transaction, yaitu management transaction secara
     *   declarative, yaitu dengan menggunakan annotation @Transactional
     * ● Annotation ini secara otomatis dibaca oleh Spring AOP, dan akan menjalankan transaction secara
     *   otomatis ketika memanggil method yang terdapat annotation @Transactional nya
     * ● https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html
     *
     * Yang Perlu Diperhatikan (Note)
     * ● Saat membuat method dengan annotation @Transactional, karena dia dibungkus oleh Spring AOP,
     *   jadi untuk menjalankannya, kita harus memanggil method tersebut dari luar object
     * ● Misal kita memiliki CategoryService.create() dengan annotation @Transactional, jika kita panggil
     *   dari CategoryController, maka Spring AOP akan berjalan, namun jika dipanggil di
     *   CategoryService.test() misalnya, maka Spring AOP tidak akan berjalan
     */

    @Autowired
    CategoryService categoryService;

    @Test
    void testSuccesstGapTransactional(){

        // kita akan uji coba kepada method yang akan roolback ketika kena Exception RunTime di console
        // ini yang kita harapkan

        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.create(); // void create() // method yang sudah di set annotation @Transactional
        });

        // harusnya jika success maka datanya akan di roolback tidak akan di commit ke table db

    }

    @Test
    void testFailedGapWithoutTransactional(){

        // kita akan uji coba kepada Method yang tidak di kasih annotation @Transactional.. ini akan lolos dari Exception Runtime
        // ini yang tidak kita harapkan

        Assertions.assertThrows(RuntimeException.class, () -> {
           categoryService.test(); // void test() // method yang tidak di set annotation @Transactional
        });

        // harusnya jika success maka datanya tidak akan kena Gap dan akan di commit ke table

    }

}
