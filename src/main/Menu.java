package main;

import namer.*;
import normalizer.RunNormalizer;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private boolean cycleVar = true;

    public void runMenu() throws Exception {
        while (cycleVar) {
            System.out.println("1 - Namer, 2 - Normalizer, 3 - Exit");
            int choice = scanner.nextInt();

            if (choice == 1) {
                RunNamer runNamer = new RunNamer(createConcertFolderProcessor());
                runNamer.run(System.getenv("torrentDirectory"));
            } else if (choice == 2) {
                RunNormalizer runNormalizer = new RunNormalizer();
                runNormalizer.run();
            } else if (choice == 3) {
                cycleVar = false;
            } else {
                System.out.println("No such choice, try again =)");
            }
        }
    }

    private ConcertFolderProcessor createConcertFolderProcessor() {
        return new ConcertFolderProcessor(
                new ConcertInfoReader(new DateParser(), new PlaceFormatter(), new TitleHandler()),
                new FileRenamer()
        );
    }
}
