package collector;

public class RunCollector {

    String concertDirectory = System.getenv("concertDirectory");
    String loggingFile = System.getenv("loggingFile");

    public void run() {
        SongNameCollector collector = new SongNameCollector(concertDirectory, loggingFile);
        collector.run();
    }
}
