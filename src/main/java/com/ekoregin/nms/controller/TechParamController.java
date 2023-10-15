package com.ekoregin.nms.controller;


import com.ekoregin.nms.dto.TechParamDto;
import com.ekoregin.nms.dto.TypeTechParamDto;
import com.ekoregin.nms.service.TechParamService;
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

    @GetMapping
    public String allTechParams(Model model) {
        List<TechParamDto> techParamDtoList = techParamService.findAll().stream()
                .map(TechParamDto::new)
                .toList();
        model.addAttribute("techParams", techParamDtoList);
        return "techParams";
    }

    @GetMapping("/addForm")
    public String formCreateTechParam() {
        return "addTechParam";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TechParamDto techParamDto) {
        techParamService.create(techParamDto);
        return "redirect:/techParams";
    }
}
