package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;

/**
 * Implementation of Insertion Sort algorithm.
 * 
 * Insertion Sort builds the final sorted array one item at a time.
 * It is efficient for small data sets and nearly sorted arrays.
 * 
 * Algorithm works similar to sorting playing cards:
 * - Take one element from unsorted part
 * - Insert it into correct position in sorted part
 * - Repeat until all elements are processed
 * 
 * Time Complexity:
 * - Best Case: O(n) - when array is already sorted
 * - Average Case: O(n²)
 * - Worst Case: O(n²) - when array is reverse sorted
 * 
 * Space Complexity: O(1) - sorts in-place
 * Stability: Stable - maintains relative order of equal elements
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-08-22
 */
public class InsertionSort implements SortingAlgorithm {
    
    /**
     * Sorts the array using insertion sort algorithm.
     * 
     * Algorithm steps:
     * 1. Start with second element (first is considered sorted)
     * 2. Compare current element with sorted elements
     * 3. Shift larger elements one position ahead
     * 4. Insert current element at correct position
     * 5. Repeat for all elements
     * 
     * @param array Array to be sorted
     * @throws IllegalArgumentException if array is null
     */
    @Override
    public void sort(int[] array) {
        // Validate input
        BenchmarkUtils.validateArray(array);
        
        int n = array.length;
        
        // Handle trivial cases
        if (n <= 1) return;
        
        // Insertion sort implementation
        for (int i = 1; i < n; i++) {
            int currentElement = array[i];  // Element to be inserted
            int j = i - 1;  // Index of last element in sorted part
            
            // Shift elements of sorted part that are greater than currentElement
            // to one position ahead of their current position
            while (j >= 0 && BenchmarkUtils.compare(array[j], currentElement) > 0) {
                array[j + 1] = array[j];  // Shift element to right
                j = j - 1;  // Move to previous element
                
                // Count this as a comparison and implicit swap
                // Note: This is not a direct swap but element movement
                // We'll count it as a swap for benchmarking consistency
                BenchmarkUtils.swap(array, j + 1, j + 2);
            }
            
            // Insert the current element at its correct position
            array[j + 1] = currentElement;
        }
    }
    
    /**
     * Alternative implementation with explicit swap counting.
     * This version uses actual swaps for clearer benchmark metrics.
     */
    public void sortWithExplicitSwaps(int[] array) {
        BenchmarkUtils.validateArray(array);
        
        int n = array.length;
        if (n <= 1) return;
        
        for (int i = 1; i < n; i++) {
            int j = i;
            
            // Bubble the element to its correct position
            while (j > 0 && BenchmarkUtils.compare(array, j - 1, j)) {
                BenchmarkUtils.swap(array, j - 1, j);
                j--;
            }
        }
    }
    
    /**
     * Returns the name of this sorting algorithm.
     * 
     * @return Algorithm name as string
     */
    @Override
    public String getAlgorithmName() {
        return "Insertion Sort";
    }
    
    /**
     * Returns the time complexity of insertion sort.
     * 
     * @return Time complexity string
     */
    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }
    
    /**
     * Returns the space complexity of insertion sort.
     * 
     * @return Space complexity string
     */
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
    
    /**
     * Insertion sort is stable - maintains relative order of equal elements.
     * 
     * @return true indicating stability
     */
    @Override
    public boolean isStable() {
        return true;
    }
    
    /**
     * Insertion sort is in-place - uses constant extra memory.
     * 
     * @return true indicating in-place sorting
     */
    @Override
    public boolean isInPlace() {
        return true;
    }
    
    /**
     * Best case occurs when array is already sorted.
     * 
     * @return Best case scenario description
     */
    @Override
    public String getBestCaseScenario() {
        return "Already sorted array - O(n) comparisons, no swaps needed";
    }
    
    /**
     * Worst case occurs when array is reverse sorted.
     * 
     * @return Worst case scenario description
     */
    @Override
    public String getWorstCaseScenario() {
        return "Reverse sorted array - maximum comparisons and shifts";
    }
}
