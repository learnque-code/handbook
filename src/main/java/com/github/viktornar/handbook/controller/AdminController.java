package com.github.viktornar.handbook.controller;

import com.github.viktornar.handbook.dao.GuideDao;
import com.github.viktornar.handbook.github.repositories.RepositoryExportException;
import com.github.viktornar.handbook.github.repositories.RepositoryService;
import com.github.viktornar.handbook.github.repositories.RepositoryType;
import com.github.viktornar.handbook.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping(value = {"/admin"})
@RequiredArgsConstructor
public class AdminController {
    private final GuideDao guideDao;
    private final RepositoryService repositoryService;
    private final GuideService guideService;

    @GetMapping(value = {""})
    String show(Model model) {
        var guides = guideDao.allGuides();
        model.addAttribute("guides", guides);
        return "admin";
    }

    @GetMapping(value = {"/_update/{id}"})
    String update(@PathVariable String id, Model model)
            throws RepositoryExportException, IOException, ExecutionException, InterruptedException, TimeoutException {
        var guide = guideDao.findById(id);
        if (guide != null) {
            var type = RepositoryType.fromRepositoryName(guide.getRepositoryName());
            Optional<Path> exportedPath = repositoryService.extract(type, guide.getRepositoryName());
            exportedPath.ifPresent(path -> guideService.persistGuideMetadata(guide.getRepositoryName(), type, path));
            exportedPath.orElseThrow(() ->
                    new RepositoryExportException("Was not able to download and / or export repository"));
        }
        return "redirect:/admin";
    }

    @GetMapping(value = {"/_switch/{id}"})
    String activateDeactivate(@PathVariable String id, Model model) {
        Optional.ofNullable(guideDao.findById(id)).ifPresent(g -> {
            guideDao.updateGuideActiveStatus(!g.getActive(), id);
        });
        return "redirect:/admin";
    }

    @GetMapping(value = {"/_delete/{id}"})
    String delete(@PathVariable String id, Model model) {
        guideService.deleteById(id);
        return "redirect:/admin";
    }
}
