package com.ecommerce.library.service;

import com.ecommerce.library.Dto.AddressDto;
import com.ecommerce.library.model.Address;

import java.util.Map;
import java.util.Optional;

public interface AddressService {

    Address addAddress(Address address,String username);
    Address findAddressByCustomerId(Long id);
    void deleteAddress(Long id);
    AddressDto findById(Long id);
    Address getById(Long id);
    Address transferFormAddress(Map<String,Object> requestData,String username);
    Address updateAddress(Address address,String username);
}
