package com.tutorial.repository;

import com.tutorial.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    // @Param akan binding paramaeter yand ada di query where name= :name
    List<Product> searchProductUsingName(@Param("name") String name);

    /**
     * Sorting dan Paging dengan Pageable
     */
    List<Product> searchProductUsingName(@Param("name") String name, Pageable pageable);


}
