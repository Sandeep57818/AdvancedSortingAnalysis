package com.research.sorting.utils;

import java.util.Arrays;
import java.util.Random;

public class BenchmarkUtils {
    
    /** Global counter for comparison operations */
    private static long comparisonCount = 0;
    
    /** Global counter for swap operations */
    private static long swapCount = 0;
    
    /** Random number generator with fixed seed for reproducibility */
    private static final Random RANDOM = new Random(42);
    
    /** Thread-local storage for operation counting in multi-threaded environments */
    private static final ThreadLocal<Long> THREAD_COMPARISON_COUNT = 
        ThreadLocal.withInitial(() -> 0L);
    private static final ThreadLocal<Long> THREAD_SWAP_COUNT = 
        ThreadLocal.withInitial(() -> 0L);
    
    /**
     * Resets all operation counters to zero.
     * Should be called before each benchmark run.
     */
    public static void resetCounters() {
        comparisonCount = 0;
        swapCount = 0;
        THREAD_COMPARISON_COUNT.set(0L);
        THREAD_SWAP_COUNT.set(0L);
    }
    
    /**
     * Compares two array elements and increments comparison counter.
     * 
     * @param arr The array containing elements to compare
     * @param i Index of first element
     * @param j Index of second element
     * @return true if arr[i] > arr[j], false otherwise
     */
    public static boolean compare(int[] arr, int i, int j) {
        comparisonCount++;
        THREAD_COMPARISON_COUNT.set(THREAD_COMPARISON_COUNT.get() + 1);
        return arr[i] > arr[j];
    }
    
    /**
     * Compares two integer values and increments comparison counter.
     * 
     * @param a First value
     * @param b Second value
     * @return positive if a > b, negative if a < b, zero if equal
     */
    public static int compare(int a, int b) {
        comparisonCount++;
        THREAD_COMPARISON_COUNT.set(THREAD_COMPARISON_COUNT.get() + 1);
        return Integer.compare(a, b);
    }
    
    /**
     * Swaps two elements in an array and increments swap counter.
     * 
     * @param arr The array containing elements to swap
     * @param i Index of first element
     * @param j Index of second element
     */
    public static void swap(int[] arr, int i, int j) {
        if (i != j) {  // Only count actual swaps
            swapCount++;
            THREAD_SWAP_COUNT.set(THREAD_SWAP_COUNT.get() + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    
    /**
     * Returns current comparison count.
     * 
     * @return Number of comparisons performed
     */
    public static long getComparisonCount() {
        return comparisonCount;
    }
    
    /**
     * Returns current swap count.
     * 
     * @return Number of swaps performed
     */
    public static long getSwapCount() {
        return swapCount;
    }
    
    /**
     * Measures current memory usage of the JVM.
     * Forces garbage collection before measurement for accuracy.
     * 
     * @return Memory usage in bytes
     */
    public static long measureMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // Suggest garbage collection for more accurate measurement
        System.gc();
        
        // Small delay to allow GC to complete
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Creates a deep copy of an integer array.
     * Essential for fair benchmarking with identical inputs.
     * 
     * @param original The array to copy
     * @return A new array with identical contents
     */
    public static int[] copyArray(int[] original) {
        if (original == null) return null;
        return Arrays.copyOf(original, original.length);
    }
    
    /**
     * Verifies that an array is sorted in ascending order.
     * Used for correctness validation after sorting.
     * 
     * @param arr The array to verify
     * @return true if array is sorted, false otherwise
     */
    public static boolean isSorted(int[] arr) {
        if (arr == null || arr.length <= 1) return true;
        
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i-1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Generates a random array with specified size.
     * Uses fixed seed for reproducible results.
     * 
     * @param size Number of elements in array
     * @param maxValue Maximum value for array elements
     * @return Array filled with random integers
     */
    public static int[] generateRandomArray(int size, int maxValue) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(maxValue);
        }
        return array;
    }
    
    /**
     * Generates a sorted array with specified size.
     * 
     * @param size Number of elements in array
     * @return Array sorted in ascending order
     */
    public static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }
        return array;
    }
    
    /**
     * Generates a reverse-sorted array with specified size.
     * 
     * @param size Number of elements in array
     * @return Array sorted in descending order
     */
    public static int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
    
    /**
     * Provides formatted output of current benchmark statistics.
     * 
     * @return Formatted string with comparison and swap counts
     */
    public static String getStatistics() {
        return String.format("Comparisons: %,d | Swaps: %,d", 
                           comparisonCount, swapCount);
    }
    
    /**
     * Validates array input for sorting operations.
     * 
     * @param arr Array to validate
     * @throws IllegalArgumentException if array is null
     */
    public static void validateArray(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
    }

    public static int[] generateNearlySortedArray(int size) {
    int[] arr = generateSortedArray(size); // Reuse your existing sorted array generator
    java.util.Random rand = new java.util.Random(42); // Fixed seed for reproducibility
    int swaps = Math.max(1, size / 10); // Make 10% of elements "unsorted"
    for (int i = 0; i < swaps; i++) {
        int idx1 = rand.nextInt(size);
        int idx2 = rand.nextInt(size);
        // Swap two random positions
        int temp = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2] = temp;
    }
    return arr;
}

}

