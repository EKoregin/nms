package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.CheckDto;
import jakarta.persistence.*;
import lombok.*;

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

    public Check(CheckDto checkDto) {
        this.checkId = checkDto.getCheckId();
        this.checkName = checkDto.getCheckName();
        this.checkType = checkDto.getCheckType();
        this.snmpOID = checkDto.getSnmpOID();
    }
}
