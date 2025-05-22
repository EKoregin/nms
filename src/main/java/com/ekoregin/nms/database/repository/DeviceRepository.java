package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.Device;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query(
            "SELECT d " +
            "FROM Device d " +
            "WHERE upper(d.name) LIKE upper(:textSearch) " +
            "OR upper(d.model.name) LIKE upper(:textSearch) " +
            "OR upper(d.model.type) LIKE upper(:textSearch) " +
            "OR TEXT(d.ip) LIKE :textSearch"
    )
    List<Device> findDeviceCustomSearch(String textSearch, Pageable pageable);
}
