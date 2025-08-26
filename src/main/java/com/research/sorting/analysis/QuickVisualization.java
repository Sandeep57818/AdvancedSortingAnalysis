package com.research.sorting.analysis;

import com.research.sorting.PerformanceMetrics;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Quick visualization generator for initial data exploration.
 * Creates text-based charts and tables for immediate analysis.
 */
public class QuickVisualization {
    
    public static void generateQuickCharts(String csvFilePath) throws IOException {
        System.out.println("=== QUICK VISUALIZATION GENERATION ===");
        
        List<PerformanceMetrics> data = DataQualityAnalyzer.loadDataFromCSV(csvFilePath);
        
        if (data.isEmpty()) {
            System.out.println("No data available for visualization");
            return;
        }
        
        // Generate performance comparison table
        generatePerformanceTable(data);
        
        // Generate complexity growth analysis
        generateComplexityChart(data);
        
        // Generate algorithm ranking
        generateAlgorithmRanking(data);
        
        System.out.println("Quick visualization complete!");
    }
    
    /**
     * Generates performance comparison table.
     */
    private static void generatePerformanceTable(List<PerformanceMetrics> data) {
        System.out.println("\n=== PERFORMANCE COMPARISON TABLE ===");
        
        // Group by algorithm and data type
        Map<String, Map<String, List<PerformanceMetrics>>> grouped = data.stream()
            .collect(Collectors.groupingBy(
                PerformanceMetrics::getAlgorithmName,
                Collectors.groupingBy(PerformanceMetrics::getDataType)));
        
        System.out.printf("%-15s %-12s %-10s %-12s %-10s%n", 
                         "Algorithm", "DataType", "AvgTime(ms)", "AvgComparisons", "AvgSwaps");
        System.out.println("-".repeat(70));
        
        for (String algorithm : grouped.keySet()) {
            Map<String, List<PerformanceMetrics>> dataTypes = grouped.get(algorithm);
            for (String dataType : dataTypes.keySet()) {
                List<PerformanceMetrics> typeData = dataTypes.get(dataType);
                
                double avgTime = typeData.stream()
                    .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                    .average().orElse(0.0);
                
                double avgComparisons = typeData.stream()
                    .mapToLong(PerformanceMetrics::getComparisonCount)
                    .average().orElse(0.0);
                
                double avgSwaps = typeData.stream()
                    .mapToLong(PerformanceMetrics::getSwapCount)
                    .average().orElse(0.0);
                
                System.out.printf("%-15s %-12s %10.2f %12.0f %10.0f%n",
                                 algorithm, dataType, avgTime, avgComparisons, avgSwaps);
            }
        }
    }
    
    /**
     * Generates complexity growth analysis chart.
     */
    private static void generateComplexityChart(List<PerformanceMetrics> data) {
        System.out.println("\n=== COMPLEXITY GROWTH ANALYSIS ===");
        
        // Focus on random data for clearest complexity patterns
        List<PerformanceMetrics> randomData = data.stream()
            .filter(m -> "Random".equals(m.getDataType()))
            .collect(Collectors.toList());
        
        Map<String, List<PerformanceMetrics>> byAlgorithm = randomData.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        System.out.printf("%-15s", "Input Size");
        for (String algorithm : byAlgorithm.keySet()) {
            System.out.printf(" %-12s", algorithm.substring(0, Math.min(12, algorithm.length())));
        }
        System.out.println();
        System.out.println("-".repeat(15 + byAlgorithm.size() * 13));
        
        int[] sizes = {1000, 5000, 10000, 25000, 50000, 100000};
        for (int size : sizes) {
            System.out.printf("%-15d", size);
            
            for (String algorithm : byAlgorithm.keySet()) {
                List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
                OptionalDouble avgTime = algorithmData.stream()
                    .filter(m -> m.getInputSize() == size)
                    .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                    .average();
                
                if (avgTime.isPresent()) {
                    System.out.printf(" %12.2f", avgTime.getAsDouble());
                } else {
                    System.out.printf(" %12s", "N/A");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Generates algorithm ranking based on average performance.
     */
    private static void generateAlgorithmRanking(List<PerformanceMetrics> data) {
        System.out.println("\n=== ALGORITHM RANKING (Average Performance) ===");
        
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        List<AlgorithmSummary> rankings = new ArrayList<>();
        
        for (String algorithm : byAlgorithm.keySet()) {
            List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
            
            double avgTime = algorithmData.stream()
                .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                .average().orElse(Double.MAX_VALUE);
            
            rankings.add(new AlgorithmSummary(algorithm, avgTime, algorithmData.size()));
        }
        
        rankings.sort(Comparator.comparing(s -> s.averageTime));
        
        System.out.printf("%-4s %-15s %-12s %-10s%n", "Rank", "Algorithm", "AvgTime(ms)", "Tests");
        System.out.println("-".repeat(45));
        
        for (int i = 0; i < rankings.size(); i++) {
            AlgorithmSummary summary = rankings.get(i);
            System.out.printf("%-4d %-15s %12.2f %10d%n",
                             i + 1, summary.algorithmName, summary.averageTime, summary.testCount);
        }
    }
    
    /**
     * Helper class for algorithm ranking.
     */
    private static class AlgorithmSummary {
        final String algorithmName;
        final double averageTime;
        final int testCount;
        
        AlgorithmSummary(String algorithmName, double averageTime, int testCount) {
            this.algorithmName = algorithmName;
            this.averageTime = averageTime;
            this.testCount = testCount;
        }
    }
    
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java QuickVisualization <csv-file-path>");
            return;
        }
        
        generateQuickCharts(args[0]);
    }
}
