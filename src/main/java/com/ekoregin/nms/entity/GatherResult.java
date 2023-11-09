package com.ekoregin.nms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GatherResult {

    private List<String> totalResult;
    private List<String> dataResult;
}
