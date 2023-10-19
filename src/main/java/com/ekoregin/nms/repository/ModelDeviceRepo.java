package com.ekoregin.nms.repository;

import com.ekoregin.nms.entity.ModelDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModelDeviceRepo extends JpaRepository<ModelDevice, Long> {

}
