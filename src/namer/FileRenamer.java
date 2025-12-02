package namer;

import java.io.IOException;
import java.nio.file.*;

public class FileRenamer {

    private static final String TRACK_FMT = "%02d - %s%s";

    public void renameFile(Path file, String title, int disc, int track, Path targetRoot) throws IOException {

        Path discFolder = targetRoot.resolve("Disc " + disc);
        Files.createDirectories(discFolder);

        String ext = file.getFileName().toString()
                .substring(file.getFileName().toString().lastIndexOf('.'));

        String newName = TRACK_FMT.formatted(track, title, ext);
        Path target = discFolder.resolve(newName);

        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
    }
}
