package com.example.capstone1.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name is empty")
    @Size(min = 4 , message = "name have to be more than 3 characters long")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotNull(message = "Price is null")
    @Positive(message = "Price must be positive number")
    @Column(columnDefinition = "int not null")
    private Integer price;

    @NotNull(message = "category id is empty")
    @Column(columnDefinition = "int not null")
    private Integer category_id;

    private Integer rating_count = 0; // Number of ratings received
    private Double total_rating = 0.0;  // Sum of all ratings
    private Double average_rating = 0.0;

}
