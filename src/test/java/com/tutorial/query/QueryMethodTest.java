package com.tutorial.query;

import com.tutorial.entity.Category;
import com.tutorial.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class QueryMethodTest {

    /**
     * Query Method
     * ● Saat kita menggunakan EntityManager, kita bisa membuat query menggunakan JPA QL, namun
     *   bagaimana jika menggunakan Repository?
     * ● Spring Data menyediakan fitur Query Method, yaitu membuat query menggunakan nama method secara otomatis
     * ● Spring Data akan melakukan penerjemahan secara otomatis dari nama method menjadi JPA QL
     *
     * Format Query Method
     * ● Untuk melakukan query yang mengembalikan data lebih dari satu, kita bisa gunakan prefix findAll…
     * ● Untuk melakukan query yang mengembalikan data pertama, kita bisa gunakan prefix findFirst…
     * ● Selanjutnya diikuti dengan kata By dan diikuti dengan operator query nya
     * ● Untuk operator query yang didukung, kita bisa lihat di halaman ini
     * ● https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
     *
     */

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void testQueryMethodfindFirstByNameEquals(){

        // Optional<Category> findFirstByNameEquals(String name) // method query "select name from table categories where name = ?"
        // T orElse(T other) // jika query name tidak akan return null
        Category category = categoryRepository.findFirstByNameEquals("ALAT TULIS").orElse(null);

        Assertions.assertNotNull(category);
        Assertions.assertEquals("ALAT TULIS", category.getName());

        log.info("data: {}", category.getName());

        /**
         * query result:
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.name=? limit ?
         */

    }

    @Test
    void testQueryMethodfindAllByNameLike(){

        List<Category> categories = categoryRepository.findAllByNameLike("%ALAT%");

        Assertions.assertEquals(1, categories.size());
        Assertions.assertEquals("ALAT TULIS", categories.get(0).getName());

        /**
         * query result:
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.name like ? escape '\\'
          */

    }

}
