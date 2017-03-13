package cz.uhk.pgrf.geometry;

import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.transforms.Point3D;

/**
 * Tøída jehlanu.
 * 
 * @author Tomáš Novák
 * @version 2016
 */ 

public class Tetrahedron extends GeometricObject {
	
	public Tetrahedron(){
		Integer[] ints = new Integer[]{0,1,2,  2,0,3,  2,3,1,  3,0,1};
        indexBuffer = new ArrayList<>(Arrays.asList(ints));

        vertexBuffer = new ArrayList<>();
		vertexBuffer.add(new Point3D(-2, -2, -2));
		vertexBuffer.add(new Point3D(2, -2, 2));
		vertexBuffer.add(new Point3D(-2, 2, 2));
		vertexBuffer.add(new Point3D(2, 2, -2));
		
		color = new ArrayList<>();
		color.add(0x25B7E8);
		color.add(0xB825E8);
		color.add(0xE85725);
		color.add(0x55E825);
	}
	
}
