package utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;

import java.util.ArrayList;
import java.util.List;

public class WindowHelper {

    private static final int MAX_RETRIES    = 60;
    private static final int RETRY_DELAY_MS = 1000;
    public static String findWindowHandle(String titleContains,
                                          int maxRetries)
            throws InterruptedException {

        for (int attempt = 0; attempt < maxRetries; attempt++) {

            List<String> found = new ArrayList<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {

                char[] buffer = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buffer, 512);

                String title = Native.toString(buffer);

                if (User32.INSTANCE.IsWindowVisible(hwnd)
                        && title != null
                        && title.toLowerCase()
                        .contains(titleContains.toLowerCase())) {

                    long val =
                            Pointer.nativeValue(hwnd.getPointer());

                    found.add(String.valueOf(val));
                }

                return true;

            }, null);

            if (!found.isEmpty()) {

                System.out.println(
                        "[WindowHelper] Found '" +
                                titleContains +
                                "' at attempt " +
                                (attempt + 1));

                return found.get(0);
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        return null;
    }
    public static String findWindowHandle(String titleContains)
            throws InterruptedException {

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            List<String> found = new ArrayList<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {
                char[] buffer = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buffer, 512);
                String title = Native.toString(buffer);
                if (User32.INSTANCE.IsWindowVisible(hwnd)
                        && title != null
                        && title.toLowerCase()
                        .contains(titleContains.toLowerCase())) {
                    long val = Pointer.nativeValue(hwnd.getPointer());
                    found.add(String.valueOf(val));
                }
                return true;
            }, null);

            if (!found.isEmpty()) {
                System.out.println("[WindowHelper] Found '" + titleContains
                        + "' at attempt " + (attempt + 1));
                return found.get(0);
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        System.out.println("[WindowHelper] NOT FOUND: " + titleContains);
        printAllWindows();
        return null;
    }
    public static void printAllWindows() {

        System.out.println("====== WINDOWS ======");

        User32.INSTANCE.EnumWindows((hwnd, data) -> {

            char[] buffer = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, buffer, 512);

            String title = Native.toString(buffer);

            if (User32.INSTANCE.IsWindowVisible(hwnd)
                    && !title.isBlank()) {

                System.out.println("WINDOW -> " + title);
            }

            return true;

        }, null);

        System.out.println("=====================");
    }
    public static String waitForNewWindow(String titleContains,
                                          String originalHandle,
                                          int maxWaitSeconds,
                                          String... excludeTitleSuffixes)
            throws InterruptedException {

        for (int i = 0; i < maxWaitSeconds; i++) {
            List<String> found = new ArrayList<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {
                char[] buffer = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buffer, 512);
                String title = Native.toString(buffer);

                if (User32.INSTANCE.IsWindowVisible(hwnd)
                        && title != null
                        && title.toLowerCase()
                        .contains(titleContains.toLowerCase())) {
                    boolean excluded = false;
                    for (String suffix : excludeTitleSuffixes) {
                        if (title.contains(suffix)) { excluded = true; break; }
                    }
                    if (!excluded) {
                        long val = Pointer.nativeValue(hwnd.getPointer());
                        found.add(String.valueOf(val));
                    }
                }
                return true;
            }, null);

            for (String handle : found) {
                if (!handle.equals(originalHandle)) {
                    System.out.println("[WindowHelper] New window found: " + handle);
                    return handle;
                }
            }

            if (i % 5 == 0)
                System.out.println("[WindowHelper] Waiting... " + i + "s");
            Thread.sleep(1000);
        }
        System.out.println("[WindowHelper] TIMEOUT — final visible windows:");
        User32.INSTANCE.EnumWindows((hwnd, data) -> {
            char[] buf = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, buf, 512);
            String t = Native.toString(buf);
            if (User32.INSTANCE.IsWindowVisible(hwnd) && !t.isEmpty())
                System.out.println("  >> '" + t + "'");
            return true;
        }, null);

        throw new RuntimeException("[WindowHelper] New window '" + titleContains
                + "' not found after " + maxWaitSeconds + "s");
    }

    public static String toHexHandle(String decimalHandle) {
        long val = Long.parseLong(decimalHandle);
        return "0x" + Long.toHexString(val).toUpperCase();
    }

    public static String waitForBlockerWindow(int timeoutSeconds)
            throws InterruptedException {

        for (int i = 0; i < timeoutSeconds; i++) {

            String handle =
                    findWindowHandle("Mersive Room Blocker");

            if (handle != null) {
                System.out.println(
                        "[WindowHelper] Blocker window found: "
                                + handle
                );
                return handle;
            }



            if (isWindowPresent("Zoom Meeting")
                    || isWindowPresent("Zoom Workplace")) {

                System.out.println(
                        "[WindowHelper] Zoom launched outside Mersive."
                );

                printAllWindows();
                return null;
            }


            if (i % 5 == 0) {
                System.out.println(
                        "[WindowHelper] Waiting for Blocker... "
                                + i + "s"
                );
            }

            Thread.sleep(1000);
        }

        printAllWindows();
        return null;
    }


    public static boolean isWindowPresent(String titleContains)
            throws InterruptedException {

        return findWindowHandle(titleContains) != null;
    }
}