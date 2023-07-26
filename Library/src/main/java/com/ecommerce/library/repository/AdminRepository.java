package com.ecommerce.library.repository;

import com.ecommerce.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Component
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByEmail(String username);
}
