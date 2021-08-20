package com.github.viktornar.handbook.controller.api;

import com.github.viktornar.handbook.dao.GuideDao;
import com.github.viktornar.handbook.github.repositories.Repository;
import com.github.viktornar.handbook.github.repositories.RepositoryExportException;
import com.github.viktornar.handbook.github.repositories.RepositoryService;
import com.github.viktornar.handbook.github.repositories.RepositoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RepositoriesV1Controller extends ApiV1Controller {
    private final RepositoryService repositoryService;
    private final GuideDao guideDao;

    @GetMapping(value = {"/repositories"})
    @ResponseStatus(value = HttpStatus.OK)
    List<Repository> getRepositories() {
        return repositoryService.getRepositories();
    }

    @GetMapping(value = "/repositories/{name}")
    @ResponseStatus(value = HttpStatus.OK)
    Repository getRepository(@PathVariable String name) {
        return repositoryService.getRepositoryByName(name);
    }

    @PostMapping(value = "/repositories/index")
    @ResponseStatus(value = HttpStatus.CREATED)
    void persistAndIndexRepositories() {
        log.info("Re-indexing all repositories");
    }

    @PostMapping(value = "/repositories/{repositoryName}/download")
    @ResponseStatus(value = HttpStatus.CREATED)
    void downloadAndExtractRepository(@PathVariable String repositoryName) throws
            IOException, ExecutionException, InterruptedException, TimeoutException, RepositoryExportException {
        log.info("Got guide repository {{}} to download and extract", repositoryName);
        var type = RepositoryType.fromRepositoryName(repositoryName);
        Optional<Path> exportedPath = repositoryService.extract(type, repositoryName);
        exportedPath.ifPresent(path -> {
            log.info("Guide exported to {{}}", path);
            var repository = repositoryService.getRepositoryByName(repositoryName);
            var tokens = repository.getDescription().split("::");
            var name = String.join("::", Arrays.copyOfRange(tokens, 0, tokens.length - 1));
            var description = tokens[tokens.length - 1].trim();
            var topics = String.join(", ", repository.getTopics());
            var id = guideDao.existsByName(name);
            Optional.ofNullable(id).ifPresentOrElse(ei -> {
                var changed = guideDao.updateGuide("/guides/" + type + "/" + repositoryName,
                        path.toString(), type.toString(), name, description, topics, ei);
                Optional.ofNullable(changed).ifPresentOrElse(
                        c -> log.info("Guide metadata updated in database with id {{}} at {{}}.", ei, changed),
                        () -> log.warn("Was not able to update guide metadata."));
            }, () -> {
                String newId = guideDao.insertGuide("/guides/" + type + "/" + repositoryName,
                        path.toString(), type.toString(), name, description, topics);
                Optional.ofNullable(newId).ifPresentOrElse(
                        ni -> log.info("Guide metadata persisted in database with id {{}}.", ni),
                        () -> log.warn("Was not able to persist guide metadata."));
            });
        });

        exportedPath.orElseThrow(() -> new RepositoryExportException("Was not able to download and / or export repository"));
    }
}
