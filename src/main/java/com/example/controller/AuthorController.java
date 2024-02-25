package com.example.controller;


import com.example.DTO.AuthorDTO;
import com.example.entity.Author;
import com.example.entity.Product;
import com.example.exceptions.AuthorsNotFoundException;
import com.example.repos.AuthorsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(path = "/api")
public class AuthorController {

    @Autowired
    AuthorsRepository authorsRepository;

    AuthorController(AuthorsRepository authorsRepository){
        this.authorsRepository = authorsRepository;
    }

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

    //TODO rewrite all post, put methods to use DTOs

    @CrossOrigin(origins = "*")
    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthorsByIds(@RequestParam(value = "ids", required=false) String ids) throws UnsupportedEncodingException {
        System.out.println("Запрос пришел");
        if (ids == null){
            return new ResponseEntity<>(authorsRepository.findAll(), HttpStatus.OK);

        }else{
            String s = decodeUrl(ids);
            List<Long> arrayOfIds =  fromStringUrlToInt(s);
            return new ResponseEntity<>(authorsRepository.findAllById(arrayOfIds),  HttpStatus.OK);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/authors/{id}")
    Author one(@PathVariable Long id) {
        return authorsRepository.findById(id)
                .orElseThrow(() -> new AuthorsNotFoundException(id));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/authors")
    ResponseEntity<Author> newAuthor(@RequestBody AuthorDTO authorDTO){
        var author = new Author();
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        author.setJob(authorDTO.getJob());
        author.setInfo(authorDTO.getInfo());
        author.setPhoto(authorDTO.getPhoto());
        author.setProducts(authorDTO.getProductsDTO().stream().map(pDto-> {
           Product p = new Product();
           BeanUtils.copyProperties(pDto, p);
           return p;
       }).toList());
        authorsRepository.save(author);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }


    @CrossOrigin(origins = "*")
    @PutMapping("/authors/{id}")
    ResponseEntity<Author> replaceAuthor(@RequestBody AuthorDTO newAuthor, @PathVariable Long id) {
        Author author = authorsRepository.findById(id).orElseThrow(() -> new AuthorsNotFoundException(id));
        author.setFirstName(newAuthor.getFirstName());
        author.setLastName(newAuthor.getLastName());
        author.setJob(newAuthor.getJob());
        author.setInfo(newAuthor.getInfo());
        author.setPhoto(newAuthor.getPhoto());
        return new ResponseEntity<>(authorsRepository.save(author), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/authors/{id}")
    void deleteAuthor(@PathVariable Long id) {
        authorsRepository.deleteById(id);
    }


    @DeleteMapping("/authors")
    void deleteAllAuthors(){
        authorsRepository.deleteAll();
    }

}
