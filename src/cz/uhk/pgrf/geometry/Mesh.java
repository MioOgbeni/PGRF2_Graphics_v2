package cz.uhk.pgrf.geometry;

import java.util.ArrayList;

import cz.uhk.pgrf.transforms.Bicubic;
import cz.uhk.pgrf.transforms.Cubic;
import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Point3D;

public class Mesh extends GeometricObject{

	private Mat4 krivka;
	private Bicubic mesh;
	
	public static final int body = 16;
	public static final Point3D p11 = new Point3D(0, 3, 0);
	public static final Point3D p12 = new Point3D(1, 3, 0);
	public static final Point3D p13 = new Point3D(2, 3, 0);
	public static final Point3D p14 = new Point3D(3, 3, 0);
	
	public static final Point3D p21 = new Point3D(0, 2, 0);
	public static final Point3D p22 = new Point3D(1, 2, 1);
	public static final Point3D p23 = new Point3D(2, 2, 1);
	public static final Point3D p24 = new Point3D(3, 2, 0);
	
	public static final Point3D p31 = new Point3D(0, 1, 0);
	public static final Point3D p32 = new Point3D(1, 1, 1);
	public static final Point3D p33 = new Point3D(2, 1, 1);
	public static final Point3D p34 = new Point3D(3, 1, 0);
	
	public static final Point3D p41 = new Point3D(0, 0, 0);
	public static final Point3D p42 = new Point3D(1, 0, 1);
	public static final Point3D p43 = new Point3D(2, 0, 1);
	public static final Point3D p44 = new Point3D(3, 0, 0);
	
	public Mesh() {
	 krivka = Cubic.BEZIER;
	 mesh = new Bicubic(krivka, p11, p12, p13, p14, p21, p22, p23, p24, p31, p32, p33, p34, p41, p42, p43, p44);
	vertexBuffer = new ArrayList<>();
	indexBuffer = new ArrayList<>();
	for (int i = 0; i < body-1; i++){
		for (int j = 0; j < body-1; j++){
			vertexBuffer.add(mesh.compute((double) i / body,(double) j / body));
			if (i % 2 == 1){
				indexBuffer.add(j);
				indexBuffer.add(j+1);
				indexBuffer.add(j+3);
				
				indexBuffer.add(j);
				indexBuffer.add(j+2);
				indexBuffer.add(j+3);
			}
		}
	 }
	 
	}
}
