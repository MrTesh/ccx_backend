package com.example.DTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorDTO {
    private String firstName;
    private String lastName;
    private String job;
    private String info;
    private String photo;
    private List<ProductDTO> productsDTO = new ArrayList<>();
}
