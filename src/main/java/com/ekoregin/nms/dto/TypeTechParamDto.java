package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.TypeTechParameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeTechParamDto {
    private Long id;

    private String typeTechParamName;

    private String typeTechParamDescription;

    public TypeTechParamDto(TypeTechParameter typeTechParameter) {
        id = typeTechParameter.getId();
        typeTechParamName = typeTechParameter.getName();
        typeTechParamDescription = typeTechParameter.getDescription();
    }
}
