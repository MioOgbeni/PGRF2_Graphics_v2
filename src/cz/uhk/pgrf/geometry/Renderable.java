package cz.uhk.pgrf.geometry;

import java.awt.image.BufferedImage;
import java.util.List;

import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Point3D;

/**
 * interface pro WireFrameRenderer.
 * 
 * @author Tomáš Novák
 * @version 2016
 */ 

public interface Renderable {

	void setBufferedImage(BufferedImage img);

	BufferedImage getBufferedImage();
	
	void setView(Mat4 mat);

	void setProj(Mat4 mat);

	void render(Objekt3D obj);

	void render(GeometricObject obj, Mat4 mod);

	void render(List<Objekt3D> objs);
	
	void drawTriangle(Point3D vertexA, Point3D vertexB, Point3D vertexC);
	
	void setFillOrNot(boolean fillOrNot);
	
	boolean isFillOrNot();

}
