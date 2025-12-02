package namer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Утилита для разбора и нормализации названий треков вида:
 *   d<номер диска>t<номер трека> - <название>
 */
public class TitleHandler {

    /** Шаблон для строк вида: d2t05 - Song Title */
    private static final Pattern TRACK_PATTERN =
            Pattern.compile("d(\\d+)t(\\d+)\\s*-\\s*(.+)");

    /** Слова, которые должны оставаться строчными, если они не первые и не последние в названии */
    private static final Set<String> LOWER_CASE_WORDS = Set.of(
            "a", "an", "the",
            "and", "or", "but",
            "in", "on", "at", "to", "from",
            "for", "of", "with", "without",
            "into", "over", "under", "up", "down"
    );

    /** Специальные замены (поиск без учета регистра, но фиксированная форма вывода) */
    private static final Map<String, String> SPECIAL_WORDS = Map.of(
            "mcgee", "McGee",
            "half-step", "Half-Step"
    );

    /**
     * Разбирает строки вида "d1t03 - Shakedown Street"
     * и возвращает мап: ключ = d1t03, значение = нормализованное название
     */
    public Map<String, String> parseTrackTitles(List<String> lines) {
        Map<String, String> map = new HashMap<>();

        for (String line : lines) {
            Matcher matcher = TRACK_PATTERN.matcher(line.trim());
            if (!matcher.matches()) {
                continue;
            }

            int disc = Integer.parseInt(matcher.group(1));
            int track = Integer.parseInt(matcher.group(2));
            String rawTitle = matcher.group(3);

            String normalized = normalizeTitle(rawTitle);
            String key = String.format("d%dt%02d", disc, track);

            map.put(key, normalized);
        }

        return map;
    }

    /**
     * Нормализует название:
     *  - удаляет "->"
     *  - применяет специальные замены
     *  - приводит к корректному кейсу с исключениями
     *  - удаляет недопустимые символы файловой системы
     */
    public String normalizeTitle(String title) {
        // Удаление стрелок и пробелов по краям
        title = title.replace("->", "").trim();

        // Применение специальных замен (без учета регистра)
        for (var entry : SPECIAL_WORDS.entrySet()) {
            String regex = "(?i)" + entry.getKey();
            title = title.replaceAll(regex, entry.getValue());
        }

        String[] words = title.split("\\s+");
        if (words.length == 0) {
            return title;
        }

        List<String> processed = new ArrayList<>();
        processed.add(capitalizeWord(words[0])); // Первое слово всегда с заглавной

        for (int i = 1; i < words.length; i++) {
            String lower = words[i].toLowerCase();

            if (SPECIAL_WORDS.containsKey(lower)) {
                processed.add(SPECIAL_WORDS.get(lower));
            } else if (LOWER_CASE_WORDS.contains(lower) && i != words.length - 1) {
                processed.add(lower);
            } else {
                processed.add(capitalizeWord(words[i]));
            }
        }

        String result = String.join(" ", processed);

        // Удаление недопустимых символов файловой системы
        return result.replaceAll("[\\\\/:*?\"<>|]", "").trim();
    }

    /**
     * Приводит слово (возможно, с дефисом) к корректному виду: "half-step" → "Half-Step".
     */
    private String capitalizeWord(String word) {
        if (word.contains("-")) {
            String[] parts = word.split("-");
            for (int i = 0; i < parts.length; i++) {
                parts[i] = capitalizeSingle(parts[i]);
            }
            return String.join("-", parts);
        }
        return capitalizeSingle(word);
    }

    /**
     * Делает первую букву заглавной, остальные строчными: "word" → "Word".
     */
    private String capitalizeSingle(String w) {
        if (w.isEmpty()) {
            return w;
        }
        return w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase();
    }
}
