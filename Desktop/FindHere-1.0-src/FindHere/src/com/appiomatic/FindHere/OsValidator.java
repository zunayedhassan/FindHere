package com.appiomatic.FindHere;

/**
 *
 * @author Zunayed Hassan
 */
public class OsValidator {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean IS_WINDOWS() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean IS_MAC() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean IS_UNIX() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static boolean IS_SOLARIS() {
        return (OS.indexOf("sunos") >= 0);
    }
}