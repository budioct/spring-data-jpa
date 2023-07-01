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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.support.TransactionOperations;

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

//    @Test
//    void testFindProductWithPageable(){
//
//        // halaman ke 0 dan ukuran datanya
//        PageRequest pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id"))); // static PageRequest of(int page, int size, Sort sort) // kita bisa atur offset and limit juga order by pada query method
//        List<Product> products = productRepository.findAllByCategory_Name("BUKU", pageable);
//
//        // hasil sudah di kasih offset dan limiet juga order by dalam query nya
//        Assertions.assertEquals(1, products.size());
//        Assertions.assertEquals("masak", products.get(0).getName());
//
//        // halaman ke 1 dan ukuran datanya
//        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id"))); // static PageRequest of(int page, int size, Sort sort) // kita bisa atur offset and limit juga order by pada query method
//        products = productRepository.findAllByCategory_Name("BUKU", pageable);
//
//        Assertions.assertEquals(1, products.size());
//        Assertions.assertEquals("komik", products.get(0).getName());
//
//        /**
//         * query result:
//         * Hibernate:
//         *     select
//         *         p1_0.id,
//         *         p1_0.category_id,
//         *         p1_0.name,
//         *         p1_0.price
//         *     from
//         *         products p1_0
//         *     left join
//         *         categories c1_0
//         *             on c1_0.id=p1_0.category_id
//         *     where
//         *         c1_0.name=?
//         *     order by
//         *         p1_0.id desc limit ?,
//         *         ?
//         * Hibernate:
//         *     select
//         *         c1_0.id,
//         *         c1_0.name
//         *     from
//         *         categories c1_0
//         *     where
//         *         c1_0.id=?
//         */
//    }

    /**
     * Page Result
     * ● Saat kita menggunakan Paging, kadang kita ingin tahu seperti jumlah total data hasil query, dan juga total page nya
     * ● Hal ini biasanya kita akan lakukan dengan cara manual dengan cara menghitung count dari hasil total hasil query tanpa paging
     * ● Untungnya, Spring Data JPA menyediakan return value berupa Page<T>, dimana secara otomatis akan diambil informasi total data dan total page nya
     * ● https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html
     */

    @Test
    void testFindProductWithPageableResultPage(){

        // halaman ke 0 dan ukuran datanya
        PageRequest pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id"))); // static PageRequest of(int page, int size, Sort sort) // kita bisa atur offset and limit juga order by pada query method
        Page<Product> products = productRepository.findAllByCategory_Name("BUKU", pageable);

        // hasil sudah di kasih offset dan limiet juga order by dalam query nya
        Assertions.assertEquals(1, products.getContent().size()); // List<T> getContent() // resutn kontent halaman sebagai List<T>
        Assertions.assertEquals(0, products.getNumber()); // int getNumber() // return jumlah arus Slice (sekarang berada di halaman)
        Assertions.assertEquals(2, products.getTotalElements()); // long getTotalElements() // return jumlah total element
        Assertions.assertEquals(2, products.getTotalPages()); // int getTotalPages() // return jumlah total halaman
        Assertions.assertEquals("masak", products.getContent().get(0).getName()); // // List<T> getContent() // E get(int index) // get field berdasarkan index list

        // halaman ke 1 dan ukuran datanya
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id"))); // static PageRequest of(int page, int size, Sort sort) // kita bisa atur offset and limit juga order by pada query method
        products = productRepository.findAllByCategory_Name("BUKU", pageable);

        Assertions.assertEquals(1, products.getContent().size());
        Assertions.assertEquals(1, products.getNumber());
        Assertions.assertEquals(2, products.getTotalElements());
        Assertions.assertEquals(2, products.getTotalPages());
        Assertions.assertEquals("komik", products.getContent().get(0).getName());

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
         * Hibernate:
         *     select
         *         count(p1_0.id)
         *     from
         *         products p1_0
         *     left join
         *         categories c1_0
         *             on c1_0.id=p1_0.category_id
         *     where
         *         c1_0.name=?
         */
    }

    /**
     * Count Query Method
     * ● JPA Repository juga bisa digunakan untuk membuat count query method
     * ● Cukup gunakan prefix method countBy…
     * ● Selebihnya kita bisa membuat format seperti Query Method biasanya
     */

    @Test
    void testCountProductRelasiCategory(){

        long count = productRepository.count(); // long count() // return jumlah data entity yang tersedia
        Assertions.assertEquals(2L, count);

        // query method
        count = productRepository.countByCategory_Name("BUKU");
        Assertions.assertEquals(2L, count);

        /**
         * query result:
         * Hibernate:
         *     select
         *         count(*)
         *     from
         *         products p1_0
         * Hibernate:
         *     select
         *         count(p1_0.id)
         *     from
         *         products p1_0
         *     left join
         *         categories c1_0
         *             on c1_0.id=p1_0.category_id
         *     where
         *         c1_0.name=?
         */
    }

    /**
     * Exist Query Method (cek apakah ada datanya)
     * ● Selain Count, kita juga bisa membuat Exists method di Query Method
     * ● Method ini sebenarnya sederhana, return value nya adalah boolean, untuk mengecek apakah ada
     *   data sesuai dengan Query Method atau tidak
     * ● Untuk membuatnya kita bisa gunakan prefix existsBy…
     */

    @Test
    void testExistProductsRelasiCategory(){

        boolean exists = productRepository.existsByName("masak");
        Assertions.assertTrue(exists);

        // test noExist / tidak ada
        exists = productRepository.existsByName("naruto");
        Assertions.assertFalse(exists);

        /**
         * query result:
         * Hibernate:
         *     select
         *         p1_0.id
         *     from
         *         products p1_0
         *     where
         *         p1_0.name=? limit ?
         */
    }

    /**
     * Delete Query Method
     * ● Kita juga bisa membuat delete Query Method dengan prefix deleteBy
     * ● Untuk delete, kita bisa return int sebagai penanda jumlah record yang berhasil di hapus
     * ● Untuk membuat delete query method, kita bisa gunakan prefix deleteBy…
     */

    @Autowired
    private TransactionOperations transactionOperations; // object untuk melakukan prgorammatic transaction yang sudah otomatis di spring boot

    @Test
    void testDeleteProduct(){
        // TransactionOperations ini akan berjalan satu transaksi jika ada masalah akan di roolback tidak akan di commit ke table
        transactionOperations.executeWithoutResult(transactionStatus -> {
            // kita find id category dengan id 2
            Category category = categoryRepository.findById(2L).orElse(null);
            Assertions.assertNotNull(category);

            // tambahkan data product ke id yang ada di category
            Product product = new Product();
            product.setName("Naruto");
            product.setPrice(30_000L);
            product.setCategory(category);
            productRepository.save(product);

            // hapus data table product dengan berdasarkan nama yang ada
            int delete = productRepository.deleteByName("Naruto"); // jika ada data naruto di table maka hapus. jika berhasil di hapus akan return 1
            Assertions.assertEquals(1, delete);

            // test no exist data
            delete = productRepository.deleteByName("Naruto"); // jika ada data naruto di table maka hapus. jika berhasil di hapus akan return 1
            Assertions.assertEquals(0, delete);

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
             *     select
             *         p1_0.id,
             *         p1_0.category_id,
             *         p1_0.name,
             *         p1_0.price
             *     from
             *         products p1_0
             *     where
             *         p1_0.name=?
             *** Hibernate:
             *     delete
             *     from
             *         products
             *     where
             *         id=?
             */

        });


    }





}
