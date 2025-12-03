package namer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, предназначенный для получения корректной даты.
 * Изымает дату в одном из двух прописанных форматов
 * и преобразует к корректному, желаемому виду.
 */
public class DateParser {

    // Формат 1: August 8, 1971
    private static final Pattern LONG_DATE_PATTERN =
            Pattern.compile("(\\w+ \\d{1,2}, \\d{4})");

    // Формат 2: 4/2/73
    private static final Pattern SHORT_DATE_PATTERN =
            Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{2})");

    private static final SimpleDateFormat INPUT_LONG =
            new SimpleDateFormat("MMMM d, yyyy", Locale.US);

    private static final SimpleDateFormat INPUT_SHORT =
            new SimpleDateFormat("M/d/yy", Locale.US);

    private static final SimpleDateFormat OUTPUT =
            new SimpleDateFormat("yyyy-MM-dd");

    public String extractDate(List<String> lines) {
        // Сначала ищется длинный формат
        for (String line : lines) {
            Matcher m = LONG_DATE_PATTERN.matcher(line);
            if (m.find()) {
                return formatLong(m.group(1));
            }
        }

        // Если не нашелся — ищется короткий
        for (String line : lines) {
            Matcher m = SHORT_DATE_PATTERN.matcher(line);
            if (m.find()) {
                return formatShort(m.group(1));
            }
        }

        throw new IllegalArgumentException("Date not found in info.txt");
    }

    private String formatLong(String date) {
        try {
            return OUTPUT.format(INPUT_LONG.parse(date));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid long date format: " + date, e);
        }
    }

    private String formatShort(String date) {
        try {
            return OUTPUT.format(INPUT_SHORT.parse(date));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid short date format: " + date, e);
        }
    }
}
