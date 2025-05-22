package com.ekoregin.nms.http.controller;

import com.ekoregin.nms.dto.PortCreateEditDto;
import com.ekoregin.nms.dto.PortReadDto;
import com.ekoregin.nms.service.PortService;
import com.ekoregin.nms.util.Jacksonable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/ports")
public class PortController {

    private final PortService portService;

    @GetMapping("/addForm/modelDevice/{modelId}")
    public String formCreatePortGroupForModelDevice(@PathVariable("modelId") Long modelId,  Model model) {
        model.addAttribute("modelId", modelId);
        return "port/addPorts";
    }

    /*
    Create ports from modelDevice for device
     */
    @PostMapping("/group/device/{deviceId}")
    public ResponseEntity<Void> createPortsForDeviceFromModel(@PathVariable("deviceId") Long deviceId) {
        log.info("Create ports for device: {} from Model", deviceId);
        portService.createPortsForDeviceFromModel(deviceId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // Create default port group
    @PostMapping("/group/{modelId}")
    public String createDefaultPortGroupForModelDevice(@PathVariable("modelId") Long modelId,
                                                       @RequestParam("ports") int ports) {
        log.info("Group of default ports are creating... " + ports);
        portService.createBunchOfPorts(modelId, ports);
        return "redirect:/modelDevices/editForm/" + modelId;
    }

    @PostMapping
    public ResponseEntity<PortReadDto> create(@RequestBody PortCreateEditDto portCreateEditDto) {
        log.info("Create port. Incoming JSON: " + System.lineSeparator() + Jacksonable.fromObjectToJson(portCreateEditDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(portService.create(portCreateEditDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id,
                                 @RequestBody PortCreateEditDto portCreateEditDto) {
        log.info("Update port. Incoming JSON: " + System.lineSeparator() + Jacksonable.fromObjectToJson(portCreateEditDto));
        portService.update(id, portCreateEditDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        log.info("Delete port. ID: {}", id);
        HttpStatus httpStatus = HttpStatus.OK;
        if(!portService.delete(id)) {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity
                .status(httpStatus)
                .build();
    }
}
