package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.TechParameter;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechParameterDto {

    private Long paramId;

    private String value;

    private Long typeId;

    private Long customerId;

    public TechParameterDto(TechParameter parameter) {
        paramId = parameter.getParamId();
        value = parameter.getValue();
        typeId = parameter.getType().getId();
        customerId = parameter.getCustomer().getId();
    }

}
