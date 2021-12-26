# GaussJordanEliminate

Gaussian elimination, also known as row reduction, is an algorithm in linear algebra for solving a system of linear equations. It is usually understood as a sequence of operations performed on the corresponding matrix of coefficients. This method can also be used to find the rank of a matrix, to calculate the determinant of a matrix, and to calculate the inverse of an invertible square matrix. 

[See Wikipedia](https://en.wikipedia.org/wiki/Gaussian_elimination)

## Usage

Java : 
```java
int[][] matrix = {{2,1,-1,8},
                  {-3,-1,2,-11},
                  {-2,1,2,-3}};

matrix = GaussJordanEliminate.solve(matrix);
System.out.println(Arrays.toString(GaussJordanEliminate.extractAnswers(matrix)));
```

Swift :
```swift
var matrix = [[2,1,-1,8],
              [-3,-1,2,-11],
              [-2,1,2,-3]]
        
matrix = GaussJordanEliminate.solve(matrix)
print(GaussJordanEliminate.extractAnswers(matrix))
```

![Image](https://wikimedia.org/api/rest_v1/media/math/render/svg/143d91265d57a1d2bd91ef95aec9f2e466ba411b) : ![Image](https://wikimedia.org/api/rest_v1/media/math/render/svg/158d23387edac419ba2e00a3b6bcf0f400779f2c)

Logs :
```
Input : 
    [2, 1, -1, 8],
    [-3, -1, 2, -11],
    [-2, 1, 2, -3]
    
Start : 
    [2, 1, -1, 8],
    [0, 1, 1, 2],
    [0, 0, 1, -1]
    
Output : 
    [1, 0, 0, 2],
    [0, 1, 0, 3],
    [0, 0, 1, -1]

Answers :
    [2, 3, -1]
```
