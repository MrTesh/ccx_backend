package com.example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authors_generator")
    @SequenceGenerator(name = "authors_generator", allocationSize = 1)
    private Long id;
    private String firstName;
    private String lastName;
    private String job;
    @Column(length = 5000)
    private String info;
    private String photo;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
    public Author(String first_name, String last_name, String job, String info, String photo,    List<Product> products) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.job = job;
        this.info = info;
        this.photo = photo;
        this.products = products;
    }

    public Author() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
