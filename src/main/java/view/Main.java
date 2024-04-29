package view;

import model.App;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            App.getMenu().checkCommand(scanner);
        }
    }
}
