import model.App;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (App.getMenu() != null) {
            App.getMenu().check(scanner);
        }
    }
}
