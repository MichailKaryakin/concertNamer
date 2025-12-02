package normalizer;

import java.io.IOException;

public class RunNormalizer {

    String concertDirectory = System.getenv("concertDirectory");
    String loggingFile = System.getenv("loggingFile");

    public void run() throws IOException {

        SongNameCollector collector = new SongNameCollector(concertDirectory, loggingFile);
        collector.run();

        SongNormalizer normalizer = new SongNormalizer();
        normalizer.loadNormalizedNames(loggingFile);
        normalizer.normalizeDirectory(concertDirectory);
    }
}
