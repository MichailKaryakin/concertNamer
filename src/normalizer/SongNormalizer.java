package normalizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SongNormalizer {

    private final Map<String, String> corrections = new HashMap<>();

    public void loadNormalizedNames(String songsFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(songsFile), StandardCharsets.UTF_8);

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                String bad = parts[0].trim();
                String good = parts[1].trim();
                if (!good.isEmpty()) {
                    corrections.put(bad, good);
                }
            } else {
                corrections.put(line, line);
            }
        }
    }

    public void normalizeDirectory(String directory) throws IOException {
        Path root = Paths.get(directory);

        Files.walk(root)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        normalizeFile(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void normalizeFile(Path file) throws IOException {
        String filename = file.getFileName().toString();
        int dot = filename.lastIndexOf('.');
        String ext = dot > 0 ? filename.substring(dot) : "";
        String base = dot > 0 ? filename.substring(0, dot) : filename;

        String[] parts = base.split("-", 2);
        if (parts.length < 2) return;

        String trackNumber = parts[0].trim();
        String currentSong = parts[1].trim();

        String newSong = corrections.getOrDefault(currentSong, currentSong);

        if (newSong.equals(currentSong)) return;

        String newName = trackNumber + " - " + newSong + ext;
        Path newPath = file.resolveSibling(newName);

        System.out.println("Переименовано: " + filename + " -> " + newName);

        Files.move(file, newPath);
    }
}
