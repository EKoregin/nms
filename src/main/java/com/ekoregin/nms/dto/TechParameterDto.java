package com.ekoregin.nms.dto;

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
}
