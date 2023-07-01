package com.tutorial.query;

import com.tutorial.entity.Category;
import com.tutorial.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

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

    /**
     * Example
     * ● Spring Data JPA memiliki fitur Query by Example, dimana kita bisa membuat data object Entity, lalu
     *   meminta Spring Data JPA untuk membuat Query berdasarkan data example Entity yang kita buat
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Example.html
     *
     * Example Repository
     * ● JpaRepository memiliki parent interface QueryByExampleExecutor
     * ● Dimana sudah disediakan banyak method yang bisa kita gunakan dengan parameter Example untuk mencari data
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/query/QueryByExampleExecutor.html
     */

    @Test
    void testQueryByExample(){

        Category category = new Category();
        category.setName("BUKU");

        Example<Category> example = Example.of(category); // interface Example<T> // static <T> Example<T> of(T probe) // buat yang baru Example termasuk semua propertry non-null secara default

        List<Category> categories = categoryRepository.findAll(example); // <S extends T> List<S> findAll(Example<S> example)
        Assertions.assertEquals(1, categories.size());

        /**
         * result query:
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.created_date,
         *         c1_0.last_modified_date,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         c1_0.name=?
         */

    }

    /**
     * Example Matcher
     * ● Example memiliki fitur Matcher, dimana kita bisa atur cara Example melakukan query
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/ExampleMatcher.html
     */

    @Test
    void testQueryByExampleMatcher(){

        Category category = new Category();
        category.setName("alat tulis");

        // interface ExampleMatcher
        // static ExampleMatcher matching() // akan di cocokan dengan query reflection object
        // ExampleMatcher withIgnoreCase() // akan mengabaikan huruf besar kecilnya yang ada di kolom
        // ExampleMatcher withIgnoreNullValues() // akan mengabaikan apakah null atau tidak null
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues();

        Example<Category> example = Example.of(category, matcher); // static <T> Example<T> of(T probe, ExampleMatcher matcher) // buat yang baru Example termasuk semua propertry non-null secara default

        List<Category> categories = categoryRepository.findAll(example); // <S extends T> List<S> findAll(Example<S> example)
        Assertions.assertEquals(1, categories.size());

        /**
         * result query:
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.created_date,
         *         c1_0.last_modified_date,
         *         c1_0.name
         *     from
         *         categories c1_0
         *     where
         *         lower(c1_0.name)=?
         */

    }


}
