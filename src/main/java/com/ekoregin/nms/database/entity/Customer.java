package com.ekoregin.nms.database.entity;

import com.ekoregin.nms.dto.CustomerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity(name = "customer")
public class Customer implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String name;

    private String address;
    private String info;
    private Double latitude;
    private Double longitude;

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechParameter> params = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "customer_device",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    private List<Device> devices = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "customer")
    Set<CustomerDevice> connections = new HashSet<>();

    public Customer(CustomerDto customerDto) {
        id = customerDto.getCustomerId();
        name = customerDto.getName();
        address = customerDto.getAddress();
        info = customerDto.getInfo();
    }
}
