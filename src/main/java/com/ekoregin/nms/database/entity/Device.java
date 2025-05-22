package com.ekoregin.nms.database.entity;

import com.ekoregin.nms.dto.DeviceDto;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Device")
@Table(name = "device")
public class Device implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Type(PostgreSQLInetType.class)
    @Column(
            name = "ip",
            columnDefinition = "inet",
            nullable = false,
            unique = true
    )
    private Inet ip;

    @Column(name = "mac", unique = true)
    private String mac;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "snmp_community")
    private String snmpCommunity;

    @Column(name = "snmp_port")
    private int snmpPort;

    @Column(name = "manage_protocol")
    private String protocol;

    @Column(name = "manage_port")
    private int port = 23;

    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelDevice model;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "customer_device",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers = new ArrayList<>();

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tech_parameter_device",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_parameter_id")
    )
    private List<TechParameter> parameters = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "device")
    private Set<CustomerDevice> connections = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<Port> ports = new ArrayList<>();

    public Device(DeviceDto deviceDto) {
        this.id = deviceDto.getId();
        this.name = deviceDto.getName();
        this.description = deviceDto.getDescription();
        this.ip = new Inet(deviceDto.getIp());
        this.login = deviceDto.getLogin();
        this.password = deviceDto.getPassword();
        this.snmpCommunity = "public";
        this.snmpPort = 161;
        this.protocol = "telnet";
        this.port = deviceDto.getManagePort();
        this.mac = deviceDto.getMac();
    }
}