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

    private Boolean isCreator;

    private String snmpOID;

    private String telnetCommands;

    private Long modelDeviceId;

    private String description;

    private String substRules;

    private String jsonFilter;

    private String regexFilter;

    private List<TypeTechParameter> typeTechParams;

    private String checkScope;

    public CheckDto(Check check) {
        this.checkId = check.getCheckId();
        this.checkName = check.getCheckName();
        this.checkType = check.getCheckType();
        this.isCreator = check.isCreator();
        this.snmpOID = check.getSnmpOID();
        this.typeTechParams = check.getTypeTechParams();
        this.modelDeviceId = check.getModelDevice().getId();
        this.description = check.getDescription();
        this.telnetCommands = check.getTelnetCommands();
        this.substRules = check.getSubstRules();
        this.jsonFilter = check.getJsonFilter();
        this.regexFilter = check.getRegexFilter();
        this.checkScope = check.getCheckScope();
    }
}
