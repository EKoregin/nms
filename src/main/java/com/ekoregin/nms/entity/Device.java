package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.DeviceDto;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Device")
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_name")
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

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelDevice model;

    @ManyToMany
    @JoinTable(
            name = "customer_device",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tech_parameter_device",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_parameter_id")
    )
    private List<TechParameter> parameters = new ArrayList<>();

    @OneToMany(mappedBy = "device")
    Set<CustomerDevice> connections;

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