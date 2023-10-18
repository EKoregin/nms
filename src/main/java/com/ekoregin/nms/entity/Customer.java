package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.CustomerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String name;

    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<TechParameter> params;

    public Customer(CustomerDto customerDto) {
        id = customerDto.getId();
        name = customerDto.getName();
        address = customerDto.getAddress();
    }
}
