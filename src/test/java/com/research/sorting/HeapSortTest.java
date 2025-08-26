package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.utils.BenchmarkUtils;
import com.research.sorting.algorithms.HeapSort;


/**
 * Unit tests for HeapSort algorithm implementation.
 */
class HeapSortTest {
    
    private HeapSort heapSort;
    
    @BeforeEach
    void setUp() {
        heapSort = new HeapSort();
        BenchmarkUtils.resetCounters();
    }
    
    @Test
    @DisplayName("Test sorting empty array")
    void testEmptyArray() {
        int[] array = {};
        heapSort.sort(array);
        
        assertEquals(0, array.length);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting single element array")
    void testSingleElement() {
        int[] array = {42};
        heapSort.sort(array);
        
        assertArrayEquals(new int[]{42}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting already sorted array")
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5, 6, 7};
        heapSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting reverse sorted array")
    void testReverseSorted() {
        int[] array = {7, 6, 5, 4, 3, 2, 1};
        heapSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting random array")
    void testRandomArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        heapSort.sort(array);
        
        assertArrayEquals(expected, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting array with duplicates")
    void testArrayWithDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        heapSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(11, array.length);
    }
    
    @Test
    @DisplayName("Test heap property validation")
    void testHeapProperty() {
        int[] array = {4, 10, 3, 5, 1};
        
        // Build max heap and verify heap property
        // Note: This test requires access to buildMaxHeap method
        // For testing purposes, we'll sort and verify the result
        heapSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertArrayEquals(new int[]{1, 3, 4, 5, 10}, array);
    }
    
    @Test
    @DisplayName("Test large array performance")
    void testLargeArray() {
        int[] array = BenchmarkUtils.generateRandomArray(1000, 1000);
        BenchmarkUtils.resetCounters();
        
        heapSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(1000, array.length);
        
        System.out.println("Heap Sort on 1000 elements: " + BenchmarkUtils.getStatistics());
    }
    
    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            heapSort.sort(null);
        });
    }
    
    @Test
    @DisplayName("Test algorithm properties")
    void testAlgorithmProperties() {
        assertEquals("Heap Sort", heapSort.getAlgorithmName());
        assertEquals("O(n log n)", heapSort.getTimeComplexity());
        assertEquals("O(1)", heapSort.getSpaceComplexity());
        assertFalse(heapSort.isStable());
        assertTrue(heapSort.isInPlace());
    }
    
    @Test
    @DisplayName("Test consistent O(n log n) performance")
    void testConsistentPerformance() {
        int size = 200;
        
        // Test different input types
        int[] sortedArray = BenchmarkUtils.generateSortedArray(size);
        int[] reverseArray = BenchmarkUtils.generateReverseSortedArray(size);
        int[] randomArray = BenchmarkUtils.generateRandomArray(size, size);
        
        // Measure performance on each
        BenchmarkUtils.resetCounters();
        heapSort.sort(sortedArray);
        long sortedComparisons = BenchmarkUtils.getComparisonCount();
        
        BenchmarkUtils.resetCounters();
        heapSort.sort(reverseArray);
        long reverseComparisons = BenchmarkUtils.getComparisonCount();
        
        BenchmarkUtils.resetCounters();
        heapSort.sort(randomArray);
        long randomComparisons = BenchmarkUtils.getComparisonCount();
        
        System.out.printf("Heap Sort performance: Sorted=%d, Reverse=%d, Random=%d%n",
                         sortedComparisons, reverseComparisons, randomComparisons);
        
        // All arrays should be sorted
        assertTrue(BenchmarkUtils.isSorted(sortedArray));
        assertTrue(BenchmarkUtils.isSorted(reverseArray));
        assertTrue(BenchmarkUtils.isSorted(randomArray));
        
        // Performance should be reasonably consistent
        long maxComparisons = Math.max(Math.max(sortedComparisons, reverseComparisons), randomComparisons);
        long minComparisons = Math.min(Math.min(sortedComparisons, reverseComparisons), randomComparisons);
        
        // Ratio should not be too high
        assertTrue((double) maxComparisons / minComparisons < 3.0, 
                  "Heap sort performance should be consistent");
    }
    
    @Test
    @DisplayName("Test max heap property utility method")
    void testMaxHeapUtility() {
        // Test the isMaxHeap utility method
        int[] maxHeap = {10, 8, 9, 4, 7, 5, 6, 1, 2, 3};
        assertTrue(heapSort.isMaxHeap(maxHeap, maxHeap.length));
        
        int[] notMaxHeap = {1, 8, 9, 4, 7, 5, 6, 10, 2, 3};
        assertFalse(heapSort.isMaxHeap(notMaxHeap, notMaxHeap.length));
    }
}
