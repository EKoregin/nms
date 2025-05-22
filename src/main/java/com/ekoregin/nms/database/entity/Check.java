package com.ekoregin.nms.database.entity;

import com.ekoregin.nms.dto.CheckDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checks")
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long checkId;

    @Column(name = "check_name", nullable = false)
    private String checkName;

    @Column(name = "check_type", nullable = false)
    private String checkType;

    @Column(name = "is_creator")
    private boolean isCreator = false;

    @Column(name = "for_connecting")
    private boolean forConnecting = false;

    @Column(name = "for_disconnecting")
    private boolean forDisconnecting = false;

    @Column(name = "snmp_oid")
    private String snmpOID;

    @Column(name = "telnet_commands")
    private String telnetCommands;

    @Column(name = "description")
    private String description;

    @Column(name = "subst_rules")
    private String substRules;

    @Column(name = "json_filter")
    private String jsonFilter;

    @Column(name = "regex_filter")
    private String regexFilter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_device_id")
    private ModelDevice modelDevice;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "ttp_checks",
            joinColumns = @JoinColumn(name = "check_id"),
            inverseJoinColumns = @JoinColumn(name = "type_tech_parameter_id")
    )
    private List<TypeTechParameter> typeTechParams = new ArrayList<>();

    @Column(name = "check_scope")
    private String checkScope = CheckScope.CUSTOMER.name();

    public Check(CheckDto checkDto) {
        this.checkId = checkDto.getCheckId();
        this.checkName = checkDto.getCheckName();
        this.checkType = checkDto.getCheckType();
        this.isCreator = checkDto.getIsCreator();
        this.snmpOID = checkDto.getSnmpOID();
        this.description = checkDto.getDescription();
        this.telnetCommands = checkDto.getTelnetCommands();
        this.substRules = checkDto.getSubstRules();
        this.jsonFilter = checkDto.getJsonFilter();
        this.regexFilter = checkDto.getRegexFilter();
        this.checkScope = checkDto.getCheckScope();
    }
}
