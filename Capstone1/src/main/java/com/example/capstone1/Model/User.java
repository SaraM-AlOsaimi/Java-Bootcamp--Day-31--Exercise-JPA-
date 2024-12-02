package com.example.capstone1.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @NotEmpty(message = "Username is empty")
    @Size(min = 6, message = "Username must be 6 or more characters long")
    @Column(columnDefinition = "varchar(50) not null unique")
    private String username;


    @NotEmpty(message = "Password is empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{7,}$", message = "Password must be 7 or more characters long, and contain both letters and digits")
    @Column(columnDefinition = "varchar(50) not null")
    private String password;


    @NotBlank(message = "Email is empty")
    @Email(message = "Enter a valid email format")
    @Column(columnDefinition = "varchar(50) not null unique")
    private String email;


    @NotEmpty(message = "Role is empty")
    @Pattern(regexp = "^admin|customer$", message = "Role must be either Admin or Customer")
    @Column(columnDefinition = "varchar(8) not null")
    private String role;


    @NotNull(message = "Balance is null")
    @Positive(message = "Balance must be positive")
    @Column(columnDefinition = "int not null")
    private Integer balance;


}
