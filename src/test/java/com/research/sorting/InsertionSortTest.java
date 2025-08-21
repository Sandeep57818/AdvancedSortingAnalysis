package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.utils.BenchmarkUtils;
import com.research.sorting.algorithms.InsertionSort;


/**
 * Unit tests for InsertionSort algorithm implementation.
 */
class InsertionSortTest {
    
    private InsertionSort insertionSort;
    
    @BeforeEach
    void setUp() {
        insertionSort = new InsertionSort();
        BenchmarkUtils.resetCounters();
    }
    
    @Test
    @DisplayName("Test sorting empty array")
    void testEmptyArray() {
        int[] array = {};
        insertionSort.sort(array);
        
        assertEquals(0, array.length);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting single element array")
    void testSingleElement() {
        int[] array = {42};
        insertionSort.sort(array);
        
        assertArrayEquals(new int[]{42}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting already sorted array")
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5};
        BenchmarkUtils.resetCounters();
        
        insertionSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
        
        // Should be very efficient on sorted array
        System.out.println("Insertion sort on sorted array: " + BenchmarkUtils.getStatistics());
    }
    
    @Test
    @DisplayName("Test sorting reverse sorted array")
    void testReverseSorted() {
        int[] array = {5, 4, 3, 2, 1};
        insertionSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting random array")
    void testRandomArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        insertionSort.sort(array);
        
        assertArrayEquals(expected, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting array with duplicates")
    void testArrayWithDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        insertionSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(9, array.length);
    }
    
    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            insertionSort.sort(null);
        });
    }
    
    @Test
    @DisplayName("Test algorithm properties")
    void testAlgorithmProperties() {
        assertEquals("Insertion Sort", insertionSort.getAlgorithmName());
        assertEquals("O(n²)", insertionSort.getTimeComplexity());
        assertEquals("O(1)", insertionSort.getSpaceComplexity());
        assertTrue(insertionSort.isStable());
        assertTrue(insertionSort.isInPlace());
    }
    
    @Test
    @DisplayName("Test nearly sorted array efficiency")
    void testNearlySortedArray() {
        int[] array = {1, 2, 4, 3, 5}; // Only one element out of place
        BenchmarkUtils.resetCounters();
        
        insertionSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        
        // Should be very efficient
        long comparisons = BenchmarkUtils.getComparisonCount();
        System.out.println("Insertion sort on nearly sorted: " + BenchmarkUtils.getStatistics());
        
        // Should require minimal operations
        assertTrue(comparisons < 15); // Much less than worst case
    }
}
