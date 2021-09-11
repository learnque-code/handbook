package com.github.viktornar.handbook.service;

import com.github.viktornar.handbook.HandbookProperties;
import com.github.viktornar.handbook.dao.GuideDao;
import com.github.viktornar.handbook.domain.Guide;
import com.github.viktornar.handbook.github.repositories.RepositoryService;
import com.github.viktornar.handbook.github.repositories.RepositoryType;
import com.github.viktornar.handbook.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
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
                    path.toString(), type.toString(), name, description, topics, repositoryName, ei);
            Optional.ofNullable(changed).ifPresentOrElse(
                    c -> log.info("Guide metadata updated in database with id {{}} at {{}}.", ei, changed),
                    () -> log.warn("Was not able to update guide metadata."));
        }, () -> {
            String newId = guideDao.insertGuide("/guides/" + type + "/" + repositoryName,
                    path.toString(), type.toString(), name, description, topics, repositoryName);
            Optional.ofNullable(newId).ifPresentOrElse(
                    ni -> log.info("Guide metadata persisted in database with id {{}}.", ni),
                    () -> log.warn("Was not able to persist guide metadata."));
        });
    }

    public void deleteById(String id) {
        var guide = guideDao.findById(id);
        Optional.ofNullable(guide).ifPresentOrElse(g -> {
            log.info("Trying to remove guide with id {{}}", id);
            var count = guideDao.deleteById(id);
            log.info("Removed guide with id {{}} ({})", id, count);
            assert guide != null;
            var path = Path.of(guide.getPath());
            FileUtils.deleteRecursively(path);
        }, () -> {
            log.warn("Unable to delete guide with id {{}}. Probably already removed.", id);
        });
    }

    public List<Guide> findAll() {
        return guideDao.allGuides();
    }
}
