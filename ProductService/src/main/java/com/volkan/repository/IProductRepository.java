package com.volkan.repository;

import com.volkan.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product,String> {
    List<Product> findByIdContainingIgnoreCaseAndNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String id, String name, String description);
    @Query(
            value = "SELECT * FROM tblproduct p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:name, '%'))",
            nativeQuery = true
    )
    List<Product> findProductByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
}
