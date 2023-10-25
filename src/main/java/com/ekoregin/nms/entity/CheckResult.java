package com.ekoregin.nms.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckResult {
    private Long id;
    private CheckResultStatus status;
    private String result;
}

