package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.entity.Check;

import java.util.List;

public interface CheckService {
    Check create(CheckDto checkDto);

    void update(CheckDto checkDto);

    void delete(long checkId);

    Check findById(long checkId);

    List<Check> findAll();
}
