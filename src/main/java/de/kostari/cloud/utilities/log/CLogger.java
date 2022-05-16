package de.kostari.cloud.utilities.log;

public class CLogger {

    public static void logError(String message, String errorType) {
        System.err.println(String.format("{Cloud: %s} %s", errorType, message));
    }

    public static void logError(String message) {
        logError(message, "Error");
    }

    public static void log(String message, String messageType) {
        System.out.println(String.format("{Cloud: %s} %s", messageType, message));
    }

    public static void log(String message) {
        System.out.println(String.format("{Cloud} %s", message));
    }

}
