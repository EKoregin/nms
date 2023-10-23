package com.ekoregin.nms.entity;

import com.ekoregin.nms.util.CheckType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checks")
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long checkId;

    private String checkName;

    private String checkType;

    private String snmpOID;

}
