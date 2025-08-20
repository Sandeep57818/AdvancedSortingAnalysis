package com.research.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class PerformanceMetricsTest {
    
    private PerformanceMetrics metrics;
    
    @BeforeEach
    void setUp() {
        metrics = new PerformanceMetrics();
    }
    
    @Test
    void testDefaultConstructor() {
        assertEquals(0, metrics.getExecutionTimeNanos());
        assertEquals(0, metrics.getMemoryUsageBytes());
        assertEquals(0, metrics.getComparisonCount());
        assertEquals(0, metrics.getSwapCount());
    }
    
    @Test
    void testParameterizedConstructor() {
        PerformanceMetrics testMetrics = new PerformanceMetrics(
            1000000, 2048, 100, 50, "QuickSort", 1000, "Random"
        );
        
        assertEquals(1000000, testMetrics.getExecutionTimeNanos());
        assertEquals(2048, testMetrics.getMemoryUsageBytes());
        assertEquals(100, testMetrics.getComparisonCount());
        assertEquals(50, testMetrics.getSwapCount());
        assertEquals("QuickSort", testMetrics.getAlgorithmName());
    }
    
    @Test
    void testUnitConversions() {
        metrics.setExecutionTimeNanos(2000000);  // 2 milliseconds
        metrics.setMemoryUsageBytes(2048);       // 2 KB
        
        assertEquals(2.0, metrics.getExecutionTimeMillis(), 0.001);
        assertEquals(2.0, metrics.getMemoryUsageKB(), 0.001);
    }
    
    @Test
    void testCSVFormat() {
        metrics.setAlgorithmName("BubbleSort");
        metrics.setDataType("Random");
        metrics.setInputSize(1000);
        
        String csv = metrics.toCSV();
        assertTrue(csv.contains("BubbleSort"));
        assertTrue(csv.contains("Random"));
        assertTrue(csv.contains("1000"));
    }
}
