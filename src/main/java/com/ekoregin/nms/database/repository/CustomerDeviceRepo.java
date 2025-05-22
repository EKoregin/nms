package com.ekoregin.nms.database.repository;

import com.ekoregin.nms.database.entity.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDeviceRepo extends JpaRepository<CustomerDevice, Long> {
}
