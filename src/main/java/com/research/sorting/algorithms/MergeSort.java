package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;

/**
 * Implementation of Merge Sort algorithm.
 * 
 * Merge Sort is a stable, divide-and-conquer algorithm that divides the array
 * into two halves, recursively sorts them, and then merges the sorted halves.
 * 
 * Key advantages:
 * - Guaranteed O(n log n) performance in all cases
 * - Stable sorting (maintains relative order of equal elements)
 * - Predictable performance regardless of input distribution
 * 
 * Time Complexity:
 * - Best Case: O(n log n)
 * - Average Case: O(n log n)
 * - Worst Case: O(n log n)
 * 
 * Space Complexity: O(n) - requires additional space for merging
 * Stability: Stable - maintains relative order of equal elements
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-08-23
 */
public class MergeSort implements SortingAlgorithm {
    
    /**
     * Entry point for Merge Sort algorithm.
     * 
     * @param array Array to be sorted
     * @throws IllegalArgumentException if array is null
     */
    @Override
    public void sort(int[] array) {
        BenchmarkUtils.validateArray(array);
        
        if (array.length <= 1) return;
        
        // Start recursive sorting
        mergeSort(array, 0, array.length - 1);
    }
    
    /**
     * Recursive Merge Sort implementation.
     * 
     * Algorithm steps:
     * 1. Divide array into two halves
     * 2. Recursively sort both halves
     * 3. Merge the sorted halves
     * 
     * @param array Array to sort
     * @param left Starting index
     * @param right Ending index
     */
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            // Find middle point to divide array into two halves
            int mid = left + (right - left) / 2; // Prevents integer overflow
            
            // Recursively sort first and second halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            
            // Merge the sorted halves
            merge(array, left, mid, right);
        }
    }
    
    /**
     * Merges two sorted subarrays into one sorted array.
     * 
     * This is the core operation of merge sort that combines two sorted
     * subarrays: array[left...mid] and array[mid+1...right]
     * 
     * @param array Main array containing both subarrays
     * @param left Starting index of first subarray
     * @param mid Ending index of first subarray
     * @param right Ending index of second subarray
     */
    private void merge(int[] array, int left, int mid, int right) {
        // Calculate sizes of two subarrays to be merged
        int leftSize = mid - left + 1;
        int rightSize = right - mid;
        
        // Create temporary arrays for left and right subarrays
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];
        
        // Copy data to temporary arrays
        System.arraycopy(array, left, leftArray, 0, leftSize);
        System.arraycopy(array, mid + 1, rightArray, 0, rightSize);
        
        // Merge the temporary arrays back into array[left...right]
        int i = 0; // Initial index of left subarray
        int j = 0; // Initial index of right subarray
        int k = left; // Initial index of merged subarray
        
        // Merge elements while both arrays have elements
        while (i < leftSize && j < rightSize) {
            // Compare elements and choose smaller one
            if (BenchmarkUtils.compare(leftArray[i], rightArray[j]) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }
        
        // Copy remaining elements of leftArray, if any
        while (i < leftSize) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        
        // Copy remaining elements of rightArray, if any
        while (j < rightSize) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
    
    /**
     * Optimized merge sort with insertion sort for small arrays.
     * Uses insertion sort for arrays smaller than threshold for better performance.
     */
    public void optimizedSort(int[] array, int threshold) {
        BenchmarkUtils.validateArray(array);
        if (array.length <= 1) return;
        
        optimizedMergeSort(array, 0, array.length - 1, threshold);
    }
    
    /**
     * Hybrid merge sort that switches to insertion sort for small subarrays.
     */
    private void optimizedMergeSort(int[] array, int left, int right, int threshold) {
        if (left < right) {
            if (right - left + 1 <= threshold) {
                // Use insertion sort for small arrays
                insertionSort(array, left, right);
            } else {
                int mid = left + (right - left) / 2;
                optimizedMergeSort(array, left, mid, threshold);
                optimizedMergeSort(array, mid + 1, right, threshold);
                merge(array, left, mid, right);
            }
        }
    }
    
    /**
     * Insertion sort for small subarrays in hybrid approach.
     */
    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            int j = i - 1;
            
            while (j >= left && BenchmarkUtils.compare(array[j], key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Merge Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }
    
    @Override
    public boolean isStable() {
        return true; // Merge sort is stable
    }
    
    @Override
    public boolean isInPlace() {
        return false; // Requires O(n) extra space
    }
    
    @Override
    public String getBestCaseScenario() {
        return "Any input - merge sort has consistent O(n log n) performance";
    }
    
    @Override
    public String getWorstCaseScenario() {
        return "Any input - merge sort has consistent O(n log n) performance";
    }
}
