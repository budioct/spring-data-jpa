package com.tutorial.repository;

import com.tutorial.entity.Category;
import com.tutorial.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository // annotation @Repository optional bolah ada boleh tidak
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * kita akan implementasi Query Method Relation
     * karena di method tidak boleh menggunakan . akan di gantikan dengan _ (underscore)
     */
    // query method relasi: select * from products left join categories on (categories.id = product.category_id) where name=?
    List<Product> findAllByCategory_Name(String name);


    // hasil akan di sorting
    // query method relasi dengan sorting: select * from products left join categories on (categories.id = product.category_id) where name=? order by products.id desc
    List<Product> findAllByCategory_Name(String name, Sort sort);


    // mendapatkan offset and limit
    // query method relasi dengan pageable: select * from products left join categories on (categories.id = product.category_id) where name=? order by products.id desc limit ?, ?
    //List<Product> findAllByCategory_Name(String name, Pageable pageable);

    // ingin mendapatkan Page Result
    // query method relasi dengan pageable: select * from products left join categories on (categories.id = products.category_id) where name=? order by products.id desc limit ?, ?
    Page<Product> findAllByCategory_Name(String name, Pageable pageable);

    /**
     * Count Query Method
     */
    // ingin mendapatkan total data dari product berdasarkan relasi category dengan where name=?
    // query method relasi: select count(p.id) from product p left join categories c on (c.id = p.category_id) where c.name=?
    Long countByCategory_Name(String name);

    /**
     * Exist Query Method
     */
    // ingin mendapatkan apakah data ada atau tidak (untuk memastikan terlebih dahulu)
    // query method relasi: select p.id from products p where p.name=? limit ?
    boolean existsByName(String name);

    /**
     * Delete Query Method
     */
    // ingin menghapus data berdasarkan kolom name table product
    // query method relasi: delete from products where id=?
    @Transactional
    int deleteByName(String name);

    /**
     * Named Query Method
     */
    // binding named query yang ada di entity dengan query method di repository
    // @Param akan binding query paramaeter yand ada di query where name= :name
    List<Product> searchProductUsingName(@Param("name") String name);

    /**
     * Sorting dan Paging dengan Pageable
     */
    // binding named query yang ada di entity dengan query method di repository dan juga support Pageable untuk(sorting/paging)
    // @Param akan binding query paramaeter yand ada di query where name= :name
    List<Product> searchProductUsingName(@Param("name") String name, Pageable pageable);

    /**
     * Query Annotation
     * untuk query yang panjang dan dinamis
     */
    // query method relasi: SELECT p.* FROM products p join categories c on (c.id = p.category_id) WHERE p.name LIKE '%komik%' ESCAPE '' OR p.name LIKE '%BUKU%' ESCAPE '';
    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE :name OR p.category.name LIKE :name")
    List<Product> searchProduct(@Param("name") String name);

    /**
     * Query Annotation Sorting
     * untuk query yang panjang dan dinamis
     */
    // query method relasi: SELECT p.* FROM products p join categories c on (c.id = p.category_id) WHERE p.name LIKE '%komik%' ESCAPE '' OR p.name LIKE '%BUKU%' ESCAPE '';
    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE :name OR p.category.name LIKE :name")
    List<Product> searchProduct(@Param("name") String name, Pageable pageable);


    /**
     * @Modifying memberi tahu kalau ini bukan untuk query select
     * tetapi untuk query untuk update dan delete
     */
    @Modifying
    @Query(value = "DELETE FROM Product p WHERE p.name= :name")
    int deleteProductUsingName(@Param("name") String name); // jika query delete berhasil maka akan return int value 1

    @Modifying
    @Query(value = "UPDATE Product p SET p.price = 0 WHERE p.id= :id")
    int updateProductPriceToZero(@Param("id") Long id);


    /**
     * Stream<T>
     * ketika kita menggunakan List<T> dan query method findAll.. maka itu akan di simpan ke memory yang di takutkan adalah terjadi error OutOfMemory
     * kita bisa menangani dengan return Stream<T> dan query method streamAll.. ini bisa fetching data sedikit2 jika di perlukan dengan Java Stream
     */
    // query method relasi: SELECT p.* FROM products p WHERE p.category_id= ?
    Stream<Product> streamAllByCategory(Category category);


    /**
     * Slice<T> versi lengkap dari Page<T>, untuk mendapatkan Page Result
     */
    // query method relasi: SELECT p.* FROM products p WHERE p.category_id=? limit ?,?
    Slice<Product> findAllByCategory(Category category, Pageable pageable);


    /**
     * Locking Optimistis Locking (yang paling cepat dia dapat) atau Pesimistic Locking (Queue)
     * di spring jpa kita tinggal menggunakan @Lock tidak perlu lagi buat manual dengan EntityManager
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findFirstByIdEquals(Long id); // kita akan mencari id, karna id belum tentu ada kita return Optional<T> bisa menangani nullable

}
