package com.ekoregin.nms.http.controller;

import com.ekoregin.nms.database.entity.Check;
import com.ekoregin.nms.database.entity.CheckScope;
import com.ekoregin.nms.database.entity.Device;
import com.ekoregin.nms.database.entity.ModelDevice;
import com.ekoregin.nms.dto.*;
import com.ekoregin.nms.mapper.DeviceReadMapper;
import com.ekoregin.nms.service.DeviceService;
import com.ekoregin.nms.service.LinkService;
import com.ekoregin.nms.service.ModelDeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/devices")
public class DeviceController {

    public final DeviceService deviceService;
    public final ModelDeviceService modelDeviceService;
    private final DeviceReadMapper deviceReadMapper;
    private final LinkService linkService;

    @ModelAttribute
    private void currentAuthUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("currentUser", userDetails.getUsername());
    }

    @ModelAttribute
    public void addModelDevices(Model model) {
        List<ModelDevice> models = modelDeviceService.findAll();
        model.addAttribute("models", models);
    }

    @RequestMapping(path = {"", "/search"})
    public String searchDevices(Model model, String searchKeyword,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("sortField") Optional<String> sortField) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(50);
        String currentSortField = sortField.orElse("id");
        model.addAttribute("countDevices", deviceService.count());
        var pageRequest = PageRequest.of(currentPage - 1, pageSize);
        Page<Device> devicePage;
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            log.info("Search keyword is: " + searchKeyword);
            devicePage = deviceService.findByNamePaginated("%" + searchKeyword + "%", pageRequest);
            model.addAttribute("devicePage", devicePage);
            model.addAttribute("searchFlag", true);
        } else {
            log.info("Get all devices");
            devicePage = deviceService.findPaginated(pageRequest, currentSortField);
            model.addAttribute("sortField", sortField);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("devicePage", devicePage);
        }

        int totalPages = devicePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "devices";
    }

    @GetMapping("/addForm")
    public String formCreateModelDevice(Model model) {
        model.addAttribute("deviceDto", new DeviceDto());
        return "addDevice";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute DeviceDto deviceDto, Errors errors) {
        if (errors.hasErrors()) {
            return "addDevice";
        }
        deviceService.create(deviceDto);
        return "redirect:/devices";
    }

    @GetMapping("/{deviceId}")
    public String formEditDevice(@PathVariable long deviceId, Model model) {
        Device device = deviceService.findById(deviceId);
        if (device != null) {
            DeviceReadDto deviceReadDto = deviceReadMapper.map(device);
            List<Check> checks = device.getModel().getChecks().stream()
                    .filter(check -> check.getCheckScope().equals(CheckScope.DEVICE.name())).toList();
            List<LinkReadDto> links = linkService.findAllByDeviceId(deviceId);
            model.addAttribute("links", links);
            model.addAttribute("checks", checks);
            model.addAttribute("deviceDto", deviceReadDto);
        } else {
            log.info("Device with ID: {} not found", deviceId);
            throw new NoSuchElementException("Device with ID: " + deviceId + " not found");
        }
        return "editDevice";
    }

//    @GetMapping("/{deviceId}")
//    public String formEditDevice(@PathVariable long deviceId, Model model) {
//        Device device = deviceService.findById(deviceId);
//        if (device != null) {
//            DeviceDto deviceDto = new DeviceDto(device);
//            List<Check> checks = device.getModel().getChecks().stream()
//                    .filter(check -> check.getCheckScope().equals(CheckScope.DEVICE.name())).toList();
//            model.addAttribute("checks", checks);
//            model.addAttribute("deviceDto", deviceDto);
//        } else {
//            log.info("Device with ID: {} not found", deviceId);
//            throw new NoSuchElementException("Device with ID: " + deviceId + " not found");
//        }
//        return "editDevice";
//    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute DeviceDto deviceDto, Errors errors) {
        if (errors.hasErrors()) {
            return "editDevice";
        }
        return deviceService.update(id, deviceDto)
                .map(customer -> "redirect:/devices")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{deviceId}")
    public String delete(@PathVariable("deviceId") Long deviceId) {
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
    public DeviceDto findFreePortsByDeviceId(@PathVariable long deviceId) {
        log.info("Request for busy ports for deviceId: {}", deviceId);
        return deviceService.findFreePortsByDeviceId(deviceId);
    }
}
