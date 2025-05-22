package com.ekoregin.nms.database.entity;

import com.ekoregin.nms.dto.TypeTechParameterDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "type_tech_parameter")
public class TypeTechParameter implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
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
