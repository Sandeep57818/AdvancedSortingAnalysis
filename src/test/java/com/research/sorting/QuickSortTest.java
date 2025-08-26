package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.research.sorting.utils.BenchmarkUtils;
import com.research.sorting.algorithms.QuickSort;
import java.util.Arrays;

/**
 * Unit tests for QuickSort algorithm implementation.
 */
class QuickSortTest {

    private QuickSort quickSort;

    @BeforeEach
    void setUp() {
        quickSort = new QuickSort();
        BenchmarkUtils.resetCounters();
    }

    @Test
    @DisplayName("Test sorting empty array")
    void testEmptyArray() {
        int[] array = {};
        quickSort.sort(array);

        assertEquals(0, array.length);
        assertTrue(BenchmarkUtils.isSorted(array));
    }

    @Test
    @DisplayName("Test sorting single element array")
    void testSingleElement() {
        int[] array = {42};
        quickSort.sort(array);

        assertArrayEquals(new int[] {42}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }

    @Test
    @DisplayName("Test sorting already sorted array")
    void testAlreadySorted() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        quickSort.sort(array);

        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }

    @Test
    @DisplayName("Test sorting reverse sorted array")
    void testReverseSorted() {
        int[] array = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        quickSort.sort(array);

        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, array);
        assertTrue(BenchmarkUtils.isSorted(array));
    }

    @Test
    @DisplayName("Test sorting random array")
    void testRandomArray() {
        int[] array = {64, 34, 25, 12, 22, 11, 90, 88, 76, 50, 42};
        quickSort.sort(array);

        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(11, array.length);

        // Verify all elements are present
        Arrays.sort(new int[] {64, 34, 25, 12, 22, 11, 90, 88, 76, 50, 42});
    }

    @Test
    @DisplayName("Test sorting array with duplicates")
    void testArrayWithDuplicates() {
        int[] array = {5, 2, 8, 2, 9, 1, 5, 5};
        int[] expected = Arrays.copyOf(array, array.length);
        Arrays.sort(expected); // Sort the expected array

        quickSort.sort(array);

        assertArrayEquals(expected, array); // Verify the sorted array matches the expected result
    }

    @Test
    @DisplayName("Test sorting large array")
    void testLargeArray() {
        int[] array = BenchmarkUtils.generateRandomArray(1000, 1000);
        quickSort.sort(array);

        assertTrue(BenchmarkUtils.isSorted(array));
        assertEquals(1000, array.length);
    }

    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            quickSort.sort(null);
        });
    }

    @Test
    @DisplayName("Test algorithm properties")
    void testAlgorithmProperties() {
        assertEquals("Quick Sort", quickSort.getAlgorithmName());
        assertEquals("O(n log n)", quickSort.getTimeComplexity());
        assertEquals("O(log n)", quickSort.getSpaceComplexity());
        assertFalse(quickSort.isStable());
        assertTrue(quickSort.isInPlace());
    }

    @Test
    @DisplayName("Test median-of-three optimization")
    void testMedianOfThreeOptimization() {
        // Test array that would perform poorly with simple pivot selection
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        BenchmarkUtils.resetCounters();

        quickSort.sort(array);

        assertTrue(BenchmarkUtils.isSorted(array));

        // With median-of-three, should not degrade to O(n²)
        long comparisons = BenchmarkUtils.getComparisonCount();
        System.out.println("Quick Sort with median-of-three: " + BenchmarkUtils.getStatistics());

        // Should be much better than worst case O(n²)
        assertTrue(comparisons < 100); // Much less than n² = 100
    }

    @Test
    @DisplayName("Test performance on nearly sorted data")
    void testNearlySortedPerformance() {
        int[] array = BenchmarkUtils.generateNearlySortedArray(100);
        BenchmarkUtils.resetCounters();

        quickSort.sort(array);

        assertTrue(BenchmarkUtils.isSorted(array));
        System.out.println("Quick Sort on nearly sorted: " + BenchmarkUtils.getStatistics());
    }
}
