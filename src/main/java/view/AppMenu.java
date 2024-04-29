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

        for (Method method : App.getController().getClass().getDeclaredMethods()) {
            Command command = method.getAnnotation(Command.class);
            System.out.println("method : " + method.getName());
            if (command != null) {
                if (input.replaceAll(" -.++", "").equals(
                        command.command().replaceAll(" -.++", ""))) {
                    System.out.println(CommandMatcher.run(method, command.command(), input));
                    return;
                }
            }
        }
        System.out.println("invalid command");

    }
}
