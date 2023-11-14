package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.TypeTechParameter;
import com.ekoregin.nms.repository.TypeTechParameterRepo;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TypeTechParameterIdConverter implements Converter<String, TypeTechParameter> {

    private final TypeTechParameterRepo typeTechParameterRepo;

    public TypeTechParameterIdConverter(TypeTechParameterRepo typeTechParameterRepo) {
        this.typeTechParameterRepo = typeTechParameterRepo;
    }

    @Override
    public TypeTechParameter convert(@NonNull String id) {
        return typeTechParameterRepo.findById(Long.valueOf(id)).orElse(null);
    }
}
