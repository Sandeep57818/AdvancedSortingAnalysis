package com.research.sorting.benchmarking;

import com.research.sorting.algorithms.*;
import com.research.sorting.PerformanceMetrics;
import com.research.sorting.utils.DataGenerationUtilities;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TestingPipeline {

    /** Algorithms to be tested */
    private static final SortingAlgorithm[] ALGORITHMS = {new BubbleSort(), new SelectionSort(),
            new InsertionSort(), new QuickSort(), new MergeSort(), new HeapSort()};

    /** Input sizes for testing */
       private static final int[] TEST_SIZES = {100, 200, 400, 600, 800, 1_000};
    /** Data types for comprehensive testing */
    private static final String[] DATA_TYPES =
            {"Random", "Sorted", "ReverseSorted", "NearlySorted", "WithDuplicates"};

    /** Results storage */
    private final List<PerformanceMetrics> allResults = new ArrayList<>();

    /** Progress tracking */
    private int totalTests;
    private int completedTests;

    /**
     * Executes the complete testing pipeline.
     * 
     * @param outputDirectory Directory to store results
     * @return List of all performance metrics collected
     */
    public List<PerformanceMetrics> executeFullPipeline(String outputDirectory) {
        System.out.println("=== Starting Comprehensive Sorting Algorithm Analysis ===");
        BenchmarkingFramework.validateEnvironment();

        // Calculate total number of tests
        totalTests = ALGORITHMS.length * DATA_TYPES.length * TEST_SIZES.length;
        completedTests = 0;

        System.out.printf("Total tests to execute: %d%n", totalTests);
        System.out.printf("Estimated completion time: %.1f minutes%n", estimateCompletionTime());

        long pipelineStartTime = System.currentTimeMillis();

        // Execute tests for each combination
        for (SortingAlgorithm algorithm : ALGORITHMS) {
            for (String dataType : DATA_TYPES) {
                for (int size : TEST_SIZES) {
                    try {
                        executeTest(algorithm, dataType, size);
                        completedTests++;

                        // Progress reporting
                        double progress = (double) completedTests / totalTests * 100;
                        System.out.printf("Progress: %.1f%% (%d/%d tests completed)%n", progress,
                                completedTests, totalTests);

                    } catch (Exception e) {
                        System.err.printf("Test failed: %s on %s (%d elements) - %s%n",
                                algorithm.getAlgorithmName(), dataType, size, e.getMessage());
                        continue;
                    }
                }
            }
        }

        long pipelineEndTime = System.currentTimeMillis();
        double totalTime = (pipelineEndTime - pipelineStartTime) / 1000.0;

        System.out.printf("Pipeline completed in %.2f seconds%n", totalTime);
        System.out.printf("Collected %d performance measurements%n", allResults.size());

        // Save results
        try {
            saveResults(outputDirectory);
        } catch (IOException e) {
            System.err.println("Failed to save results: " + e.getMessage());
        }

        return new ArrayList<>(allResults);
    }

    /**
     * Executes a single test configuration.
     */
    private void executeTest(SortingAlgorithm algorithm, String dataType, int size) {
        // Generate test data
        int[] testArray = generateTestData(dataType, size);

        // Verify test data was generated correctly
        if (testArray == null || testArray.length != size) {
            throw new RuntimeException("Failed to generate test data");
        }

        // Execute benchmark
        PerformanceMetrics metrics = BenchmarkingFramework.benchmark(algorithm, testArray,
                algorithm.getAlgorithmName(), dataType);

        // Store results
        allResults.add(metrics);

        // Optional: Immediate result validation
        validateResult(metrics, algorithm, dataType, size);
    }

    /**
     * Generates test data based on specified type and size.
     */
    private int[] generateTestData(String dataType, int size) {
        switch (dataType) {
            case "Random":
                return DataGenerationUtilities.generateRandomArray(size, size * 10);
            case "Sorted":
                return DataGenerationUtilities.generateSortedArray(size);
            case "ReverseSorted":
                return DataGenerationUtilities.generateReverseSortedArray(size);
            case "NearlySorted":
                return DataGenerationUtilities.generateNearlySortedArray(size);
            case "WithDuplicates":
                return DataGenerationUtilities.generateArrayWithDuplicates(size);
            default:
                throw new IllegalArgumentException("Unknown data type: " + dataType);
        }
    }

    /**
     * Validates individual test results for sanity checks.
     */
    private void validateResult(PerformanceMetrics metrics, SortingAlgorithm algorithm,
            String dataType, int size) {

        // Check for reasonable execution times (not too fast or too slow)
        double timeMs = metrics.getExecutionTimeMillis();

        if (timeMs < 0.001 && size > 1000) {
            System.out.printf("Warning: Very fast execution time %.6fms for %s on %d elements%n",
                    timeMs, algorithm.getAlgorithmName(), size);
        }

        if (timeMs > 60000) { // More than 1 minute
            System.out.printf("Warning: Very slow execution time %.2fms for %s on %d elements%n",
                    timeMs, algorithm.getAlgorithmName(), size);
        }

        // Check operation counts make sense
        long comparisons = metrics.getComparisonCount();
        long swaps = metrics.getSwapCount();

        if (comparisons == 0 && size > 1) {
            System.out.printf("Warning: No comparisons recorded for %s on %d elements%n",
                    algorithm.getAlgorithmName(), size);
        }

        if (swaps > (long) size * size) {
            System.out.printf("Warning: Excessive swaps %d for %s on %d elements%n", swaps,
                    algorithm.getAlgorithmName(), size);
        }
    }

    /**
     * Saves comprehensive results to CSV file.
     */
    private void saveResults(String outputDirectory) throws IOException {
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = String.format("sorting_results_%s.csv", timestamp);
        File outputFile = new File(dir, filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            // Write CSV header
            writer.println(PerformanceMetrics.getCSVHeader());

            // Write all results
            for (PerformanceMetrics metrics : allResults) {
                writer.println(metrics.toCSV());
            }
        }

        System.out.printf("Results saved to: %s%n", outputFile.getAbsolutePath());

        // Generate summary statistics
        generateSummaryReport(outputDirectory, timestamp);
    }

    /**
     * Generates summary report with key findings.
     */
    private void generateSummaryReport(String outputDirectory, String timestamp)
            throws IOException {
        String summaryFilename = String.format("summary_report_%s.txt", timestamp);
        File summaryFile = new File(outputDirectory, summaryFilename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(summaryFile))) {
            writer.println("SORTING ALGORITHM PERFORMANCE ANALYSIS SUMMARY");
            writer.println("=".repeat(50));
            writer.printf("Generated: %s%n", LocalDateTime.now());
            writer.printf("Total measurements: %d%n", allResults.size());
            writer.println();

            // Algorithm performance summary
            writer.println("ALGORITHM PERFORMANCE OVERVIEW:");
            writer.println("-".repeat(40));

            Map<String, List<PerformanceMetrics>> byAlgorithm = groupByAlgorithm();

            for (String algorithm : Arrays.asList("Bubble Sort", "Selection Sort", "Insertion Sort",
                    "Quick Sort", "Merge Sort", "Heap Sort")) {
                List<PerformanceMetrics> algorithmResults = byAlgorithm.get(algorithm);
                if (algorithmResults != null && !algorithmResults.isEmpty()) {
                    double avgTime = algorithmResults.stream()
                            .mapToDouble(PerformanceMetrics::getExecutionTimeMillis).average()
                            .orElse(0.0);

                    writer.printf("%-15s: Avg Time = %8.2f ms (%d measurements)%n", algorithm,
                            avgTime, algorithmResults.size());
                }
            }

            writer.println();
            writer.println("Detailed analysis available in CSV file.");
        }

        System.out.printf("Summary report saved to: %s%n", summaryFile.getAbsolutePath());
    }

    /**
     * Groups results by algorithm for analysis.
     */
    private Map<String, List<PerformanceMetrics>> groupByAlgorithm() {
        Map<String, List<PerformanceMetrics>> grouped = new HashMap<>();

        for (PerformanceMetrics metrics : allResults) {
            grouped.computeIfAbsent(metrics.getAlgorithmName(), _ -> new ArrayList<>())
                    .add(metrics);
        }

        return grouped;
    }

    /**
     * Estimates pipeline completion time based on test configuration.
     */
    private double estimateCompletionTime() {
        // Rough estimates based on algorithm complexity and array sizes
        double estimatedSeconds = 0;

        for (SortingAlgorithm algorithm : ALGORITHMS) {
            for (int size : TEST_SIZES) {
                // Base time estimate (very rough)
                double baseTime = 0.1; // 0.1 seconds for small operations

                // Complexity-based scaling
                if (algorithm.getTimeComplexity().contains("n²")) {
                    baseTime *= (size / 1000.0) * (size / 1000.0) * 0.1;
                } else if (algorithm.getTimeComplexity().contains("n log n")) {
                    baseTime *= (size / 1000.0) * Math.log(size / 1000.0) * 0.05;
                }

                estimatedSeconds += baseTime * DATA_TYPES.length;
            }
        }

        return estimatedSeconds / 60.0; // Convert to minutes
    }

    /**
     * Main method to run the testing pipeline.
     */
    public static void main(String[] args) {
        String outputDir = args.length > 0 ? args[0] : "results";

        TestingPipeline pipeline = new TestingPipeline();
        pipeline.executeFullPipeline(outputDir);

        System.out.println("=== Pipeline Execution Complete ===");
        System.out.printf("Results available in: %s%n", outputDir);
    }
}
