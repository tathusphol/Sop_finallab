package com.example.sop_finallab.repository;

import com.example.sop_finallab.pojo.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    private List<Product> plist = new ArrayList<>();
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = "GetAllProductQueue")
    public List<Product> getAllProduct(){
        plist = this.productRepository.findAll();
        try {
            return plist;
        }
        catch (Exception e){
            return null;
        }
    }

    @RabbitListener(queues = "GetNameProductQueue")
    public Product getProductByName(String name){
        return productRepository.findByName(name);
    }
    @RabbitListener(queues = "AddProductQueue")
    public boolean addProduct(Product p){
        try{
            productRepository.save(p);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    @RabbitListener(queues = "DeleteProductQueue")
    public boolean deleteProduct(String id){
        Product p = productRepository.findByid(id);
        try { productRepository.delete(p);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    @RabbitListener(queues = "UpdateProductQueue")
    public boolean updateProduct(Product p){
        try{
            productRepository.save(p);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
