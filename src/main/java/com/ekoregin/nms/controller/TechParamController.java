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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/techParams")
public class TechParamController {

    private final TechParamService techParamService;
    private final TypeTechParameterService typeTechParamService;

    @ModelAttribute(name = "techParameterDto")
    public TechParameterDto techParameterDto() {
        return new TechParameterDto();
    }

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
        return "addTechParam";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TechParameterDto techParameterDto) {
        log.info("TechParamDto for save: {}", techParameterDto);
        techParamService.create(techParameterDto);
        return "redirect:/techParams";
    }
}
