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
    private static final long serialVersionUID = 1L;

    private Long id;

    private String techParamValue;

    private TypeTechParamDto typeTechParamDto;

    public TechParamDto(TechParameter techParameter) {
        id = techParameter.getId();
        techParamValue = techParameter.getValue();
        typeTechParamDto = new TypeTechParamDto(techParameter.getType());
    }
}
