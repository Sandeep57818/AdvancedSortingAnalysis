package com.research.sorting;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class PerformanceMetrics {
    
    /** Execution time in nanoseconds */
    private long executionTimeNanos;
    
    /** Memory usage in bytes */
    private long memoryUsageBytes;
    
    /** Number of comparison operations performed */
    private long comparisonCount;
    
    /** Number of swap operations performed */
    private long swapCount;
    
    /** Algorithm name for identification */
    private String algorithmName;
    
    /** Input array size */
    private int inputSize;
    
    /** Data type description (e.g., "Random", "Sorted") */
    private String dataType;
    
    /** Timestamp when measurement was taken */
    private long timestamp;
    
    /**
     * Default constructor initializing all metrics to zero.
     */
    public PerformanceMetrics() {
        this.executionTimeNanos = 0;
        this.memoryUsageBytes = 0;
        this.comparisonCount = 0;
        this.swapCount = 0;
        this.algorithmName = "";
        this.inputSize = 0;
        this.dataType = "";
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Parameterized constructor for complete metric initialization.
     * 
     * @param executionTimeNanos Execution time in nanoseconds
     * @param memoryUsageBytes Memory consumption in bytes
     * @param comparisonCount Number of comparisons performed
     * @param swapCount Number of swaps performed
     * @param algorithmName Name of the sorting algorithm
     * @param inputSize Size of input array
     * @param dataType Type of input data
     */
    public PerformanceMetrics(long executionTimeNanos, long memoryUsageBytes, 
                            long comparisonCount, long swapCount, 
                            String algorithmName, int inputSize, String dataType) {
        this.executionTimeNanos = executionTimeNanos;
        this.memoryUsageBytes = memoryUsageBytes;
        this.comparisonCount = comparisonCount;
        this.swapCount = swapCount;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.dataType = dataType;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getter methods
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public long getMemoryUsageBytes() { return memoryUsageBytes; }
    public long getComparisonCount() { return comparisonCount; }
    public long getSwapCount() { return swapCount; }
    public String getAlgorithmName() { return algorithmName; }
    public int getInputSize() { return inputSize; }
    public String getDataType() { return dataType; }
    public long getTimestamp() { return timestamp; }
    
    // Setter methods
    public void setExecutionTimeNanos(long executionTimeNanos) { 
        this.executionTimeNanos = executionTimeNanos; 
    }
    public void setMemoryUsageBytes(long memoryUsageBytes) { 
        this.memoryUsageBytes = memoryUsageBytes; 
    }
    public void setComparisonCount(long comparisonCount) { 
        this.comparisonCount = comparisonCount; 
    }
    public void setSwapCount(long swapCount) { 
        this.swapCount = swapCount; 
    }
    public void setAlgorithmName(String algorithmName) { 
        this.algorithmName = algorithmName; 
    }
    public void setInputSize(int inputSize) { 
        this.inputSize = inputSize; 
    }
    public void setDataType(String dataType) { 
        this.dataType = dataType; 
    }
    
    /**
     * Converts execution time to milliseconds for readable output.
     * 
     * @return Execution time in milliseconds
     */
    public double getExecutionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }
    
    /**
     * Converts memory usage to kilobytes for readable output.
     * 
     * @return Memory usage in kilobytes
     */
    public double getMemoryUsageKB() {
        return memoryUsageBytes / 1024.0;
    }
    
    /**
     * Creates a CSV-formatted string representation of the metrics.
     * 
     * @return CSV string with all metrics
     */
    public String toCSV() {
        return String.format("%s,%s,%d,%d,%d,%d,%d,%d",
            algorithmName, dataType, inputSize, executionTimeNanos,
            memoryUsageBytes, comparisonCount, swapCount, timestamp);
    }
    
    /**
     * Returns CSV header for batch export.
     * 
     * @return CSV header string
     */
    public static String getCSVHeader() {
        return "Algorithm,DataType,InputSize,ExecutionTime(ns)," +
               "MemoryUsage(bytes),Comparisons,Swaps,Timestamp";
    }
    
    /**
     * Provides formatted string representation for console output.
     */
    @Override
    public String toString() {
        return String.format(
            "PerformanceMetrics{algorithm='%s', dataType='%s', " +
            "inputSize=%d, time=%.3fms, memory=%.2fKB, " +
            "comparisons=%d, swaps=%d}",
            algorithmName, dataType, inputSize, getExecutionTimeMillis(),
            getMemoryUsageKB(), comparisonCount, swapCount
        );
    }
    
    /**
     * Calculate median from a list of performance metrics for a specific metric type.
     * Used for statistical analysis to eliminate outliers.
     * 
     * @param metrics List of performance metrics
     * @param metricType Type of metric to calculate median for
     * @return Median value
     */
    public static double calculateMedian(List<PerformanceMetrics> metrics, String metricType) {
        if (metrics.isEmpty()) return 0.0;
        
        List<Long> values = new ArrayList<>();
        for (PerformanceMetrics metric : metrics) {
            switch (metricType.toLowerCase()) {
                case "time":
                    values.add(metric.getExecutionTimeNanos());
                    break;
                case "memory":
                    values.add(metric.getMemoryUsageBytes());
                    break;
                case "comparisons":
                    values.add(metric.getComparisonCount());
                    break;
                case "swaps":
                    values.add(metric.getSwapCount());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown metric type: " + metricType);
            }
        }
        
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size/2 - 1) + values.get(size/2)) / 2.0;
        } else {
            return values.get(size/2);
        }
    }
}
