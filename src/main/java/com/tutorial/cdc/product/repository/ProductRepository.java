package com.tutorial.cdc.product.repository;

import com.tutorial.cdc.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    void removeProductByMongoId(String mongoId);

    Product findByMongoId(String mongoId);
}
