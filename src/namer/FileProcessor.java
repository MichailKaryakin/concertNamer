package namer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessor {

    public void processFile(Path file, Map<String, String> titles, Path targetRoot) {
        String name = file.getFileName().toString();

        Matcher m = Pattern.compile("d(\\d+)t(\\d+)").matcher(name.toLowerCase());
        if (!m.find()) {
            System.out.println("Пропускается (не dXtYY): " + name);
            return;
        }

        int disc = Integer.parseInt(m.group(1));
        int track = Integer.parseInt(m.group(2));

        String key = String.format("d%dt%02d", disc, track);
        String title = titles.getOrDefault(key, "Unknown");

        Path discFolder = targetRoot.resolve("Disc " + disc);

        try {
            Files.createDirectories(discFolder);
        } catch (IOException ignored) {
        }

        String ext = name.substring(name.lastIndexOf('.'));
        String newName = String.format("%02d - %s%s", track, title, ext);

        Path target = discFolder.resolve(newName);

        try {
            Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("→ " + target);
        } catch (Exception e) {
            System.out.println("Ошибка при копировании " + file);
            e.printStackTrace();
        }
    }
}
