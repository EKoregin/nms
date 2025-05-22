package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.Port;
import com.ekoregin.nms.dto.PortReadDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortRepository extends JpaRepository<Port, Long> {

    @Query(
            "SELECT p " +
            "FROM Port p " +
            "WHERE p.device.id = :deviceId"
    )
    List<Port> findAllByDeviceId(Long deviceId);

    @Query(
            "SELECT p " +
            "FROM Port p " +
            "WHERE p.modelDevice.id = :modelDeviceId"
    )
    List<Port> findAllByModelDeviceId(Long modelDeviceId);
}
