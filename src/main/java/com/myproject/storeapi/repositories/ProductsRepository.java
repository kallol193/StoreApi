package com.myproject.storeapi.repositories;

import com.myproject.storeapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product,Integer> {
}
