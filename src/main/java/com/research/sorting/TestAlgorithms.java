package com.research.sorting;

import com.research.sorting.algorithms.*;
import com.research.sorting.utils.BenchmarkUtils;
import java.util.Arrays;

/**
 * Simple test runner to verify algorithm implementations.
 */
public class TestAlgorithms {
    
    public static void main(String[] args) {
        System.out.println("Testing Sorting Algorithms Implementation");
        System.out.println("========================================");
        
        // Test data
        int[] testArray = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Original array: " + Arrays.toString(testArray));
        
        // Test Bubble Sort
        testAlgorithm(new BubbleSort(), testArray.clone());
        
        // Test Selection Sort
        testAlgorithm(new SelectionSort(), testArray.clone());
        
        // Test Insertion Sort
        testAlgorithm(new InsertionSort(), testArray.clone());
    }
    
    private static void testAlgorithm(SortingAlgorithm algorithm, int[] array) {
        System.out.println("\n" + algorithm.getAlgorithmName() + ":");
        System.out.println("Before: " + Arrays.toString(array));
        
        BenchmarkUtils.resetCounters();
        long startTime = System.nanoTime();
        
        algorithm.sort(array);
        
        long endTime = System.nanoTime();
        
        System.out.println("After:  " + Arrays.toString(array));
        System.out.println("Sorted: " + BenchmarkUtils.isSorted(array));
        System.out.println("Time:   " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Stats:  " + BenchmarkUtils.getStatistics());
    }
}
