package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.CheckTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckTypeRepo extends JpaRepository<CheckTypeEntity, Long> {
}
