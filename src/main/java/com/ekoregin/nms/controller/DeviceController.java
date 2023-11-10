package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckScope;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.service.DeviceService;
import com.ekoregin.nms.service.ModelDeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/devices")
public class DeviceController {

    public final DeviceService deviceService;
    public final ModelDeviceService modelDeviceService;

    @ModelAttribute
    public void addModelDevices(Model model) {
        List<ModelDevice> models = modelDeviceService.findAll();
        model.addAttribute("models", models);
    }

    @GetMapping
    public String allDevices(@RequestParam("sortField") Optional<String> sortField,
                             Model model) {
        String currentSortField = sortField.orElse("id");
        model.addAttribute("devices", deviceService.findAll(currentSortField));
        model.addAttribute("sortField", sortField);
        return "devices";
    }

    @GetMapping("/addForm")
    public String formCreateModelDevice(Model model) {
        model.addAttribute("deviceDto", new DeviceDto());
        return "addDevice";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute DeviceDto deviceDto, Errors errors) {
        if (errors.hasErrors()) {
            return "addDevice";
        }
        deviceService.create(deviceDto);
        return "redirect:/devices";
    }

    @GetMapping("/editForm/{deviceId}")
    public String formEditDevice(@PathVariable long deviceId, Model model) {
        Device device = deviceService.findById(deviceId);
        if (device != null) {
            DeviceDto deviceDto = new DeviceDto(device);
            List<Check> checks = device.getModel().getChecks().stream()
                    .filter(check -> check.getCheckScope().equals(CheckScope.DEVICE.name())).toList();
            model.addAttribute("checks", checks);
            model.addAttribute("deviceDto", deviceDto);
        } else {
            log.info("Device with ID: {} not found", deviceId);
            throw new NoSuchElementException("Device with ID: " + deviceId + " not found");
        }
        return "editDevice";
    }

    @PutMapping("/update")
    public String update(@Valid @ModelAttribute DeviceDto deviceDto, Errors errors) {
        if (errors.hasErrors()) {
            return "editDevice";
        }
        deviceService.update(deviceDto);
        return "redirect:/devices";
    }

    @DeleteMapping("/{deviceId}")
    public String delete(@PathVariable long deviceId) {
        deviceService.delete(deviceId);
        return "redirect:/devices";
    }

    @ResponseBody
    @GetMapping("/checks/{deviceId}")
    public List<CheckDto> findAllChecksByDeviceId(@PathVariable long deviceId) {
        log.info("Request for checks for deviceId: {}", deviceId);
        return deviceService.findAllChecksByDeviceId(deviceId);
    }

    @ResponseBody
    @GetMapping("/customers/{deviceId}")
    public List<Integer> findFreePortsByDeviceId(@PathVariable long deviceId) {
        log.info("Request for busy ports for deviceId: {}", deviceId);
        return deviceService.findFreePortsByDeviceId(deviceId);
    }
}
