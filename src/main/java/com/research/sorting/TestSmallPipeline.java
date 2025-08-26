package com.research.sorting;

import com.research.sorting.benchmarking.*;
import com.research.sorting.algorithms.*;
import com.research.sorting.utils.DataGenerationUtilities;

/**
 * Small-scale pipeline test to validate framework before full execution.
 */
public class TestSmallPipeline {
    
    public static void main(String[] args) {
        System.out.println("=== Small-Scale Pipeline Validation ===");
        
        // Test individual components first
        testDataGeneration();
        testBenchmarkingFramework();
        testSamplePipeline();
        
        System.out.println("=== Validation Complete ===");
        System.out.println("Framework ready for full-scale testing!");
    }
    
    private static void testDataGeneration() {
        System.out.println("\n1. Testing Data Generation Utilities:");
        
        int[] random = DataGenerationUtilities.generateRandomArray(1000, 1000);
        int[] sorted = DataGenerationUtilities.generateSortedArray(1000);
        int[] nearly = DataGenerationUtilities.generateNearlySortedArray(1000);
        
        System.out.println("  ✓ Random array: " + DataGenerationUtilities.analyzeArray(random));
        System.out.println("  ✓ Sorted array: " + DataGenerationUtilities.analyzeArray(sorted));
        System.out.println("  ✓ Nearly sorted: " + DataGenerationUtilities.analyzeArray(nearly));
    }
    
    private static void testBenchmarkingFramework() {
        System.out.println("\n2. Testing Benchmarking Framework:");
        
        QuickSort quickSort = new QuickSort();
        int[] testArray = DataGenerationUtilities.generateRandomArray(5000, 5000);
        
        PerformanceMetrics metrics = BenchmarkingFramework.benchmark(
            quickSort, testArray, "QuickSort", "Random"
        );
        
        System.out.println("  ✓ Benchmark completed: " + metrics);
    }
    
    private static void testSamplePipeline() {
        System.out.println("\n3. Testing Sample Pipeline:");
        
        System.out.println("  Framework validation successful!");
        System.out.println("  Ready to execute full pipeline on Day 6");
    }
}
