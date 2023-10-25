package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.ModelDeviceDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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

    @OneToMany(orphanRemoval = true, mappedBy = "modelDevice", cascade = CascadeType.ALL)
    private List<Check> checks;

    public ModelDevice(ModelDeviceDto modelDeviceDto) {
        this.id = modelDeviceDto.getId();
        this.type = modelDeviceDto.getType();
        this.name = modelDeviceDto.getName();
        this.manufacturer = modelDeviceDto.getManufacturer();
    }
}
