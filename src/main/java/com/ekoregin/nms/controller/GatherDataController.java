package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.service.DeviceService;
import com.ekoregin.nms.service.GatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/gathering")
public class GatherDataController {

    private final GatherDataService gatherDataService;
    private final DeviceService deviceService;

    @GetMapping("/getMacByIpAllCustomers")
    private String formGetMacByIp(Model model) {

        List<DeviceDto> devices = deviceService.findAll()
                .stream()
                .filter(device -> device.getModel().getType().equals("ROUTER"))
                .map(DeviceDto::new)
                .toList();

        model.addAttribute("devices", devices);
        return "getMacByIpForm";
    }

    @PostMapping("/getMacByIp")
    private String getMacByIp(Model model,
                              @RequestParam("deviceId") long deviceId,
                              @RequestParam("checkId") long checkId) {
        gatherDataService.getMacByIP(deviceId, checkId);

        return "getMacByIpResult";
    }
}
