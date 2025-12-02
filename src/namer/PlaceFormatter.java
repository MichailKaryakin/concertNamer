package namer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlaceFormatter {

    private static final Set<String> US_STATES = Set.of(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
    );

    public String extractCityState(List<String> lines) {
        return lines.subList(1, lines.size()).stream()
                .filter(l -> l.contains(" - ") && l.contains(","))
                .map(l -> l.substring(l.indexOf(" - ") + 3).trim())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("City/State not found"));
    }

    public String formatLocation(String cityState) {
        String[] parts = cityState.split(",");
        if (parts.length < 2) return cityState.trim();

        String city = parts[0].trim();
        String region = parts[1].trim();

        if (region.equals("NY")) {
            return "New York City";
        } else if (US_STATES.contains(region)) {
            return city + ", " + normalizedState(region);
        } else {
            return city + ", " + region;
        }
    }

    private static final Map<String, String> STATE_MAP = Map.ofEntries(
            Map.entry("AL", "Alabama"),
            Map.entry("AK", "Alaska"),
            Map.entry("AZ", "Arizona"),
            Map.entry("AR", "Arkansas"),
            Map.entry("CA", "California"),
            Map.entry("CO", "Colorado"),
            Map.entry("CT", "Connecticut"),
            Map.entry("DE", "Delaware"),
            Map.entry("FL", "Florida"),
            Map.entry("GA", "Georgia"),
            Map.entry("HI", "Hawaii"),
            Map.entry("ID", "Idaho"),
            Map.entry("IL", "Illinois"),
            Map.entry("IN", "Indiana"),
            Map.entry("IA", "Iowa"),
            Map.entry("KS", "Kansas"),
            Map.entry("KY", "Kentucky"),
            Map.entry("LA", "Louisiana"),
            Map.entry("ME", "Maine"),
            Map.entry("MD", "Maryland"),
            Map.entry("MA", "Massachusetts"),
            Map.entry("MI", "Michigan"),
            Map.entry("MN", "Minnesota"),
            Map.entry("MS", "Mississippi"),
            Map.entry("MO", "Missouri"),
            Map.entry("MT", "Montana"),
            Map.entry("NE", "Nebraska"),
            Map.entry("NV", "Nevada"),
            Map.entry("NH", "New Hampshire"),
            Map.entry("NJ", "New Jersey"),
            Map.entry("NM", "New Mexico"),
            Map.entry("NC", "North Carolina"),
            Map.entry("ND", "North Dakota"),
            Map.entry("OH", "Ohio"),
            Map.entry("OK", "Oklahoma"),
            Map.entry("OR", "Oregon"),
            Map.entry("PA", "Pennsylvania"),
            Map.entry("RI", "Rhode Island"),
            Map.entry("SC", "South Carolina"),
            Map.entry("SD", "South Dakota"),
            Map.entry("TN", "Tennessee"),
            Map.entry("TX", "Texas"),
            Map.entry("UT", "Utah"),
            Map.entry("VT", "Vermont"),
            Map.entry("VA", "Virginia"),
            Map.entry("WA", "Washington"),
            Map.entry("WV", "West Virginia"),
            Map.entry("WI", "Wisconsin"),
            Map.entry("WY", "Wyoming")
    );


    public String normalizedState(String state) {
        if (state == null) return null;
        return STATE_MAP.get(state.toUpperCase());
    }
}
