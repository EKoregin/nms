package com.ekoregin.nms.dto;


import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.TechParameter;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;

    private String name;

    private String address;

    private List<TechParameter> params;

    public CustomerDto(Customer customer) {
        id = customer.getId();
        name = customer.getName();
        address = customer.getAddress();
        params = customer.getParams();
    }
}
