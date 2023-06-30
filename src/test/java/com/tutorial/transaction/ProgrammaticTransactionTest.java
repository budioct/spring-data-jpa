package com.tutorial.transaction;

import com.tutorial.repository.CategoryRepository;
import com.tutorial.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProgrammaticTransactionTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void testProgrammaticSuccessGap(){

        // Programmatic Transaction
        // kita akan uji coba kepada method yang akan roolback ketika kena Exception RunTime di console
        // ini yang kita harapkan

        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.createCategories(); // void createCategories() // method yang sudah di set behavior insert data Programmatic Transaction
        });

        // harusnya jika success maka datanya akan di roolback tidak akan di commit ke table db

    }

    @Test
    void testManualSuccessGap(){
        // Platform Transaction Manager (manual)

        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.manual(); // void manual() // method yang sudah di set behavior insert data Platform Transaction Manager (low level manual)
        });

    }

}
