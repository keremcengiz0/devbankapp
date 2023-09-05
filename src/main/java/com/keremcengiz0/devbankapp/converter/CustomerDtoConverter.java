package com.keremcengiz0.devbankapp.converter;

import com.keremcengiz0.devbankapp.dto.CityDto;
import com.keremcengiz0.devbankapp.dto.CustomerDto;
import com.keremcengiz0.devbankapp.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoConverter {
    public CustomerDto convert(Customer customer) {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setId(customer.getId());
        customerDto.setAddress(customer.getAddress());
        customerDto.setCity(CityDto.valueOf(customer.getCity().name()));
        customerDto.setName(customer.getName());
        customerDto.setDateOfBirth(customer.getDateOfBirth());

        return customerDto;
    }
}
