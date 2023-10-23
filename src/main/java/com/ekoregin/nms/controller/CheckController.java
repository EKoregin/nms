package com.ekoregin.nms.controller;

import com.ekoregin.nms.service.CheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
