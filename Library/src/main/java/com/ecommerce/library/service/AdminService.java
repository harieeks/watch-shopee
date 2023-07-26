package com.ecommerce.library.service;

import com.ecommerce.library.Dto.AdminDto;
import com.ecommerce.library.model.Admin;

public interface AdminService {

    Admin findByUserName(String username);
    Admin save(AdminDto adminDto);
}
