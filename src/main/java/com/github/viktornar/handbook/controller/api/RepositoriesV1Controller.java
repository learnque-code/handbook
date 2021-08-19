package com.github.viktornar.handbook.controller.api;

import com.github.viktornar.handbook.dao.GuideDao;
import com.github.viktornar.handbook.repositories.Repository;
import com.github.viktornar.handbook.repositories.RepositoryService;
import com.github.viktornar.handbook.repositories.RepositoryType;
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

    @PostMapping(value = "/repositories/{name}/download")
    @ResponseStatus(value = HttpStatus.CREATED)
    void downloadAndExtractRepository(@PathVariable String name) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        log.info("Got guide repository {{}} to download and extract", name);
        var type = RepositoryType.fromRepositoryName(name);
        Optional<Path> exportedPath = repositoryService.extract(type, name);
        exportedPath.ifPresent(path -> {
            log.info("Guide exported to {{}}", path);
            var repository = repositoryService.getRepositoryByName(name);
            var tokens = repository.getDescription().split("::");
            var title = String.join("::", Arrays.copyOfRange(tokens, 0, tokens.length - 1));
            var description = tokens[tokens.length - 1].trim();
            var topics = String.join(", ", repository.getTopics());
            String id = guideDao.insertGuide("/guides/" + type + "/" + name,
                    path.toString(), type.toString(), title, description, topics);
            log.info("Guide metadata persisted in database with id {{}}", id);
        });
    }
}
