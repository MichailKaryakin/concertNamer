package namer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {

    private static final Pattern DATE_PATTERN =
            Pattern.compile("(\\w+ \\d{1,2}, \\d{4})");

    private static final SimpleDateFormat INPUT =
            new SimpleDateFormat("MMMM d, yyyy", Locale.US);
    private static final SimpleDateFormat OUTPUT =
            new SimpleDateFormat("yyyy-MM-dd");

    public String extractDate(List<String> lines) {
        return lines.stream()
                .map(DATE_PATTERN::matcher)
                .filter(Matcher::find)
                .map(m -> m.group(1))
                .findFirst()
                .map(this::format)
                .orElseThrow(() -> new IllegalArgumentException("Date not found in info.txt"));
    }

    private String format(String date) {
        try {
            return OUTPUT.format(INPUT.parse(date));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date, e);
        }
    }
}
