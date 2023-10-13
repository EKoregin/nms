package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TypeTechParamDto;
import com.ekoregin.nms.entity.TypeTechParameter;
import com.ekoregin.nms.repository.TypeTechParameterRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class TypeTechParamServiceImpl implements TypeTechParameterService{

    private final TypeTechParameterRepo repo;

    @Transactional
    @Override
    public TypeTechParameter create(TypeTechParamDto typeTechParamDto) {
        TypeTechParameter typeTechParameter = new TypeTechParameter(typeTechParamDto);
        repo.save(typeTechParameter);
        log.info("TypeTechParameter with ID: {} was created", typeTechParameter.getId());
        return typeTechParameter;
    }

    @Transactional
    @Override
    public void update(TypeTechParamDto typeTechParamDto) {
            long paramId = typeTechParamDto != null ? typeTechParamDto.getId() : 0;
            TypeTechParameter typeTechParameter = repo.findById(paramId).orElse(null);
            if (typeTechParameter != null && typeTechParamDto != null) {
                repo.save(new TypeTechParameter(typeTechParamDto));
                log.info("TypeTechParam was updated: {}", typeTechParamDto);
            } else {
                log.error("TypeTechParam with ID: {} was not found", paramId);
                throw new NoSuchElementException("TypeTechParam with ID: " + paramId + " was not found");
            }
    }

    @Transactional
    @Override
    public void delete(long id) {
        repo.deleteById(id);
    }

    @Override
    public TypeTechParameter findById(long id) {
        TypeTechParameter typeTechParameter = repo.findById(id).orElse(null);
        if (typeTechParameter == null) {
            log.error("TypeTechParameter with id: {} not found!", id);
            throw new NoSuchElementException("TypeTechParameter with id: " + id + " not found!");
        }
        return typeTechParameter;
    }

    @Override
    public List<TypeTechParameter> findAll() {
        return repo.findAll();
    }
}
