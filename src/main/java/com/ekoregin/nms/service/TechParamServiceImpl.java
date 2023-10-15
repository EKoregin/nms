package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TechParamDto;
import com.ekoregin.nms.entity.TechParameter;
import com.ekoregin.nms.repository.TechParameterRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class TechParamServiceImpl implements TechParamService {

    private final TechParameterRepo repo;

    @Override
    public TechParameter create(TechParamDto techParamDto) {
        TechParameter techParameter = new TechParameter(techParamDto);
        repo.save(techParameter);
        log.info("TechParameter with ID: {} was created", techParameter.getId());
        return techParameter;
    }

    @Override
    public void update(TechParamDto techParamDto) {
        long paramId = techParamDto != null ? techParamDto.getId() : 0;
        TechParameter techParameter = repo.findById(paramId).orElse(null);
        if (techParameter != null && techParamDto != null) {
            repo.save(new TechParameter(techParamDto));
            log.info("TechParam was updated: {}", techParamDto);
        } else {
            log.error("TechParam with ID: {} was not found", paramId);
            throw new NoSuchElementException("TechParam with ID: " + paramId + " was not found");
        }
    }

    @Override
    public void delete(long id) {
        repo.deleteById(id);
    }

    @Override
    public TechParameter findById(long id) {
        TechParameter techParameter = repo.findById(id).orElse(null);
        if (techParameter == null) {
            log.error("TechParameter with id: {} not found!", id);
            throw new NoSuchElementException("TechParameter with id: " + id + " not found!");
        }
        return techParameter;
    }

    @Override
    public List<TechParameter> findAll() {
        List<TechParameter> techParameters = repo.findAll();
        techParameters.forEach(element -> log.info("{}", element));
        return repo.findAll();
    }
}
