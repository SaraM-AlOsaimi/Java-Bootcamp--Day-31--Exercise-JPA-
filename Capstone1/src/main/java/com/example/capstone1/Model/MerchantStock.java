package com.example.capstone1.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor

public class MerchantStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "product id is empty")
    @Column(columnDefinition = "int not null")
    private Integer product_id;

    @NotNull(message = "merchant id is empty")
    @Column(columnDefinition = "int not null")
    private Integer merchant_id;

    @NotNull(message = "Stock is empty")
    @Min(value = 11 , message = "Stock have to be more than 10 at start")
    @Column(columnDefinition = "int not null")
    private Integer stock;

}
