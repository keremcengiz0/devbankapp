package com.keremcengiz0.devbankapp.service;

import com.keremcengiz0.devbankapp.converter.CustomerDtoConverter;
import com.keremcengiz0.devbankapp.dto.CustomerDto;
import com.keremcengiz0.devbankapp.dto.request.CreateCustomerRequest;
import com.keremcengiz0.devbankapp.dto.request.UpdateCustomerRequest;
import com.keremcengiz0.devbankapp.exception.CustomerNotFoundException;
import com.keremcengiz0.devbankapp.model.City;
import com.keremcengiz0.devbankapp.model.Customer;
import com.keremcengiz0.devbankapp.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter customerDtoConverter;

    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter customerDtoConverter) {
        this.customerRepository = customerRepository;
        this.customerDtoConverter = customerDtoConverter;
    }

    public CustomerDto createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();

        customer.setName(createCustomerRequest.getName());
        customer.setCity(City.valueOf(createCustomerRequest.getCity().name()));
        customer.setAddress(createCustomerRequest.getAddress());
        customer.setDateOfBirth(createCustomerRequest.getDateOfBirth());

        customerRepository.save(customer);

        CustomerDto customerDto = customerDtoConverter.convert(customer);

        return customerDto;

    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();

        List<CustomerDto> customerDtoList = customerList.stream()
                .map(customer -> customerDtoConverter.convert(customer))
                .collect(Collectors.toList());

        return customerDtoList;

        /*  return customerList.stream()
                .map(customerDtoConverter::convert)
                .toList();
        */

    }

    public CustomerDto getCustomerDtoById(Long id) {

        Optional<Customer> customer = customerRepository.findById(id);

        if (!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + id + " could not be found!");
        }

        CustomerDto customerDto = customerDtoConverter.convert(customer.get());

        return customerDto;


    }

    public Customer getCustomerById(Long id) {

        Optional<Customer> customer = customerRepository.findById(id);

        if (!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + id + " could not be found!");
        }

        return customer.get();
    }

    public CustomerDto updateCustomer(Long id, UpdateCustomerRequest updateCustomerRequest) {

        Optional<Customer> customer = customerRepository.findById(id);

        if (!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + id + " could not be found!");
        }

        Customer updateCustomer = customer.get();
        updateCustomer.setName(updateCustomerRequest.getName());
        updateCustomer.setCity(City.valueOf(updateCustomerRequest.getCity().name()));
        updateCustomer.setAddress(updateCustomerRequest.getAddress());
        updateCustomer.setDateOfBirth(updateCustomerRequest.getDateOfBirth());

        customerRepository.save(updateCustomer);

        CustomerDto customerDto = customerDtoConverter.convert(updateCustomer);

        return customerDto;

    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
