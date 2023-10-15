package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.TechParameter;
import com.ekoregin.nms.entity.TypeTechParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechParamDto {
    private Long id;

    private String techParamValue;

    private TypeTechParameter typeTechParameter;

    public TechParamDto(TechParameter techParameter) {
        id = techParameter.getId();
        techParamValue = techParameter.getValue();
        typeTechParameter = techParameter.getType();
    }
}
