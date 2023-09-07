package com.ecommerce.library.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;
    @Size(min = 3,max = 10,message = "Invalid Name")
    private String name;
    private String username;
    @Size(min=4,max=8,message = "The password must require 4 to 8 characters")
    private String password;
    private String repeatPassword;
    @Size(min = 10, max = 15, message = "Phone number contains 10-12 characters")
    private String phoneNo;

}
