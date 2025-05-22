package com.ekoregin.nms.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Link implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkType type;

    @Column(name = "destination", nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkDestination destination;

    @OneToOne
    @JoinColumn(name = "port_id")
    private Port port;

    @OneToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
