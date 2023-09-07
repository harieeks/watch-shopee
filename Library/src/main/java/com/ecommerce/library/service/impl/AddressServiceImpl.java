package com.ecommerce.library.service.impl;

import com.ecommerce.library.Dto.AddressDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.AddressRepository;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Address addAddress(Address address,String username) {

        Customer customer=customerRepository.findByUsername(username);
        List<Address> addressList=customer.getAddresses();
        if(addressList == null){
            addressList=new ArrayList<>();
        }

        Address newAddress=new Address();
        newAddress.setCity(address.getCity());
        newAddress.setDistrict(address.getDistrict());
        newAddress.setHouseName(address.getHouseName());
        newAddress.setPinCode(address.getPinCode());
        newAddress.setCountry(address.getCountry());
        newAddress.setState(address.getState());
        newAddress.setCustomer(customer);

        addressList.add(newAddress);
        customer.setAddresses(addressList);

        customerRepository.save(customer);
        return addressRepository.save(newAddress);
    }

    @Override
    public Address findAddressByCustomerId(Long id) {
        return addressRepository.findRandomAddressByCustomerId(id);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address=addressRepository.getById(id);
        addressRepository.delete(address);
    }

    @Override
    public AddressDto findById(Long id) {
        Address address=addressRepository.getById(id);

        AddressDto addressDto=new AddressDto();
        addressDto.setHouseName(address.getHouseName());
        addressDto.setCity(address.getCity());
        addressDto.setDistrict(address.getDistrict());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        addressDto.setPinCode(address.getPinCode());
        addressDto.setId(address.getId());
        return addressDto;
    }

    @Override
    public Address getById(Long id) {
        return addressRepository.getById(id);
    }

    @Override
    public Address transferFormAddress(Map<String, Object> requestData,String username) {
        Address address=new Address();
        Customer customer=customerRepository.findByUsername(username);

        String houseName= (String) requestData.get("houseName");
        String city= (String) requestData.get("city");
        String district= (String) requestData.get("district");
        String state= (String) requestData.get("state");
        String pinCodeString= (String) requestData.get("pinCode");
        Long pinCode=Long.parseLong(pinCodeString);
        String country= (String) requestData.get("country");

        address.setHouseName(houseName);
        address.setCity(city);
        address.setDistrict(district);
        address.setState(state);
        address.setPinCode(pinCode);
        address.setCountry(country);
        address.setCustomer(customer);

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Address address,String username) {
        Customer customer=customerRepository.findByUsername(username);
        Address address1=getById(address.getId());
        address1.setHouseName(address.getHouseName());
        address1.setCity(address.getCity());
        address1.setDistrict(address.getDistrict());
        address1.setState(address.getState());
        address1.setPinCode(address.getPinCode());
        address1.setCountry(address1.getCountry());
        address1.setCustomer(customer);

        return addressRepository.save(address1);
    }
}
