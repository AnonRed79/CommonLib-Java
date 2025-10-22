package net.anonhub.commonlib;

public class Text {
    private int lines = 0;
    private String baseStyle = "\u001B[0m";
    public void setBase(String style) {
        baseStyle = style;
    }
    public String base() {
        return baseStyle;
    }
    public String reset() {
        return "\u001B[0m";
    }
    public String bold() {
        return "\u001B[1m";
    }
    public String underline() {
        return "\u001B[4m";
    }
    public String reverseColor() {
        return "\u001B[7m";
    }
    public String color(String color) {
        return switch (color) {
            case ("black") -> "\u001B[30m";
            case ("dark") -> "\u001B[90m";
            case ("red") -> "\u001B[31m";
            case ("pink") -> "\u001B[91m";
            case ("green") -> "\u001B[32m";
            case ("mint") -> "\u001B[92m";
            case ("yellow") -> "\u001B[33m";
            case ("banana") -> "\u001B[93m";
            case ("blue") -> "\u001B[34m";
            case ("azure") -> "\u001B[94m";
            case ("purple") -> "\u001B[35m";
            case ("violet") -> "\u001B[95m";
            case ("teal") -> "\u001B[36m";
            case ("cyan") -> "\u001B[96m";
            case ("gray") -> "\u001B[37m";
            case ("white") -> "\u001B[97m";
            default -> "\u001B[0mUnkownColor";
        };
    }
    public String background(String color) {
        return switch (color) {
            case ("black") -> "\u001B[80m";
            case ("red") -> "\u001B[41m";
            case ("green") -> "\u001B[42m";
            case ("yellow") -> "\u001B[43m";
            case ("blue") -> "\u001B[44m";
            case ("purple") -> "\u001B[45m";
            case ("teal") -> "\u001B[46m";
            case ("gray") -> "\u001B[47m";
            default -> "\u001B[0m";
        };
    }
    public String preset(String set) {
        return switch (set) {
            case "obj" -> color("purple");
            case "var" -> color("pink");
            case "null" -> color("violet") + "NULL" + base();
            case "/" -> color("green") + "/" + base();
            case ":" -> color("green") + ":" + base();
            case "op" -> color("banana") + "(" + base();
            case "cp" -> color("banana") + ")" + base();
            case "ob" -> color("banana") + "[" + base();
            case "cb" -> color("banana") + "]" + base();
            case "osb" -> color("banana") + "{" + base();
            case "csb" -> color("banana") + "}" + base();
            default -> "Unknown present";
        };

    }
    public String capFirst(String str) {
        return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
    }
    public void print(Object obj) {
        System.out.print(obj);
    }
    public void println(Object obj) {
        System.out.println(obj);
    }
    public void upLine(int amount) {
        System.out.print("\033[" + amount + "A");
    }
    public void upLine() {
        System.out.print("\033[1A");
    }
    public void clearLines(int amount) {
        for (int x = 0; x < amount; x++) {
            System.out.print("\033[2K");
            System.out.print("\033[1A");
        }
    }
    public void clearLines() {
        System.out.print("\033[2K");
        System.out.print("\033[1A");
    }
    public void update(String toDisplay) {
        clearLines(lines);
        lines = toDisplay.split("\n").length+1;
        println(toDisplay);
    }
}
