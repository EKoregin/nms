package com.ekoregin.nms.dto;

import com.ekoregin.nms.entity.TypeTechParameter;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeTechParameterDto {
    private Long ttpId;

    private String ttpName;

    private String ttpDescription;

    private String regexRule;

    public TypeTechParameterDto(TypeTechParameter typeTechParameter) {
        this.ttpId = typeTechParameter.getId();
        this.ttpName = typeTechParameter.getName();
        this.ttpDescription = typeTechParameter.getDescription();
        this.regexRule = typeTechParameter.getRegexRule();
    }
}
