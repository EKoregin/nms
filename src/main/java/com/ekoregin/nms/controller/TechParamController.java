package com.ekoregin.nms.controller;


import com.ekoregin.nms.dto.TechParamDto;
import com.ekoregin.nms.dto.TypeTechParamDto;
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

    @ModelAttribute(name = "techParamDto")
    public TechParamDto techParamDto() {
        return new TechParamDto();
    }

    @ModelAttribute(name = "typeTechParams")
    public List<TypeTechParamDto> typeTechParamDtoList() {
        return typeTechParamService.findAll().stream()
                .map(TypeTechParamDto::new)
                .toList();
    }

    @GetMapping
    public String allTechParams(Model model) {
        List<TechParamDto> techParamDtoList = techParamService.findAll().stream()
                .map(TechParamDto::new)
                .toList();
        model.addAttribute("techParams", techParamDtoList);
        return "techParams";
    }

    @GetMapping("/addForm")
    public String formCreateTechParam(Model model) {
        return "addTechParam";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TechParamDto techParamDto) {
        log.info("TechParamDto for save: {}", techParamDto);
        techParamService.create(techParamDto);
        return "redirect:/techParams";
    }
}
