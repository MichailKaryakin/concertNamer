package namer;

import java.util.Map;

public record ConcertInfo(
        String date,
        String location,
        Map<String, String> tracks
) {
}
