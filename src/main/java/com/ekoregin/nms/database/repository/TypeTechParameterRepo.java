package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.TypeTechParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeTechParameterRepo extends JpaRepository<TypeTechParameter, Long> {

    TypeTechParameter findByName(String name);
}
