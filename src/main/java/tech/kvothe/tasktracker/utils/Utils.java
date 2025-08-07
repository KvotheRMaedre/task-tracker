package tech.kvothe.tasktracker.utils;

public class Utils {
    public static void printMsg(String message){
        var outline = "+-----------------------------------------------------------------------------------------------+";
        System.out.println(outline);
        System.out.println("  " + message);
        System.out.println(outline);
    }
}
