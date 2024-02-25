package com.example.repos;

import com.example.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorsRepository extends JpaRepository<Author, Long> {

}
