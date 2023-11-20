package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.TypeTechParameterDto;
import com.ekoregin.nms.entity.TypeTechParameter;
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
@RequestMapping("/typeTechParams")
public class TypeTechParameterController {

   private final TypeTechParameterService service;

   @GetMapping
   public String allTypeTechParams(Model model) {
      List<TypeTechParameter> typeTechParameterList = service.findAll();
      model.addAttribute("typeTechParams", typeTechParameterList);
      return "typeTechParams";
   }

   @GetMapping("/addForm")
   public String formCreateTypeTechParam() {
      return "addTypeTechParam";
   }

   @GetMapping("/editForm/{typeTechParamId}")
   public String formEditTechParam(@PathVariable long typeTechParamId, Model model) {
      TypeTechParameter typeTechParameter = service.findById(typeTechParamId);
      TypeTechParameterDto typeTechParameterDto = new TypeTechParameterDto(typeTechParameter);
      model.addAttribute("typeTechParameterDto", typeTechParameterDto);
      return "editTypeTechParam";
   }

   @PostMapping("/create")
   public String create(@ModelAttribute TypeTechParameter typeTechParam) {
      service.create(typeTechParam);
      return "redirect:/typeTechParams";
   }

   @PutMapping("/update")
   public String update(@ModelAttribute TypeTechParameterDto typeTechParameterDto) {
      log.info("TypeTechParamDto for update: {}", typeTechParameterDto);
      service.update(new TypeTechParameter(typeTechParameterDto));
      return "redirect:/typeTechParams";
   }
}
