package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;

public class BubbleSort implements SortingAlgorithm {
    
    /**
     * Sorts the array using bubble sort algorithm.
     * 
     * Algorithm steps:
     * 1. Compare adjacent elements
     * 2. Swap if left > right
     * 3. Continue until no swaps needed
     * 4. Optimize: break early if no swaps in a pass
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
        
        // Bubble sort implementation with early termination optimization
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;  // Flag to detect if any swap occurred
            
            // Last i elements are already in place, so reduce comparison range
            for (int j = 0; j < n - i - 1; j++) {
                // Compare adjacent elements using BenchmarkUtils to count comparisons
                if (BenchmarkUtils.compare(array, j, j + 1)) {
                    // Swap elements using BenchmarkUtils to count swaps
                    BenchmarkUtils.swap(array, j, j + 1);
                    swapped = true;
                }
            }
            
            // Optimization: If no swapping occurred, array is sorted
            if (!swapped) {
                break;
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
        return "Bubble Sort";
    }
    
    /**
     * Returns the time complexity of bubble sort.
     * 
     * @return Time complexity string
     */
    @Override
    public String getTimeComplexity() {
        return "O(n²)";
    }
    
    /**
     * Returns the space complexity of bubble sort.
     * 
     * @return Space complexity string
     */
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
    
    /**
     * Bubble sort is stable - maintains relative order of equal elements.
     * 
     * @return true indicating stability
     */
    @Override
    public boolean isStable() {
        return true;
    }
    
    /**
     * Bubble sort is in-place - uses constant extra memory.
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
        return "Already sorted array - O(n) comparisons with early termination";
    }
    
    /**
     * Worst case occurs when array is reverse sorted.
     * 
     * @return Worst case scenario description
     */
    @Override
    public String getWorstCaseScenario() {
        return "Reverse sorted array - maximum swaps and comparisons";
    }
}
