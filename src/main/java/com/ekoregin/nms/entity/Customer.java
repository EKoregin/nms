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

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechParameter> params;

    @ManyToMany
    @JoinTable(
            name = "customer_device",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    private List<Device> devices;

    public Customer(CustomerDto customerDto) {
        id = customerDto.getCustomerId();
        name = customerDto.getName();
        address = customerDto.getAddress();
    }
}
