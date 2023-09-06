package com.keremcengiz0.devbankapp.dto;

import com.keremcengiz0.devbankapp.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private Integer dateOfBirth;
    private CityDto city;
    private Address address;
}
