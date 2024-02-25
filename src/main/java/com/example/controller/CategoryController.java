package com.example.controller;

import com.example.DTO.CategoryDTO;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.exceptions.AuthorsNotFoundException;
import com.example.repos.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    public String decodeUrl(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public static List<Long> fromStringUrlToInt(String s){
        String[] str = s.replaceAll("ids=", "").replaceAll("\\[", "").replaceAll("]", "").split(",");
        List<Long> arr = new ArrayList<Long>();
        for(String i: str){
            arr.add(Long.valueOf(i));
        }
        return arr;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/category")
    ResponseEntity<Category> newCategory(@RequestBody CategoryDTO categoryDTO){
        var category = new Category();
        category.setLabel(categoryDTO.getLabel());
        category.setSlogan(categoryDTO.getSlogan());
        category.setProducts(categoryDTO.getProductsDTO().stream().map(pDto-> {
            Product p = new Product();
            BeanUtils.copyProperties(pDto, p);
            return p;
        }).toList());
        categoryRepository.save(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/category")
    ResponseEntity<List<Category>> getAllCategories(@RequestParam(value = "ids", required=false) String ids) throws UnsupportedEncodingException{
//        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        if (ids == null){
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        }else{
            String s = decodeUrl(ids);
            List<Long> arrayOfIds =  fromStringUrlToInt(s);
            return new ResponseEntity<>(categoryRepository.findAllById(arrayOfIds),  HttpStatus.OK);

        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/category/{id}")
    Category one(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AuthorsNotFoundException(id));
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/category/{id}")
    Category replaceCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long id) {

        return categoryRepository.findById(id)
                .map(category -> {
                    category.setLabel(categoryDTO.getLabel());
                    category.setSlogan(categoryDTO.getSlogan());
                    return categoryRepository.save(category);
                }).orElseThrow();
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/category/{id}")
    void deleteCategoryById(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }

    @DeleteMapping("/category")
    void deleteAllCategories(){
        categoryRepository.deleteAll();
    }
}
