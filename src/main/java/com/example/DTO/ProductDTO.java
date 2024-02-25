package com.example.DTO;
import lombok.Data;


@Data
public class ProductDTO {
    private Long id;
    private Long idOfAuthor;
    private Long idOfCategory;
    private String name;
    private int height;
    private int width;
    private int price;
    private String color;
    private String description;
    private String image;
    private AuthorDTO authorDTO;
    private CategoryDTO categoryDTO;
}
