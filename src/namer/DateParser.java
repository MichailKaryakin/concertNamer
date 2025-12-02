package namer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {

    public String extractDate(List<String> lines) {
        Pattern p = Pattern.compile("(\\w+ \\d{1,2}, \\d{4})");

        return lines.stream()
                .map(p::matcher)
                .filter(Matcher::find)
                .map(m -> m.group(1))
                .findFirst()
                .map(this::convertDateToStandard)
                .orElseThrow(() -> new RuntimeException("Date not found"));
    }

    public String convertDateToStandard(String date) {
        try {
            SimpleDateFormat in = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");
            return out.format(in.parse(date));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
