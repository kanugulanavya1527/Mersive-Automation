package utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

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

            if (User32.INSTANCE.IsWindowVisible(hwnd) && !title.isBlank()) {

                long handle = Pointer.nativeValue(hwnd.getPointer());

                System.out.println(
                        "HWND=" + handle +
                                " HEX=0x" + Long.toHexString(handle).toUpperCase() +
                                " TITLE=" + title
                );
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

            List<String> found = new ArrayList<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {

                char[] buffer = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buffer, 512);

                String title = Native.toString(buffer);

                if (User32.INSTANCE.IsWindowVisible(hwnd)
                        && title != null
                        && title.toLowerCase().contains("mersive room blocker")) {

                    long val = Pointer.nativeValue(hwnd.getPointer());
                    found.add(String.valueOf(val));
                }

                return true;

            }, null);

            if (!found.isEmpty()) {

                System.out.println(
                        "[WindowHelper] Blocker window found at "
                                + (i + 1) + " sec"
                );

                return found.get(0);
            }

            if (i % 5 == 0) {
                System.out.println(
                        "[WindowHelper] Waiting for Blocker... "
                                + i + " sec"
                );
            }

            Thread.sleep(1000);
        }

        System.out.println("[WindowHelper] Blocker window not found.");
        printAllWindows();

        return null;
    }
    public static boolean isWindowPresent(String titleContains)
            throws InterruptedException {

        return findWindowHandle(titleContains) != null;
    }
    public static boolean isWindowPresentNow(String titleContains) {

        List<String> found = new ArrayList<>();

        User32.INSTANCE.EnumWindows((hwnd, data) -> {

            char[] buffer = new char[512];
            User32.INSTANCE.GetWindowText(hwnd, buffer, 512);

            String title = Native.toString(buffer);

            if (User32.INSTANCE.IsWindowVisible(hwnd)
                    && title != null
                    && title.toLowerCase().contains(titleContains.toLowerCase())) {

                found.add(title);
            }

            return true;

        }, null);

        return !found.isEmpty();
    }

    public static String findChromeRenderChildHandle(String parentTitleContains,
                                                     int maxRetries)
            throws InterruptedException {

        for (int attempt = 0; attempt < maxRetries; attempt++) {

            List<Long> parentHandles = new ArrayList<>();

            User32.INSTANCE.EnumWindows((hwnd, data) -> {
                char[] buffer = new char[512];
                User32.INSTANCE.GetWindowText(hwnd, buffer, 512);
                String title = Native.toString(buffer);

                if (User32.INSTANCE.IsWindowVisible(hwnd)
                        && title != null
                        && title.toLowerCase().contains(parentTitleContains.toLowerCase())) {
                    parentHandles.add(Pointer.nativeValue(hwnd.getPointer()));
                }
                return true;
            }, null);

            for (Long parentVal : parentHandles) {

                WinDef.HWND parentHwnd = new WinDef.HWND(new Pointer(parentVal));
                List<String> childMatches = new ArrayList<>();

                User32.INSTANCE.EnumChildWindows(parentHwnd, (childHwnd, data) -> {

                    char[] classBuffer = new char[256];
                    User32.INSTANCE.GetClassName(childHwnd, classBuffer, 256);
                    String className = Native.toString(classBuffer);

                    if ("Chrome_RenderWidgetHostHWND".equals(className)) {
                        long childVal = Pointer.nativeValue(childHwnd.getPointer());
                        childMatches.add(String.valueOf(childVal));
                    }
                    return true;

                }, null);

                if (!childMatches.isEmpty()) {
                    System.out.println(
                            "[WindowHelper] Found Chrome_RenderWidgetHostHWND child under '"
                                    + parentTitleContains + "' at attempt " + (attempt + 1)
                                    + " -> " + childMatches.get(0));
                    return childMatches.get(0);
                }
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        System.out.println("[WindowHelper] NOT FOUND: Chrome_RenderWidgetHostHWND child under "
                + parentTitleContains);
        printAllWindows();
        return null;
    }


}