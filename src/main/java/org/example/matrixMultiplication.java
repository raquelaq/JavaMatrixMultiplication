package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;


public class matrixMultiplication {
    static int ROWS;
    static int COLS;

    int[][] A = new int[ROWS][COLS];
    int[][] B = new int[ROWS][COLS];

    @Setup(Level.Trial)
    public void setUp() {
        generateMatrix(A, 1, 9);
        generateMatrix(B, 1, 9);
    }

    public static void generateMatrix(int[][] matrix, int min, int max) {
        Random random = new Random();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = random.nextInt(max - min + 1) + min;
            }
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static int[][] matrixMultiplication(int[][] A, int[][] B) {
        int[][] C = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                C[i][j] = 0;
                for (int k = 0; k < COLS; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    public static void writeToCSV(String language, int size, long execTime, double memoryUsed, double cpuLoad) {
        try (FileWriter writer = new FileWriter("../benchmark_results.csv", true)) {
            if (Files.size(Paths.get("../benchmark_results.csv")) == 0) {
                writer.append("Language,Matrix Size,Execution Time,Memory Use,CPU use\n");
            }
            String formattedCpuLoad = String.format(Locale.US, "%.2f", cpuLoad);
            String formattedMemoryUsed = String.format(Locale.US, "%.2f", memoryUsed);
            String formattedExecTime = String.format(Locale.US, "%d", execTime);

            writer.append(String.format("%s,%d,%s,%s,%s\n", language, size, formattedExecTime, formattedMemoryUsed, formattedCpuLoad));        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
