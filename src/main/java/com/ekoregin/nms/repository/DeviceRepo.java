package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepo extends JpaRepository<Device, Long> {
}
