package com.ekoregin.nms.util;

import com.ekoregin.nms.dto.CustomerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CustomerToDeviceForm {
    private Map<Long, String> parameters = new HashMap<>();
    private CustomerDto customer;
    private Long deviceId;
    private Long portNumber;
}
