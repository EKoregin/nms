package com.ekoregin.nms.service;

import com.ekoregin.nms.database.entity.TypeTechParameter;
import com.ekoregin.nms.database.repository.TypeTechParameterRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TypeTechParamService {

    private final TypeTechParameterRepo repo;

    @Transactional
    public TypeTechParameter create(TypeTechParameter typeTechParam) {
        repo.save(typeTechParam);
        log.info("TypeTechParameter with ID: {} was created", typeTechParam.getId());
        return typeTechParam;
    }

    @Transactional
    public void update(TypeTechParameter typeTechParam) {
            long paramId = typeTechParam != null ? typeTechParam.getId() : 0;
            TypeTechParameter typeTechParameter = repo.findById(paramId).orElse(null);
            if (typeTechParameter != null && typeTechParam != null) {
                repo.save(typeTechParam);
                log.info("TypeTechParam was updated: {}", typeTechParam);
            } else {
                log.error("TypeTechParam with ID: {} was not found", paramId);
                throw new NoSuchElementException("TypeTechParam with ID: " + paramId + " was not found");
            }
    }

    @Transactional
    public void delete(long id) {
        repo.deleteById(id);
    }

    public TypeTechParameter findById(long id) {
        TypeTechParameter typeTechParameter = repo.findById(id).orElse(null);
        if (typeTechParameter == null) {
            log.error("TypeTechParameter with id: {} not found!", id);
            throw new NoSuchElementException("TypeTechParameter with id: " + id + " not found!");
        }
        return typeTechParameter;
    }

    public List<TypeTechParameter> findAll() {
        return repo.findAll();
    }
}
