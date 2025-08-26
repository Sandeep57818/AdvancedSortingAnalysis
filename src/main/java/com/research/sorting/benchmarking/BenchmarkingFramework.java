package com.research.sorting.benchmarking;

import com.research.sorting.algorithms.SortingAlgorithm;
import com.research.sorting.PerformanceMetrics;
import com.research.sorting.utils.BenchmarkUtils;
import java.util.*;

/**
 * Professional benchmarking framework for sorting algorithm performance analysis.
 * 
 * This framework implements industry-standard benchmarking practices including:
 * - JVM warmup to ensure Just-In-Time compilation optimization
 * - Multiple measurement iterations with statistical analysis
 * - Memory profiling with garbage collection management
 * - Outlier detection and median calculation for reliable results
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-08-26
 */
public class BenchmarkingFramework {
    
    /** Number of warmup iterations to optimize JVM performance */
    private static final int WARMUP_ITERATIONS = 1000;
    
    /** Number of measurement iterations for statistical reliability */
    private static final int MEASUREMENT_ITERATIONS = 100;
    
    /** Sleep time between measurements to allow system stabilization */
    private static final long STABILIZATION_DELAY_MS = 10;
    
    
    /**
     * Benchmarks a sorting algorithm with comprehensive performance analysis.
     * 
     * Process:
     * 1. JVM Warmup Phase - Run algorithm multiple times to trigger JIT optimization
     * 2. Measurement Phase - Collect precise timing and memory metrics
     * 3. Statistical Analysis - Calculate median and filter outliers
     * 
     * @param algorithm Sorting algorithm to benchmark
     * @param inputArray Array to sort (will be cloned for each test)
     * @param algorithmName Name for identification in results
     * @param dataType Description of input data type (e.g., "Random", "Sorted")
     * @return Comprehensive performance metrics
     */
    public static PerformanceMetrics benchmark(SortingAlgorithm algorithm, 
                                             int[] inputArray, 
                                             String algorithmName, 
                                             String dataType) {
        
        System.out.printf("Benchmarking %s on %s data (%d elements)...%n", 
                         algorithmName, dataType, inputArray.length);
        
        // Phase 1: JVM Warmup
        performWarmup(algorithm, inputArray);
        
        // Phase 2: Data Collection
        List<MeasurementResult> results = collectMeasurements(algorithm, inputArray);
        
        // Phase 3: Statistical Analysis
        return analyzeResults(results, algorithmName, dataType, inputArray.length);
    }
    
    /**
     * Performs JVM warmup to ensure optimal performance measurement.
     * Runs the algorithm multiple times to trigger Just-In-Time compilation.
     */
    private static void performWarmup(SortingAlgorithm algorithm, int[] inputArray) {
        System.out.print("  Warming up JVM");
        
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            int[] warmupArray = BenchmarkUtils.copyArray(inputArray);
            BenchmarkUtils.resetCounters();
            
            try {
                algorithm.sort(warmupArray);
            } catch (Exception e) {
                System.err.println("Warmup failed: " + e.getMessage());
                break;
            }
            
            // Progress indicator
            if (i % (WARMUP_ITERATIONS / 10) == 0) {
                System.out.print(".");
            }
        }
        
        System.out.println(" Complete");
        
        // Allow system to stabilize after warmup
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Collects measurement data through multiple algorithm iterations.
     */
    private static List<MeasurementResult> collectMeasurements(SortingAlgorithm algorithm, 
                                                              int[] inputArray) {
        List<MeasurementResult> results = new ArrayList<>();
        System.out.print("  Collecting measurements");
        
        for (int i = 0; i < MEASUREMENT_ITERATIONS; i++) {
            // Stabilization delay between measurements
            try {
                Thread.sleep(STABILIZATION_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            // Prepare clean test environment
            int[] testArray = BenchmarkUtils.copyArray(inputArray);
            BenchmarkUtils.resetCounters();
            
            // Force garbage collection for accurate memory measurement
            System.gc();
            long memoryBefore = BenchmarkUtils.measureMemoryUsage();
            
            // High-precision timing measurement
            long startTime = System.nanoTime();
            
            try {
                algorithm.sort(testArray);
            } catch (Exception e) {
                System.err.println("Measurement failed: " + e.getMessage());
                continue;
            }
            
            long endTime = System.nanoTime();
            
            // Post-execution measurements
            System.gc();
            long memoryAfter = BenchmarkUtils.measureMemoryUsage();
            
            // Validate sorting correctness
            if (!BenchmarkUtils.isSorted(testArray)) {
                System.err.println("Warning: Array not sorted correctly in iteration " + i);
                continue;
            }
            
            // Record measurement
            results.add(new MeasurementResult(
                endTime - startTime,
                Math.max(0, memoryAfter - memoryBefore), // Ensure non-negative
                BenchmarkUtils.getComparisonCount(),
                BenchmarkUtils.getSwapCount()
            ));
            
            // Progress indicator
            if (i % (MEASUREMENT_ITERATIONS / 10) == 0) {
                System.out.print(".");
            }
        }
        
        System.out.println(" Complete");
        return results;
    }
    
    /**
     * Analyzes measurement results with statistical processing.
     */
    private static PerformanceMetrics analyzeResults(List<MeasurementResult> results, 
                                                   String algorithmName, 
                                                   String dataType, 
                                                   int inputSize) {
        
        if (results.isEmpty()) {
            System.err.println("No valid measurements collected");
            return new PerformanceMetrics(0, 0, 0, 0, algorithmName, inputSize, dataType);
        }
        
        // Extract measurement vectors
        List<Long> executionTimes = new ArrayList<>();
        List<Long> memoryUsages = new ArrayList<>();
        List<Long> comparisons = new ArrayList<>();
        List<Long> swaps = new ArrayList<>();
        
        for (MeasurementResult result : results) {
            executionTimes.add(result.executionTime);
            memoryUsages.add(result.memoryUsage);
            comparisons.add(result.comparisons);
            swaps.add(result.swaps);
        }
        
        // Statistical analysis - use median to avoid outlier influence
        long medianTime = calculateMedian(executionTimes);
        long medianMemory = calculateMedian(memoryUsages);
        long medianComparisons = calculateMedian(comparisons);
        long medianSwaps = calculateMedian(swaps);
        
        // Additional statistics for analysis
        double avgTime = executionTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double stdDevTime = calculateStandardDeviation(executionTimes, avgTime);
        
        System.out.printf("  Results: Time=%.2f±%.2fms, Memory=%dKB, Ops=%d/%d%n",
                         medianTime / 1_000_000.0, stdDevTime / 1_000_000.0,
                         medianMemory / 1024, medianComparisons, medianSwaps);
        
        return new PerformanceMetrics(
            medianTime, medianMemory, medianComparisons, medianSwaps,
            algorithmName, inputSize, dataType
        );
    }
    
    /**
     * Calculates median value from a list of measurements.
     * Median is preferred over mean to reduce outlier influence.
     */
    private static long calculateMedian(List<Long> values) {
        if (values.isEmpty()) return 0;
        
        List<Long> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2;
        } else {
            return sorted.get(size / 2);
        }
    }
    
    /**
     * Calculates standard deviation for outlier analysis.
     */
    private static double calculateStandardDeviation(List<Long> values, double mean) {
        if (values.size() <= 1) return 0.0;
        
        double sumSquaredDiffs = 0.0;
        for (long value : values) {
            double diff = value - mean;
            sumSquaredDiffs += diff * diff;
        }
        
        return Math.sqrt(sumSquaredDiffs / (values.size() - 1));
    }
    
    /**
     * Data class to hold individual measurement results.
     */
    private static class MeasurementResult {
        final long executionTime;
        final long memoryUsage;
        final long comparisons;
        final long swaps;
        
        MeasurementResult(long executionTime, long memoryUsage, 
                         long comparisons, long swaps) {
            this.executionTime = executionTime;
            this.memoryUsage = memoryUsage;
            this.comparisons = comparisons;
            this.swaps = swaps;
        }
    }
    
    /**
     * Validates benchmarking environment and provides system information.
     */
    public static void validateEnvironment() {
        Runtime runtime = Runtime.getRuntime();
        
        System.out.println("=== Benchmarking Environment ===");
        System.out.printf("Java Version: %s%n", System.getProperty("java.version"));
        System.out.printf("JVM: %s%n", System.getProperty("java.vm.name"));
        System.out.printf("OS: %s %s%n", System.getProperty("os.name"), System.getProperty("os.arch"));
        System.out.printf("Available Processors: %d%n", runtime.availableProcessors());
        System.out.printf("Max Memory: %.1f MB%n", runtime.maxMemory() / (1024.0 * 1024.0));
        System.out.printf("Free Memory: %.1f MB%n", runtime.freeMemory() / (1024.0 * 1024.0));
        System.out.println("==============================");
    }
}
