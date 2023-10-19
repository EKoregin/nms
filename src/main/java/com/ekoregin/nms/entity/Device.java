package com.ekoregin.nms.entity;

import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

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
            columnDefinition = "inet"
    )
    private Inet ip;

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
    private int port;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelDevice model;
}