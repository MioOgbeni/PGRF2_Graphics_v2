package cz.uhk.pgrf.geometry.models;

import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.geometry.GeometricObject;
import cz.uhk.pgrf.transforms.Point3D;

/**
 * Tøída pro osy prostoru XYZ.
 * 
 * @author Tomáš Novák
 * @version 2017
 */

public class Osy extends GeometricObject {

	public Osy() {
		transferable = false;

		Integer[] ints = new Integer[] { 0, 1, 2, 3, 4, 5 };
		indexBuffer = new ArrayList<>(Arrays.asList(ints));

		vertexBuffer = new ArrayList<>();
		vertexBuffer.add(new Point3D(10, 0, 0));
		vertexBuffer.add(new Point3D(-10, 0, 0));
		vertexBuffer.add(new Point3D(0, -10, 0));
		vertexBuffer.add(new Point3D(0, 10, 0));
		vertexBuffer.add(new Point3D(0, 0, 10));
		vertexBuffer.add(new Point3D(0, 0, -10));
		
		// každá osa jinou barvou
		color = new ArrayList<>();
		color.add(0xff0000);
		color.add(0x00ff00);
		color.add(0x0000ff);
	}
}
