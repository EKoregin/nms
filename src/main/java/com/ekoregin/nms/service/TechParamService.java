package com.ekoregin.nms.service;

import com.ekoregin.nms.dto.TechParameterDto;
import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.database.entity.TechParameter;
import com.ekoregin.nms.database.entity.TypeTechParameter;
import com.ekoregin.nms.database.repository.TechParameterRepo;
import com.ekoregin.nms.mapper.CustomerCreateEditMapper;
import com.ekoregin.nms.util.Validation;
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
public class TechParamService {

    private final TechParameterRepo repo;
    private final TypeTechParamService typeTechParameterService;
    private final CustomerService customerService;
    private final CustomerCreateEditMapper customerCreateEditMapper;

    @Transactional
    public TechParameter create(TechParameterDto techParameterDto) {
        long typeId = techParameterDto.getTypeId();
        long customerId = techParameterDto.getCustomerId();
        TypeTechParameter typeTechParameter = typeTechParameterService.findById(typeId);
        Customer customer = customerService.findById(customerId);
//        Customer customer = customerService.findById(customerId)
//                .map(customerCreateEditMapper::map)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (typeTechParameter == null) {
            log.error("TypeTechParam with ID: {} was not found", typeId);
            throw new NoSuchElementException("TypeTechParam with ID: " + typeId + " was not found");
        }

        Validation.verifyParameterForValidity(typeTechParameter, techParameterDto.getValue());

        TechParameter techParameter = TechParameter.builder()
                .value(techParameterDto.getValue().toUpperCase())
                .type(typeTechParameter)
                .customer(customer)
                .build();
        repo.save(techParameter);
        log.info("TechParameter with ID: {} was created", techParameter.getParamId());
        return techParameter;
    }

    @Transactional
    public TechParameter create(TechParameter techParameter) {
        repo.save(techParameter);
        return techParameter;
    }

    @Transactional
    public void update(TechParameter techParameter) {
        long paramId = techParameter != null ? techParameter.getParamId() : 0;
        TechParameter techParameterFound = repo.findById(paramId).orElse(null);
        if (techParameterFound != null && techParameter != null) {

            TypeTechParameter typeTechParameter = techParameterFound.getType();
            Validation.verifyParameterForValidity(typeTechParameter, techParameterFound.getValue());

            repo.save(techParameter);
            log.info("TechParam was updated: {}", techParameter);
        } else {
            log.error("TechParam with ID: {} was not found", paramId);
            throw new NoSuchElementException("TechParam with ID: " + paramId + " was not found");
        }
    }

    @Transactional
    public void delete(long id) {
        repo.deleteById(id);
    }

    public TechParameter findById(long id) {
        TechParameter techParameter = repo.findById(id).orElse(null);
        if (techParameter == null) {
            log.error("TechParameter with id: {} not found!", id);
            throw new NoSuchElementException("TechParameter with id: " + id + " not found!");
        }
        return techParameter;
    }

    public List<TechParameter> findAll() {
        List<TechParameter> techParameters = repo.findAll();
        techParameters.forEach(element -> log.info("{}", element));
        return repo.findAll();
    }
}
