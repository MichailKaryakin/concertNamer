package namer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleHandler {

    private static final Set<String> LOWERCASE_WORDS = Set.of(
            "a", "an", "the",
            "and", "or", "but",
            "in", "on", "at", "to", "from", "for",
            "of", "with", "without", "into", "over",
            "under", "up", "down"
    );

    private static final Map<String, String> SPECIAL_CAPS = Map.of(
            "mcgee", "McGee",
            "half-step", "Half-Step"
    );

    public String normalizeTitle(String title) {
        title = title.replace("->", "").trim();

        String lower = title.toLowerCase();
        for (var e : SPECIAL_CAPS.entrySet()) {
            if (lower.contains(e.getKey())) {
                title = title.replaceAll("(?i)" + Pattern.quote(e.getKey()), e.getValue());
            }
        }

        String[] words = title.split("\\s+");
        if (words.length == 0) return title;

        List<String> out = new ArrayList<>();

        out.add(capitalizeComplex(words[0]));

        for (int i = 1; i < words.length; i++) {
            String w = words[i];

            String wLower = w.toLowerCase();
            if (SPECIAL_CAPS.containsKey(wLower)) {
                out.add(SPECIAL_CAPS.get(wLower));
                continue;
            }

            if (w.contains("-")) {
                out.add(capitalizeHyphenated(w));
                continue;
            }

            if (i < words.length - 1 && LOWERCASE_WORDS.contains(wLower)) {
                out.add(wLower);
            } else {
                out.add(capitalize(w));
            }
        }

        String joined = String.join(" ", out);

        joined = joined.replaceAll("[\\\\/:*?\"<>|]", "");

        return joined.trim();
    }

    public String capitalize(String w) {
        if (w.isEmpty()) return w;

        if (w.matches("(?i)^mc[a-z].*")) {
            return "Mc" + w.substring(2, 3).toUpperCase() + w.substring(3).toLowerCase();
        }

        return w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase();
    }

    private String capitalizeHyphenated(String w) {
        String[] parts = w.split("-");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = capitalize(parts[i]);
        }
        return String.join("-", parts);
    }

    private String capitalizeComplex(String w) {
        if (w.contains("-")) return capitalizeHyphenated(w);
        return capitalize(w);
    }

    public Map<String, String> parseTrackTitles(List<String> lines) {
        Pattern p = Pattern.compile("d(\\d+)t(\\d+)\\s*-\\s*(.+)");
        Map<String, String> map = new HashMap<>();

        for (String line : lines) {
            Matcher m = p.matcher(line.trim());
            if (m.find()) {
                int disc = Integer.parseInt(m.group(1));
                int track = Integer.parseInt(m.group(2));
                String raw = m.group(3);
                String norm = normalizeTitle(raw);

                String key = String.format("d%dt%02d", disc, track);
                map.put(key, norm);
            }
        }
        return map;
    }
}
