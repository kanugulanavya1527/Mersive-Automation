package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Test Configuration & Utilities for JoinWithIdIterationTest
 *
 * This class provides:
 * - Test data management
 * - Performance analysis utilities
 * - CSV/JSON report generation
 * - Timing analysis and statistics
 */
public class JoinWithIdTestConfig {

    // ──────────────────────────────────────────────────────────
    // TEST DATA CONFIGURATIONS
    // ──────────────────────────────────────────────────────────

    /**
     * Test data set for different scenarios
     */
    public static class TestDataSet {
        public String name;
        public String meetingId;
        public String passcode;
        public String platform;
        public long expectedDurationMs;

        public TestDataSet(String name, String meetingId, String passcode, String platform, long expectedDurationMs) {
            this.name = name;
            this.meetingId = meetingId;
            this.passcode = passcode;
            this.platform = platform;
            this.expectedDurationMs = expectedDurationMs;
        }
    }

    /**
     * Predefined test data sets
     */
    public static class TestData {
        // Teams Meeting
        public static final TestDataSet TEAMS_MEETING = new TestDataSet(
                "Teams Meeting",
                "249 434 512 849 474",
                "nk7vi9ap",
                "TEAMS",
                45000  // Expected: 45 seconds
        );

        // Zoom Meeting
        public static final TestDataSet ZOOM_MEETING = new TestDataSet(
                "Zoom Meeting",
                "249 434 512 849 474",
                "nk7vi9ap",
                "ZOOM",
                50000  // Expected: 50 seconds
        );

        // Large Meeting ID (9+ digits)
        public static final TestDataSet LARGE_MEETING_ID = new TestDataSet(
                "Large Meeting ID",
                "123 456 789 012 345",
                "largetest",
                "ZOOM",
                48000
        );

        // Simple Meeting ID
        public static final TestDataSet SIMPLE_MEETING_ID = new TestDataSet(
                "Simple Meeting ID",
                "123456789",
                "simple",
                "TEAMS",
                42000
        );
    }

    // ──────────────────────────────────────────────────────────
    // PERFORMANCE ANALYSIS
    // ──────────────────────────────────────────────────────────

    /**
     * Analyzes performance metrics from iteration results
     */
    public static class PerformanceAnalyzer {

        public static class PerformanceMetrics {
            public long minDuration;
            public long maxDuration;
            public double averageDuration;
            public double standardDeviation;
            public int successCount;
            public int failureCount;
            public double successRate;
            public long totalDuration;
            public List<Long> durations;

            @Override
            public String toString() {
                return String.format(
                        "Performance Metrics:\n" +
                                "  Min Duration: %d ms\n" +
                                "  Max Duration: %d ms\n" +
                                "  Average Duration: %.2f ms\n" +
                                "  Std Deviation: %.2f ms\n" +
                                "  Success Rate: %.2f%%\n" +
                                "  Total Duration: %d ms",
                        minDuration, maxDuration, averageDuration, standardDeviation, successRate * 100, totalDuration
                );
            }
        }

        /**
         * Calculate performance metrics from durations
         */
        public static PerformanceMetrics analyze(List<Long> successfulDurations, int totalIterations) {
            PerformanceMetrics metrics = new PerformanceMetrics();
            metrics.durations = successfulDurations;

            if (successfulDurations.isEmpty()) {
                return metrics;
            }

            // Min/Max
            metrics.minDuration = Collections.min(successfulDurations);
            metrics.maxDuration = Collections.max(successfulDurations);

            // Average
            long sum = successfulDurations.stream().mapToLong(Long::longValue).sum();
            metrics.averageDuration = (double) sum / successfulDurations.size();
            metrics.totalDuration = sum;

            // Standard Deviation
            double variance = successfulDurations.stream()
                    .mapToDouble(d -> Math.pow(d - metrics.averageDuration, 2))
                    .average()
                    .orElse(0.0);
            metrics.standardDeviation = Math.sqrt(variance);

            // Success Rate
            metrics.successCount = successfulDurations.size();
            metrics.failureCount = totalIterations - successfulDurations.size();
            metrics.successRate = (double) successfulDurations.size() / totalIterations;

            return metrics;
        }

        /**
         * Check if performance is within acceptable range
         */
        public static boolean isPerformanceAcceptable(PerformanceMetrics metrics,
                                                      long maxAcceptableDuration,
                                                      double minAcceptableSuccessRate) {
            return metrics.averageDuration <= maxAcceptableDuration
                    && metrics.successRate >= minAcceptableSuccessRate;
        }
    }

    // ──────────────────────────────────────────────────────────
    // REPORT GENERATION
    // ──────────────────────────────────────────────────────────

    /**
     * Generate test report in CSV format
     */
    public static class ReportGenerator {

        public static void generateCSVReport(List<IterationResult> results, String filePath) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Header
                writer.write("Iteration,Start Time,End Time,Duration (ms),Status,Timestamp,Error Message\n");

                // Data
                for (IterationResult result : results) {
                    writer.write(String.format(
                            "%d,%d,%d,%d,%s,%s,%s\n",
                            result.iteration,
                            result.startTime,
                            result.endTime,
                            result.totalDuration,
                            result.success ? "PASSED" : "FAILED",
                            result.timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                            result.errorMessage != null ? result.errorMessage : ""
                    ));
                }

                System.out.println("✓ CSV Report generated: " + filePath);
            }
        }

        /**
         * Generate test report in JSON format
         */
        public static void generateJSONReport(List<IterationResult> results, String filePath) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("{\n");
                writer.write("  \"testName\": \"JoinWithIdIterationTest\",\n");
                writer.write("  \"timestamp\": \"" + LocalDateTime.now() + "\",\n");
                writer.write("  \"totalIterations\": " + results.size() + ",\n");
                writer.write("  \"results\": [\n");

                for (int i = 0; i < results.size(); i++) {
                    IterationResult result = results.get(i);
                    writer.write("    {\n");
                    writer.write("      \"iteration\": " + result.iteration + ",\n");
                    writer.write("      \"startTime\": " + result.startTime + ",\n");
                    writer.write("      \"endTime\": " + result.endTime + ",\n");
                    writer.write("      \"duration\": " + result.totalDuration + ",\n");
                    writer.write("      \"success\": " + result.success + ",\n");
                    writer.write("      \"timestamp\": \"" + result.timestamp + "\",\n");
                    writer.write("      \"errorMessage\": \"" + (result.errorMessage != null ? result.errorMessage : "") + "\"\n");
                    writer.write("    }");
                    if (i < results.size() - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }

                writer.write("  ]\n");
                writer.write("}\n");

                System.out.println("✓ JSON Report generated: " + filePath);
            }
        }

        /**
         * Generate HTML report
         */
        public static void generateHTMLReport(List<IterationResult> results, String filePath) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("<!DOCTYPE html>\n");
                writer.write("<html>\n");
                writer.write("<head>\n");
                writer.write("  <title>Join with ID Test Report</title>\n");
                writer.write("  <style>\n");
                writer.write("    body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
                writer.write("    .container { background-color: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }\n");
                writer.write("    h1 { color: #333; border-bottom: 3px solid #2196F3; padding-bottom: 10px; }\n");
                writer.write("    table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
                writer.write("    th { background-color: #2196F3; color: white; padding: 12px; text-align: left; }\n");
                writer.write("    td { padding: 10px; border-bottom: 1px solid #ddd; }\n");
                writer.write("    tr:hover { background-color: #f5f5f5; }\n");
                writer.write("    .passed { background-color: #d4edda; color: #155724; }\n");
                writer.write("    .failed { background-color: #f8d7da; color: #721c24; }\n");
                writer.write("    .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin: 20px 0; }\n");
                writer.write("    .stat-box { background-color: #f0f0f0; padding: 15px; border-radius: 5px; }\n");
                writer.write("    .stat-value { font-size: 24px; font-weight: bold; color: #2196F3; }\n");
                writer.write("    .stat-label { font-size: 12px; color: #666; margin-top: 5px; }\n");
                writer.write("  </style>\n");
                writer.write("</head>\n");
                writer.write("<body>\n");
                writer.write("  <div class=\"container\">\n");
                writer.write("    <h1>Join with ID Iteration Test Report</h1>\n");
                writer.write("    <p>Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>\n");

                // Stats
                int successCount = (int) results.stream().filter(r -> r.success).count();
                double avgDuration = results.stream()
                        .filter(r -> r.success)
                        .mapToLong(r -> r.totalDuration)
                        .average()
                        .orElse(0);

                writer.write("    <div class=\"stats\">\n");
                writer.write("      <div class=\"stat-box\">\n");
                writer.write("        <div class=\"stat-value\">" + results.size() + "</div>\n");
                writer.write("        <div class=\"stat-label\">Total Iterations</div>\n");
                writer.write("      </div>\n");
                writer.write("      <div class=\"stat-box\">\n");
                writer.write("        <div class=\"stat-value\">" + successCount + "</div>\n");
                writer.write("        <div class=\"stat-label\">Successful</div>\n");
                writer.write("      </div>\n");
                writer.write("      <div class=\"stat-box\">\n");
                writer.write("        <div class=\"stat-value\">" + String.format("%.0f", avgDuration) + " ms</div>\n");
                writer.write("        <div class=\"stat-label\">Avg Duration</div>\n");
                writer.write("      </div>\n");
                writer.write("    </div>\n");

                // Table
                writer.write("    <table>\n");
                writer.write("      <tr><th>Iteration</th><th>Duration (ms)</th><th>Status</th><th>Timestamp</th><th>Error</th></tr>\n");

                for (IterationResult result : results) {
                    String statusClass = result.success ? "passed" : "failed";
                    String statusText = result.success ? "PASSED" : "FAILED";
                    writer.write("      <tr class=\"" + statusClass + "\">\n");
                    writer.write("        <td>" + result.iteration + "</td>\n");
                    writer.write("        <td>" + result.totalDuration + "</td>\n");
                    writer.write("        <td>" + statusText + "</td>\n");
                    writer.write("        <td>" + result.timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "</td>\n");
                    writer.write("        <td>" + (result.errorMessage != null ? result.errorMessage : "-") + "</td>\n");
                    writer.write("      </tr>\n");
                }

                writer.write("    </table>\n");
                writer.write("  </div>\n");
                writer.write("</body>\n");
                writer.write("</html>\n");

                System.out.println("✓ HTML Report generated: " + filePath);
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // WAIT TIME CONFIGURATIONS
    // ──────────────────────────────────────────────────────────

    /**
     * Configurable wait times for different scenarios
     */
    public static class WaitConfig {
        public static final int WAIT_HOME_SCREEN = 2000;           // ms
        public static final int WAIT_AFTER_CLICK_JOIN = 2000;       // ms
        public static final int WAIT_PLATFORM_SELECT = 2000;        // ms
        public static final int WAIT_AFTER_PLATFORM = 2000;         // ms
        public static final int WAIT_PRE_JOIN_SCREEN = 1000;        // ms
        public static final int WAIT_MEETING_OVERLAY = 3000;        // ms
        public static final int WAIT_MEETING_FULLY_LOAD = 5000;     // ms
        public static final int WAIT_UI_VERIFICATION = 2000;        // ms
    }

    // ──────────────────────────────────────────────────────────
    // HELPER CLASSES
    // ──────────────────────────────────────────────────────────

    /**
     * Result class for storing iteration data
     */
    public static class IterationResult {
        public int iteration;
        public long startTime;
        public long endTime;
        public long totalDuration;
        public boolean success;
        public String errorMessage;
        public LocalDateTime timestamp;

        public IterationResult(int iteration) {
            this.iteration = iteration;
            this.timestamp = LocalDateTime.now();
        }
    }

    /**
     * Enum for platform selection
     */
    public enum Platform {
        TEAMS("Microsoft Teams"),
        ZOOM("Zoom"),
        UNDEFINED("Undefined");

        public final String displayName;

        Platform(String displayName) {
            this.displayName = displayName;
        }
    }
}