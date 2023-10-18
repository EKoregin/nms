package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.service.ModelDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/modelDevice")
public class ModelDeviceController {

    public final ModelDeviceService modelDeviceService;

    @GetMapping
    public String allModelDevices(Model model) {
        model.addAttribute("models", modelDeviceService.findAll());
        return "modelDevices";
    }

    @GetMapping("/addForm")
    public String formCreateModelDevice(Model model) {
        model.addAttribute("modelDeviceDto", new ModelDeviceDto());
        return "addModelDevice";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ModelDeviceDto modelDeviceDto) {
        modelDeviceService.create(modelDeviceDto);
        return "redirect:/modelDevices";
    }

    @GetMapping("/editForm/{modelId}")
    public String formEditModelDevice(@PathVariable long modelId, Model model) {
        ModelDevice modelDevice = modelDeviceService.findById(modelId);
        if (modelDevice != null) {
            ModelDeviceDto modelDeviceDto = new ModelDeviceDto(modelDevice);
            model.addAttribute("modelDeviceDto", modelDeviceDto);
        } else {
            log.info("ModelDevice with ID: {} not found", modelId);
            throw new NoSuchElementException("ModelDevice with ID: " + modelId + " not found");
        }
        return "editModelDevice";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute ModelDeviceDto modelDeviceDto) {
        modelDeviceService.update(modelDeviceDto);
        return "redirect:/modelDevices";
    }
}
