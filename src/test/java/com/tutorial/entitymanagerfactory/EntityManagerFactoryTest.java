package com.tutorial.entitymanagerfactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityManagerFactoryTest {

    /**
     * Entity Manager Factory
     * ● Selain DataSource, Spring Boot juga secara otomatis membuatkan bean EntityMangerFactory,
     *   sehingga kita tidak perlu membuatnya secara manual. seperti persist.xml di data source dan di koneksikan ke Entity Manager Factory
     * ● Itu semua secara otomatis dibuat oleh Spring Boot
     * ● https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaAutoConfiguration.java
     */

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testEntityManagerFactoryExist() {
        Assertions.assertNotNull(entityManagerFactory); // cek sebernya EntityManagerFactory datanya sudah ada karna di handle spring boot

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Assertions.assertNotNull(entityManager);

        entityManager.close();

    }

}
