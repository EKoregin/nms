package com.ekoregin.nms.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

