package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.CheckDto;
import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.service.CheckService;
import com.ekoregin.nms.util.CheckType;
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
@RequestMapping("/checks")
public class CheckController {

    private final CheckService checkService;

    @GetMapping String allChecks(Model model) {
        model.addAttribute("checks", checkService.findAll());
        return "checks";
    }

    @GetMapping("/addForm")
    public String formCreateCheck(Model model) {
        List<String> checkTypes = Arrays.stream(CheckType.class.getEnumConstants()).map(Enum::name).toList();
        model.addAttribute("checkTypes", checkTypes);
        model.addAttribute("checkDto", new CheckDto());
        return "addCheck";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CheckDto checkDto) {
        checkService.create(checkDto);
        return "redirect:/checks";
    }

    @GetMapping("/editForm/{checkId}")
    public String formEditCheck(@PathVariable long checkId, Model model) {
        List<String> checkTypes = Arrays.stream(CheckType.class.getEnumConstants()).map(Enum::name).toList();
        model.addAttribute("checkTypes", checkTypes);
        Check foundCheck = checkService.findById(checkId);
        if (foundCheck != null) {
            CheckDto checkDto = new CheckDto(foundCheck);
            model.addAttribute("checkDto", checkDto);
        } else {
            log.info("Check with ID: {} not found", checkId);
            throw new NoSuchElementException("Check with ID: " + checkId + " not found");
        }
        return "editCheck";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute CheckDto checkDto) {
        checkService.update(checkDto);
        return "redirect:/checks";
    }

    @DeleteMapping("/{checkId}")
    public String delete(@PathVariable long checkId) {
        checkService.delete(checkId);
        return "redirect:/checks";
    }
}
