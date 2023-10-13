package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.TypeTechParamDto;
import com.ekoregin.nms.service.TypeTechParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/typeTechParams")
public class TypeTechParameterController {

   private final TypeTechParameterService service;

   @GetMapping
   public String allTypeTechParams(Model model) {
      List<TypeTechParamDto> typeTechParamDtoList = service.findAll().stream()
              .map(TypeTechParamDto::new)
              .toList();
      model.addAttribute("typeTechParams", typeTechParamDtoList);
      return "typeTechParams";
   }
}
