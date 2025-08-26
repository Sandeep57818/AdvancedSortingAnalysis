package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;

/**
 * Implementation of Heap Sort algorithm.
 * 
 * Heap Sort is a comparison-based sorting algorithm that uses a binary heap
 * data structure. It divides input into sorted and unsorted regions, and
 * iteratively shrinks the unsorted region by extracting the largest element
 * and moving it to the sorted region.
 * 
 * Algorithm overview:
 * 1. Build a max heap from the input array
 * 2. Repeatedly extract maximum element and place it at the end
 * 3. Restore heap property after each extraction
 * 
 * Time Complexity:
 * - Best Case: O(n log n)
 * - Average Case: O(n log n)
 * - Worst Case: O(n log n)
 * 
 * Space Complexity: O(1) - sorts in-place
 * Stability: Not stable - equal elements may change relative order
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-08-23
 */
public class HeapSort implements SortingAlgorithm {
    
    /**
     * Entry point for Heap Sort algorithm.
     * 
     * @param array Array to be sorted
     * @throws IllegalArgumentException if array is null
     */
    @Override
    public void sort(int[] array) {
        BenchmarkUtils.validateArray(array);
        
        int n = array.length;
        if (n <= 1) return;
        
        // Step 1: Build max heap from array
        buildMaxHeap(array, n);
        
        // Step 2: Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root (maximum element) to end
            BenchmarkUtils.swap(array, 0, i);
            
            // Call heapify on reduced heap (exclude sorted elements)
            heapify(array, i, 0);
        }
    }
    
    /**
     * Builds a max heap from an unsorted array.
     * 
     * A max heap is a complete binary tree where each parent node
     * is greater than or equal to its children.
     * 
     * @param array Array to convert to heap
     * @param heapSize Size of heap
     */
    private void buildMaxHeap(int[] array, int heapSize) {
        // Start from last non-leaf node and heapify each node
        // Last non-leaf node index = (heapSize / 2) - 1
        for (int i = (heapSize / 2) - 1; i >= 0; i--) {
            heapify(array, heapSize, i);
        }
    }
    
    /**
     * Maintains the max heap property for a subtree rooted at given index.
     * 
     * This method assumes that the binary trees rooted at left and right
     * children of index are max heaps, but array[index] might violate
     * the max heap property.
     * 
     * @param array Array representing the heap
     * @param heapSize Size of heap
     * @param rootIndex Root index of subtree to heapify
     */
    private void heapify(int[] array, int heapSize, int rootIndex) {
        int largest = rootIndex; // Initialize largest as root
        int leftChild = 2 * rootIndex + 1; // Left child index
        int rightChild = 2 * rootIndex + 2; // Right child index
        
        // Check if left child exists and is greater than root
        if (leftChild < heapSize && BenchmarkUtils.compare(array, leftChild, largest)) {
            largest = leftChild;
        }
        
        // Check if right child exists and is greater than current largest
        if (rightChild < heapSize && BenchmarkUtils.compare(array, rightChild, largest)) {
            largest = rightChild;
        }
        
        // If largest is not root, swap and continue heapifying
        if (largest != rootIndex) {
            BenchmarkUtils.swap(array, rootIndex, largest);
            
            // Recursively heapify the affected subtree
            heapify(array, heapSize, largest);
        }
    }
    
    /**
     * Alternative iterative heapify implementation.
     * May be more efficient for deep heaps by avoiding recursion overhead.
     */
    private void iterativeHeapify(int[] array, int heapSize, int rootIndex) {
        while (true) {
            int largest = rootIndex;
            int leftChild = 2 * rootIndex + 1;
            int rightChild = 2 * rootIndex + 2;
            
            // Find largest among root and children
            if (leftChild < heapSize && BenchmarkUtils.compare(array, leftChild, largest)) {
                largest = leftChild;
            }
            
            if (rightChild < heapSize && BenchmarkUtils.compare(array, rightChild, largest)) {
                largest = rightChild;
            }
            
            // If root is largest, heap property is satisfied
            if (largest == rootIndex) break;
            
            // Otherwise, swap and continue with affected subtree
            BenchmarkUtils.swap(array, rootIndex, largest);
            rootIndex = largest;
        }
    }
    
    /**
     * Utility method to verify heap property for testing.
     * 
     * @param array Array to check
     * @param heapSize Size of heap
     * @return true if array satisfies max heap property
     */
    public boolean isMaxHeap(int[] array, int heapSize) {
        // Check all non-leaf nodes
        for (int i = 0; i <= (heapSize / 2) - 1; i++) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            
            // Check left child
            if (leftChild < heapSize && array[i] < array[leftChild]) {
                return false;
            }
            
            // Check right child
            if (rightChild < heapSize && array[i] < array[rightChild]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Heap Sort";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
    
    @Override
    public boolean isStable() {
        return false; // Heap sort is not stable
    }
    
    @Override
    public boolean isInPlace() {
        return true; // Sorts in-place using only O(1) extra space
    }
    
    @Override
    public String getBestCaseScenario() {
        return "Any input - heap sort has consistent O(n log n) performance";
    }
    
    @Override
    public String getWorstCaseScenario() {
        return "Any input - heap sort has consistent O(n log n) performance";
    }
}
