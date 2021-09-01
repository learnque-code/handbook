package com.github.viktornar.handbook.controller;

import com.github.viktornar.handbook.dao.GuideDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = { "/admin" })
@RequiredArgsConstructor
public class AdminController {
    private final GuideDao guideDao;

    @GetMapping(value = { "" })
    String welcome(Model model) {
        var guides = guideDao.allGuides();
        model.addAttribute("guides", guides);
        return "welcome";
    }
}
