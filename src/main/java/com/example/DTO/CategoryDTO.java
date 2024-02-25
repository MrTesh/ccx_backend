package com.example.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {
    private String label;
    private String slogan;
    private List<ProductDTO> productsDTO = new ArrayList<>();
}
