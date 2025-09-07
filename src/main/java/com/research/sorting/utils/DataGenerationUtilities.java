package com.research.sorting.utils;

import java.util.Random;

public class DataGenerationUtilities {
    
    /** Fixed seed for reproducible random number generation */
    private static final long RANDOM_SEED = 42L;
    
    /** Random number generator with fixed seed */
    private static final Random RANDOM = new Random(RANDOM_SEED);
    
    /**
     * Generates array filled with random integers.
     * Values are uniformly distributed between 1 and maxValue.
     * 
     * @param size Array size
     * @param maxValue Maximum value for array elements (exclusive)
     * @return Array with random integers
     */
    public static int[] generateRandomArray(int size, int maxValue) {
        validateSize(size);
        
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(maxValue) + 1; // Values from 1 to maxValue
        }
        return array;
    }
    
    /**
     * Generates array sorted in ascending order.
     * Best case scenario for many algorithms.
     * 
     * @param size Array size
     * @return Sorted array [1, 2, 3, ..., size]
     */
    public static int[] generateSortedArray(int size) {
        validateSize(size);
        
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }
        return array;
    }
    
    /**
     * Generates array sorted in descending order.
     * Worst case scenario for many comparison-based algorithms.
     * 
     * @param size Array size
     * @return Reverse sorted array [size, size-1, ..., 2, 1]
     */
    public static int[] generateReverseSortedArray(int size) {
        validateSize(size);
        
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }
    
    /**
     * Generates nearly sorted array with specified disorder percentage.
     * Creates sorted array then performs random swaps.
     * 
     * @param size Array size
     * @param disorderPercentage Percentage of elements to be out of order (0-100)
     * @return Nearly sorted array
     */
    public static int[] generateNearlySortedArray(int size, double disorderPercentage) {
        validateSize(size);
        validatePercentage(disorderPercentage);
        
        int[] array = generateSortedArray(size);
        int swapCount = (int) Math.max(1, size * disorderPercentage / 100.0);
        
        for (int i = 0; i < swapCount; i++) {
            int index1 = RANDOM.nextInt(size);
            int index2 = RANDOM.nextInt(size);
            
            // Swap elements
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
        
        return array;
    }
    
    /**
     * Generates nearly sorted array with default 10% disorder.
     * Convenience method for standard nearly-sorted testing.
     * 
     * @param size Array size
     * @return Nearly sorted array with 10% disorder
     */
    public static int[] generateNearlySortedArray(int size) {
        return generateNearlySortedArray(size, 10.0);
    }
    
    /**
     * Generates array with many duplicate values.
     * Tests algorithm behavior with equal elements.
     * 
     * @param size Array size
     * @param uniquePercentage Percentage of unique values (1-100)
     * @return Array with specified duplicate density
     */
    public static int[] generateArrayWithDuplicates(int size, double uniquePercentage) {
        validateSize(size);
        validatePercentage(uniquePercentage);
        
        int uniqueCount = Math.max(1, (int) (size * uniquePercentage / 100.0));
        int[] array = new int[size];
        
        for (int i = 0; i < size; i++) {
            array[i] = RANDOM.nextInt(uniqueCount) + 1;
        }
        
        return array;
    }
    
    /**
     * Generates array with default 10% unique values (heavy duplicates).
     * 
     * @param size Array size
     * @return Array with heavy duplicate concentration
     */
    public static int[] generateArrayWithDuplicates(int size) {
        return generateArrayWithDuplicates(size, 10.0);
    }
    
    /**
     * Generates array with alternating high-low pattern.
     * Creates challenging pattern for some algorithms.
     * 
     * @param size Array size
     * @return Array with alternating pattern [1, size, 2, size-1, ...]
     */
    public static int[] generateAlternatingArray(int size) {
        validateSize(size);
        
        int[] array = new int[size];
        boolean low = true;
        int lowValue = 1;
        int highValue = size;
        
        for (int i = 0; i < size; i++) {
            if (low) {
                array[i] = lowValue++;
            } else {
                array[i] = highValue--;
            }
            low = !low;
        }
        
        return array;
    }
    
    /**
     * Generates array based on normal (Gaussian) distribution.
     * More realistic data distribution for many real-world scenarios.
     * 
     * @param size Array size
     * @param mean Distribution mean
     * @param stdDev Standard deviation
     * @return Array with normally distributed values
     */
    public static int[] generateNormalDistributionArray(int size, double mean, double stdDev) {
        validateSize(size);
        
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            double value = RANDOM.nextGaussian() * stdDev + mean;
            array[i] = Math.max(1, (int) Math.round(value)); // Ensure positive values
        }
        
        return array;
    }
    
    /**
     * Generates array with specific pattern for algorithm testing.
     * 
     * @param size Array size
     * @param pattern Pattern type ("MOUNTAIN", "VALLEY", "SAWTOOTH", "PIPE_ORGAN")
     * @return Array with specified pattern
     */
    public static int[] generatePatternArray(int size, String pattern) {
        validateSize(size);
        
        int[] array = new int[size];
        
        switch (pattern.toUpperCase()) {
            case "MOUNTAIN":
                // Ascending then descending
                int mid = size / 2;
                for (int i = 0; i < mid; i++) {
                    array[i] = i + 1;
                }
                for (int i = mid; i < size; i++) {
                    array[i] = size - i;
                }
                break;
                
            case "VALLEY":
                // Descending then ascending
                mid = size / 2;
                for (int i = 0; i < mid; i++) {
                    array[i] = mid - i;
                }
                for (int i = mid; i < size; i++) {
                    array[i] = i - mid + 1;
                }
                break;
                
            case "SAWTOOTH":
                // Repeated ascending pattern
                int period = size / 10; // 10 periods
                for (int i = 0; i < size; i++) {
                    array[i] = (i % period) + 1;
                }
                break;
                
            case "PIPE_ORGAN":
                // Multiple peaks and valleys
                int quarterSize = size / 4;
                for (int i = 0; i < size; i++) {
                    int pos = i % quarterSize;
                    if ((i / quarterSize) % 2 == 0) {
                        array[i] = pos + 1; // Ascending
                    } else {
                        array[i] = quarterSize - pos; // Descending
                    }
                }
                break;
                
            default:
                // Default to random array
                return generateRandomArray(size, size);
        }
        
        return array;
    }
    
    /**
     * Creates a comprehensive test suite with various data types.
     * Returns map of data type name to generated array.
     * 
     * @param size Array size for all test cases
     * @return Map containing various test arrays
     */
    public static java.util.Map<String, int[]> generateComprehensiveTestSuite(int size) {
        java.util.Map<String, int[]> testSuite = new java.util.LinkedHashMap<>();
        
        testSuite.put("Random", generateRandomArray(size, size * 10));
        testSuite.put("Sorted", generateSortedArray(size));
        testSuite.put("ReverseSorted", generateReverseSortedArray(size));
        testSuite.put("NearlySorted", generateNearlySortedArray(size));
        testSuite.put("WithDuplicates", generateArrayWithDuplicates(size));
        testSuite.put("Alternating", generateAlternatingArray(size));
        testSuite.put("Normal", generateNormalDistributionArray(size, size / 2.0, size / 6.0));
        testSuite.put("Mountain", generatePatternArray(size, "MOUNTAIN"));
        testSuite.put("Sawtooth", generatePatternArray(size, "SAWTOOTH"));
        
        return testSuite;
    }
    
    /**
     * Validates array size parameter.
     */
    private static void validateSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Array size cannot be negative: " + size);
        }
        if (size > 10_000_000) { // 10 million element limit
            throw new IllegalArgumentException("Array size too large: " + size);
        }
    }
    
    /**
     * Validates percentage parameter.
     */
    private static void validatePercentage(double percentage) {
        if (percentage < 0.0 || percentage > 100.0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100: " + percentage);
        }
    }
    
    /**
     * Provides statistics about generated array.
     */
    public static String analyzeArray(int[] array) {
        if (array.length == 0) return "Empty array";
        
        int min = array[0], max = array[0];
        long sum = 0;
        int duplicates = 0;
        
        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            min = Math.min(min, value);
            max = Math.max(max, value);
            sum += value;
            
            // Count duplicates (simple approach)
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] == value) {
                    duplicates++;
                    break;
                }
            }
        }
        
        double average = (double) sum / array.length;
        boolean isSorted = BenchmarkUtils.isSorted(array);
        
        return String.format(
            "Array Analysis: Size=%d, Min=%d, Max=%d, Avg=%.1f, Duplicates=%d, Sorted=%s",
            array.length, min, max, average, duplicates, isSorted
        );
    }
    
    /**
     * Resets random seed for reproducible test sequences.
     */
    public static void resetRandomSeed() {
        RANDOM.setSeed(RANDOM_SEED);
    }
}
