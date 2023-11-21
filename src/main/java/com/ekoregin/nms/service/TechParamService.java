package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TechParameterDto;
import com.ekoregin.nms.entity.TechParameter;

import java.util.List;

public interface TechParamService {
    TechParameter create(TechParameterDto techParameterDto);

    TechParameter create(TechParameter techParameter);

    void update(TechParameter techParameter);

    void delete(long id);

    TechParameter findById(long id);

    List<TechParameter> findAll();
}
