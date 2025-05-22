package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query(
            "SELECT l " +
            "FROM Link l " +
            "WHERE l.device.id = :deviceId"
    )
    List<Link> findAllByDeviceId(Long deviceId);
}
