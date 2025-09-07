package com.research.sorting.visualization;

import com.research.sorting.PerformanceMetrics;
import com.research.sorting.analysis.DataQualityAnalyzer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessionalChartGenerator {
    
    private static final Color[] ALGORITHM_COLORS = {
        new Color(228, 26, 28),   // Red - Bubble Sort
        new Color(55, 126, 184),  // Blue - Selection Sort
        new Color(77, 175, 74),   // Green - Insertion Sort
        new Color(152, 78, 163),  // Purple - Quick Sort
        new Color(255, 127, 0),   // Orange - Merge Sort
        new Color(166, 86, 40)    // Brown - Heap Sort
    };
    
    private static final String OUTPUT_DIR = "results/charts";
    
    /**
     * Generates complete set of professional charts from CSV data.
     */
    public static void generateAllCharts(String csvFilePath) throws IOException {
        System.out.println("=== PROFESSIONAL CHART GENERATION ===");
        System.out.printf("Processing data from: %s%n", csvFilePath);
        System.out.printf("Output directory: %s%n", OUTPUT_DIR);
        
        // Ensure output directory exists
        new File(OUTPUT_DIR).mkdirs();
        
        // Load data
        List<PerformanceMetrics> data = DataQualityAnalyzer.loadDataFromCSV(csvFilePath);
        
        if (data.isEmpty()) {
            System.out.println("ERROR: No data found for visualization");
            return;
        }
        
        System.out.printf("Loaded %d data points for visualization%n", data.size());
        System.out.println();
        
        // Generate different types of charts
        generatePerformanceComparisonCharts(data);
        generateComplexityGrowthCharts(data);
        generateAlgorithmRankingCharts(data);
        generateDataTypeImpactCharts(data);
        generateStatisticalDistributionCharts(data);
        
        System.out.println("=== CHART GENERATION COMPLETE ===");
        System.out.printf("All charts saved to: %s%n", new File(OUTPUT_DIR).getAbsolutePath());
    }
    
    /**
     * Generates performance comparison line charts.
     */
    private static void generatePerformanceComparisonCharts(List<PerformanceMetrics> data) throws IOException {
        System.out.println("1. Generating Performance Comparison Charts...");
        
        // Group data by data type for separate charts
        Map<String, List<PerformanceMetrics>> byDataType = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getDataType));
        
        for (String dataType : byDataType.keySet()) {
            List<PerformanceMetrics> typeData = byDataType.get(dataType);
            generateSinglePerformanceChart(typeData, dataType);
        }
        
        // Generate overall comparison
        generateOverallPerformanceChart(data);
        System.out.println("   ✓ Performance comparison charts completed");
    }
    
    /**
     * Generates single performance chart for specific data type.
     */
    private static void generateSinglePerformanceChart(List<PerformanceMetrics> data, String dataType) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // Group by algorithm
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        // Create series for each algorithm
        for (String algorithm : byAlgorithm.keySet()) {
            List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
            
            XYSeries series = new XYSeries(algorithm);
            
            // Sort by input size and add points
            algorithmData.stream()
                .sorted(Comparator.comparing(PerformanceMetrics::getInputSize))
                .forEach(metrics -> series.add(
                    metrics.getInputSize(), 
                    metrics.getExecutionTimeMillis()
                ));
            
            dataset.addSeries(series);
        }
        
        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
            String.format("Algorithm Performance - %s Data", dataType),
            "Input Size (elements)",
            "Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // Customize appearance
        customizeXYChart(chart);
        
        // Save chart
        String filename = String.format("performance_%s.png", dataType.toLowerCase());
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, filename), chart, 800, 600);
    }
    
    /**
     * Generates overall performance comparison chart.
     */
    private static void generateOverallPerformanceChart(List<PerformanceMetrics> data) throws IOException {
        // Calculate average performance for each algorithm
        Map<String, Double> avgPerformance = data.stream()
            .collect(Collectors.groupingBy(
                PerformanceMetrics::getAlgorithmName,
                Collectors.averagingDouble(PerformanceMetrics::getExecutionTimeMillis)
            ));
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Add data to dataset
        avgPerformance.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEach(entry -> dataset.addValue(
                entry.getValue(),
                "Average Time",
                entry.getKey()
            ));
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Overall Algorithm Performance Comparison",
            "Algorithm",
            "Average Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // Customize appearance
        customizeCategoryChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "overall_performance.png"), chart, 800, 600);
    }
    
    /**
     * Generates complexity growth analysis charts.
     */
    private static void generateComplexityGrowthCharts(List<PerformanceMetrics> data) throws IOException {
        System.out.println("2. Generating Complexity Growth Charts...");
        
        // Focus on random data for clearest complexity patterns
        List<PerformanceMetrics> randomData = data.stream()
            .filter(m -> "Random".equals(m.getDataType()))
            .collect(Collectors.toList());
        
        generateComplexityComparisonChart(randomData);
        generateLogScaleComplexityChart(randomData);
        
        System.out.println("   ✓ Complexity growth charts completed");
    }
    
    /**
     * Generates linear scale complexity comparison.
     */
    private static void generateComplexityComparisonChart(List<PerformanceMetrics> data) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // Group by algorithm
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        // Separate O(n²) and O(n log n) algorithms
        String[] onSquaredAlgorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort"};
        String[] onLogNAlgorithms = {"Quick Sort", "Merge Sort", "Heap Sort"};
        
        // Add O(n log n) algorithms first (better performance)
        for (String algorithm : onLogNAlgorithms) {
            if (byAlgorithm.containsKey(algorithm)) {
                addAlgorithmSeries(dataset, algorithm, byAlgorithm.get(algorithm));
            }
        }
        
        // Add O(n²) algorithms
        for (String algorithm : onSquaredAlgorithms) {
            if (byAlgorithm.containsKey(algorithm)) {
                addAlgorithmSeries(dataset, algorithm, byAlgorithm.get(algorithm));
            }
        }
        
        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Complexity Growth Comparison (Random Data)",
            "Input Size (elements)",
            "Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // Customize with complexity annotations
        customizeComplexityChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "complexity_growth.png"), chart, 900, 700);
    }
    
    /**
     * Generates logarithmic scale complexity chart.
     */
    private static void generateLogScaleComplexityChart(List<PerformanceMetrics> data) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // Group by algorithm
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        // Add all algorithms
        for (String algorithm : byAlgorithm.keySet()) {
            addAlgorithmSeries(dataset, algorithm, byAlgorithm.get(algorithm));
        }
        
        // Create chart with log scale
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Algorithm Complexity - Logarithmic Scale",
            "Input Size (elements)",
            "Execution Time (ms) - Log Scale",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // Set logarithmic scale
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis yAxis = new NumberAxis("Execution Time (ms) - Log Scale");
        yAxis.setAutoRange(true);
        plot.setRangeAxis(yAxis);
        
        customizeXYChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "complexity_log_scale.png"), chart, 900, 700);
    }
    
    /**
     * Adds algorithm data series to dataset.
     */
    private static void addAlgorithmSeries(XYSeriesCollection dataset, String algorithm, List<PerformanceMetrics> data) {
        XYSeries series = new XYSeries(algorithm);
        
        data.stream()
            .sorted(Comparator.comparing(PerformanceMetrics::getInputSize))
            .forEach(metrics -> series.add(
                metrics.getInputSize(),
                metrics.getExecutionTimeMillis()
            ));
        
        dataset.addSeries(series);
    }
    
    /**
     * Generates algorithm ranking charts.
     */
    private static void generateAlgorithmRankingCharts(List<PerformanceMetrics> data) throws IOException {
        System.out.println("3. Generating Algorithm Ranking Charts...");
        
        generateOverallRankingChart(data);
        generateRankingByDataTypeChart(data);
        
        System.out.println("   ✓ Algorithm ranking charts completed");
    }
    
    /**
     * Generates overall algorithm ranking chart.
     */
    private static void generateOverallRankingChart(List<PerformanceMetrics> data) throws IOException {
        // Calculate comprehensive ranking metrics
        Map<String, AlgorithmSummary> summaries = calculateAlgorithmSummaries(data);
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Sort by average performance
        summaries.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(
                Comparator.comparing(s -> s.avgExecutionTime)))
            .forEach(entry -> {
                AlgorithmSummary summary = entry.getValue();
                dataset.addValue(summary.avgExecutionTime, "Avg Time", entry.getKey());
            });
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Algorithm Performance Ranking",
            "Algorithm (Ranked by Performance)",
            "Average Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        customizeCategoryChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "algorithm_ranking.png"), chart, 800, 600);
    }
    
    /**
     * Generates ranking by data type chart.
     */
    private static void generateRankingByDataTypeChart(List<PerformanceMetrics> data) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Group by data type and algorithm
        Map<String, Map<String, List<PerformanceMetrics>>> grouped = data.stream()
            .collect(Collectors.groupingBy(
                PerformanceMetrics::getDataType,
                Collectors.groupingBy(PerformanceMetrics::getAlgorithmName)
            ));
        
        // Calculate averages for each combination
        for (String dataType : grouped.keySet()) {
            Map<String, List<PerformanceMetrics>> algorithms = grouped.get(dataType);
            
            for (String algorithm : algorithms.keySet()) {
                List<PerformanceMetrics> algorithmData = algorithms.get(algorithm);
                double avgTime = algorithmData.stream()
                    .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                    .average().orElse(0.0);
                
                dataset.addValue(avgTime, dataType, algorithm);
            }
        }
        
        // Create grouped bar chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Algorithm Performance by Data Type",
            "Algorithm",
            "Average Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        customizeCategoryChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "performance_by_datatype.png"), chart, 1000, 700);
    }
    
    /**
     * Generates data type impact analysis charts.
     */
    private static void generateDataTypeImpactCharts(List<PerformanceMetrics> data) throws IOException {
        System.out.println("4. Generating Data Type Impact Charts...");
        
        generateDataTypeComparisonChart(data);
        generateBestWorstCaseChart(data);
        
        System.out.println("   ✓ Data type impact charts completed");
    }
    
    /**
     * Generates data type comparison chart.
     */
    private static void generateDataTypeComparisonChart(List<PerformanceMetrics> data) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Calculate average performance for each data type across all algorithms
        Map<String, Double> dataTypeAverages = data.stream()
            .collect(Collectors.groupingBy(
                PerformanceMetrics::getDataType,
                Collectors.averagingDouble(PerformanceMetrics::getExecutionTimeMillis)
            ));
        
        // Add to dataset
        dataTypeAverages.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEach(entry -> dataset.addValue(
                entry.getValue(),
                "Average Time",
                entry.getKey()
            ));
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Performance Impact of Data Types",
            "Data Type",
            "Average Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        customizeCategoryChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "datatype_impact.png"), chart, 800, 600);
    }
    
    /**
     * Generates best/worst case analysis chart.
     */
    private static void generateBestWorstCaseChart(List<PerformanceMetrics> data) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Group by algorithm
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        for (String algorithm : byAlgorithm.keySet()) {
            List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
            
            // Find best and worst performance by data type
            DoubleSummaryStatistics stats = algorithmData.stream()
                .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                .summaryStatistics();
            
            dataset.addValue(stats.getMin(), "Best Case", algorithm);
            dataset.addValue(stats.getMax(), "Worst Case", algorithm);
            dataset.addValue(stats.getAverage(), "Average Case", algorithm);
        }
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Best vs Worst Case Performance",
            "Algorithm",
            "Execution Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        customizeCategoryChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "best_worst_case.png"), chart, 900, 600);
    }
    
    /**
     * Generates statistical distribution charts.
     */
    private static void generateStatisticalDistributionCharts(List<PerformanceMetrics> data) throws IOException {
        System.out.println("5. Generating Statistical Distribution Charts...");
        
        generateOperationsCorrelationChart(data);
        generateMemoryUsageChart(data);
        
        System.out.println("   ✓ Statistical distribution charts completed");
    }
    
    /**
     * Generates operations correlation chart (comparisons vs swaps).
     */
    private static void generateOperationsCorrelationChart(List<PerformanceMetrics> data) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        // Group by algorithm
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        for (String algorithm : byAlgorithm.keySet()) {
            List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
            
            XYSeries series = new XYSeries(algorithm);
            
            algorithmData.forEach(metrics -> {
                if (metrics.getComparisonCount() > 0 && metrics.getSwapCount() >= 0) {
                    series.add(metrics.getComparisonCount(), metrics.getSwapCount());
                }
            });
            
            if (series.getItemCount() > 0) {
                dataset.addSeries(series);
            }
        }
        
        // Create scatter plot
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Operations Correlation - Comparisons vs Swaps",
            "Comparison Count",
            "Swap Count",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        customizeXYChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "operations_correlation.png"), chart, 800, 600);
    }
    
    /**
     * Generates memory usage analysis chart.
     */
    private static void generateMemoryUsageChart(List<PerformanceMetrics> data) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Calculate average memory usage by algorithm
        Map<String, Double> avgMemory = data.stream()
            .collect(Collectors.groupingBy(
                PerformanceMetrics::getAlgorithmName,
                Collectors.averagingDouble(PerformanceMetrics::getMemoryUsageKB)
            ));
        
        // Add to dataset
        avgMemory.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEach(entry -> dataset.addValue(
                entry.getValue(),
                "Memory Usage",
                entry.getKey()
            ));
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Memory Usage by Algorithm",
            "Algorithm",
            "Average Memory Usage (KB)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        customizeCategoryChart(chart);
        
        // Save chart
        ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, "memory_usage.png"), chart, 800, 600);
    }
    
    // Helper methods for chart customization
    
    /**
     * Customizes XY line charts with professional styling.
     */
    private static void customizeXYChart(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        
        // Set background colors
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Customize renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        // Set colors and line styles
        for (int i = 0; i < ALGORITHM_COLORS.length; i++) {
            if (i < plot.getDataset().getSeriesCount()) {
                renderer.setSeriesPaint(i, ALGORITHM_COLORS[i]);
                renderer.setSeriesShapesVisible(i, true);
                renderer.setSeriesLinesVisible(i, true);
                renderer.setSeriesStroke(i, new BasicStroke(2.0f));
            }
        }
        
        plot.setRenderer(renderer);
        
        // Customize axes
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        
        xAxis.setAutoRange(true);
        yAxis.setAutoRange(true);
        
        // Set fonts
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
    }
    
    /**
     * Customizes category charts with professional styling.
     */
    private static void customizeCategoryChart(JFreeChart chart) {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        
        // Set background colors
        chart.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Customize renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        // Set colors
        for (int i = 0; i < ALGORITHM_COLORS.length; i++) {
            renderer.setSeriesPaint(i, ALGORITHM_COLORS[i]);
        }
        
        // Customize axes
        CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        
        xAxis.setCategoryLabelPositions(
            org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        yAxis.setAutoRange(true);
        
        // Set fonts
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
        xAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        yAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
    }
    
    /**
     * Customizes complexity charts with annotations.
     */
    private static void customizeComplexityChart(JFreeChart chart) {
        customizeXYChart(chart);
        
        // Add complexity annotations if needed
        XYPlot plot = (XYPlot) chart.getPlot();
        
        // Set title with complexity information
        chart.setTitle("Algorithm Complexity Comparison\nO(n²): Bubble, Selection, Insertion | O(n log n): Quick, Merge, Heap");
    }
    
    /**
     * Calculates comprehensive algorithm summaries.
     */
    private static Map<String, AlgorithmSummary> calculateAlgorithmSummaries(List<PerformanceMetrics> data) {
        Map<String, List<PerformanceMetrics>> byAlgorithm = data.stream()
            .collect(Collectors.groupingBy(PerformanceMetrics::getAlgorithmName));
        
        Map<String, AlgorithmSummary> summaries = new HashMap<>();
        
        for (String algorithm : byAlgorithm.keySet()) {
            List<PerformanceMetrics> algorithmData = byAlgorithm.get(algorithm);
            
            DoubleSummaryStatistics timeStats = algorithmData.stream()
                .mapToDouble(PerformanceMetrics::getExecutionTimeMillis)
                .summaryStatistics();
            
            LongSummaryStatistics compStats = algorithmData.stream()
                .mapToLong(PerformanceMetrics::getComparisonCount)
                .summaryStatistics();
            
            summaries.put(algorithm, new AlgorithmSummary(
                timeStats.getAverage(),
                timeStats.getMin(),
                timeStats.getMax(),
                compStats.getAverage(),
                algorithmData.size()
            ));
        }
        
        return summaries;
    }
    
    /**
     * Helper class for algorithm summaries.
     */
    private static class AlgorithmSummary {
        final double avgExecutionTime;
        final double minExecutionTime;
        final double maxExecutionTime;
        final double avgComparisons;
        final int testCount;
        
        AlgorithmSummary(double avgExecutionTime, double minExecutionTime, 
                        double maxExecutionTime, double avgComparisons, int testCount) {
            this.avgExecutionTime = avgExecutionTime;
            this.minExecutionTime = minExecutionTime;
            this.maxExecutionTime = maxExecutionTime;
            this.avgComparisons = avgComparisons;
            this.testCount = testCount;
        }
    }
    
    /**
     * Main method for standalone chart generation.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ProfessionalChartGenerator <csv-file-path>");
            System.out.println("Example: java ProfessionalChartGenerator results/data/sorting_results_20250829_*.csv");
            return;
        }
        
        try {
            generateAllCharts(args[0]);
        } catch (IOException e) {
            System.err.println("Chart generation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
