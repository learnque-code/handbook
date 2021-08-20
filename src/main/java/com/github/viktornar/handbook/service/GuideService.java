package com.github.viktornar.handbook.service;

import com.github.viktornar.handbook.dao.GuideDao;
import com.github.viktornar.handbook.github.repositories.RepositoryService;
import com.github.viktornar.handbook.github.repositories.RepositoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuideService {
    private final RepositoryService repositoryService;
    private final GuideDao guideDao;

    public void persistGuideMetadata(String repositoryName, RepositoryType type, Path path) {
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
    }
}
