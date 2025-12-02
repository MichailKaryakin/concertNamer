package main;

import namer.*;
import normalizer.RunNormalizer;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private boolean cycleVar = true;

    public void runMenu(String folderName) throws Exception {
        while (cycleVar) {
            System.out.println("1 - Именование, 2 - Нормализация, 3 - Завершить работу");
            int choice = scanner.nextInt();
            if (choice == 1) {
                DateParser dateParser = new DateParser();
                FileProcessor fileProcessor = new FileProcessor();
                PlaceFormatter placeFormatter = new PlaceFormatter();
                TitleHandler titleHandler = new TitleHandler();

                RunNamer runNamer = new RunNamer(dateParser, fileProcessor, placeFormatter, titleHandler);
                runNamer.run("D:\\TorrentShelf\\temporary\\GD\\" + folderName);
            } else if (choice == 2) {
                RunNormalizer runNormalizer = new RunNormalizer();
                runNormalizer.run();
            } else if (choice == 3) {
                cycleVar = false;
            } else {
                System.out.println("Такой опции нет, попробуйте снова =)");
            }
        }
    }
}
