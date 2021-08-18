package com.github.viktornar.handbook.controller.api;

import com.github.viktornar.handbook.HandbookProperties;
import com.github.viktornar.handbook.client.github.GithubClient;
import com.github.viktornar.handbook.repositories.Repository;
import com.github.viktornar.handbook.repositories.RepositoryService;
import com.github.viktornar.handbook.repositories.RepositoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RepositoriesV1Controller extends ApiV1Controller {
    private final GithubClient githubClient;
    private final RepositoryService repositoryService;
    private final HandbookProperties properties;

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
    void downloadAndExtractRepository(@PathVariable String repositoryName) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        log.info("Got guide repository {{}} to download and extract", repositoryName);
        Optional<Path> exportedPath = repositoryService.extract(RepositoryType.fromRepositoryName(repositoryName), repositoryName);
        exportedPath.ifPresent(path -> log.info("Guide exported to {{}}", path));
    }
}
