package com.ekoregin.nms.dto;

import com.ekoregin.nms.database.entity.Customer;
import com.ekoregin.nms.database.entity.Device;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {

    private Long id;

    @NotBlank(message = "Необходимо ввести название")
    private String name;

    private String description;

    @NotBlank(message = "Необходимо ввести IP адрес устройства")
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$",
                        message = "IP address is not valid")
    private String ip;

    @NotBlank(message = "Необходимо ввести MAC адрес устройства")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "Неверный формат MAC адреса")
    private String mac;

    private int managePort = 23;
    private String login;
    private String password;
    private Double latitude;
    private Double longitude;
    private Long modelId;
    private List<Customer> customers;
    private List<Integer> freePorts = new ArrayList<>();
    private List<PortReadDto> ports;

    public DeviceDto(Device device) {
        this.id = device.getId();
        this.name = device.getName();
        this.description = device.getDescription();
        this.ip = device.getIp().getAddress();
        this.managePort = device.getPort();
        this.login = device.getLogin();
        this.password = device.getPassword();
        latitude = device.getLatitude();
        longitude = device.getLongitude();
        this.modelId = device.getModel().getId();
        this.mac = device.getMac();
        this.customers = device.getCustomers();
    }
}
