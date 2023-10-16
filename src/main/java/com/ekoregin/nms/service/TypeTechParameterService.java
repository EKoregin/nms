package com.ekoregin.nms.service;

import com.ekoregin.nms.entity.TypeTechParameter;

import java.util.List;

public interface TypeTechParameterService {

    TypeTechParameter create(TypeTechParameter typeTechParam);

    void update(TypeTechParameter typeTechParam);

    void delete(long id);

    TypeTechParameter findById(long id);

    List<TypeTechParameter> findAll();

}
