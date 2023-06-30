package com.tutorial.repository;

import com.tutorial.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // @Repository opsional boleh di kasih, boleh tidak
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * ini adalah class repository yang management komunikasi query ke DB. ini sebagai penganti EntityManagerFactory dan EntityManager
     *
     * untuk bisa menggunakan Repository kita perlu extends object interface JpaRepository<T, ID> yang sudah di sediakan spring data jpa
     * T adalah class entity yang di buat
     * ID adalah id class entity yang menjadi primaryKey
     *
     * kita tidak perlu lagi membuat method implementasi query ke db.. karena semua sudah di sediakan oleh spring data jpa dengan caranya
     *
     */

    // Query Method (adalah query yang sederhana fitur dari spring boot data jpa). kita tidak pelu membuat JPA QL untuk kasus query yang sederhana
    // where name = ?
    Optional<Category> findFirstByNameEquals(String name);

    // where name like
    List<Category> findAllByNameLike(String name);


}
