package com.example.controller;


import com.example.DTO.ProductDTO;
import com.example.entity.Author;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.repos.AuthorsRepository;
import com.example.repos.CategoryRepository;
import com.example.repos.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(path = "/api")
public class ProductsController {

    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public String decodeUrl(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public String decodeMap(Map<String, String> dict) throws UnsupportedEncodingException{
        for (Map.Entry<String, String> entry: dict.entrySet()){
            String productName = entry.getValue();
            return URLDecoder.decode(productName, StandardCharsets.UTF_8);
        }
        return null;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("products-contain/{name}")
    public ResponseEntity <List<Product>> getProductByName(@PathVariable(value = "name") String name){
        var foundProducts = productsRepository.findByNameLike("%" + name + "%");
        return new ResponseEntity<>(foundProducts, HttpStatus.OK);
//        if (foundProducts.isEmpty()){
//            System.out.println("did not find products, that contain these letters");
//            return new ResponseEntity<>(foundProducts, HttpStatus.NO_CONTENT);
//        }else{
//            System.out.println("found products");
//            return new ResponseEntity<>(foundProducts, HttpStatus.OK);
//        }

    }

    //categoryId authorId

    @CrossOrigin(origins = "*")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam (value = "authorId", required=false) Long authorId,
                                                        @RequestParam (value = "categoryId", required = false) Long categoryId,
                                                        @RequestParam (value = "filters", required = false) Map<String, String> filters) throws UnsupportedEncodingException {
        if (authorId != null){
            Author author = authorsRepository.findById(authorId).orElseThrow();
            return new ResponseEntity<>(author.getProducts(), HttpStatus.OK);

        }else if (categoryId != null){
            Category category= categoryRepository.findById(categoryId).orElseThrow();
            return new ResponseEntity<>(category.getProducts(), HttpStatus.OK);
        }else if(filters != null){
            String productName = decodeMap(filters);
            System.out.println(productName);
            return new ResponseEntity<>(productsRepository.findByNameContains("%" + productName + "%"), HttpStatus.OK);
        }
        else{
            System.out.println("returning whole list");
            return new ResponseEntity<>(productsRepository.findAll(), HttpStatus.OK);
        }

    }


    @CrossOrigin(origins = "*")
    @GetMapping("/get-authors/{productId}")
    public ResponseEntity<Author> getAuthorFromProduct(@PathVariable (value = "productId") Long productId){
            Product product = productsRepository.findById(productId).orElseThrow();
            return new ResponseEntity<>(product.getAuthor(), HttpStatus.OK);
    }


    @CrossOrigin(origins = "*")
    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long id){
        Product product = productsRepository.findById(id).orElseThrow();
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //TODO rewrite all post, put methods to use DTOs


//    @CrossOrigin(origins = "*")
//    @PostMapping("/products")
//    public ResponseEntity<Product> createNewProduct(@RequestBody Product newProductFromFront){
//        Long idOfAuthorToSet = newProductFromFront.getIdOfAuthor();
//        Long idOfCategoryToSet = newProductFromFront.getIdOfCategory();
//        Author author = authorsRepository.findById(idOfAuthorToSet).orElseThrow();
//        Category category = categoryRepository.findById(idOfCategoryToSet).orElseThrow();
//        newProductFromFront.setAuthor(author);
//        newProductFromFront.setCategory(category);
//        Product product = productsRepository.save(newProductFromFront);
//        return new ResponseEntity<>(product, HttpStatus.CREATED);
//    }

    @CrossOrigin(origins = "*")
    @PostMapping("/products")
    public ResponseEntity<Product> createNewProduct(@RequestBody ProductDTO productDTO){
        var product = new Product();
        Long idOfAuthorToSet = productDTO.getIdOfAuthor();
        Long idOfCategoryToSet = productDTO.getIdOfCategory();
        Author author = authorsRepository.findById(idOfAuthorToSet).orElseThrow();
        Category category = categoryRepository.findById(idOfCategoryToSet).orElseThrow();
        product.setAuthor(author);
        product.setCategory(category);
        product.setColor(productDTO.getColor());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setWidth(productDTO.getWidth());
        product.setHeight(productDTO.getHeight());
        product.setPrice(productDTO.getPrice());
        product.setIdOfCategory(productDTO.getIdOfCategory());
        product.setIdOfAuthor(productDTO.getIdOfAuthor());
        product.setName(productDTO.getName());
        productsRepository.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }



    @CrossOrigin(origins = "*")
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long id, @RequestBody ProductDTO productRequest){
        Product product = productsRepository.findById(id).orElseThrow();
        product.setIdOfCategory(productRequest.getIdOfCategory());
        product.setIdOfAuthor(productRequest.getIdOfAuthor());
        product.setName(productRequest.getName());
        product.setColor(productRequest.getColor());
        product.setHeight(productRequest.getHeight());
        product.setWidth(productRequest.getWidth());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setImage(productRequest.getImage());
        return new ResponseEntity<>(productsRepository.save(product), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("products/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable(value = "id") Long id){
        productsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
