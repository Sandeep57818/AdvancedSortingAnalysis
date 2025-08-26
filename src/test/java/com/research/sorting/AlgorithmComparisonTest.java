package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.algorithms.*;
import com.research.sorting.utils.BenchmarkUtils;
import java.util.Arrays;
 
/**
 * Integration tests comparing all sorting algorithms.
 */
class AlgorithmComparisonTest {
    
    @Test
    @DisplayName("Test all algorithms produce same sorted result")
    void testAllAlgorithmsProduceSameResult() {
        int[] originalArray = {64, 34, 25, 12, 22, 11, 90, 88, 76, 50, 42};
        int[] expected = originalArray.clone();
        Arrays.sort(expected); // Get correct sorted result
        
        // Test each algorithm
        SortingAlgorithm[] algorithms = {
            new BubbleSort(),
            new SelectionSort(),
            new InsertionSort(),
            new QuickSort(),
            new MergeSort(),
            new HeapSort()
        };
        
        for (SortingAlgorithm algorithm : algorithms) {
            int[] testArray = originalArray.clone();
            algorithm.sort(testArray);
            
            assertArrayEquals(expected, testArray, 
                algorithm.getAlgorithmName() + " should produce correct sorted result");
            assertTrue(BenchmarkUtils.isSorted(testArray),
                algorithm.getAlgorithmName() + " result should be sorted");
        }
    }
    
    @Test
    @DisplayName("Compare algorithm performance on different input types")
    void compareAlgorithmPerformance() {
        int size = 1000;
        SortingAlgorithm[] algorithms = {
            new QuickSort(),
            new MergeSort(),
            new HeapSort()
        };
        
        String[] dataTypes = {"Sorted", "Reverse", "Random"};
        
        System.out.println("\n=== Algorithm Performance Comparison ===");
        System.out.printf("%-12s %-10s %-12s %-10s %-10s%n", 
                         "Algorithm", "DataType", "Comparisons", "Swaps", "Time(ms)");
        System.out.println("-".repeat(60));
        
        for (SortingAlgorithm algorithm : algorithms) {
            for (String dataType : dataTypes) {
                int[] testArray = generateTestArray(dataType, size);
                
                BenchmarkUtils.resetCounters();
                long startTime = System.nanoTime();
                
                algorithm.sort(testArray);
                
                long endTime = System.nanoTime();
                
                assertTrue(BenchmarkUtils.isSorted(testArray));
                
                System.out.printf("%-12s %-10s %-12d %-10d %-10.2f%n",
                    algorithm.getAlgorithmName(), dataType,
                    BenchmarkUtils.getComparisonCount(),
                    BenchmarkUtils.getSwapCount(),
                    (endTime - startTime) / 1_000_000.0);
            }
        }
    }
    
    @Test
    @DisplayName("Test algorithm stability properties")
    void testAlgorithmStability() {
        // Create array with duplicate values to test stability
        // Note: This is a conceptual test since we're using int arrays
        int[] testArray = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        
        SortingAlgorithm[] stableAlgorithms = {
            new BubbleSort(),
            new InsertionSort(),
            new MergeSort()
        };
        
        SortingAlgorithm[] unstableAlgorithms = {
            new SelectionSort(),
            new QuickSort(),
            new HeapSort()
        };
        
        // Test stable algorithms
        for (SortingAlgorithm algorithm : stableAlgorithms) {
            assertTrue(algorithm.isStable(), 
                algorithm.getAlgorithmName() + " should be stable");
            
            int[] array = testArray.clone();
            algorithm.sort(array);
            assertTrue(BenchmarkUtils.isSorted(array));
        }
        
        // Test unstable algorithms
        for (SortingAlgorithm algorithm : unstableAlgorithms) {
            assertFalse(algorithm.isStable(), 
                algorithm.getAlgorithmName() + " should not be stable");
            
            int[] array = testArray.clone();
            algorithm.sort(array);
            assertTrue(BenchmarkUtils.isSorted(array));
        }
    }
    
    @Test
    @DisplayName("Test in-place sorting properties")
    void testInPlaceSorting() {
        SortingAlgorithm[] inPlaceAlgorithms = {
            new BubbleSort(),
            new SelectionSort(),
            new InsertionSort(),
            new QuickSort(),
            new HeapSort()
        };
        
        SortingAlgorithm[] notInPlaceAlgorithms = {
            new MergeSort()
        };
        
        for (SortingAlgorithm algorithm : inPlaceAlgorithms) {
            assertTrue(algorithm.isInPlace(), 
                algorithm.getAlgorithmName() + " should be in-place");
        }
        
        for (SortingAlgorithm algorithm : notInPlaceAlgorithms) {
            assertFalse(algorithm.isInPlace(), 
                algorithm.getAlgorithmName() + " should not be in-place");
        }
    }
    
    private int[] generateTestArray(String type, int size) {
        switch (type) {
            case "Sorted":
                return BenchmarkUtils.generateSortedArray(size);
            case "Reverse":
                return BenchmarkUtils.generateReverseSortedArray(size);
            case "Random":
                return BenchmarkUtils.generateRandomArray(size, size * 10);
            default:
                throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }
}
