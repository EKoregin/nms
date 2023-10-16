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

    @GetMapping("/addForm")
    public String formCreateTechParam(Model model) {
        model.addAttribute("techParameterDto", new TechParameterDto());
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
        techParamService.create(techParameterDto);
        return "redirect:/techParams";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute TechParameterDto techParameterDto) {
        log.info("TechParamDto for update: {}", techParameterDto);
        long techParamId = techParameterDto.getId();
        TypeTechParameter typeTechParameter = typeTechParamService.findById(techParameterDto.getTypeId());
        TechParameter techParameter = techParamService.findById(techParamId);
        techParameter.setValue(techParameterDto.getValue());
        techParameter.setType(typeTechParameter);
        techParamService.update(techParameter);
        return "redirect:/techParams";
    }

    @DeleteMapping("/{techParamId}")
    public String delete(@PathVariable("techParamId") long techParamId) {
        techParamService.delete(techParamId);
        return "redirect:/techParams";
    }
}
