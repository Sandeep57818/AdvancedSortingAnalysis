package com.research.sorting.algorithms;

import com.research.sorting.utils.BenchmarkUtils;


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
        if (n <= 1)
            return;

        // Step 1: Build max heap from array
        buildMaxHeap(array, n);

        // Step 2: Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            BenchmarkUtils.swap(array, 0, i);
            heapify(array, i, 0); // Use heapify instead of iterativeHeapify
        }
    }

    /**
     * Builds a max heap from an unsorted array.
     * 
     * A max heap is a complete binary tree where each parent node is greater than or equal to its
     * children.
     * 
     * @param array Array to convert to heap
     * @param heapSize Size of heap
     */
    private void buildMaxHeap(int[] array, int heapSize) {
        for (int i = (heapSize / 2) - 1; i >= 0; i--) {
            heapify(array, heapSize, i); // Use heapify instead of iterativeHeapify
        }
    }

    /**
     * Maintains the max heap property for a subtree rooted at given index.
     * 
     * This method assumes that the binary trees rooted at left and right children of index are max
     * heaps, but array[index] might violate the max heap property.
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
    /**
 * Verifies if the array up to heapSize satisfies max-heap property.
 * @param array    The array to check
 * @param heapSize The number of heap elements to check
 * @return true if it's a max heap, false otherwise
 */
public boolean isMaxHeap(int[] array, int heapSize) {
    // Check all parent nodes (last parent is at (heapSize/2)-1)
    for (int i = 0; i <= (heapSize / 2) - 1; i++) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < heapSize && array[i] < array[left]) {
            return false;
        }
        if (right < heapSize && array[i] < array[right]) {
            return false;
        }
    }
    return true;
}

}
