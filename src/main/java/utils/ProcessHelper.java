package utils;

import java.util.concurrent.TimeUnit;

public class ProcessHelper {

    public static Process launch(String exePath) throws Exception {
        System.out.println("[ProcessHelper] Launching: " + exePath);
        Process p = new ProcessBuilder(exePath).start();
        if (p == null || !p.isAlive()) {
            throw new RuntimeException("Failed to launch: " + exePath);
        }
        System.out.println("[ProcessHelper] Launched PID: " + p.pid());
        Thread.sleep(8000);
        return p;
    }
    public static boolean isRunning(String processName) {
        try {
            Process p = Runtime.getRuntime().exec(
                    "tasklist /FI \"IMAGENAME eq " + processName + "\""
            );

            String output = new String(
                    p.getInputStream().readAllBytes()
            );

            return output.contains(processName);

        } catch (Exception e) {
            return false;
        }
    }
    //    public static void kill(String processName) {
//        try {
//            Process p = Runtime.getRuntime()
//                    .exec("taskkill /F /IM " + processName + " /T");
//            p.waitFor(3, TimeUnit.SECONDS);
//            System.out.println("[ProcessHelper] Killed: " + processName);
//        } catch (Exception ignored) {
//            System.out.println("[ProcessHelper] Not found: " + processName);
//        }
//    }
    public static void kill(String processName) {
        try {
            Process p = Runtime.getRuntime()
                    .exec("taskkill /F /IM " + processName + " /T");

            p.waitFor();

            System.out.println(
                    "[ProcessHelper] Exit code for "
                            + processName
                            + " = "
                            + p.exitValue()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy(Process process) {
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
            System.out.println("[ProcessHelper] Process destroyed");
        }
    }
}