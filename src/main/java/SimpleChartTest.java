import java.io.*;

public class SimpleChartTest {
    public static void main(String[] args) {
        System.out.println("=== SIMPLE CHART TEST ===");
        
        // Check if file exists
        String[] possiblePaths = {
            "sorting_results.csv",
            "results/data/sorting_results_20250829_230229.csv",
            "results\\data\\sorting_results_20250829_230229.csv",
            "C:\\Users\\SANDEEP THARALLA\\Desktop\\SortingAlgorithmResearch\\results\\data\\sorting_results_20250829_230229.csv"
        };
        
        for (String path : possiblePaths) {
            File file = new File(path);
            System.out.printf("Checking: %s -> %s%n", path, file.exists() ? "EXISTS" : "NOT FOUND");
            if (file.exists()) {
                System.out.printf("Absolute path: %s%n", file.getAbsolutePath());
                break;
            }
        }
    }
}
