package com.myproject.storeapi.controller;

import java.util.HashMap;
import java.util.List;

import com.myproject.storeapi.models.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.myproject.storeapi.models.Product;
import com.myproject.storeapi.repositories.ProductsRepository;

@RestController
@RequestMapping("/api/products")
public class ProductControllers {
    @Autowired
    private ProductsRepository repo;

    @GetMapping
    public List<Product> getProducts() {
        return repo.findAll();
    }


    //⬇️code checks whether a item is present/absent ,if present
    //returns it ,if absent returns error 404 in Postman Terminal
    @GetMapping("{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product =repo.findById(id).orElse(null);

        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Object>createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
            ){

        double price =0;
        try {
            price = Double.parseDouble(productDTO.getPrice());
        }catch(Exception ex){
            result.addError(new FieldError("productDto","price","The price should be a number"));

        }


        if (result.hasErrors()){
            var errorList =result.getAllErrors();
            var errorsMap =new HashMap<String,String>();
            for(int i =0;i< errorList.size();i++){
                var error =(FieldError) errorList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());

            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(price);
        product.setDescription(productDTO.getDescription());

        repo.save(product);

        return ResponseEntity.ok(product);
    }


    //updating an item
    @PutMapping("{id}")
    public ResponseEntity<Object>updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        Product product = repo.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        double price =0;
        try {
            price = Double.parseDouble(productDTO.getPrice());
        }catch(Exception ex){
            result.addError(new FieldError("productDto","price","The price should be a number"));

        }

        if (result.hasErrors()){
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String , String>();
            for (int i =0;i < errorsList.size();i++){
                var error =(FieldError) errorsList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(price);
        product.setDescription(productDTO.getDescription());

        repo.save(product);

        return ResponseEntity.ok(product);}

        @DeleteMapping("{id}")
        public ResponseEntity<Object>deleteProduct(@PathVariable int id){
            Product product =repo.findById(id).orElse(null);
            if (product == null){
                return  ResponseEntity.notFound().build();
            }

            repo.delete(product);
            return ResponseEntity.ok().build();
        }
    }

