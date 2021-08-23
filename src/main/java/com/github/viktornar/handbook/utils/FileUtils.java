package com.github.viktornar.handbook.utils;

import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {
    private FileUtils() {
    }

    public static void copyRecursively(Path source, Path target, CopyOption... options) throws IOException {
        // TODO: replace with FileSystemUtils.copyRecursively
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), options);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void deleteRecursively(Path source) {
        FileSystemUtils.deleteRecursively(source.toFile());
    }
}
