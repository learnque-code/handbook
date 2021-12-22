package com.github.viktornar.handbook.controller;

import com.github.viktornar.handbook.dao.GuideDao;
import com.github.viktornar.handbook.dto.GuideDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = { "/" })
@RequiredArgsConstructor
public class WelcomeController {
    private final GuideDao guideDao;

    @GetMapping(value = { "" })
    String show(Model model) {
        var guides = guideDao.allGuides();
        var noneMatch = guides.stream().noneMatch(GuideDTO::getActive);
        model.addAttribute("guides", guides);
        model.addAttribute("noneMatch", noneMatch);
        return "welcome";
    }
}
