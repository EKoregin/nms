package com.ekoregin.nms.http.controller;

import com.ekoregin.nms.dto.DeviceDto;
import com.ekoregin.nms.database.entity.GatherResult;
import com.ekoregin.nms.service.DeviceService;
import com.ekoregin.nms.service.GatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/gathering")
public class GatherDataController {

    private final GatherDataService gatherDataService;
    private final DeviceService deviceService;

    @ModelAttribute
    private void currentAuthUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("currentUser", userDetails.getUsername());
    }

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
        GatherResult result = gatherDataService.getMacByIP(deviceId, checkId);
        model.addAttribute("results", result);
        return "getMacByIpResult";
    }
}
