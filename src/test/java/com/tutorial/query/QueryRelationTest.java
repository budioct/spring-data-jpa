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



}
