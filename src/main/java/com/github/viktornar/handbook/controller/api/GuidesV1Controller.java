package com.github.viktornar.handbook.controller.api;

import com.github.viktornar.handbook.domain.Guide;
import com.github.viktornar.handbook.service.GuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GuidesV1Controller extends ApiV1Controller {
    private final GuideService guideService;

    @GetMapping(value = {"guides"})
    List<Guide> getGuides() {
        return guideService.findAll();
    }

    @DeleteMapping(value = {"guides/{guideId}"})
    void deleteGuideByName(@PathVariable("guideId") String guideId) {
        guideService.deleteById(guideId);
    }
}
