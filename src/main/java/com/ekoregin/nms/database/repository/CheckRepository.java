package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.Check;
import com.ekoregin.nms.database.entity.ModelDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckRepository extends JpaRepository<Check, Long> {

    Optional<Check> findByModelDeviceAndForConnectingIs(ModelDevice modelDevice, Boolean status);

    Optional<Check> findByModelDeviceAndForDisconnectingIs(ModelDevice modelDevice, Boolean status);
}
