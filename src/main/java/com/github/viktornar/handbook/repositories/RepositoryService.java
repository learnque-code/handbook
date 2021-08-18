package com.github.viktornar.handbook.repositories;

import com.github.viktornar.handbook.HandbookProperties;
import com.github.viktornar.handbook.client.github.GithubClient;
import com.github.viktornar.handbook.task.MdCompileTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@RequiredArgsConstructor
@Service
public class RepositoryService {
    private final static String MD_COMPILER_NAME = "mdbook";
    private final static String COMPILED_GUIDE_FOLDER = "book";
    private final static String GUIDES_FOLDER = "guides";

    private final GithubClient githubClient;
    private final HandbookProperties properties;
    private final ExecutorService executorService;


    public Repository getRepositoryByName(String name) {
        return githubClient.fetchOrgRepository(properties.getGuides().getOrganization(), name);
    }

    public List<Repository> getRepositories() {
        return githubClient.fetchOrgRepositories(properties.getGuides().getOrganization());
    }

    public Optional<Path> extract(final RepositoryType type, final String repositoryName) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        String org = properties.getGuides().getOrganization();
        String tempFilePrefix = org + "-" + repositoryName;

        File unzippedRoot = null;
        File zipball = null;

        try {
            byte[] download = githubClient.downloadRepositoryAsZipball(org, repositoryName);
            // First, write the downloaded stream of bytes into a file
            zipball = File.createTempFile(
                    tempFilePrefix, ".zip", Path.of(properties.getWeb().getTmp()).toFile());
            zipball.deleteOnExit();
            FileOutputStream zipOut = new FileOutputStream(zipball);
            zipOut.write(download);
            zipOut.close();

            // Open the zip file and unpack it
            try (ZipFile zipFile = new ZipFile(zipball)) {
                for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e
                        .hasMoreElements(); ) {
                    ZipEntry entry = e.nextElement();

                    if (entry.isDirectory()) {
                        File dir = new File(
                                zipball.getParent() + File.separator + entry.getName());

                        if ((dir.mkdir() || dir.exists()) && unzippedRoot == null) {
                            unzippedRoot = dir; // first directory is the root
                        }
                    } else {
                        StreamUtils.copy(zipFile.getInputStream(entry),
                                new FileOutputStream(zipball.getParent() + File.separator
                                        + entry.getName()));
                    }
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Could not create temp file for source: " + tempFilePrefix, ex);
        } finally {
            FileSystemUtils.deleteRecursively(zipball);
        }

        if (unzippedRoot != null) {
            compile(unzippedRoot.toPath());
            Path guidePath = copy(type, repositoryName, unzippedRoot);

            return Optional.of(guidePath);
        }

        return Optional.empty();
    }

    private Path copy(RepositoryType type, String repositoryName, File unzippedRoot) throws IOException {
        var targetPath = Path.of(
                properties.getWeb().getHome(), GUIDES_FOLDER, type.toString(), repositoryName);
        RepositoryUtils.copyRecursively(
                unzippedRoot.toPath().resolve(COMPILED_GUIDE_FOLDER), targetPath, StandardCopyOption.REPLACE_EXISTING);
        FileSystemUtils.deleteRecursively(unzippedRoot);

        return targetPath;
    }

    private void compile(Path sourcePath) throws ExecutionException, InterruptedException, TimeoutException {
        var binPath = Path.of(properties.getWeb().getBin(), MD_COMPILER_NAME);
        var printTask = new MdCompileTask(binPath, sourcePath);
        var compileFuture = executorService.submit(printTask);
        compileFuture.get(10, TimeUnit.SECONDS);
    }
}
