package com.example.sop_finallab.controller;

import com.example.sop_finallab.pojo.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public boolean serviceAddProduct(@RequestBody Product p){
        System.out.println(p);
        try{
            rabbitTemplate.convertSendAndReceive("ProductExchange", "add", p);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public boolean serviceUpdateProduct(@RequestBody Product p){
        try{
            rabbitTemplate.convertSendAndReceive("ProductExchange", "update", p);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    public boolean serviceDeleteProduct(@RequestBody String id){
        try{
            rabbitTemplate.convertSendAndReceive("ProductExchange", "delete", id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    @RequestMapping(value = "/getname/{name}", method = RequestMethod.GET)
    public Product serviceGetProductName(@PathVariable("name") String name){
        try{
            Object p = rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", name);
            return (Product) p;
        }
        catch (Exception e) {
            return null;
        }
    }
    @RequestMapping(value = "/getAllProduct", method = RequestMethod.GET)
    public List<Product> serviceGetAllProduct(){
        try {
            Object p = rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "");
            List<Product> products = (List<Product>)p;
//            System.out.println(products.get(0) instanceof Product);
            return (List<Product>)p;
        }
        catch (Exception e){
            return null;
        }
    }
}
