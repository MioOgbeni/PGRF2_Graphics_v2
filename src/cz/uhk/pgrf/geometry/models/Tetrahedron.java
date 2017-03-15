package cz.uhk.pgrf.geometry.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.geometry.GeometricObject;
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
		for (int i = 0; i < indexBuffer.size(); i++){
			int r = (int) (Math.random() * (255 - 0)) + 0;
			int g = (int) (Math.random() * (255 - 0)) + 0;
			int b = (int) (Math.random() * (255 - 0)) + 0;
			color.add(new Color(r, g, b).getRGB());
		}
	}
	
}
