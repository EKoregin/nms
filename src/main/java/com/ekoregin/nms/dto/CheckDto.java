package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.Check;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckDto {

    private Long checkId;

    private String checkName;

    private String checkType;

    private String snmpOID;

    public CheckDto(Check check) {
        this.checkId = check.getCheckId();
        this.checkName = check.getCheckName();
        this.checkType = check.getCheckType();
        this.snmpOID = check.getSnmpOID();
    }
}
