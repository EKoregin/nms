package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.CheckTypeEntity;
import com.ekoregin.nms.repository.CheckTypeRepo;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CheckTypeIdConverter implements Converter<String, CheckTypeEntity> {

    private final CheckTypeRepo checkTypeRepo;

    public CheckTypeIdConverter(CheckTypeRepo checkTypeRepo) {
        this.checkTypeRepo = checkTypeRepo;
    }

    @Override
    public CheckTypeEntity convert(@NonNull String id) {
        return checkTypeRepo.findById(Long.valueOf(id)).orElse(null);
    }
}
