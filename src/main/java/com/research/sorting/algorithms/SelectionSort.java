package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;

public class SelectionSort implements SortingAlgorithm {
    
    /**
     * Sorts the array using selection sort algorithm.
     * 
     * Algorithm steps:
     * 1. Find minimum element in unsorted array
     * 2. Swap it with first element of unsorted part
     * 3. Move boundary of sorted and unsorted parts
     * 4. Repeat until entire array is sorted
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
        
        // Selection sort implementation
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in remaining unsorted array
            int minIndex = i;
            
            // Search for minimum element in unsorted part
            for (int j = i + 1; j < n; j++) {
                // Compare current element with current minimum
                if (BenchmarkUtils.compare(array, minIndex, j)) {
                    minIndex = j;  // Update minimum index
                }
            }
            
            // Swap the found minimum element with the first element of unsorted part
            // Note: Only swap if minimum is not already in correct position
            if (minIndex != i) {
                BenchmarkUtils.swap(array, i, minIndex);
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
        return "Selection Sort";
    }
    
    /**
     * Returns the time complexity of selection sort.
     * 
     * @return Time complexity string
     */
    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }
    
    /**
     * Returns the space complexity of selection sort.
     * 
     * @return Space complexity string
     */
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
    
    /**
     * Selection sort is not stable in typical implementation.
     * Equal elements may change their relative order.
     * 
     * @return false indicating instability
     */
    @Override
    public boolean isStable() {
        return false;
    }
    
    /**
     * Selection sort is in-place - uses constant extra memory.
     * 
     * @return true indicating in-place sorting
     */
    @Override
    public boolean isInPlace() {
        return true;
    }
    
    /**
     * Selection sort always performs O(n²) comparisons regardless of input.
     * 
     * @return Best case scenario description
     */
    @Override
    public String getBestCaseScenario() {
        return "No best case - always O(n²) comparisons, but fewer swaps if partially sorted";
    }
    
    /**
     * Worst case occurs when maximum swaps are needed.
     * 
     * @return Worst case scenario description
     */
    @Override
    public String getWorstCaseScenario() {
        return "Reverse sorted array - maximum number of swaps required";
    }
}
