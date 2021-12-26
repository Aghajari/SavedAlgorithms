//
//  GaussJordanEliminate.swift
//  GaussJordanEliminate
//
//  Created by AmirHossein Aghajari on 10/26/20.
//  Copyright © 2020 Amir Hossein Aghajari. All rights reserved.
//

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

import Foundation

public class GaussJordanEliminate {
    
    // TEST
    public static func testExample() {
        // https://en.wikipedia.org/wiki/Gaussian_elimination
        
        GaussJordanEliminate.LOG = true
        
        var matrix = [[2,1,-1,8],
                      [-3,-1,2,-11],
                      [-2,1,2,-3]]
        
        matrix = GaussJordanEliminate.solve(matrix)
        print(GaussJordanEliminate.extractAnswers(matrix))
    }
    
    private static var LOG = false;
    
    public static func solve(_ matrix: [[Int]]) -> [[Int]] {
        var cells = gaussJordanEliminate(matrix)
        
        if (LOG){
            print("Start : \n"+matrixToString(cells))
            print("________")
        }
        
        var answers = Dictionary<Int,[Int]>()
        while (!hasSolved(cells)) {
            findAnswers(&answers, &cells)
            checkAnswers(&answers, &cells)
        }
        cells = gaussJordanEliminate(cells)
        
        if (LOG){
            print("Final : \n"+matrixToString(cells))
            print("________")
        }
        return cells
    }
    
    public static func gaussJordanEliminate(_ matrix: [[Int]]) -> [[Int]] {
        var cells = matrix
        
        let rows = cells.count
        let cols = cells[0].count
        
        // Simplify all rows
        for i in 0..<rows {
            cells[i] = simplifyRow(cells[i])
        }
        
        // Compute row echelon form (REF)
        var numPivots = 0
        for i in 0..<cols {
            // Find pivot
            var pivotRow = numPivots
            while (pivotRow < rows && cells[pivotRow][i] == 0){
                pivotRow += 1
            }
            if (pivotRow == rows){
                continue
            }
            let pivot = cells[pivotRow][i]
            swapRows(numPivots, pivotRow,&cells)
            numPivots += 1
            
            // Eliminate below
            for j in numPivots..<rows {
                let g = gcd(pivot, cells[j][i])
                cells[j] = simplifyRow(addRows(multiplyRow(cells[j], pivot / g), multiplyRow(cells[i], -cells[j][i] / g)))
            }
        }
        return cells
    }
    
    /**
     * @return solved matrix answers
     * for example :
     * [1, 0, 0, 2],
     * [0, 1, 0, 3],
     * [0, 0, 1, -1]
     * will return [2,3,-1]
     */
    public static func extractAnswers (_ matrix : [[Int]]) -> [Int] {
        var res = [Int]()
        if (hasSolved(matrix)){
            let rows = matrix.count
            let cols = matrix[0].count
            for r in 0..<rows{
                for c in 0..<(cols-1) {
                    if (matrix[r][c] != 0){
                        res.append(matrix[r][cols-1]/matrix[r][c])
                        break
                    }
                }
            }
        }
        return res
    }
    
    
    /**
     * @return matrix as a String
     * for example :
     * [1, 0, 0, 2],
     * [0, 1, 0, 3],
     * [0, 0, 1, -1]
     */
    public static func matrixToString(_ matrix: [[Int]]) -> String {
        let rows = matrix.count
        let cols = matrix[0].count
        var result = ""
        for i in 0..<rows{
            if (i != 0) {
                result += ",\n"
            }
            result += "["
            for j in 0..<cols {
                if (j != 0) {
                    result += ", "
                }
                result += String(matrix[i][j])
            }
            result += "]"
        }
        return result
    }
    
    /**
     * find the solved args in matrix and save it in a map
     */
    private static func findAnswers(_ answers : inout Dictionary<Int,[Int]> ,_ cells : inout [[Int]]){
        let rows = cells.count
        let cols = cells[0].count
        for r in 0..<rows{
            if (countNonzeroArg(cells, r) == 1){
                for c in 0..<(cols-1) {
                    if (cells[r][c] != 0){
                        answers[c] = [cells[r][c],cells[r][cols-1]]
                        break
                    }
                }
            }
        }
    }
    
    /**
     * replace the founded answers in matrix
     */
    private static func checkAnswers(_ answers : inout Dictionary<Int,[Int]> ,_ cells : inout [[Int]]) {
        let rows = cells.count
        let cols = cells[0].count
        for r in 0..<rows{
            if (countNonzeroArg(cells, r) > 1){
                for c in 0..<(cols-1) {
                    if (cells[r][c] != 0){
                        if (answers.keys.contains(c)){
                            let count = cells[r][c]
                            var res = answers[c]
                            let before = cells[r][cols-1]
                            cells[r][cols-1] = before - (count * res![1] / res![0])
                            cells[r][c] = 0
                        }
                    }
                }
            }
        }
    }
    
    /**
     * @return the count of non zero values in a row (except last column)
     */
    private static func countNonzeroArg(_ matrix: [[Int]] , _ row : Int) -> Int  {
        var count = 0
        let cols = matrix[0].count
        for i in 0..<(cols-1) {
            if (matrix[row][i] != 0){
                count += 1
            }
        }
        return count
    }
    
    /**
     * @return true if matrix has been solved.
     */
    private static func hasSolved(_ matrix : [[Int]]) -> Bool{
        let rows = matrix.count
        for i in 0..<rows {
            if (countNonzeroArg(matrix,i)>1) {
                return false
            }
        }
        return true
    }
    
    /**
     * @return the GCD of all the numbers in the given row. The row is is not an index.
     * for example, gcdRow([3, 6, 9, 12]) = 3.
     */
    private static func gcdRow(_ x : [Int]) -> Int {
        var result = 0
        for value in x {
            result = gcd(value, result)
        }
        return result
    }
    
    /**
     * @return the greatest common divisor of the given integers.
     */
    private static func gcd(_ a : Int, _ b : Int) -> Int {
        var x = abs(a)
        var y = abs(b)
        while (y != 0) {
            let z = x % y
            x = y
            y = z
        }
        return x
    }
    
    /**
     * @return a new row where the leading non-zero number (if any) is positive, and the GCD of the row is 0 or 1.
     * for example, simplifyRow([0, -2, 2, 4]) = [0, 1, -1, -2].
     */
    private static func simplifyRow(_ x : [Int]) -> [Int]{
        var sign = 0
        for value in x{
            if (value > 0) {
                sign = 1
                break
            } else if (value < 0) {
                sign = -1
                break
            }
        }
        var y = [Int]()
        y += x
        if (sign == 0){
            return y
        }
        let g = gcdRow(x) * sign
        for i in 0..<y.count{
            y[i] /= g
        }
        return y
    }
    
    private static func swapRows(_ i : Int, _ j : Int,_ cells : inout [[Int]]) {
        let temp = cells[i]
        cells[i] = cells[j]
        cells[j] = temp
    }
    
    /**
     * @return a new row that is the sum of the two given rows. The rows are not indices.
     * for example, addRow([3, 1, 4], [1, 5, 6]) = [4, 6, 10].
     */
    private static func addRows(_ x : [Int],_ y : [Int]) -> [Int] {
        var z = [Int]()
        z += x
        for i in 0..<z.count{
            z[i] = x[i] + y[i]
        }
        return z
    }
    
    /**
     * @return a new row that is the product of the given row with the given scalar. The row is is not an index.
     * for example, multiplyRow([0, 1, 3], 4) = [0, 4, 12].
     */
    private static func multiplyRow(_ x : [Int],_ c : Int) -> [Int]{
        var y = [Int]()
        y += x
        for i in 0..<y.count{
            y[i] = (x[i] * c)
        }
        return y
    }
}
