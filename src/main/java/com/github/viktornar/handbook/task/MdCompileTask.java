package com.github.viktornar.handbook.task;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Slf4j
public class MdCompileTask implements Callable<Integer> {
    private final Path binPath;
    private final Path srcPath;

    public MdCompileTask(Path binPath, Path sourcePath) {
        this.binPath = binPath;
        this.srcPath = sourcePath;
    }

    @Override
    public Integer call() throws Exception {
        var command = new StringBuilder();
        command.append(binPath);
        command.append(" ");
        command.append("build");
        command.append(" ");
        command.append(srcPath);

        final Process process;

        try {
            log.info("Start compile job [bin: {}, src: {}]", binPath, srcPath);
            process = Runtime.getRuntime().exec(command.toString());
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }
}
