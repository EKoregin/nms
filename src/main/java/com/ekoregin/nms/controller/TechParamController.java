package com.ekoregin.nms.controller;


import com.ekoregin.nms.dto.TechParameterDto;
import com.ekoregin.nms.entity.TechParameter;
import com.ekoregin.nms.entity.TypeTechParameter;
import com.ekoregin.nms.service.TechParamService;
import com.ekoregin.nms.service.TypeTechParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/techParams")
public class TechParamController {

    private final TechParamService techParamService;
    private final TypeTechParameterService typeTechParamService;

    @ModelAttribute(name = "typeTechParams")
    public List<TypeTechParameter> typeTechParameterList() {
        return typeTechParamService.findAll();
    }

    @GetMapping
    public String allTechParams(Model model) {
        List<TechParameter> techParameterList = techParamService.findAll();
        model.addAttribute("techParams", techParameterList);
        return "techParams";
    }

    @GetMapping("/addForm/{customerId}")
    public String formCreateTechParam(@PathVariable long customerId, Model model) {
        model.addAttribute("customerId");
        TechParameterDto techParameterDto = new TechParameterDto();
        techParameterDto.setCustomerId(customerId);
        model.addAttribute("techParameterDto", techParameterDto);
        return "addTechParam";
    }

    @GetMapping("/editForm/{techParamId}")
    public String formEditTechParam(@PathVariable long techParamId, Model model) {
        TechParameter techParameter = techParamService.findById(techParamId);
        TechParameterDto techParameterDto = new TechParameterDto(techParameter);
        model.addAttribute("techParameterDto", techParameterDto);
        return "editTechParam";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TechParameterDto techParameterDto) {
        log.info("TechParamDto for save: {}", techParameterDto);
        long customerId = techParameterDto.getCustomerId();
        techParamService.create(techParameterDto);
        return "redirect:/customers/editForm/" + customerId;
    }

    @PutMapping("/update")
    public String update(@ModelAttribute TechParameterDto techParameterDto) {
        log.info("TechParamDto for update: {}", techParameterDto);
        long techParamId = techParameterDto.getParamId();
        long customerId = techParameterDto.getCustomerId();
        TypeTechParameter typeTechParameter = typeTechParamService.findById(techParameterDto.getTypeId());
        TechParameter techParameter = techParamService.findById(techParamId);
        techParameter.setValue(techParameterDto.getValue());
        techParameter.setType(typeTechParameter);
        techParamService.update(techParameter);
        return "redirect:/customers/editForm/" + customerId;
    }

    @DeleteMapping("/{techParamId}/customer/{customerId}")
    public String delete(@PathVariable long techParamId,
                         @PathVariable long customerId) {
        log.info("Delete techParam with ID {}, from customer ID: {}", techParamId, customerId);
        techParamService.delete(techParamId);
        return "redirect:/customers/editForm/" + customerId;
    }
}
