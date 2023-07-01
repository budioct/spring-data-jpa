package com.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@EntityListeners({AuditingEntityListener.class}) // supaya bisa aktif create_data dan last_modified_data saat query
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createDate; // return bisa Date, Timestamps, Instance atau Long(milis) // otomatis insert dari spring data jpa

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate; // return bisa Date, Timestamps, Instance atau Long(milis) // otomatis insert dari spring data jpa

    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
