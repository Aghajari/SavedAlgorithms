
We must examine the collision of two shapes (in this simple exercise two rectangles parallel to the coordinate axes) and if there is a collision between them we must calculate their common area.

If you want to calculate only the common area without collision (ie if the smaller rectangle was inside the larger rectangle) you can pass false to `checkForCollision` in `findCommonRect` function.

The order of the input points does not matter because the sorting algorithm is already written.

By default, the area of the shape is calculated with the Shoelace formula

This simple code is for University first practice of Advanced-Programming at the Department of Computer Engineering, Amirkabir University of Technology.

Input:
```
1,1 3,1 1,3 3,3
2,2 2,-5 -4,-5 -4,2
```

Output:
```
True
1
```

<img src="https://user-images.githubusercontent.com/30867537/155894671-8ff228ce-3024-418a-89b0-038d1540b5d7.jpg" width=300 title="Image">
