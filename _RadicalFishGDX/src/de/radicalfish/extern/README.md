External Classes
=================

This package contains classes and functions from other developers (always with permission to use them). 


[Bresenham][1]
----------------

The Bresenham class can be used to get all points on a line. This is very useful for collision. The following code shows how to get all points on a line.
```Java
public static void main(String[] args) throws Exception {
	
	// Create a Bresenham. a Static object can work too
	Bresenham b = new Bresenham();
		
	// plot a line from 0,0 to 30,50
	b.plot(0, 0, 30, 50);
	
	// move over all points
	while(b.next()) {
		System.out.println("Point: (" + b.getX() + ", " + b.getY() + ")");
	}
}
```

[1]: https://github.com/Regiden/RadicalFishEngine/blob/master/_RadicalFishGDX/src/de/radicalfish/extern/Bresenham.java

