package com.ekoregin.nms.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Customer {
    private Long id;
    private String name;
    private String Address;
    private List<TechParameter> params;
    private List<Check> checks;
}
