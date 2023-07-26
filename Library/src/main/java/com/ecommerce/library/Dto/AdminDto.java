package com.ecommerce.library.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {

    @Size(min = 3,max = 10,message = "Invalid Name")
    private String firstName;
    @Size(min = 3,max = 10,message = "Invalid Name")
    private String lastName;
    private String email;
    @Size(min=4,max=8,message = "The password must require 4 to 8 characters")
    private String password;
    private String repeatPassword;
}
