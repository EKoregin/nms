package com.ekoregin.nms.entity;

import com.ekoregin.nms.dto.TechParamDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity(name = "tech_parameter")
public class TechParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private TypeTechParameter type;

    @Column(name = "value")
    private String value;

    public TechParameter(TechParamDto techParamDto) {
        id = techParamDto.getId();
        type = techParamDto.getTypeTechParameter();
        value = techParamDto.getTechParamValue();
    }
}

