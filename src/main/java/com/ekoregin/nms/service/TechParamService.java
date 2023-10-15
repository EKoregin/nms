package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TechParamDto;
import com.ekoregin.nms.entity.TechParameter;

public interface TechParamService {
    TechParameter create(TechParamDto techParamDto);

    void update(TechParamDto techParamDto);

    void delete(long id);

    TechParameter findById(long id);
}
