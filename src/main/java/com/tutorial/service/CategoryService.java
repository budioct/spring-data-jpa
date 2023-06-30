package com.tutorial.service;

import com.tutorial.entity.Category;
import com.tutorial.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

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
     * Yang Perlu Diperhatikan
     * ● Saat membuat method dengan annotation @Transactional, karena dia dibungkus oleh Spring AOP,
     *   jadi untuk menjalankannya, kita harus memanggil method tersebut dari luar object
     * ● Misal kita memiliki CategoryService.create() dengan annotation @Transactional, jika kita panggil
     *   dari CategoryController, maka Spring AOP akan berjalan, namun jika dipanggil di
     *   CategoryService.test() misalnya, maka Spring AOP tidak akan berjalan
     *
     *   Transaction Propagation
     * ● Saat kita membuat method dengan annotation @Transactional, kita mungkin didalamnya
     *   memanggil method @Transactional lainnya
     * ● Pada kasus seperti itu, ada baiknya kita mengerti tentang attribute propagation pada @Transactional
     * ● Kita bisa memilih nilai apa yang ingin kita gunakan
     * ● https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Propagation.html
     */

    @Transactional
    public void create(){

        // jika ingin insert data ke table category dengan interasi 5 data, tetapi setelah proses loop nya selesai kita ingin exception runntime
        // supaya datanya di roolback tidak di commit ke table
        // ingi tau apakah proses itu berjalan maka dari itu kita menggunakan annotation @Transactional untuk handle kasus seperti ini

        for (int i = 0; i < 5; i++){
            Category category = new Category();
            category.setName("Category " + i);
            categoryRepository.save(category); // <S extends T> S save(S entity) // simpan data ke db

        }
        throw new RuntimeException("Ups rollback please"); // set exception supaya roolback, dan akan di Gap @Transactional
    }

    public void test(){
        create(); // void create() // memangil method di atasnya.. method ini tidak akan di Gap dan datanya di teruskan commit ke db
        // @Transactional tidak bekerja karena AOP nya tidak berjalan.
        // namun jika dipanggil di CategoryService.test() misalnya, maka Spring AOP tidak akan berjalan
    }

}
