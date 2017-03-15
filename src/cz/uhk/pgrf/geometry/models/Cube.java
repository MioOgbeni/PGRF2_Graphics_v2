package cz.uhk.pgrf.geometry.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.geometry.GeometricObject;
import cz.uhk.pgrf.transforms.Point3D;

/**
 * Tøída Krychle.
 * 
 * @author Tomáš Novák
 * @version 2016
 */

public class Cube extends GeometricObject {

	public Cube() {

		Integer[] ints = new Integer[] { 0,1,2,  0,2,3,  0,4,5,  0,5,1,  3,2,6,  3,6,7,  4,7,6,  4,6,5,  1,2,6,  1,6,5,  0,3,7,  0,7,4 };
		indexBuffer = new ArrayList<>(Arrays.asList(ints));

		vertexBuffer = new ArrayList<>();
		vertexBuffer.add(new Point3D(-1, -1,  1));
		vertexBuffer.add(new Point3D(-1,  1,  1));
		vertexBuffer.add(new Point3D( 1,  1,  1));
		vertexBuffer.add(new Point3D( 1, -1,  1));

		vertexBuffer.add(new Point3D(-1, -1, -1));
		vertexBuffer.add(new Point3D(-1,  1, -1));
		vertexBuffer.add(new Point3D( 1,  1, -1));
		vertexBuffer.add(new Point3D( 1, -1, -1));
		
		color = new ArrayList<>();
		for (int i = 0; i < indexBuffer.size(); i++){
			int r = (int) (Math.random() * (255 - 0)) + 0;
			int g = (int) (Math.random() * (255 - 0)) + 0;
			int b = (int) (Math.random() * (255 - 0)) + 0;
			color.add(new Color(r, g, b).getRGB());
		}
	}
}
