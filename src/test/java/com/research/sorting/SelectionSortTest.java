
package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.utils.BenchmarkUtils;
import com.research.sorting.algorithms.SelectionSort;


/**
 * Unit tests for SelectionSort algorithm implementation.
 */
class SelectionSortTest {
    
    private SelectionSort selectionSort;
    
    @BeforeEach
    void setUp() {
        selectionSort = new SelectionSort();
        BenchmarkUtils.resetCounters();
    }
    
    @Test
    @DisplayName("Test sorting empty array")
    void testEmptyArray() {
        int[] array = {};
        selectionSort.sort(array);
        
        assertEquals(0, array.length);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting single element array")
    void testSingleElement() {
        int[] array = {42};
        selectionSort.sort(array);
        
        assertArrayEquals(new int[]{42}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting already sorted array")
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5};
        selectionSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting reverse sorted array")
    void testReverseSorted() {
        int[] array = {5, 4, 3, 2, 1};
        selectionSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting random array")
    void testRandomArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        selectionSort.sort(array);
        
        assertArrayEquals(expected, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting array with duplicates")
    void testArrayWithDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        selectionSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(9, array.length);
    }
    
    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            selectionSort.sort(null);
        });
    }
    
    @Test
    @DisplayName("Test algorithm properties")
    void testAlgorithmProperties() {
        assertEquals("Selection Sort", selectionSort.getAlgorithmName());
        assertEquals("O(n²)", selectionSort.getTimeComplexity());
        assertEquals("O(1)", selectionSort.getSpaceComplexity());
        assertFalse(selectionSort.isStable()); // Selection sort is not stable
        assertTrue(selectionSort.isInPlace());
    }
    
    @Test
    @DisplayName("Test consistent comparison count")
    void testConsistentComparisons() {
        int[] array1 = {1, 2, 3, 4, 5}; // Sorted
        int[] array2 = {5, 4, 3, 2, 1}; // Reverse sorted
        
        BenchmarkUtils.resetCounters();
        selectionSort.sort(array1);
        long comparisons1 = BenchmarkUtils.getComparisonCount();
        
        BenchmarkUtils.resetCounters();
        selectionSort.sort(array2);
        long comparisons2 = BenchmarkUtils.getComparisonCount();
        
        // Selection sort should make same number of comparisons regardless of input
        assertEquals(comparisons1, comparisons2);
        System.out.println("Selection Sort consistent comparisons: " + comparisons1);
    }
}
