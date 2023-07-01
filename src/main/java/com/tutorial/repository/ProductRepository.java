package com.tutorial.repository;

import com.tutorial.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // annotation @Repository optional bolah ada boleh tidak
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * kita akan implementasi Query Method Relation
     * karena di method tidak boleh menggunakan . akan di gantikan dengan _ (underscore)
     */

    // query method relasi: select * from products left join categories on (categories.id = product.category_id) where name = ?
    List<Product> findAllByCategory_Name(String name);


    // query method relasi dengan sorting: select * from products left join categories on (categories.id = product.category_id) where name = ? order by products.id desc
    List<Product> findAllByCategory_Name(String name, Sort sort);


    // query method relasi dengan pageable: select * from products left join categories on (categories.id = product.category_id) where name = ? order by products.id desc limit ?, ?
    List<Product> findAllByCategory_Name(String name, Pageable pageable);

}
