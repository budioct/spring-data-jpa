package com.tutorial.crudrepository;

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
public class CrudRepositoryTest {

    /**
     * Crud Repository
     *
     * Category Repository
     * ● JpaRepository adalah turunan dari interface CrudRepository dan ListCrudRepository, dimana di
     *   interface tersebut banyak method yang bisa digunakan untuk melakukan operasi CRUD
     * ● Kita tidak perlu lagi menggunakan Entity Manager untuk melakukan operasi CRUD, cukup gunakan JpaRepository
     * ● Ada yang perlu diperhatikan di JpaRepository, method untuk CREATE dan UPDATE digabung
     *   dalam satu method save(), yang artinya method save() adalah CREATE or UPDATE
     *   ketika datanya belum ada method save() akan simpan
     *   ketika datanya sudah ada method save() akan update
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/ListCrudRepository.html
     *
     */

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateCategory(){

        Category category = new Category(); // entity
        category.setName("ALAT TULIS");

        categoryRepository.save(category); // <S extends T> S save(S entity) // simpan ke db

        Assertions.assertNotNull(category.getId()); // cek harusnya id ada karna kita sudah simpan

        log.info("success create data");
        log.info("id  : {}", category.getId());
        log.info("name: {}", category.getName());


        /**
         * result query:
         * Hibernate:
         *     insert
         *     into
         *         categories
         *         (name)
         *     values
         *         (?)
         */

    }

    @Test
    void testGetDataCategory(){

        List<Category> dataList = categoryRepository.findAll(); // List<T> findAll() // fetching data semua pada table category. dengan return List<T>

        Assertions.assertNotNull(dataList);

        log.info("success fetch data");
        for (Category category : dataList){
            log.info("id   : {}", category.getId());
            log.info("name : {}", category.getName());
        }

        /**
         * result query:
         * Hibernate:
         *     select
         *         c1_0.id,
         *         c1_0.name
         *     from
         *         categories c1_0
         */

    }

    @Test
    void testUpdateCategory(){

        // Optional<T> findById(ID id) // mencari id pada table
        // T orElse(T other) // jika tidak ada id maka kembalikan null
        Category category = categoryRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(category);

        category.setName("BUKU KOMIK"); // set lagi untuk update data

        categoryRepository.save(category); // <S extends T> S save(S entity) simpan ke db
        Assertions.assertNotNull(category);

        category = categoryRepository.findById(1L).orElse(null);
        Assertions.assertEquals("BUKU KOMIK", category.getName()); // cek harusnya name nya sudah di rubah

        log.info("success update data");
        log.info("id  : {}", category.getId());
        log.info("name: {}", category.getName());

        /**
         * result query:
         * Hibernate:
         *     update
         *         categories
         *     set
         *         name=?
         *     where
         *         id=?
         */

    }

    @Test
    void testDeleteCategory(){

        // Optional<T> findById(ID id) // mencari id pada table
        // T orElse(T other) // jika tidak ada id maka kembalikan null
        Category category = categoryRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(category.getId());

        // jika id ada maka hapus datanya
        if (category.getId() != null){
            categoryRepository.delete(category); // void delete(T entity) hapus data table
            log.info("success delete data");
        }

        /**
         * result query:
         * delete
         *     from
         *         categories
         *     where
         *         id=?
         */


    }



}
