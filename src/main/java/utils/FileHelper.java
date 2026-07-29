package utils;

import java.io.IOException;
import java.nio.file.*;

public class FileHelper {

    // Delete all files inside Logs folder
    public static void clearLogs() {
        Path logsFolder = Paths.get("C:\\Users\\Admin\\AppData\\Local\\MersiveRoom\\Logs");

        if (Files.exists(logsFolder) && Files.isDirectory(logsFolder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder)) {
                for (Path file : stream) {
                    Files.deleteIfExists(file);
                    System.out.println("Deleted log: " + file.getFileName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete configuration.json
    public static void deleteConfigurationFile() {
        deleteFile("C:\\Users\\Admin\\AppData\\Local\\MersiveRoom\\configuration.json");
    }

    // Delete provisioning-state.json
    public static void deleteProvisioningStateFile() {
        deleteFile("C:\\Users\\Admin\\AppData\\Local\\MersiveRoom\\provisioning-state.json");
    }

    // Common delete method
    private static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.deleteIfExists(path)) {
                System.out.println("Deleted: " + path.getFileName());
            } else {
                System.out.println("File not found: " + path.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}