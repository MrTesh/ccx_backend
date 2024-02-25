package com.example.repos;

import com.example.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    // returns all Products of an Author specified by authorId
    List<Product> findByAuthorId(Long authorsId);

    List<Product> findByNameContains(String name);
    List<Product> findByNameLike(String name);
    @Transactional
    void deleteByAuthorId(long authorId);
}
