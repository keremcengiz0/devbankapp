package com.keremcengiz0.devbankapp.dto;

import com.keremcengiz0.devbankapp.model.City;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AddressDto {
    private String id;
    private City city;
    private String postCode;
}
