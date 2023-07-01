package com.tutorial.query;

import com.tutorial.entity.Category;
import com.tutorial.entity.Product;
import com.tutorial.repository.CategoryRepository;
import com.tutorial.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
@SpringBootTest
public class QueryRelationTest {

    /**
     * Query Relation
     * ● Saat kita belajar JPA, kita bisa melakukan query ke relasi Entity atau Embedded field secara
     *   otomatis menggunakan tanda . (titik)
     * ● Di Spring Data Repository, kita bisa gunakan _ (garis bawah) untuk menyebutkan bahwa itu adalah tanda . (titik) nya
     * ● Misal ProductRepository.findAllByCategory_Name(String)
     */

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void createMethodRelationProduct(){

        Category category = categoryRepository.findById(2L).orElse(null);

        {
            Product product = new Product();
            product.setName("komik");
            product.setPrice(25_000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        {
            Product product = new Product();
            product.setName("masak");
            product.setPrice(35_000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        /**
         * result query:
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.id=?
         * Hibernate:
         *     insert
         *     into
         *         products
         *         (category_id, name, price)
         *     values
         *         (?, ?, ?)
         * Hibernate:
         *     insert
         *     into
         *         products
         *         (category_id, name, price)
         *     values
         *         (?, ?, ?)
         */

    }

    @Test
    void findMethodRelationProduct(){

        List<Product> products = productRepository.findAllByCategory_Name("BUKU");

        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals("komik", products.get(0).getName());
        Assertions.assertEquals("masak", products.get(1).getName());

        for (Product product : products){
            log.info("id: {}", product.getId());
            log.info("name: {}", product.getName());
            log.info("price: {}", product.getPrice());
            log.info("category_id: {}", product.getCategory().getId());
        }

        /**
         * query result:
         * Hibernate:
         *     select
         *         p1_0.id,
         *         p1_0.category_id,
         *         p1_0.name,
         *         p1_0.price
         *     from
         *         products p1_0
         *     left join
         *         categories c1_0
         *             on c1_0.id=p1_0.category_id
         *     where
         *         c1_0.name=?
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.id=?
         */

    }

    /**
     * Sorting
     * ● Spring Data Repository juga memiliki fitur untuk melakukan Sorting, caranya kita bisa tambahkan
     *   parameter Sort pada posisi parameter terakhir
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Sort.html
     */

    @Test
    void testFindProductSort(){
        Sort sort = Sort.by(Sort.Order.desc("id")); // static Sort by(Order... orders) // order by kita ubah urutanya menjadi desceding
        List<Product> products = productRepository.findAllByCategory_Name("BUKU", sort);

        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals("masak", products.get(0).getName());
        Assertions.assertEquals("komik", products.get(1).getName());

        /**
         * query result:
         * Hibernate:
         *     select
         *         p1_0.id,
         *         p1_0.category_id,
         *         p1_0.name,
         *         p1_0.price
         *     from
         *         products p1_0
         *     left join
         *         categories c1_0
         *             on c1_0.id=p1_0.category_id
         *     where
         *         c1_0.name=?
         *     order by
         *         p1_0.id desc
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.id=?
         */

    }

    /**
     * Paging
     * ● Selain Sort, Spring Data Repository juga mendukung paging seperti di EntityManager
     * ● Caranya kita bisa tambahkan parameter Pageable di posisi terakhir parameter
     * ● Pageable adalah sebuah interface, biasanya kita akan menggunakan PageRequest sebagai class implementasinya
     * ● Dan jika sudah menggunakan Pageable, kita tidak perlu lagi menggunakan Sort, karena sudah bisa dihandle oleh Pageable
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageRequest.html
     */

    @Test
    void testFindProductWithPageable(){

        // halaman ke 1 dan ukuran datanya
        PageRequest pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id"))); // static PageRequest of(int page, int size, Sort sort) // kita bisa atur offset and limit juga order by pada query method
        List<Product> products = productRepository.findAllByCategory_Name("BUKU", pageable);

        // hasil sudah di kasih offset dan limiet juga order by dalam query nya
        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("masak", products.get(0).getName());

        // halaman ke 2 dan ukuran datanya
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id"))); // static PageRequest of(int page, int size, Sort sort) // kita bisa atur offset and limit juga order by pada query method
        products = productRepository.findAllByCategory_Name("BUKU", pageable);

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("komik", products.get(0).getName());

        /**
         * query result:
         * Hibernate:
         *     select
         *         p1_0.id,
         *         p1_0.category_id,
         *         p1_0.name,
         *         p1_0.price
         *     from
         *         products p1_0
         *     left join
         *         categories c1_0
         *             on c1_0.id=p1_0.category_id
         *     where
         *         c1_0.name=?
         *     order by
         *         p1_0.id desc limit ?,
         *         ?
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.id=?
         */
    }





}
