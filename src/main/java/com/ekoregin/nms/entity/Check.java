package com.ekoregin.nms.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Check {
    private Long id;
    private String name;
    private ModelDevice modelDevice;

    void checkExecution() {
        // do something for check
    }
}
