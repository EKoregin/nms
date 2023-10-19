package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.service.DeviceService;
import com.ekoregin.nms.service.ModelDeviceService;
import com.ekoregin.nms.util.TypeDevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/devices")
public class DeviceController {

    public final DeviceService deviceService;
    public final ModelDeviceService modelDeviceService;

    @ModelAttribute
    public void addModelDevices(Model model) {
        List<String> types = Arrays.stream(TypeDevice.class.getEnumConstants()).map(Enum::name).toList();
        List<ModelDevice> models = modelDeviceService.findAll();
        model.addAttribute("models", models);
    }

    @GetMapping
    public String allDevices(Model model) {
        model.addAttribute("devices", deviceService.findAll());
        return "devices";
    }

    @GetMapping("/addForm")
    public String formCreateModelDevice(Model model) {
        model.addAttribute("deviceDto", new DeviceDto());
        return "addDevice";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute DeviceDto deviceDto) {
        deviceService.create(deviceDto);
        return "redirect:/devices";
    }

    @GetMapping("/editForm/{deviceId}")
    public String formEditDevice(@PathVariable long deviceId, Model model) {
        Device device = deviceService.findById(deviceId);
        if (device != null) {
            DeviceDto deviceDto = new DeviceDto(device);
            model.addAttribute("deviceDto", deviceDto);
        } else {
            log.info("Device with ID: {} not found", deviceId);
            throw new NoSuchElementException("Device with ID: " + deviceId + " not found");
        }
        return "editDevice";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute DeviceDto deviceDto) {
        deviceService.update(deviceDto);
        return "redirect:/devices";
    }

    @DeleteMapping("/{deviceId}")
    public String delete(@PathVariable long deviceId) {
        deviceService.delete(deviceId);
        return "redirect:/devices";
    }
}
