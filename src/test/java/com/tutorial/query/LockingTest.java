package com.tutorial.query;

import com.tutorial.entity.Product;
import com.tutorial.repository.CategoryRepository;
import com.tutorial.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionOperations;

import java.util.function.Consumer;

@SpringBootTest
public class LockingTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TransactionOperations transactionOperations;

    /**
     * Locking
     * ● Di kelas JPA, kita sudah bahas melakukan Pessimistic Locking
     * ● Karena di Spring Data JPA, kita tidak perlu lagi menggunakan Entity Manager, bagaimana jika kita
     *   butuh melakukan Pessimistic Locking?
     * ● Kita bisa membuat Query Method dengan menambahkan annotation @Lock
     * ● https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/Lock.html
     */

    // jadi kita akan melakukan skema lock1() mengubah data price where = id
    // lock1() akan di thread.sleep selama 20 detika
    // dan lock2() akan jalan berbarengan juga saat lock2() berjalan, dia harus menunggu
    // sampai proses lock1() selesai
    // karena Locking nya menggunakan pesimistic locking QUEUE (pertama kali di insert pertama kali di commit)
    // saat lock1() commit, lock2() juga langsung commit yang dan hasil nya adalah paling update dari lock2()

    @Test
    void lock1(){

        // lock1() dengan Thread sleep 20 detik

        transactionOperations.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                try{

                    Product product = productRepository.findFirstByIdEquals(1L).orElse(null); // T orElse(T other)
                    Assertions.assertNotNull(product);

                    product.setPrice(100_000L);
                    Thread.sleep(20_000L);
                    productRepository.save(product);

                }catch (InterruptedException exception){
                    throw new RuntimeException(exception);
                }
            }
        });

        /**
         * Hibernate:
         *     select
         *         p1_0.id,
         *         p1_0.category_id,
         *         p1_0.name,
         *         p1_0.price
         *     from
         *         products p1_0
         *     where
         *         p1_0.id=? limit ? for update
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.id=?
         * Hibernate:
         *     update
         *         products
         *     set
         *         category_id=?,
         *         name=?,
         *         price=?
         *     where
         *         id=?
         */

    }

    @Test
    void lock2(){

        // lock2() tanpa Thread sleep
        transactionOperations.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {

                    Product product = productRepository.findFirstByIdEquals(1L).orElse(null); // T orElse(T other)
                    Assertions.assertNotNull(product);

                    product.setPrice(200_000L);
                    productRepository.save(product);

            }
        });

        /**
         * Hibernate:
         *     select
         *         p1_0.id,
         *         p1_0.category_id,
         *         p1_0.name,
         *         p1_0.price
         *     from
         *         products p1_0
         *     where
         *         p1_0.id=? limit ? for update
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.id=?
         * Hibernate:
         *     update
         *         products
         *     set
         *         category_id=?,
         *         name=?,
         *         price=?
         *     where
         *         id=?
         */

    }


}
