package com.ekoregin.nms.database.entity;

import com.ekoregin.nms.dto.ModelDeviceDto;
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
@Entity(name = "model_device")
public class ModelDevice implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_device", nullable = false)
    private String type;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "ports")
    private int numberOfPorts;

    @Builder.Default
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

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "model_device_ttp",
            joinColumns = @JoinColumn(name = "model_device_id"),
            inverseJoinColumns = @JoinColumn(name = "type_tech_parameter_id")
    )
    private List<TypeTechParameter> typeTechParameters = new ArrayList<>();

    @Builder.Default
    @OneToMany(orphanRemoval = true, mappedBy = "modelDevice", cascade = CascadeType.ALL)
    private List<Check> checks = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "model_id")
    private List<Device> devices = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "modelDevice", cascade = CascadeType.ALL)
    private List<Port> ports = new ArrayList<>();

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
