package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.TypeTechParamDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity(name = "type_tech_parameter")
public class TypeTechParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    public TypeTechParameter(TypeTechParamDto typeTechParamDto) {
        id = typeTechParamDto.getId();
        name = typeTechParamDto.getTypeTechParamName();
        description = typeTechParamDto.getTypeTechParamDescription();
    }
}
