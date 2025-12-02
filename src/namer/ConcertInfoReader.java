package namer;

import java.nio.file.*;
import java.util.List;

public record ConcertInfoReader(DateParser dateParser, PlaceFormatter placeFormatter, TitleHandler titleHandler) {

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
