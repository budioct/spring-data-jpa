package com.tutorial.service;

import com.tutorial.entity.Category;
import com.tutorial.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;

import java.util.function.Consumer;

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

    /**
     * Programmatic Transaction
     * ● Fitur Declarative Transaction sangat mudah untuk digunakan, karena hanya butuh menggunakan annotation
     * ● Namun pada beberapa kasus, misal kode yang kita buat butuh jalan secara async misal nya, maka
     *   Declarative Transaction tidak akan berjalan, mau tidak mau biasanya kita akan melakukan manual transaction management lagi
     * ● Kita bisa gunakan cara lama menggunakan Entity Manager, atau kita bisa menggunakan fitur
     *   Spring untuk melakukan management transaction secara manual
     * ● Ada beberapa cara untuk melakukan programmatic transaction di Spring
     *
     * Transaction Operations
     * ● Pada kasus yang sederhana, kita bisa menggunakan TransactionOperations
     * ● Kita bisa menggunakan bean TransactionOperations yang sudah secara otomatis dibuat oleh Spring Boot
     * ● https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/support/TransactionOperations.html
     *
     *  // <T> T execute(TransactionCallback<T> action) = Jalankan tindakan yang ditentukan oleh objek panggilan balik yang diberikan dalam transaksi. return value
     *  // void executeWithoutResult(Consumer<TransactionStatus> action) = Jalankan tindakan yang ditentukan oleh yang diberikan Runnable dalam transaksi. tidak return value
     *  // TransactionOperations withoutTransaction() = Kembalikan implementasi antarmuka Transaction Operations yang mengeksekusi yang diberikan Transaction Call back tanpa transaksi aktual.
     *
     */

    @Autowired
    private TransactionOperations transactionOperations; // object untuk melakukan prgorammatic transaction yang sudah otomatis di spring boot

    public void error(){
        // method Exception runtime
        throw new RuntimeException("Ups Error: must roolback data");
    }

    public void createCategories(){
        // kita akan coba bikin createCategories() di dalamnya kita melakukan transaksi tapi menggunakan programmatic
        // jadi ketika method ini melakukan insert data sebanyak 5 kali kita akan buat error supaya dia rollback tidak di commit ke table db
        transactionOperations.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                for (int i = 0; i < 5; i++){
                    Category category = new Category();
                    category.setName("Category " + i);
                    categoryRepository.save(category);
                }
                error();
            }
        });

    }

    /**
     * Platform Transaction Manager Low Level (native/ manual)
     * ● Jika kita butuh melakukan management transaction secara low level, maka sebenarnya kita bisa
     *   menggunakan Entity Manager, namun hal itu tidak disarankan
     * ● Kita bisa menggunakan Platform Transaction Manager yang sudah disediakan oleh Spring
     * ● https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/PlatformTransactionManager.html
     * ● Penggunaan ini sangat manual, sehingga kita bisa atur semuanya secara manual
     */

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void manual(){
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(); // object konfigurasi propagation transaction secara manual
        definition.setTimeout(10); // final void setTimeout(int timeout) // tetapkan batas waktu yang di tentukan
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); // Tetapkan perilaku propagasi.

        TransactionStatus transaction = transactionManager.getTransaction(definition); // TransactionStatus getTransaction(@Nullable TransactionDefinition definition) // mirip entityManager.getTransaction

        try{
            for (int i = 0; i < 5; i++){
                Category category = new Category();
                category.setName("Category " + i);
                categoryRepository.save(category);
            }
            error();
            transactionManager.commit(transaction);
        }catch (Throwable throwable){
            transactionManager.rollback(transaction);
            throw throwable;
        }

    }

}
