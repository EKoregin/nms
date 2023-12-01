package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDeviceRepo extends JpaRepository<CustomerDevice, Long> {
}
