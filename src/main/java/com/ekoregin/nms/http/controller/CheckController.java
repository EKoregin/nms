package com.ekoregin.nms.http.controller;

import com.ekoregin.nms.database.entity.Check;
import com.ekoregin.nms.database.entity.CheckResult;
import com.ekoregin.nms.database.entity.CheckScope;
import com.ekoregin.nms.database.entity.TypeTechParameter;
import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.ModelDeviceDto;
import com.ekoregin.nms.service.CheckService;
import com.ekoregin.nms.service.ModelDeviceService;
import com.ekoregin.nms.service.TypeTechParamService;
import com.ekoregin.nms.util.CheckType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/checks")
public class CheckController {

    @ModelAttribute
    private void currentAuthUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("currentUser", userDetails.getUsername());
    }

    private final CheckService checkService;
    private final TypeTechParamService typeTechParameterService;
    private final ModelDeviceService modelDeviceService;

    @GetMapping String allChecks(Model model) {
        model.addAttribute("checks", checkService.findAll());
        return "checks";
    }

    @ModelAttribute
    private void checkTypesScopes(Model model) {
        List<String> checkTypes = Arrays.stream(CheckType.class.getEnumConstants()).map(Enum::name).toList();
        List<String> checkScopes = Arrays.stream(CheckScope.class.getEnumConstants()).map(Enum::name).toList();
        model.addAttribute("checkTypes", checkTypes);
        model.addAttribute("checkScopes", checkScopes);
    }

    @GetMapping("/addForm/{modelDeviceId}")
    public String formCreateCheck(@PathVariable long modelDeviceId, Model model) {
        ModelDeviceDto modelDeviceDto = new ModelDeviceDto(modelDeviceService.findById(modelDeviceId));
        CheckDto checkDto = new CheckDto();
        model.addAttribute("modelDevice", modelDeviceDto);
        model.addAttribute("checkDto", checkDto);
        return "addCheck";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CheckDto checkDto) {
        checkService.create(checkDto);
        return "redirect:/modelDevices/editForm/" + checkDto.getModelDeviceId();
    }

    @GetMapping("/editForm/{checkId}")
    public String formEditCheck(@PathVariable long checkId, Model model) {
        Check foundCheck = checkService.findById(checkId);
        if (foundCheck != null) {
            ModelDeviceDto modelDeviceDto = new ModelDeviceDto(foundCheck.getModelDevice());
            CheckDto checkDto = new CheckDto(foundCheck);
            model.addAttribute("checkDto", checkDto);
            model.addAttribute("typeTechParams", foundCheck.getTypeTechParams());
            model.addAttribute("modelDevice", modelDeviceDto);
        } else {
            log.info("Check with ID: {} not found", checkId);
            throw new NoSuchElementException("Check with ID: " + checkId + " not found");
        }
        return "editCheck";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute CheckDto checkDto) {
        checkService.update(checkDto);
        return "redirect:/modelDevices/editForm/" + checkDto.getModelDeviceId();
    }

    @DeleteMapping("/{checkId}")
    public String delete(@PathVariable long checkId) {
        checkService.delete(checkId);
        return "redirect:/checks";
    }

    @GetMapping("/addTypeParam/{checkId}")
    public String formAddTypeParamToCheck(@PathVariable long checkId, Model model) {
        Check foundCheck = checkService.findById(checkId);
        if (foundCheck != null) {
            model.addAttribute("checkId", checkId);
            model.addAttribute("allTypeParams", typeTechParameterService.findAll());
        } else {
            log.info("Check with ID: {} not found", checkId);
            throw new NoSuchElementException("Check with ID: " + checkId + " not found");
        }
        return "addTypeParamToCheck";
    }

    @PostMapping("/addTypeParamToCheck")
    public String addTypeParamToCheck(@RequestParam("checkId") long checkId,
                                      @RequestParam("typeParamId") long typeParamId) {
        Check foundCheck = checkService.findById(checkId);
        TypeTechParameter typeTechParameter = typeTechParameterService.findById(typeParamId);
        foundCheck.getTypeTechParams().add(typeTechParameter);
        checkService.update(foundCheck);
        return "redirect:/checks/editForm/" + checkId;
    }

    @DeleteMapping("/delTypeParamFromCheck/{checkId}/typeParam/{typeParamId}")
    public String delTypeParamFromCheck(@PathVariable long checkId,
                                        @PathVariable long typeParamId) {
        log.info("Delete typeTechParameter with ID {}, from Check ID: {}", typeParamId, checkId);
        Check foundCheck = checkService.findById(checkId);
        TypeTechParameter typeTechParameter = typeTechParameterService.findById(typeParamId);
        foundCheck.getTypeTechParams().remove(typeTechParameter);
        checkService.update(foundCheck);
        return "redirect:/checks/editForm/" + checkId;
    }

    @GetMapping("/{checkId}/customer/{customerId}")
    @ResponseBody
    public CheckResult checkExecuteForCustomer(@PathVariable long checkId,
                                               @PathVariable long customerId) {
        log.info("Run Check with ID: {} for Customer with ID: {}", checkId, customerId);
        return checkService.executeForCustomer(checkId, customerId);
    }

    @GetMapping("/{checkId}/device/{deviceId}")
    @ResponseBody
    public CheckResult checkExecuteForDevice(@PathVariable long checkId,
                                    @PathVariable long deviceId) {
        log.info("Run Check with ID: {} for Device with ID: {}", checkId, deviceId);
        return checkService.executeForDevice(checkId, deviceId);
    }
}
