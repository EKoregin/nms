package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TypeTechParamDto;
import com.ekoregin.nms.entity.TypeTechParameter;

import java.util.List;

public interface TypeTechParameterService {

    TypeTechParameter create(TypeTechParamDto typeTechParamDto);

    void update(TypeTechParamDto typeTechParamDto);

    void delete(long id);

    TypeTechParameter findById(long id);

    List<TypeTechParameter> findAll();

}
