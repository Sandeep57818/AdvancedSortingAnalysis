# Sorting Algorithm Performance Analysis

## Project Overview
This research project provides a comprehensive comparative analysis of sorting algorithm performance in Java, examining six fundamental algorithms across multiple data configurations and input sizes.

## Research Objectives
- Empirical validation of theoretical time complexity analysis
- Performance comparison across different data types and input sizes
- Statistical analysis of algorithm efficiency patterns
- Professional benchmarking methodology demonstration

## Algorithms Analyzed
1. **Bubble Sort** - O(n²) comparison-based algorithm
2. **Selection Sort** - O(n²) selection-based algorithm  
3. **Insertion Sort** - O(n²) insertion-based algorithm
4. **Quick Sort** - O(n log n) divide-and-conquer algorithm
5. **Merge Sort** - O(n log n) stable divide-and-conquer algorithm
6. **Heap Sort** - O(n log n) selection-based algorithm

## Project Structure

SortingAlgorithmResearch/
├── src/main/java/com/research/sorting/
│ ├── algorithms/ # Algorithm implementations
│ ├── benchmarking/ # Benchmarking framework
│ ├── utils/ # Utility classes
│ ├── analysis/ # Statistical analysis
│ └── PerformanceMetrics.java
├── src/test/java/ # Unit tests
├── data/ # Test datasets
├── results/ # Benchmark outputs
├── docs/ # Research documentation
├── charts/ # Generated visualizations
└── pom.xml # Maven configuration


## Development Environment
- **Java Version**: 17+
- **Build Tool**: Maven 3.9+
- **IDE**: VS Code with Extension Pack for Java
- **Testing**: JUnit 5
- **Dependencies**: Apache Commons Math, OpenCSV

## Getting Started
1. Clone the repository
2. Open in VS Code
3. Ensure Java 17+ is installed
4. Run `mvn clean compile` to build project
5. Execute tests with `mvn test`

## Research Timeline
- **Days 1-3**: Algorithm implementation and testing
- **Days 4-6**: Benchmarking framework and data collection
- **Days 7-8**: Statistical analysis and visualization
- **Days 9-10**: Documentation and research paper

## Contact
**Author**: Sandeep Tharalla
**Email**: sandeeptharalla@gmail.com
**Date**: August 2025 

## Implemented Algorithms

### O(n²) Algorithms
1. **Bubble Sort** - Simple comparison-based with early termination
2. **Selection Sort** - Consistent O(n²) performance, minimal swaps  
3. **Insertion Sort** - Adaptive algorithm, excellent for nearly sorted data

### O(n log n) Algorithms
4. **Quick Sort** - Fast average case with median-of-three optimization
5. **Merge Sort** - Stable divide-and-conquer with predictable performance
6. **Heap Sort** - In-place algorithm using binary heap data structure

### Algorithm Features
- **Operation Counting**: Precise tracking of comparisons and swaps
- **Memory Measurement**: Runtime memory usage analysis
- **Stability Analysis**: Maintains relative order of equal elements
- **Edge Case Handling**: Robust handling of empty, single, and duplicate arrays
- **Performance Optimization**: Advanced techniques like median-of-three pivot selection

### Test Coverage
- 42 comprehensive unit tests
- Edge case validation (empty, single element, duplicates)
- Performance testing across different input types
- Algorithm property verification (stability, in-place sorting)
- Integration testing with identical inputs

## Day 4: Professional Benchmarking Framework

### Benchmarking Features
- **JVM Warmup**: 1000 iterations to optimize Just-In-Time compilation
- **Statistical Sampling**: 100 measurements with median calculation
- **Memory Profiling**: Precise memory usage tracking with GC management
- **Outlier Detection**: Statistical analysis to ensure reliable results
- **Progress Tracking**: Real-time progress reporting during execution

### Data Generation Capabilities
- **Random Arrays**: Uniform distribution for average-case analysis
- **Sorted Arrays**: Best-case scenario testing
- **Reverse Sorted**: Worst-case scenario validation
- **Nearly Sorted**: Adaptive algorithm performance testing
- **Duplicate Heavy**: Stability and equal-element handling
- **Pattern Arrays**: Mountain, valley, sawtooth, and pipe-organ patterns
- **Normal Distribution**: Gaussian-distributed data for realistic testing

### Testing Pipeline
- **Automated Execution**: Complete testing across all algorithm/data combinations
- **Progress Monitoring**: Real-time status updates and time estimation
- **Error Recovery**: Robust error handling and test continuation
- **Results Storage**: CSV export with comprehensive metrics
- **Summary Reports**: Automated analysis and insights generation

### Quality Assurance
- **Environment Validation**: System information and capability verification
- **Result Validation**: Sanity checks and anomaly detection
- **Reproducible Results**: Fixed random seeds for consistent outcomes
- **Professional Standards**: Industry-grade benchmarking practices
