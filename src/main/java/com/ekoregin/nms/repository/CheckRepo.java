package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.ModelDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepo extends JpaRepository<Check, Long> {

    Check findByModelDeviceAndForConnectingIs(ModelDevice modelDevice, Boolean status);

    Check findByModelDeviceAndForDisconnectingIs(ModelDevice modelDevice, Boolean status);
}
