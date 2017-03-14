package cz.uhk.pgrf.geometry;

import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.transforms.Point3D;

public class Triangle extends GeometricObject {
	
	public Triangle(){
		Integer[] ints = new Integer[]{0, 1, 2};
        indexBuffer = new ArrayList<>(Arrays.asList(ints));

        vertexBuffer = new ArrayList<>();
		vertexBuffer.add(new Point3D(1, 0, 0));
		vertexBuffer.add(new Point3D(0, 1, 0));
		vertexBuffer.add(new Point3D(0, 0, 1));	
		
		color = new ArrayList<>();
		color.add(0x25B7E8);
	}

}
