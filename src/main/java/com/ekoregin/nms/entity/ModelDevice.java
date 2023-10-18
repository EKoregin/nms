package com.ekoregin.nms.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "model_device")
public class ModelDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_device")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;
}
