package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.Check;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepo extends JpaRepository<Check, Long> {
}
