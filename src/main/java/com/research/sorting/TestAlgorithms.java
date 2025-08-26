package com.research.sorting;

import com.research.sorting.algorithms.*;
import com.research.sorting.utils.BenchmarkUtils;
import java.util.Arrays;

/**
 * Comprehensive test runner for all sorting algorithms.
 */
public class TestAlgorithms {
    
    public static void main(String[] args) {
        System.out.println("=== Comprehensive Sorting Algorithm Test ===");
        System.out.println("Testing all 6 implemented algorithms\n");
        
        // Test data sets
        int[] smallArray = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArray = BenchmarkUtils.generateSortedArray(100);
        int[] reverseArray = BenchmarkUtils.generateReverseSortedArray(100);
        int[] randomArray = BenchmarkUtils.generateRandomArray(100, 1000);
        
        SortingAlgorithm[] algorithms = {
            new BubbleSort(),
            new SelectionSort(),
            new InsertionSort(),
            new QuickSort(),
            new MergeSort(),
            new HeapSort()
        };
        
        // Test small array
        System.out.println("1. Small Array Test:");
        System.out.println("Original: " + Arrays.toString(smallArray));
        testAllAlgorithms(algorithms, smallArray, "Small");
        
        // Test different data types
        System.out.println("\n2. Performance on Different Data Types (100 elements):");
        System.out.printf("%-15s %-12s %-12s %-10s %-10s%n", 
                         "Algorithm", "Comparisons", "Swaps", "Time(ms)", "DataType");
        System.out.println("-".repeat(65));
        
        testPerformance(algorithms, sortedArray, "Sorted");
        testPerformance(algorithms, reverseArray, "Reverse");
        testPerformance(algorithms, randomArray, "Random");
        
        // Algorithm properties summary
        System.out.println("\n3. Algorithm Properties Summary:");
        System.out.printf("%-15s %-12s %-12s %-8s %-10s%n", 
                         "Algorithm", "Time", "Space", "Stable", "In-Place");
        System.out.println("-".repeat(65));
        
        for (SortingAlgorithm algorithm : algorithms) {
            System.out.printf("%-15s %-12s %-12s %-8s %-10s%n",
                algorithm.getAlgorithmName(),
                algorithm.getTimeComplexity(),
                algorithm.getSpaceComplexity(),
                algorithm.isStable() ? "Yes" : "No",
                algorithm.isInPlace() ? "Yes" : "No");
        }
        
        System.out.println("\n=== All Tests Completed Successfully! ===");
    }
    
    private static void testAllAlgorithms(SortingAlgorithm[] algorithms, int[] testArray, String testName) {
        for (SortingAlgorithm algorithm : algorithms) {
            int[] array = testArray.clone();
            algorithm.sort(array);
            
            System.out.printf("%-15s: %s (Sorted: %s)%n",
                algorithm.getAlgorithmName(),
                Arrays.toString(array),
                BenchmarkUtils.isSorted(array) ? "✓" : "✗");
        }
    }
    
    private static void testPerformance(SortingAlgorithm[] algorithms, int[] testArray, String dataType) {
        for (SortingAlgorithm algorithm : algorithms) {
            int[] array = testArray.clone();
            
            BenchmarkUtils.resetCounters();
            long startTime = System.nanoTime();
            
            algorithm.sort(array);
            
            long endTime = System.nanoTime();
            
            System.out.printf("%-15s %-12d %-12d %-10.2f %-10s%n",
                algorithm.getAlgorithmName(),
                BenchmarkUtils.getComparisonCount(),
                BenchmarkUtils.getSwapCount(),
                (endTime - startTime) / 1_000_000.0,
                dataType);
        }
    }
}
