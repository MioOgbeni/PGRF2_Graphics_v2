package cz.uhk.pgrf.geometry.models;

import java.awt.Color;
import java.util.ArrayList;

import cz.uhk.pgrf.geometry.GeometricObject;
import cz.uhk.pgrf.transforms.Bicubic;
import cz.uhk.pgrf.transforms.Cubic;
import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Point3D;

/**
 * T¯Ìda sÌùovÈ plochy.
 * 
 * @author Tom·ö Nov·k
 * @version 2017
 */

public class Mesh extends GeometricObject {

	private Mat4 krivka;
	private Bicubic mesh;

	public static final int body = 30;
	public static final Point3D p11 = new Point3D(-2, 2, 5);
	public static final Point3D p12 = new Point3D(-1, 3, 5);
	public static final Point3D p13 = new Point3D(1, 3, 5);
	public static final Point3D p14 = new Point3D(2, 2, 5);

	public static final Point3D p21 = new Point3D(-3, 1, 5);
	public static final Point3D p22 = new Point3D(-10, 10, -5);
	public static final Point3D p23 = new Point3D(10, 10, -5);
	public static final Point3D p24 = new Point3D(3, 1, 5);

	public static final Point3D p31 = new Point3D(-3, -1, 5);
	public static final Point3D p32 = new Point3D(-10, -10, -5);
	public static final Point3D p33 = new Point3D(10, -10, -5);
	public static final Point3D p34 = new Point3D(3, -1, 5);

	public static final Point3D p41 = new Point3D(-2, -2, 5);
	public static final Point3D p42 = new Point3D(-1, -3, 5);
	public static final Point3D p43 = new Point3D(1, -3, 5);
	public static final Point3D p44 = new Point3D(2, -2, 5);

	public Mesh() {
		krivka = Cubic.BEZIER;
		mesh = new Bicubic(krivka, p11, p12, p13, p14, p21, p22, p23, p24, p31, p32, p33, p34, p41, p42, p43, p44);
		vertexBuffer = new ArrayList<>();
		indexBuffer = new ArrayList<>();
		color = new ArrayList<>();

		// v˝poËet bod˘
		for (int i = 0; i <= body; i++) {
			for (int j = 0; j <= body; j++) {
				vertexBuffer.add(mesh.compute((double) i / body, (double) j / body));
			}
		}

		// generov·nÌ index˘ (spojenÌ bod˘)
		for (int i = 0; i < body; i++) {
			for (int j = 0; j < body; j++) {
				indexBuffer.add(i * (body + 1) + j);
				indexBuffer.add(i * (body + 1) + j + 1);
				indexBuffer.add((i + 1) * (body + 1) + j);

				indexBuffer.add(i * (body + 1) + j + 1);
				indexBuffer.add((i + 1) * (body + 1) + j);
				indexBuffer.add((i + 1) * (body + 1) + j + 1);
			}
		}

		// generov·nÌ barvy
		for (int i = 0; i < indexBuffer.size(); i++) {
			int r = (int) (Math.random() * (255 - 0)) + 0;
			int g = (int) (Math.random() * (255 - 0)) + 0;
			int b = (int) (Math.random() * (255 - 0)) + 0;
			color.add(new Color(r, g, b).getRGB());
		}
	}
}
