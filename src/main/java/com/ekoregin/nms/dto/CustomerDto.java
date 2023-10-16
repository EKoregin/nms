package com.ekoregin.nms.dto;


import com.ekoregin.nms.entity.Customer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;

    private String name;

    private String address;

    public CustomerDto(Customer customer) {
        id = customer.getId();
        name = customer.getName();
        address = customer.getAddress();
    }
}
