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
public class Port implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer speed = 100;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PortDuplex duplex = PortDuplex.FULL;

    @Column(name = "media_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType = MediaType.COPPER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PortType type = PortType.PHYSICAL;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "model_device_id")
    private ModelDevice modelDevice;

    @OneToOne(mappedBy = "port", fetch = FetchType.LAZY)
    private Link link;
}
