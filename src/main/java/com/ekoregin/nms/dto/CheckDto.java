package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.TypeTechParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckDto {

    private Long checkId;

    private String checkName;

    private String checkType;

    private String snmpOID;

    private String telnetCommands;

    private Long modelDeviceId;

    private String description;

    private String substRules;

    private List<TypeTechParameter> typeTechParams;

    public CheckDto(Check check) {
        this.checkId = check.getCheckId();
        this.checkName = check.getCheckName();
        this.checkType = check.getCheckType();
        this.snmpOID = check.getSnmpOID();
        this.typeTechParams = check.getTypeTechParams();
        this.modelDeviceId = check.getModelDevice().getId();
        this.description = check.getDescription();
        this.description = check.getTelnetCommands();
        this.substRules = check.getSubstRules();
    }
}
