package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.ModelDevice;
import com.ekoregin.nms.entity.TypeTechParameter;
import com.ekoregin.nms.service.CheckService;
import com.ekoregin.nms.service.ModelDeviceService;
import com.ekoregin.nms.service.TypeTechParameterService;
import com.ekoregin.nms.util.TypeDevice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/modelDevices")
public class ModelDeviceController {

    public final ModelDeviceService modelDeviceService;
    private final CheckService checkService;
    private final TypeTechParameterService ttpService;

    @ModelAttribute
    public void addDeviceType(Model model) {
        List<String> types = Arrays.stream(TypeDevice.class.getEnumConstants()).map(Enum::name).toList();
        model.addAttribute("deviceTypes", types);
    }

    @ModelAttribute
    public void addTypeParameter(Model model) {
        List<TypeTechParameter> typeTechParameters = ttpService.findAll();
        model.addAttribute("typeParams", typeTechParameters);
    }

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
    public String create(@Valid @ModelAttribute ModelDeviceDto modelDeviceDto, Errors errors) {
        if (errors.hasErrors()) {
            return "addModelDevice";
        }
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
    public String update(@Valid @ModelAttribute ModelDeviceDto modelDeviceDto, Errors errors) {
        if (errors.hasErrors()) {
            return "editModelDevice";
        }
        modelDeviceService.update(modelDeviceDto);
        return "redirect:/modelDevices";
    }

    @DeleteMapping("/{modelId}")
    public String delete(@PathVariable long modelId) {
        modelDeviceService.delete(modelId);
        return "redirect:/modelDevices";
    }

    @GetMapping("/addCheck/{modelId}")
    public String formAddCheckToModel(@PathVariable long modelId, Model model) {
        ModelDevice foundModelDevice = modelDeviceService.findById(modelId);
        if (foundModelDevice != null) {
            model.addAttribute("modelId", modelId);
            model.addAttribute("allChecks", checkService.findAll());
        } else {
            log.info("Model with ID: {} not found", modelId);
            throw new NoSuchElementException("Model with ID: " + modelId + " not found");
        }
        return "addCheckToModel";
    }

    @PostMapping("/addCheckToModel")
    public String addCheckToModel(@RequestParam("modelId") long modelId,
                                      @RequestParam("checkId") long checkId) {
        ModelDevice modelDevice = modelDeviceService.findById(modelId);
        Check check = checkService.findById(checkId);
        modelDevice.getChecks().add(check);
        log.info("Проверка ID модели перед сохранением после добавления в нее проверки. ID model = {}", modelDevice.getId());
        modelDeviceService.update(modelDevice);
        return "redirect:/modelDevices/editForm/" + modelId;
    }

    @DeleteMapping("/delCheckFromModel/{modelId}/check/{checkId}")
    public String delCheckFromModel(@PathVariable long modelId,
                                        @PathVariable long checkId) {
        log.info("Delete check with ID {}, from model ID: {}", checkId, modelId);
        ModelDevice foundModelDevice = modelDeviceService.findById(modelId);
        Check foundCheck = checkService.findById(checkId);
        log.info(foundModelDevice.toString());
        foundModelDevice.getChecks().remove(foundCheck);
        log.info(foundModelDevice.toString());
        modelDeviceService.update(foundModelDevice);
        return "redirect:/modelDevices/editForm/" + modelId;
    }
}
