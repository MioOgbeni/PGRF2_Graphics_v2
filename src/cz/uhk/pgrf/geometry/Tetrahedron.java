package cz.uhk.pgrf.geometry;

import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.transforms.Point3D;

/**
 * T��da jehlanu.
 * 
 * @author Tom� Nov�k
 * @version 2016
 */ 

public class Tetrahedron extends GeometricObject {
	
	public Tetrahedron(){
		Integer[] ints = new Integer[]{0, 1, 0, 2, 0, 3, 1, 2, 1, 3, 2, 3};
        indexBuffer = new ArrayList<>(Arrays.asList(ints));

        vertexBuffer = new ArrayList<>();
		vertexBuffer.add(new Point3D(-2, -2, -2));
		vertexBuffer.add(new Point3D(2, -2, 2));
		vertexBuffer.add(new Point3D(-2, 2, 2));
		vertexBuffer.add(new Point3D(2, 2, -2));	
	}
	
}
