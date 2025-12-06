package namer;

import java.nio.file.*;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class ConcertInfoReader {
    private final DateParser dateParser;
    private final PlaceFormatter placeFormatter;
    private final TitleHandler titleHandler;

    public ConcertInfoReader(DateParser dateParser, PlaceFormatter placeFormatter, TitleHandler titleHandler) {
        this.dateParser = dateParser;
        this.placeFormatter = placeFormatter;
        this.titleHandler = titleHandler;
    }

    public ConcertInfo read(Path folder) throws Exception {
        Path info = folder.resolve("info.txt");
        if (!Files.exists(info))
            throw new IllegalArgumentException("info.txt not found in " + folder);

        List<String> lines = Files.readAllLines(info);

        return new ConcertInfo(
                dateParser.extractDate(lines),
                placeFormatter.getFormattedLocation(lines),
                titleHandler.parseTrackTitles(lines)
        );
    }
}
