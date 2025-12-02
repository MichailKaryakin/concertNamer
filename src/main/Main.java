package main;

import namer.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String folderName = args[0];
        Menu menu = new Menu();
        menu.runMenu(folderName);
    }
}
