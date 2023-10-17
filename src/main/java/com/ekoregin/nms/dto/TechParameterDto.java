package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.TechParameter;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TechParameterDto {

    private Long id;

    private String value;

    private Long typeId;

    private Long customerId;

    public TechParameterDto(TechParameter parameter) {
        id = parameter.getId();
        value = parameter.getValue();
        typeId = parameter.getType().getId();
        customerId = parameter.getCustomer().getId();
    }

}
