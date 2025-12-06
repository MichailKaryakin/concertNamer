package collector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@SuppressWarnings("ClassCanBeRecord")
public class SongNameCollector {

    private final Path rootDirectory;
    private final Path outputFile;

    public SongNameCollector(String directoryPath, String outputFilePath) {
        this.rootDirectory = Paths.get(directoryPath);
        this.outputFile = Paths.get(outputFilePath);
    }

    /**
     * Читает уже существующие названия из файла (если он есть)
     * и возвращает их в виде сортированного множества.
     */
    private Set<String> loadExistingNames() {
        Set<String> result = new TreeSet<>();
        if (Files.exists(outputFile)) {
            try {
                for (String line : Files.readAllLines(outputFile, StandardCharsets.UTF_8)) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    if (line.startsWith("---")) continue; // метки пропускаются
                    result.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Извлекает название песни из имени файла.
     * Формат: "d1t05 - Song Name.ext" → "Song Name"
     */
    private String extractSongName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        String withoutExt = (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;

        String[] parts = withoutExt.split("-", 2);
        if (parts.length < 2) return null;

        return parts[1].trim();
    }

    /**
     * Сканирует директорию и собирает все уникальные названия песен.
     */
    private Set<String> collectFromDirectory() throws IOException {
        Set<String> collected = new TreeSet<>();

        Files.walk(rootDirectory)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    String filename = path.getFileName().toString().trim();
                    String name = extractSongName(filename);
                    if (name != null && !name.isEmpty()) {
                        collected.add(name);
                    }
                });

        return collected;
    }

    /**
     * Дописывает в outputFile только новые имена песен.
     */
    private void appendNewNames(Set<String> newNames, int runNumber) throws IOException {

        List<String> lines = new ArrayList<>();
        lines.add("\n--- Сбор #" + runNumber + " ---");
        lines.addAll(newNames);

        Files.write(outputFile, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    /**
     * Метод запуска.
     */
    public void run() {
        try {
            // уже существующие названия
            Set<String> existing = loadExistingNames();

            // новые найденные названия
            Set<String> found = collectFromDirectory();

            // вычисление новых элементов
            Set<String> onlyNew = new TreeSet<>(found);
            onlyNew.removeAll(existing);

            if (onlyNew.isEmpty()) {
                System.out.println("Новых названий песен не найдено");
                return;
            }

            // номер сбора — количество уже существующих блоков (меток)
            int runNumber = determineRunNumber();

            appendNewNames(onlyNew, runNumber);

            System.out.println("Добавлено новых песен: " + onlyNew.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Определяет номер текущего сбора
     * (количество предыдущих меток "--- Сбор #X ---").
     */
    private int determineRunNumber() {
        if (!Files.exists(outputFile)) {
            return 1;
        }
        try {
            long count = Files.lines(outputFile).filter(l -> l.startsWith("--- Сбор #")).count();
            return (int) count + 1;
        } catch (IOException e) {
            return 1;
        }
    }
}
