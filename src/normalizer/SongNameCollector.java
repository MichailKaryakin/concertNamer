package normalizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Set;
import java.util.TreeSet;

public class SongNameCollector {

    private final Path rootDirectory;
    private final Path outputFile;
    private final Set<String> uniqueSongNames = new TreeSet<>();

    public SongNameCollector(String directoryPath, String outputFilePath) {
        this.rootDirectory = Paths.get(directoryPath);
        this.outputFile = Paths.get(outputFilePath);
    }

    public boolean songsFileExists() {
        return Files.exists(outputFile);
    }

    public Set<String> collect() throws IOException {
        Files.walk(rootDirectory)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    String filename = path.getFileName().toString();
                    String songName = extractSongName(filename);
                    if (songName != null && !songName.isEmpty()) {
                        uniqueSongNames.add(songName);
                    }
                });

        return uniqueSongNames;
    }

    private String extractSongName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        String withoutExt = (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;

        String[] parts = withoutExt.split("-", 2);
        if (parts.length < 2) return null;

        return parts[1].trim();
    }

    public void writeFile(Set<String> names) throws IOException {
        Files.write(outputFile, names, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void run() {
        try {
            if (songsFileExists()) {
                System.out.println("Файл с песнями уже существует, генерация пропущена.");
                return;
            }

            Set<String> songs = collect();
            writeFile(songs);

            System.out.println("Файл с песнями создан: " + outputFile);
            System.out.println("Выход из программы... Можете указывать замены");

            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
