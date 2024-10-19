package org.example;

import static org.example.matrixMultiplication.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        int[] sizes = {10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1024};
        for (int size : sizes) {
            ROWS = size;
            COLS = size;
            int[][] A = new int[ROWS][COLS];
            int[][] B = new int[ROWS][COLS];

            generateMatrix(A, 1, 9);
            generateMatrix(B, 1, 9);

            runtime.gc();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            long startTime = System.currentTimeMillis();

            int[][] C = matrixMultiplication(A, B);

            long endTime = System.currentTimeMillis();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            double executionTime = endTime - startTime;

            double cpuUsage = osBean.getSystemCpuLoad() * 100;

            System.out.printf("\n\nMatrix size: %dx%d\n", size, size);
            System.out.printf("Execution time: %.3f milliseconds\n", executionTime);
            System.out.printf("Memory used: %d bytes\n", (memoryAfter - memoryBefore));
            System.out.printf("CPU Usage: %.3f%%\n", cpuUsage);
            System.out.printf("----------------------------------");

            try (FileWriter writer = new FileWriter("../benchmark_results.csv", true)) {
                writer.write(String.format("Java,%dx%d,%.3f,%.2f,%.3f\n", size, size, executionTime,
                        (double) (memoryAfter - memoryBefore) / (1024 * 1024), cpuUsage));
            } catch (IOException e) {
                System.err.println("Error writing to CSV file: " + e.getMessage());
            }
        }
    }

    private static void generateMatrix(int[][] matrix, int min, int max) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = (int) (Math.random() * (max - min + 1) + min);
            }
        }
    }
}
