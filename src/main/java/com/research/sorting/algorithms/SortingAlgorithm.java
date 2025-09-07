package com.research.sorting.algorithms;

public interface SortingAlgorithm {
    
    /**
     * Sorts the given array in ascending order.
     * This method should modify the array in-place and track
     * performance metrics during execution.
     * 
     * @param array The array to be sorted
     * @throws IllegalArgumentException if array is null
     */
    void sort(int[] array);
    
    /**
     * Returns the name of the sorting algorithm.
     * Used for identification in benchmarking and reporting.
     * 
     * @return String representation of algorithm name
     */
    String getAlgorithmName();
    
    /**
     * Returns the theoretical time complexity of the algorithm.
     * Used for complexity analysis and comparison.
     * 
     * @return Time complexity string (e.g., "O(n²)", "O(n log n)")
     */
    String getTimeComplexity();
    
    /**
     * Returns the theoretical space complexity of the algorithm.
     * Used for memory usage analysis and comparison.
     * 
     * @return Space complexity string (e.g., "O(1)", "O(n)")
     */
    String getSpaceComplexity();
    
    /**
     * Indicates whether the algorithm is stable.
     * Stable algorithms maintain relative order of equal elements.
     * 
     * @return true if algorithm is stable, false otherwise
     */
    boolean isStable();
    
    /**
     * Indicates whether the algorithm sorts in-place.
     * In-place algorithms use O(1) extra memory.
     * 
     * @return true if algorithm sorts in-place, false otherwise
     */
    boolean isInPlace();
    
    /**
     * Returns the best-case time complexity scenario description.
     * 
     * @return Description of best-case input scenario
     */
    default String getBestCaseScenario() {
        return "Already sorted array";
    }
    
    /**
     * Returns the worst-case time complexity scenario description.
     * 
     * @return Description of worst-case input scenario
     */
    default String getWorstCaseScenario() {
        return "Reverse sorted array";
    }
}
