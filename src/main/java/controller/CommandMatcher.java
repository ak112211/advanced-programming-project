package controller;

import model.App;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandMatcher {
    private static final HashMap<Class<?>, Function<String, Object>> toStrings = new HashMap<>() {{
        put(int.class, Integer::parseInt);
        put(Integer.class, Integer::parseInt);
        put(long.class, Long::parseLong);
        put(Long.class, Long::parseLong);
        put(double.class, Double::parseDouble);
        put(Double.class, Double::parseDouble);
        put(float.class, Float::parseFloat);
        put(Float.class, Float::parseFloat);
        put(String.class, a -> a);
        put(boolean.class, Objects::nonNull);
    }};

    public static String getPattern(String command) {
        Matcher matcher1 = Pattern.compile(" -([a-z0-9]++) <([^>]++)>").matcher(command);
        StringBuilder sb1 = new StringBuilder();
        while (matcher1.find()) {
            matcher1.appendReplacement(sb1, "(?=.+-" + matcher1.group(1) + "\\\\s++" +
                    "(?<" + matcher1.group(2) + ">\\\\S++))");
        }
        matcher1.appendTail(sb1);

        Matcher matcher2 = Pattern.compile(" --(\\S++)").matcher(sb1.toString());
        StringBuilder sb2 = new StringBuilder();
        while (matcher2.find()) {
            matcher2.appendReplacement(sb2, "(?:.+--(?<" + matcher2.group(1) + ">" + matcher2.group(1) + "))?");
        }
        matcher2.appendTail(sb2);
        return sb2.toString();
    }

    public static String run(Method method, String command, String input) {
        Pattern pattern = Pattern.compile(getPattern(command));
        Matcher matcher = pattern.matcher(input);
        if (input.endsWith("-regex")) {
            return getPattern(command);
        }
        if (matcher.lookingAt()) {
            Object[] parObjs = new Object[method.getParameterCount()];
            int i = 0;
            for (Parameter parameter : method.getParameters()) {
            }
            for (Parameter parameter : method.getParameters()) {
                Function<String, Object> toString = toStrings.get(parameter.getType());
                String parStr = matcher.group(parameter.getName());
                Object parObj;
                try {
                    parObj = toString.apply(parStr);
                } catch (Exception e) {
                    return "The flag " + parameter.getName() + " is not " + parameter.getType().getName() + ".";

                }
                parObjs[i++]=parObj;
            }
            try {
                return (String) method.invoke(App.getController(), parObjs);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "flags are not correct.\n" + "This is how you should write them:\n" + command;
        }
    }
}