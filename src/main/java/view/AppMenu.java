package view;

import enums.Menu;
import model.App;
import model.Command;
import controller.CommandMatcher;

import java.lang.reflect.Method;
import java.util.Scanner;

public abstract class AppMenu {
    public void check(Scanner scanner) {

        String input = scanner.nextLine();
        for (Method method : App.getController().getClass().getMethods()) {
            Command command = method.getAnnotation(Command.class);
            if (command != null) {
                if (input.replaceAll("-.++", "").trim().equals(
                        command.command().replaceAll("-.++", "").trim())) {
                    System.out.println(CommandMatcher.run(method, command.command(), input));
                    return;
                }
            }
        }
        System.out.println("invalid command");

    }
}
