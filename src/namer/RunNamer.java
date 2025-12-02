package namer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public record RunNamer(DateParser dateParser, FileProcessor fileProcessor, PlaceFormatter placeFormatter,
                       TitleHandler titleHandler) {

    public void run(String path) throws Exception {
        if (path.isEmpty()) {
            System.out.println("Аргумента корневой папки нет");
            return;
        }

        Path root = Paths.get(path);
        Path info = root.resolve("info.txt");

        if (!Files.exists(info)) {
            System.out.println("Файл с информацией не найден");
            return;
        }

        List<String> lines = Files.readAllLines(info);

        String formattedDate = dateParser.extractDate(lines);
        String cityStateRaw = placeFormatter.extractCityState(lines);
        String formattedCityState = placeFormatter.formatLocation(cityStateRaw);

        String finalFolderName = formattedDate + " " + formattedCityState;
        Path targetRoot = root.getParent().resolve(finalFolderName);
        Files.createDirectories(targetRoot);

        Map<String, String> trackTitles = titleHandler.parseTrackTitles(lines);

        try (var stream = Files.list(root)) {
            stream.filter(p -> {
                String n = p.toString().toLowerCase();
                return n.endsWith(".flac") || n.endsWith(".wav");
            }).forEach(p -> fileProcessor.processFile(p, trackTitles, targetRoot));
        }

        System.out.println("Выполнено.");
    }
}
