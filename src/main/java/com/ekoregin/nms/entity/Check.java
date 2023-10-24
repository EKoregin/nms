package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.CheckDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checks")
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long checkId;

    @Column(name = "check_name")
    private String checkName;

    @Column(name = "check_type")
    private String checkType;

    @Column(name = "snmp_oid")
    private String snmpOID;

    @ManyToMany
    @JoinTable(
            name = "ttp_checks",
            joinColumns = @JoinColumn(name = "check_id"),
            inverseJoinColumns = @JoinColumn(name = "type_tech_parameter_id")
    )
    private List<TypeTechParameter> typeTechParams;

    public Check(CheckDto checkDto) {
        this.checkId = checkDto.getCheckId();
        this.checkName = checkDto.getCheckName();
        this.checkType = checkDto.getCheckType();
        this.snmpOID = checkDto.getSnmpOID();
    }
}
