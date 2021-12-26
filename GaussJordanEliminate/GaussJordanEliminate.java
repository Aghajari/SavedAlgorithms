/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari;

import java.util.Arrays;
import java.util.HashMap;

public class GaussJordanEliminate {

    // TEST
    public static void main(String[] args) {
        // https://en.wikipedia.org/wiki/Gaussian_elimination

        LOG = true;
        int[][] matrix = {{2,1,-1,8},
                {-3,-1,2,-11},
                {-2,1,2,-3}};

        matrix = GaussJordanEliminate.solve(matrix);
        System.out.println(Arrays.toString(GaussJordanEliminate.extractAnswers(matrix)));
    }

    private static boolean LOG = false;

    public static int[][] solve(int[][] matrix) {
        int[][] cells = gaussJordanEliminate(matrix);

        if (LOG){
            System.out.println("Start : \n"+matrixToString(cells));
            System.out.println("________");
        }

        HashMap<Integer,int[]> answers = new HashMap<>();
        while (!hasSolved(cells)) {
            findAnswers(answers, cells);
            checkAnswers(answers, cells);
        }
        cells = gaussJordanEliminate(cells);

        if (LOG){
            System.out.println("Final : \n"+matrixToString(cells));
            System.out.println("________");
        }
        return cells;
    }

    public static int[][] gaussJordanEliminate(int[][] matrix) {
        int[][] cells = matrix.clone();
        int rows = cells.length;
        int cols = cells[0].length;

        // Simplify all rows
        for (int i = 0; i < rows; i++)
            cells[i] = simplifyRow(cells[i]);

        // Compute row echelon form (REF)
        int numPivots = 0;
        for (int i = 0; i < cols; i++) {
            // Find pivot
            int pivotRow = numPivots;
            while (pivotRow < rows && cells[pivotRow][i] == 0)
                pivotRow++;
            if (pivotRow == rows)
                continue;
            int pivot = cells[pivotRow][i];
            swapRows(numPivots, pivotRow,cells);
            numPivots++;

            // Eliminate below
            for (int j = numPivots; j < rows; j++) {
                int g = gcd(pivot, cells[j][i]);
                cells[j] = simplifyRow(addRows(multiplyRow(cells[j], pivot / g), multiplyRow(cells[i], -cells[j][i] / g)));
            }
        }
        return cells;
    }

    /**
     * @return solved matrix answers or null
     * for example :
     * [1, 0, 0, 2],
     * [0, 1, 0, 3],
     * [0, 0, 1, -1]
     * will return [2,3,-1]
     */
    public static int[] extractAnswers (int[][] matrix){
        if (hasSolved(matrix)){
            int rows = matrix.length;
            int cols = matrix[0].length;
            int[] res = new int[rows];
            for (int r=0;r<rows;r++){
                for (int c = 0; c < cols-1; c++) {
                    if (matrix[r][c] != 0){
                        res[r] = matrix[r][cols-1]/matrix[r][c];
                        break;
                    }
                }
            }
            return res;
        }
        return null;
    }

    /**
     * @return matrix as a String
     * for example :
     * [1, 0, 0, 2],
     * [0, 1, 0, 3],
     * [0, 0, 1, -1]
     */
    public static String matrixToString(int[][] matrix){
        int rows = matrix.length;
        int cols = matrix[0].length;
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < rows; i++) {
            if (i != 0) result.append(",\n");
            result.append("[");
            for (int j = 0; j < cols; j++) {
                if (j != 0) result.append(", ");
                result.append(matrix[i][j]);
            }
            result.append("]");
        }
        return result.toString();
    }

    /**
     * find the solved args in matrix and save it in a map
     */
    private static void findAnswers(HashMap<Integer,int[]> answers,int[][] cells){
        int rows = cells.length;
        int cols = cells[0].length;
        for (int r = 0; r < rows; r++) {
            if (countNonzeroArg(cells, r) == 1){
                for (int c = 0; c < cols-1; c++) {
                    if (cells[r][c] != 0){
                        answers.put(c,new int[] {cells[r][c],cells[r][cols-1]});
                        break;
                    }
                }
            }
        }
    }

    /**
     * replace the founded answers in matrix
     */
    private static void checkAnswers(HashMap<Integer,int[]> answers,int[][] cells){
        int rows = cells.length;
        int cols = cells[0].length;
        for (int r = 0; r < rows; r++) {
            if (countNonzeroArg(cells, r) > 1){
                for (int c = 0; c < cols-1; c++) {
                    if (cells[r][c] != 0){
                        if (answers.containsKey(c)){
                            int count = cells[r][c];
                            int[] res = answers.get(c);
                            int before = cells[r][cols-1];
                            cells[r][cols-1] = before - (count*res[1]/res[0]);
                            cells[r][c] = 0;
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the count of non zero values in a row (except last column)
     */
    private static int countNonzeroArg(int[][] matrix, int row) {
        int count = 0;
        int cols = matrix[0].length;
        for (int i = 0; i < cols-1; i++) {
            if (matrix[row][i] != 0)
                count++;
        }
        return count;
    }

    /**
     * @return true if matrix has been solved.
     */
    private static boolean hasSolved(int[][] matrix) {
        int rows = matrix.length;
        for (int i=0;i<rows;i++){
            if (countNonzeroArg(matrix,i)>1) return false;
        }
        return true;
    }

    /**
     * @return the GCD of all the numbers in the given row. The row is is not an index.
     * for example, gcdRow([3, 6, 9, 12]) = 3.
     */
    private static int gcdRow(int[] x) {
        int result = 0;
        for (int value : x) result = gcd(value, result);
        return result;
    }

    /**
     * @return the greatest common divisor of the given integers.
     */
    private static int gcd(int a, int b) {
        int x = Math.abs(a);
        int y = Math.abs(b);
        while (y != 0) {
            int z = x % y;
            x = y;
            y = z;
        }
        return x;
    }

    /**
     * @return a new row where the leading non-zero number (if any) is positive, and the GCD of the row is 0 or 1.
     * for example, simplifyRow([0, -2, 2, 4]) = [0, 1, -1, -2].
     */
    private static int[] simplifyRow(int[] x) {
        int sign = 0;
        for (int value : x) {
            if (value > 0) {
                sign = 1;
                break;
            } else if (value < 0) {
                sign = -1;
                break;
            }
        }
        int[] y = x.clone();
        if (sign == 0)
            return y;
        int g = gcdRow(x) * sign;
        for (int i = 0; i < y.length; i++)
            y[i] /= g;
        return y;
    }

    private static void swapRows(int i, int j,int[][] cells) {
        int[] temp = cells[i];
        cells[i] = cells[j];
        cells[j] = temp;
    }

    /**
     * @return a new row that is the sum of the two given rows. The rows are not indices.
     * for example, addRow([3, 1, 4], [1, 5, 6]) = [4, 6, 10].
     */
    private static int[] addRows(int[] x, int[] y) {
        int[] z = x.clone();
        for (int i = 0; i < z.length; i++)
            z[i] = x[i] + y[i];
        return z;
    }

    /**
     * @return a new row that is the product of the given row with the given scalar. The row is is not an index.
     * for example, multiplyRow([0, 1, 3], 4) = [0, 4, 12].
     */
    private static int[] multiplyRow(int[] x, int c) {
        int[] y = x.clone();
        for (int i = 0; i < y.length; i++)
            y[i] = (x[i] * c);
        return y;
    }
}
