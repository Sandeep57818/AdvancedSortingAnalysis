package com.research.sorting.analysis;

import com.research.sorting.PerformanceMetrics;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive data quality analyzer for research validation.
 * 
 * Performs statistical analysis and quality checks on collected
 * performance data to ensure research integrity and identify
 * patterns for further investigation.
 */
public class DataQualityAnalyzer {
    
    /**
     * Analyzes CSV file containing performance results.
     */
    public static void analyzeCSVFile(String csvFilePath) throws IOException {
        System.out.println("=== DATA QUALITY ANALYSIS ===");
        System.out.printf("Analyzing file: %s%n", csvFilePath);
        System.out.println();
        
        List<PerformanceMetrics> data = loadDataFromCSV(csvFilePath);
        
        if (data.isEmpty()) {
            System.out.println("ERROR: No data found in CSV file");
            return;
        }
        
        // Comprehensive analysis
        performCompletenessAnalysis(data);
        performConsistencyAnalysis(data);
        performComplexityValidation(data);
        generateDataSummary(data);
    }
    
    /**
     * Checks data completeness across all expected combinations.
     */
    private static void performCompletenessAnalysis(List<PerformanceMetrics> data) {
        System.out.println("1. COMPLETENESS ANALYSIS");
        System.out.println("-".repeat(30));
        
        // Expected combinations
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", 
                              "Quick Sort", "Merge Sort", "Heap Sort"};
        String[] dataTypes = {"Random", "Sorted", "ReverseSorted", "NearlySorted", "WithDuplicates"};
        int[] sizes = {100, 200, 400, 600, 800, 1000};
        
        int expectedTotal = algorithms.length * dataTypes.length * sizes.length;
        System.out.printf("Expected data points: %d%n", expectedTotal);
        System.out.printf("Actual data points: %d%n", data.size());
        System.out.printf("Completeness: %.1f%%%n", (double) data.size() / expectedTotal * 100);
        
        // Check for missing combinations
        Set<String> expectedCombinations = new HashSet<>();
        Set<String> actualCombinations = new HashSet<>();
        
        for (String alg : algorithms) {
            for (String type : dataTypes) {
                for (int size : sizes) {
                    expectedCombinations.add(alg + "|" + type + "|" + size);
                }
            }
        }
        
        for (PerformanceMetrics metrics : data) {
            String combination = metrics.getAlgorithmName() + "|" + 
                               metrics.getDataType() + "|" + 
                               metrics.getInputSize();
            actualCombinations.add(combination);
        }
        
        Set<String> missing = new HashSet<>(expectedCombinations);
        missing.removeAll(actualCombinations);
        
        if (missing.isEmpty()) {
            System.out.println("✓ All expected data combinations present");
        } else {
            System.out.printf("⚠ Missing %d combinations:%n", missing.size());
            missing.stream().limit(5).forEach(combo -> 
                System.out.println("  - " + combo.replace("|", " on ")));
            if (missing.size() > 5) {
                System.out.printf("  ... and %d more%n", missing.size() - 5);
            }
        }
        System.out.println();
    }
    
    /**
     * Analyzes data consistency and identifies outliers.
     */
    private static void performConsistencyAnalysis(List<PerformanceMetrics> data) {
        System.out.println("2. CONSISTENCY ANALYSIS");
        System.out.println("-".repeat(30));
        
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        for (String algorithm : byAlgorithm.keySet()) {
            List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
            analyzeAlgorithmConsistency(algorithm, algorithmData);
        }
        System.out.println();
    }
    
    /**
     * Validates empirical complexity against theoretical expectations.
     */
    private static void performComplexityValidation(List<PerformanceMetrics> data) {
        System.out.println("3. COMPLEXITY VALIDATION");
        System.out.println("-".repeat(30));
        
        // Group by algorithm and data type for complexity analysis
        Map<String, Map<String, List<PerformanceMetrics>>> grouped = data.stream()
            .collect(Collectors.groupingBy(
                PerformanceMetrics::getAlgorithmName,
                Collectors.groupingBy(PerformanceMetrics::getDataType)));
        
        for (String algorithm : grouped.keySet()) {
            System.out.printf("%s complexity validation:%n", algorithm);
            
            Map<String, List<PerformanceMetrics>> dataTypeGroups = grouped.get(algorithm);
            for (String dataType : dataTypeGroups.keySet()) {
                List<PerformanceMetrics> typeData = dataTypeGroups.get(dataType);
                validateComplexityGrowth(algorithm, dataType, typeData);
            }
            System.out.println();
        }
    }
    
    /**
     * Validates complexity growth patterns.
     */
    private static void validateComplexityGrowth(String algorithm, String dataType, 
                                               List<PerformanceMetrics> data) {
        if (data.size() < 3) return; // Need at least 3 points for growth analysis
        
        // Sort by input size
        data.sort(Comparator.comparing(PerformanceMetrics::getInputSize));
        
        // Calculate growth ratios
        List<Double> growthRatios = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            PerformanceMetrics current = data.get(i);
            PerformanceMetrics previous = data.get(i-1);
            
            double sizeRatio = (double) current.getInputSize() / previous.getInputSize();
            double timeRatio = (double) current.getExecutionTimeNanos() / previous.getExecutionTimeNanos();
            
            if (sizeRatio > 1.0 && timeRatio > 0) {
                growthRatios.add(timeRatio / sizeRatio);
            }
        }
        
        if (!growthRatios.isEmpty()) {
            double avgGrowthRatio = growthRatios.stream()
                .mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            String complexity = getExpectedComplexity(algorithm);
            System.out.printf("  %s on %s: growth ratio = %.2f (expected: %s)%n",
                             dataType, algorithm, avgGrowthRatio, complexity);
        }
    }
    
    /**
     * Returns expected theoretical complexity for algorithm.
     */
    private static String getExpectedComplexity(String algorithm) {
        switch (algorithm) {
            case "Quick Sort":
            case "Merge Sort":
            case "Heap Sort":
                return "O(n log n)";
            case "Bubble Sort":
            case "Selection Sort":
            case "Insertion Sort":
                return "O(n²)";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Analyzes consistency within algorithm results.
     */
    private static void analyzeAlgorithmConsistency(String algorithm, List<PerformanceMetrics> data) {
        // Calculate coefficient of variation for execution times
        List<Double> times = data.stream()
            .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
            .boxed().collect(Collectors.toList());
        
        double mean = times.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = times.stream()
            .mapToDouble(t -> Math.pow(t - mean, 2))
            .average().orElse(0.0);
        double stdDev = Math.sqrt(variance);
        double coeffVar = mean > 0 ? (stdDev / mean) * 100 : 0;
        
        System.out.printf("%-15s: CV = %5.1f%% (n=%d)%n", algorithm, coeffVar, data.size());
        
        if (coeffVar > 50) {
            System.out.printf("  ⚠ High variability detected in %s%n", algorithm);
        }
    }
    
    /**
     * Generates comprehensive data summary.
     */
    private static void generateDataSummary(List<PerformanceMetrics> data) {
        System.out.println("4. DATA SUMMARY");
        System.out.println("-".repeat(30));
        
        // Overall statistics
        DoubleSummaryStatistics timeStats = data.stream()
            .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
            .summaryStatistics();
        
        LongSummaryStatistics comparisonStats = data.stream()
            .mapToLong(PerformanceMetrics::getComparisonCount)
            .summaryStatistics();
        
        System.out.printf("Execution Time Stats:%n");
        System.out.printf("  Min: %8.3f ms%n", timeStats.getMin());
        System.out.printf("  Max: %8.3f ms%n", timeStats.getMax());
        System.out.printf("  Avg: %8.3f ms%n", timeStats.getAverage());
        System.out.println();
        
        System.out.printf("Comparison Count Stats:%n");
        System.out.printf("  Min: %8d%n", comparisonStats.getMin());
        System.out.printf("  Max: %8d%n", comparisonStats.getMax());
        System.out.printf("  Avg: %8.0f%n", comparisonStats.getAverage());
        System.out.println();
    }
    
    /**
     * Loads performance data from CSV file.
     */
    public static List<PerformanceMetrics> loadDataFromCSV(String csvFilePath) throws IOException {
        List<PerformanceMetrics> data = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String header = reader.readLine(); // Skip header
            if (header == null) return data;
            
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    PerformanceMetrics metrics = parseCSVLine(line);
                    if (metrics != null) {
                        data.add(metrics);
                    }
                } catch (Exception e) {
                    System.out.printf("Warning: Failed to parse line: %s%n", line);
                }
            }
        }
        
        return data;
    }
    
    /**
     * Parses CSV line into PerformanceMetrics object.
     */
    private static PerformanceMetrics parseCSVLine(String line) {
        String[] fields = line.split(",");
        if (fields.length < 7) return null;
        
        try {
            return new PerformanceMetrics(
                Long.parseLong(fields[3]), // executionTime
                Long.parseLong(fields[4]), // memoryUsage
                Long.parseLong(fields[5]), // comparisons
                Long.parseLong(fields[6]), // swaps
                fields[0], // algorithmName
                Integer.parseInt(fields[2]), // inputSize
                fields[1]  // dataType
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Main method for standalone data analysis.
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java DataQualityAnalyzer <csv-file-path>");
            return;
        }
        
        analyzeCSVFile(args[0]);
    }
}
