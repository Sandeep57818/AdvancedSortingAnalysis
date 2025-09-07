package com.research.sorting;

import com.research.sorting.benchmarking.BenchmarkingFramework;
import com.research.sorting.benchmarking.TestingPipeline;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

public class ExecutionController {
    
    private static final String RESULTS_BASE_DIR = "results";
    private static final String DATA_DIR = RESULTS_BASE_DIR + "/data";
    
    public static void main(String[] args) {
        System.out.println("=== SORTING ALGORITHM RESEARCH EXECUTION ===");
        System.out.println("Starting comprehensive performance analysis...");
        System.out.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println();
        
        try {
            // Step 1: Environment validation
            validateExecutionEnvironment();
            
            // Step 2: Full data collection
            List<PerformanceMetrics> results = executeFullDataCollection();
            
            // Step 3: Data validation
            validateCollectedData(results);
            
            // Step 4: Generate preliminary analysis
            generatePreliminaryAnalysis(results);
            
            // Step 5: Success summary
            printExecutionSummary(results);
            
        } catch (Exception e) {
            System.err.println("Execution failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Validates execution environment before starting data collection.
     */
    private static void validateExecutionEnvironment() {
        System.out.println("=== STEP 1: Environment Validation ===");
        
        // Validate Java environment
        BenchmarkingFramework.validateEnvironment();
        
        // Validate directory structure
        ensureDirectoryStructure();
        
        // Validate available memory
        Runtime runtime = Runtime.getRuntime();
        long availableMemoryMB = runtime.maxMemory() / (1024 * 1024);
        
        if (availableMemoryMB < 512) {
            System.out.println("WARNING: Low memory available (" + availableMemoryMB + "MB). " +
                             "Consider increasing heap size with -Xmx1G");
        } else {
            System.out.printf("✓ Memory validation passed: %dMB available%n", availableMemoryMB);
        }
        
        System.out.println("✓ Environment validation completed");
        System.out.println();
    }
    
    /**
     * Executes full-scale data collection pipeline.
     */
    private static List<PerformanceMetrics> executeFullDataCollection() {
        System.out.println("=== STEP 2: Full-Scale Data Collection ===");
        System.out.println("This process will take approximately 30-45 minutes...");
        System.out.println("Please do not interrupt the execution.");
        System.out.println();
        
        TestingPipeline pipeline = new TestingPipeline();
        long startTime = System.currentTimeMillis();
        
        List<PerformanceMetrics> results = pipeline.executeFullPipeline(DATA_DIR);
        
        long endTime = System.currentTimeMillis();
        double executionTimeMinutes = (endTime - startTime) / (1000.0 * 60.0);
        
        System.out.printf("✓ Data collection completed in %.2f minutes%n", executionTimeMinutes);
        System.out.printf("✓ Collected %d performance measurements%n", results.size());
        System.out.println();
        
        return results;
    }
    
    /**
     * Validates quality and consistency of collected data.
     */
    private static void validateCollectedData(List<PerformanceMetrics> results) {
        System.out.println("=== STEP 3: Data Quality Validation ===");
        
        if (results.isEmpty()) {
            throw new RuntimeException("No data collected - execution failed");
        }
        
        // Expected data points: 6 algorithms × 5 data types × 6 sizes = 180
        int expectedDataPoints = 6 * 5 * 6;
        System.out.printf("Expected data points: %d%n", expectedDataPoints);
        System.out.printf("Actual data points: %d%n", results.size());
        
        if (results.size() < expectedDataPoints * 0.9) { // Allow 10% tolerance
            System.out.println("WARNING: Significant data loss detected - " +
                             "less than 90% of expected data collected");
        } else {
            System.out.println("✓ Data collection completeness validated");
        }
        
        // Validate data consistency
        validateDataConsistency(results);
        
        System.out.println("✓ Data quality validation completed");
        System.out.println();
    }
    
    /**
     * Validates consistency and reasonableness of collected data.
     */
    private static void validateDataConsistency(List<PerformanceMetrics> results) {
        int anomaliesFound = 0;
        
        for (PerformanceMetrics metrics : results) {
            // Check for reasonable execution times
            double timeMs = metrics.getExecutionTimeMillis();
            int size = metrics.getInputSize();
            
            // Very rough heuristics for anomaly detection
            if (timeMs < 0.001 && size > 10000) {
                System.out.printf("ANOMALY: Suspiciously fast time %.6f ms for %s on %d elements%n",
                                 timeMs, metrics.getAlgorithmName(), size);
                anomaliesFound++;
            }
            
            if (timeMs > 60000) { // More than 1 minute
                System.out.printf("ANOMALY: Very slow execution %.2f ms for %s on %d elements%n",
                                 timeMs, metrics.getAlgorithmName(), size);
                anomaliesFound++;
            }
            
            // Check for zero operation counts (suspicious)
            if (metrics.getComparisonCount() == 0 && size > 1) {
                System.out.printf("ANOMALY: Zero comparisons for %s on %d elements%n",
                                 metrics.getAlgorithmName(), size);
                anomaliesFound++;
            }
        }
        
        System.out.printf("Data consistency check: %d anomalies detected%n", anomaliesFound);
        
        if (anomaliesFound > results.size() * 0.05) { // More than 5% anomalies
            System.out.println("WARNING: High anomaly rate detected - review data quality");
        }
    }
    
    /**
     * Generates preliminary analysis of collected data.
     */
    private static void generatePreliminaryAnalysis(List<PerformanceMetrics> results) {
        System.out.println("=== STEP 4: Preliminary Analysis ===");
        
        // Group results by algorithm
        var algorithmGroups = results.stream()
            .collect(java.util.stream.Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        System.out.println("Performance Summary by Algorithm:");
        System.out.println("-".repeat(50));
        
        for (String algorithm : java.util.Arrays.asList(
            "Bubble Sort", "Selection Sort", "Insertion Sort", 
            "Quick Sort", "Merge Sort", "Heap Sort")) {
            
            List<PerformanceMetrics> algorithmResults = algorithmGroups.get(algorithm);
            if (algorithmResults != null && !algorithmResults.isEmpty()) {
                double avgTime = algorithmResults.stream()
                    .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                    .average().orElse(0.0);
                
                long avgComparisons = (long) algorithmResults.stream()
                    .mapToLong(PerformanceMetrics::getComparisonCount)
                    .average().orElse(0.0);
                
                System.out.printf("%-15s: Avg Time = %8.2f ms, Avg Comparisons = %8d (%d tests)%n",
                                 algorithm, avgTime, avgComparisons, algorithmResults.size());
            }
        }
        
        System.out.println();
        System.out.println("✓ Preliminary analysis completed");
        System.out.println();
    }
    
    /**
     * Prints comprehensive execution summary.
     */
    private static void printExecutionSummary(List<PerformanceMetrics> results) {
        System.out.println("=== EXECUTION COMPLETED SUCCESSFULLY ===");
        System.out.println("Summary:");
        System.out.printf("• Total measurements collected: %d%n", results.size());
        System.out.printf("• Data files location: %s%n", new File(DATA_DIR).getAbsolutePath());
        System.out.printf("• Execution timestamp: %s%n", 
                         LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println();
        System.out.println("Next Steps:");
        System.out.println("1. Review data quality in generated CSV files");
        System.out.println("2. Create visualizations and charts");
        System.out.println("3. Perform detailed statistical analysis");
        System.out.println("4. Begin research paper writing");
        System.out.println();
        System.out.println("Data collection phase COMPLETE!");
    }
    
    /**
     * Ensures proper directory structure exists.
     */
    private static void ensureDirectoryStructure() {
        String[] directories = {
            RESULTS_BASE_DIR,
            DATA_DIR,
            RESULTS_BASE_DIR + "/charts",
            RESULTS_BASE_DIR + "/analysis",
            RESULTS_BASE_DIR + "/reports"
        };
        
        for (String dir : directories) {
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.printf("Created directory: %s%n", dir);
            }
        }
    }
}
