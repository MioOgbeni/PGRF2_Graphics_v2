package cz.uhk.pgrf.geometry;

import java.util.ArrayList;
import java.util.Arrays;

import cz.uhk.pgrf.transforms.Point3D;

/**
 * Tøída Krychle.
 * 
 * @author Tomáš Novák
 * @version 2016
 */

public class Cube extends GeometricObject {

	public Cube() {

		Integer[] ints = new Integer[] { 0,1,2,  0,2,3,  0,4,5,  0,5,1,  3,2,6,  3,6,7,  4,7,6,  4,6,5,  1,2,6,  1,6,5,  0,3,7,  0,7,4, };
		indexBuffer = new ArrayList<>(Arrays.asList(ints));

		vertexBuffer = new ArrayList<>();
		vertexBuffer.add(new Point3D(0, 0, 0));
		vertexBuffer.add(new Point3D(0, 1, 0));
		vertexBuffer.add(new Point3D(1, 1, 0));
		vertexBuffer.add(new Point3D(1, 0, 0));

		vertexBuffer.add(new Point3D(0, 0, -1));
		vertexBuffer.add(new Point3D(0, 1, -1));
		vertexBuffer.add(new Point3D(1, 1, -1));
		vertexBuffer.add(new Point3D(1, 0, -1));
		
		color = new ArrayList<>();
		color.add(0x25B7E8);
		color.add(0xB825E8);
		color.add(0xE85725);
		color.add(0x55E825);
		color.add(0xFF9ED7);
		color.add(0xFFF79E);
		color.add(0x9EFFC6);
		color.add(0x9EA7FF);
		color.add(0xD9E000);
		color.add(0x00E077);
		color.add(0x0700E0);
		color.add(0xE00069);
	}
}
