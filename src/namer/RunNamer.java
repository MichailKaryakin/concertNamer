package namer;

import java.nio.file.*;

@SuppressWarnings("ClassCanBeRecord")
public class RunNamer {
    private final ConcertFolderProcessor processor;

    public RunNamer(ConcertFolderProcessor processor) {
        this.processor = processor;
    }

    public void run(String rootPath) throws Exception {

        Path root = Paths.get(rootPath);
        if (!Files.isDirectory(root)) {
            System.out.println("Path is not a directory: " + root);
            return;
        }

        try (var folders = Files.list(root)) {
            folders.filter(Files::isDirectory)
                    .forEach(folder -> {
                        try {
                            processor.processConcert(folder);
                        } catch (Exception e) {
                            System.out.println("Error in folder " + folder + ": " + e.getMessage());
                        }
                    });
        }

        System.out.println("Done");
    }
}
