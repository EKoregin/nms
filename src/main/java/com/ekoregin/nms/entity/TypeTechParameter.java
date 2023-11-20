package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.TypeTechParameterDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "type_tech_parameter")
public class TypeTechParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "regex_rule")
    private String regexRule;

    public TypeTechParameter(TypeTechParameterDto typeTechParameterDto) {
        this.id = typeTechParameterDto.getTtpId();
        this.name = typeTechParameterDto.getTtpName();
        this.description = typeTechParameterDto.getTtpDescription();
        this.regexRule = typeTechParameterDto.getRegexRule();
    }

    @Override
    public String toString() {
        return name;
    }
}
