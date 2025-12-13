package main;

import collector.RunCollector;
import namer.*;
import normalizer.RunNormalizer;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private boolean cycleVar = true;

    public void runMenu() throws Exception {
        while (cycleVar) {
            System.out.println("1 - Namer, 2 - Collector, 3 - Normalizer, 4 - Exit");
            int choice = scanner.nextInt();

            if (choice == 1) {
                RunNamer runNamer = new RunNamer();
                runNamer.run(System.getenv("torrentDirectory"));
            } else if (choice == 2) {
                RunCollector runCollector = new RunCollector();
                runCollector.run();
            }  else if (choice == 3) {
                RunNormalizer runNormalizer = new RunNormalizer();
                runNormalizer.run();
            } else if (choice == 4) {
                cycleVar = false;
            } else {
                System.out.println("No such choice, try again =)");
            }
        }
    }
}
