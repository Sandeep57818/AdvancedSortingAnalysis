package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;

/**
 * Implementation of Quick Sort algorithm with median-of-three pivot selection.
 * 
 * Quick Sort is a divide-and-conquer algorithm that works by selecting a 'pivot'
 * element and partitioning the array around the pivot such that elements smaller
 * than pivot come before it and elements greater come after it.
 * 
 * Median-of-three pivot selection improves performance by choosing the median
 * of first, middle, and last elements as the pivot, reducing worst-case scenarios.
 * 
 * Time Complexity:
 * - Best Case: O(n log n) - when pivot divides array into equal halves
 * - Average Case: O(n log n) - with random pivot selection
 * - Worst Case: O(n²) - when pivot is always smallest/largest (rare with median-of-three)
 * 
 * Space Complexity: O(log n) average case due to recursion stack
 * Stability: Not stable - equal elements may change relative order
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-08-23
 */
public class QuickSort implements SortingAlgorithm {
    
    /**
     * Entry point for Quick Sort algorithm.
     * 
     * @param array Array to be sorted
     * @throws IllegalArgumentException if array is null
     */
    @Override
    public void sort(int[] array) {
        BenchmarkUtils.validateArray(array);
        
        if (array.length <= 1) return;
        
        // Start recursive sorting
        quickSort(array, 0, array.length - 1);
    }
    
    /**
     * Recursive Quick Sort implementation.
     * 
     * @param array Array to sort
     * @param low Starting index
     * @param high Ending index
     */
    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            // Partition array and get pivot index
            int pivotIndex = partitionWithMedianOfThree(array, low, high);
            
            // Recursively sort elements before and after partition
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }
    
    /**
     * Partitions array using median-of-three pivot selection.
     * This method significantly improves performance on partially sorted arrays.
     * 
     * @param array Array to partition
     * @param low Starting index
     * @param high Ending index
     * @return Index of pivot after partitioning
     */
    private int partitionWithMedianOfThree(int[] array, int low, int high) {
        // Choose median of first, middle, and last elements as pivot
        int mid = low + (high - low) / 2;
        
        // Sort the three elements: low, mid, high
        if (BenchmarkUtils.compare(array, mid, low)) {
            BenchmarkUtils.swap(array, mid, low);
        }
        if (BenchmarkUtils.compare(array, high, low)) {
            BenchmarkUtils.swap(array, high, low);
        }
        if (BenchmarkUtils.compare(array, high, mid)) {
            BenchmarkUtils.swap(array, high, mid);
        }
        
        // Now array[mid] is the median, place it at end as pivot
        BenchmarkUtils.swap(array, mid, high);
        
        // Perform standard partitioning with pivot at high
        return partition(array, low, high);
    }
    
    /**
     * Standard partitioning algorithm (Lomuto partition scheme).
     * 
     * @param array Array to partition
     * @param low Starting index
     * @param high Ending index (pivot location)
     * @return Final position of pivot
     */
    private int partition(int[] array, int low, int high) {
        int pivot = array[high]; // Pivot element
        int i = low - 1; // Index of smaller element
        
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (BenchmarkUtils.compare(pivot, array[j]) >= 0) {
                i++; // Increment index of smaller element
                BenchmarkUtils.swap(array, i, j);
            }
        }
        
        // Place pivot in its correct position
        BenchmarkUtils.swap(array, i + 1, high);
        return i + 1;
    }
    
    /**
     * Alternative implementation using Hoare partition scheme.
     * Generally more efficient with fewer swaps.
     */
    private int hoarePartition(int[] array, int low, int high) {
        int pivot = array[low]; // Choose first element as pivot
        int i = low - 1;
        int j = high + 1;
        
        while (true) {
            // Find element from left that should be on right
            do {
                i++;
            } while (BenchmarkUtils.compare(pivot, array[i]) > 0);
            
            // Find element from right that should be on left
            do {
                j--;
            } while (BenchmarkUtils.compare(array[j], pivot) > 0);
            
            // If elements crossed, partitioning is done
            if (i >= j) return j;
            
            // Swap elements
            BenchmarkUtils.swap(array, i, j);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Quick Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(log n)";
    }
    
    @Override
    public boolean isStable() {
        return false; // Quick sort is not stable
    }
    
    @Override
    public boolean isInPlace() {
        return true; // Uses only O(log n) extra space for recursion
    }
    
    @Override
    public String getBestCaseScenario() {
        return "Random array where pivot consistently divides array into equal halves";
    }
    
    @Override
    public String getWorstCaseScenario() {
        return "Already sorted or reverse sorted array with poor pivot selection (mitigated by median-of-three)";
    }
}
