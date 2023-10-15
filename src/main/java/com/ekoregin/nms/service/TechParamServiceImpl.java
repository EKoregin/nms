package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TechParamDto;
import com.ekoregin.nms.entity.TechParameter;
import com.ekoregin.nms.repository.TechParameterRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TechParamServiceImpl implements TechParamService {

    private final TechParameterRepo repo;

    @Override
    public TechParameter create(TechParamDto techParamDto) {
        return null;
    }

    @Override
    public void update(TechParamDto techParamDto) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public TechParameter findById(long id) {
        return null;
    }
}
