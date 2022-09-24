package com.example.sop_finallab.repository;

import com.example.sop_finallab.pojo.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value="{_id:'?0'}")
    public Product findByid(String id);
    @Query(value="{productName:'?0'}")
    public Product findByName(String name);
}
