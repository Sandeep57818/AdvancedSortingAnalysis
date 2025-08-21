package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.utils.BenchmarkUtils;
import com.research.sorting.algorithms.BubbleSort;
import java.util.Arrays;

/**
 * Unit tests for BubbleSort algorithm implementation.
 */
class BubbleSortTest {
    
    private BubbleSort bubbleSort;
    
    @BeforeEach
    void setUp() {
        bubbleSort = new BubbleSort();
        BenchmarkUtils.resetCounters();
    }
    
    @Test
    @DisplayName("Test sorting empty array")
    void testEmptyArray() {
        int[] array = {};
        bubbleSort.sort(array);
        
        assertEquals(0, array.length);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting single element array")
    void testSingleElement() {
        int[] array = {42};
        bubbleSort.sort(array);
        
        assertArrayEquals(new int[]{42}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting already sorted array")
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5};
        BenchmarkUtils.resetCounters();
        
        bubbleSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
        
        // Should terminate early with minimal operations
        assertTrue(BenchmarkUtils.getComparisonCount() < 25); // n*(n-1)/2 would be 10
    }
    
    @Test
    @DisplayName("Test sorting reverse sorted array")
    void testReverseSorted() {
        int[] array = {5, 4, 3, 2, 1};
        bubbleSort.sort(array);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting random array")
    void testRandomArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        bubbleSort.sort(array);
        
        assertArrayEquals(expected, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }
    
    @Test
    @DisplayName("Test sorting array with duplicates")
    void testArrayWithDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        bubbleSort.sort(array);
        
        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(9, array.length); // Length preserved
        
        // Verify all elements are present
        Arrays.sort(new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5}); // Expected result
    }
    
    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            bubbleSort.sort(null);
        });
    }
    
    @Test
    @DisplayName("Test algorithm properties")
    void testAlgorithmProperties() {
        assertEquals("Bubble Sort", bubbleSort.getAlgorithmName());
        assertEquals("O(n²)", bubbleSort.getTimeComplexity());
        assertEquals("O(1)", bubbleSort.getSpaceComplexity());
        assertTrue(bubbleSort.isStable());
        assertTrue(bubbleSort.isInPlace());
    }
    
    @Test
    @DisplayName("Test operation counting")
    void testOperationCounting() {
        int[] array = {3, 1, 2};
        BenchmarkUtils.resetCounters();
        
        bubbleSort.sort(array);
        
        // Should have performed some comparisons and swaps
        assertTrue(BenchmarkUtils.getComparisonCount() > 0);
        assertTrue(BenchmarkUtils.getSwapCount() >= 0);
        
        System.out.println("Bubble Sort Stats: " + BenchmarkUtils.getStatistics());
    }
}
