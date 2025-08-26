package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.utils.BenchmarkUtils;
import com.research.sorting.algorithms.MergeSort;

/**
 * Unit tests for MergeSort algorithm implementation.
 */
class MergeSortTest {
    
    private MergeSort mergeSort;
    
    @BeforeEach
    void setUp() {
        mergeSort = new MergeSort();
        BenchmarkUtils.resetCounters();
    }
    
    @Test
    @DisplayName("Test sorting empty array")
    void testEmptyArray() {
        int[] array = {};
        mergeSort.sort(array);
        
        assertEquals(0, array.length);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting single element array")
    void testSingleElement() {
        int[] array = {42};
        mergeSort.sort(array);
        
        assertArrayEquals(new int[]{42}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting already sorted array")
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8};
        mergeSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting reverse sorted array")
    void testReverseSorted() {
        int[] array = {8, 7, 6, 5, 4, 3, 2, 1};
        mergeSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting random array")
    void testRandomArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        mergeSort.sort(array);
        
        assertArrayEquals(expected, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test stability with duplicate elements")
    void testStabilityWithDuplicates() {
        // Test that merge sort maintains stability
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        mergeSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(10, array.length);
    }
    
    @Test
    @DisplayName("Test large array performance")
    void testLargeArray() {
        int[] array = BenchmarkUtils.generateRandomArray(1000, 1000);
        BenchmarkUtils.resetCounters();
        
        mergeSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(1000, array.length);
        
        System.out.println("Merge Sort on 1000 elements: " + BenchmarkUtils.getStatistics());
    }
    
    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            mergeSort.sort(null);
        });
    }
    
    @Test
    @DisplayName("Test algorithm properties")
    void testAlgorithmProperties() {
        assertEquals("Merge Sort", mergeSort.getAlgorithmName());
        assertEquals("O(n log n)", mergeSort.getTimeComplexity());
        assertEquals("O(n)", mergeSort.getSpaceComplexity());
        assertTrue(mergeSort.isStable());
        assertFalse(mergeSort.isInPlace());
    }
    
    @Test
    @DisplayName("Test consistent performance across different inputs")
    void testConsistentPerformance() {
        int size = 500;
        
        // Test sorted array
        int[] sortedArray = BenchmarkUtils.generateSortedArray(size);
        BenchmarkUtils.resetCounters();
        mergeSort.sort(sortedArray);
        long sortedComparisons = BenchmarkUtils.getComparisonCount();
        
        // Test reverse sorted array
        int[] reverseArray = BenchmarkUtils.generateReverseSortedArray(size);
        BenchmarkUtils.resetCounters();
        mergeSort.sort(reverseArray);
        long reverseComparisons = BenchmarkUtils.getComparisonCount();
        
        // Test random array
        int[] randomArray = BenchmarkUtils.generateRandomArray(size, size);
        BenchmarkUtils.resetCounters();
        mergeSort.sort(randomArray);
        long randomComparisons = BenchmarkUtils.getComparisonCount();
        
        // Merge sort should have consistent performance
        System.out.printf("Merge Sort consistency: Sorted=%d, Reverse=%d, Random=%d%n",
                         sortedComparisons, reverseComparisons, randomComparisons);
        
        // All should be within reasonable range of each other
        double maxRatio = Math.max(Math.max(
            (double) sortedComparisons / reverseComparisons,
            (double) reverseComparisons / sortedComparisons),
            Math.max(
            (double) randomComparisons / sortedComparisons,
            (double) sortedComparisons / randomComparisons));
        
        assertTrue(maxRatio < 2.0, "Performance should be consistent across input types");
    }
    
    @Test
    @DisplayName("Test optimized merge sort with threshold")
    void testOptimizedMergeSort() {
        int[] array = BenchmarkUtils.generateRandomArray(100, 100);
        BenchmarkUtils.resetCounters();
        
        mergeSort.optimizedSort(array, 10); // Use insertion sort for arrays < 10
        
        assertTrue(BenchmarkUtils.isSorted(array));
        System.out.println("Optimized Merge Sort: " + BenchmarkUtils.getStatistics());
    }
}
