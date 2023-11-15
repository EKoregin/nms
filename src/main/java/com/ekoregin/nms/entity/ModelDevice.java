package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.ModelDeviceDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "ports")
    private int numberOfPorts;

    @ManyToMany
    @JoinTable(
            name = "model_check_type",
            joinColumns = @JoinColumn(name = "model_device_id"),
            inverseJoinColumns = @JoinColumn(name = "check_type_id")
    )
    private List<CheckTypeEntity> controlMethods = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "type_tech_parameter_id")
    private TypeTechParameter typePort;

    @ManyToMany
    @JoinTable(
            name = "model_device_ttp",
            joinColumns = @JoinColumn(name = "model_device_id"),
            inverseJoinColumns = @JoinColumn(name = "type_tech_parameter_id")
    )
    private List<TypeTechParameter> typeTechParameters = new ArrayList<>();

    @OneToMany(orphanRemoval = true, mappedBy = "modelDevice", cascade = CascadeType.ALL)
    private List<Check> checks;

    @OneToMany
    @JoinColumn(name = "model_id")
    private List<Device> devices;

    public ModelDevice(ModelDeviceDto modelDeviceDto) {
        this.id = modelDeviceDto.getId();
        this.type = modelDeviceDto.getType();
        this.name = modelDeviceDto.getName();
        this.manufacturer = modelDeviceDto.getManufacturer();
        this.numberOfPorts = modelDeviceDto.getNumberOfPorts();
        this.controlMethods = modelDeviceDto.getControlMethods();
        this.typePort = modelDeviceDto.getTypePort();
        this.typeTechParameters = modelDeviceDto.getTypeTechParameters();
    }
}
