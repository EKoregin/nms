package com.ekoregin.nms.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tech_parameter")
public class TechParameter {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long paramId;

    @OneToOne
    @JoinColumn(name = "type_tech_parameter_id")
    private TypeTechParameter type;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "tech_parameter_device",
            joinColumns = @JoinColumn(name = "tech_parameter_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
    private List<Device> devices = new ArrayList<>();
}

