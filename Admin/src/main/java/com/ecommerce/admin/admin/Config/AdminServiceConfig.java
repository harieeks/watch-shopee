package com.ecommerce.admin.admin.Config;

import com.ecommerce.library.model.Admin;
import com.ecommerce.library.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.stream.Collectors;



public class AdminServiceConfig implements UserDetailsService {

    @Autowired
    private  AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin=adminRepository.findByEmail(username);
        if(admin==null){
            throw new UsernameNotFoundException("Could not find the user");
        }
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                admin.getRoles().stream()
                        .map(role ->
                                new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }
}
