package cz.uhk.pgrf.geometry;

import java.util.ArrayList;
import java.util.List;

import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Mat4Identity;

/**
 * T��da obecn�ho objektu.
 * 
 * @author Tom� Nov�k
 * @version 2017
 */

public class Objekt3D {

	protected List<GeometricObject> geoObj = new ArrayList<>();
	protected Mat4 model = new Mat4Identity();

	// konstruktory
	public Objekt3D(GeometricObject geoObj) {
		this.geoObj.add(geoObj);
	}

	public Objekt3D(List<GeometricObject> geoObjs) {
		this.geoObj = geoObjs;
	}

	// vlo��/nastav� modelovac� matici
	public Mat4 getMat() {
		return model;
	}

	public void setMat(Mat4 model) {
		this.model = model;
	}

	// vr�t� GeomObj na dan�m indexu
	public GeometricObject get(int i) {
		if (i >= 0 && i < geoObj.size()) {
			return geoObj.get(i);
		}
		return null;
	}

	// vr�t� po�et Objekt�
	public int getCount() {
		return geoObj.size();
	}

	// p�id� dal�� Objekt
	public void add(GeometricObject geoObj) {
		this.geoObj.add(geoObj);
	}
}
