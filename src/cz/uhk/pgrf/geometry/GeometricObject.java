package cz.uhk.pgrf.geometry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Mat4Identity;
import cz.uhk.pgrf.transforms.Point3D;

/**
 * Abstraktn� t��da (p�edek objekt�).
 * 
 * @author Tom� Nov�k
 * @version 2016
 */

public abstract class GeometricObject {

	protected int baseColor = Color.BLACK.getRGB();
	protected boolean transferable = true;

	protected List<Point3D> vertexBuffer;
	protected List<Integer> indexBuffer;
	protected List<Integer> color;
	protected Mat4 model = new Mat4Identity();

	// vrac� vertex a index buffery
	public List<Point3D> getVB() {
		return new ArrayList<Point3D>(vertexBuffer);
	}

	public List<Integer> getIB() {
		return new ArrayList<Integer>(indexBuffer);
	}

	// nastaven� a vr�cen� barvy
	public void setColors(List<Integer> color) {
		this.color = new ArrayList<>(color);
	}

	public List<Integer> getColors() {
		return new ArrayList<Integer>(color);
	}

	public int getColor(int i) {
		if (color != null) {
			if (color.size() > i && i >= 0) {
				return color.get(i);
			}
		}
		return baseColor;
	}

	public void setBaseColor(int color) {
		this.baseColor = color;
	}

	// vr�t� a nastav� modelovou matici
	public Mat4 getMat() {
		return model;
	}

	public void setMat(Mat4 model) {
		this.model = model;
	}

	// Je transformovateln�?
	public boolean isTransferable() {
		return transferable;
	}
}
