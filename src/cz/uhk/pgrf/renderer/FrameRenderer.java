package cz.uhk.pgrf.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import cz.uhk.pgrf.geometry.GeometricObject;
import cz.uhk.pgrf.geometry.Objekt3D;
import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Mat4Identity;
import cz.uhk.pgrf.transforms.Point3D;
import cz.uhk.pgrf.transforms.Vec3D;

/**
 * Zobrazovací øetìzec.
 * 
 * @author Tomáš Novák
 * @version 2017
 */

public class FrameRenderer implements Renderable {

	private BufferedImage img;
	private Zbuffer zbf;
	private Mat4 view;
	private Mat4 proj;
	private Mat4 model;
	private int count = 0;
	private boolean fillOrNot = true;
	private int color;

	// nastavené image bufferu
	@Override
	public void setBufferedImage(BufferedImage img) {
		this.img = img;
		zbf = new Zbuffer(img.getWidth(), img.getHeight());
		zbf.setMinW(1);
		zbf.Clear();
	}

	// nastavené matic pohledu a projekce
	@Override
	public void setView(Mat4 view) {
		this.view = view;
	}

	@Override
	public void setProj(Mat4 proj) {
		this.proj = proj;
	}

	// vykreslí list objektù
	@Override
	public void render(List<Objekt3D> objs) {
		for (Objekt3D obj : objs) {
			render(obj);
		}
		zbf.Clear();
	}

	// vykreslí 1 daný objekt
	@Override
	public void render(Objekt3D obj) {
		for (int i = 0; i < obj.getCount(); i++) {
			render(obj.get(i), obj.getMat());
		}
	}

	// zobrazovací øetìzec
	@Override
	public void render(GeometricObject geoObj, Mat4 model) {
		count = 0;

		if (geoObj.isTransferable()) {
			this.model = geoObj.getMat().mul(model);
		} else {
			this.model = new Mat4Identity();
		}

		final Mat4 finTransform = this.model.mul(view).mul(proj);
		List<Integer> ints = geoObj.getIB();

		// pokud není transformovatelný (Osy) vykreslý pouze osy
		if (geoObj.isTransferable() == false) {
			for (int i = 0; i < ints.size(); i += 2) {
				renderAxes(geoObj, i, finTransform);
			}
		} else {

			// vždy po 3 bodech
			for (int i = 0; i < ints.size(); i += 3) {
				color = geoObj.getColor(i - count * 2);
				count = count + 1;

				// transformace
				int indexA = geoObj.getIB().get(i);
				int indexB = geoObj.getIB().get(i + 1);
				int indexC = geoObj.getIB().get(i + 2);

				Point3D vertexA = geoObj.getVB().get(indexA);
				Point3D vertexB = geoObj.getVB().get(indexB);
				Point3D vertexC = geoObj.getVB().get(indexC);

				vertexA = vertexA.mul(finTransform);
				vertexB = vertexB.mul(finTransform);
				vertexC = vertexC.mul(finTransform);

				// oøezání w
				if (vertexA.getW() < vertexB.getW()) {
					Point3D temp = vertexA;
					vertexA = vertexB;
					vertexB = temp;
				}
				if (vertexB.getW() < vertexC.getW()) {
					Point3D temp = vertexB;
					vertexB = vertexC;
					vertexC = temp;
				}
				if (vertexA.getW() < vertexB.getW()) {
					Point3D temp = vertexA;
					vertexA = vertexB;
					vertexB = temp;
				}
				// interpolace podle W (vykreslení jen toho, co vidíme pøed
				// námi)
				if (vertexA.getW() < zbf.getMinW()) {
				} else if (vertexB.getW() < zbf.getMinW()) {
					double t = (vertexA.getW() - zbf.getMinW()) / (vertexA.getW() - vertexB.getW());
					Point3D x = vertexA.mul(1 - t).add(vertexB.mul(t));

					t = (vertexA.getW() - zbf.getMinW()) / (vertexA.getW() - vertexC.getW());
					Point3D y = vertexA.mul(1 - t).add(vertexC.mul(t));
					drawTriangle(vertexA, x, y);
				} else if (vertexC.getW() < zbf.getMinW()) {
					double t = (vertexB.getW() - zbf.getMinW()) / (vertexB.getW() - vertexC.getW());
					Point3D x = vertexB.mul(1 - t).add(vertexC.mul(t));

					t = (vertexA.getW() - zbf.getMinW()) / (vertexA.getW() - vertexC.getW());
					Point3D y = vertexA.mul(1 - t).add(vertexC.mul(t));

					drawTriangle(vertexA, x, y);
					drawTriangle(vertexA, vertexB, x);
				} else {
					drawTriangle(vertexA, vertexB, vertexC);
				}
			}
		}
	}

	@Override
	public void drawTriangle(Point3D vertexA, Point3D vertexB, Point3D vertexC) {
		// 4D -> 3D dehomog
		Vec3D vecA = vertexA.dehomog().get();
		Vec3D vecB = vertexB.dehomog().get();
		Vec3D vecC = vertexC.dehomog().get();

		// oøezání objemem
		// 3D -> 2D
		if (vecA == null || vecB == null || vecC == null) {
			return;
		}

		if (Math.min(Math.min(vecA.getX(), vecB.getX()), vecC.getX()) > 1.0
				|| Math.max(Math.max(vecA.getX(), vecB.getX()), vecC.getX()) < -1.0
				|| Math.min(Math.min(vecA.getY(), vecB.getY()), vecC.getY()) > 1.0
				|| Math.max(Math.max(vecA.getY(), vecB.getY()), vecC.getY()) < -1.0
				|| Math.min(Math.min(vecA.getZ(), vecB.getZ()), vecC.getZ()) > 1.0
				|| Math.max(Math.max(vecA.getZ(), vecB.getZ()), vecC.getZ()) < 0.0) {
			return;
		}

		// upravení na okno
		vecA = vecA.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0))
				.mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));
		vecB = vecB.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0))
				.mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));
		vecC = vecC.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0))
				.mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));

		// vyplnìn/nevyplnìn ?
		if (fillOrNot == true) {

			// vykreslení
			if (vecA.getY() > vecB.getY()) {
				Vec3D vecHelp = vecA;
				vecA = vecB;
				vecB = vecHelp;
			}
			if (vecB.getY() > vecC.getY()) {
				Vec3D vecHelp = vecB;
				vecB = vecC;
				vecC = vecHelp;
			}
			if (vecA.getY() > vecB.getY()) {
				Vec3D vecHelp = vecA;
				vecA = vecB;
				vecB = vecHelp;
			}

			// interpolace rozdìleného trojuhelníku podle Y a vyplnìní
			for (int y = Math.max((int) vecA.getY() + 1, 0); y <= Math.min((int) vecB.getY(),
					zbf.getHeight() - 1); y++) {
				double t = ((double) y - vecA.getY()) / (vecB.getY() - vecA.getY());
				double x1 = vecA.getX() * (1.0 - t) + vecB.getX() * t;
				double z1 = vecA.getZ() * (1.0 - t) + vecB.getZ() * t;

				double t2 = ((double) y - vecA.getY()) / (vecC.getY() - vecA.getY());
				double x2 = vecA.getX() * (1.0 - t2) + vecC.getX() * t2;
				double z2 = vecA.getZ() * (1.0 - t2) + vecC.getZ() * t2;

				if (x1 > x2) {
					double help = x1;
					x1 = x2;
					x2 = help;
					help = z1;
					z1 = z2;
					z2 = help;
				}

				for (int x = Math.max((int) x1 + 1, 0); x <= Math.min(x2, zbf.getWidth() - 1); x++) {
					double t3 = ((double) x - x1) / (x2 - x1);
					double z3 = z1 * (1.0 - t3) + z2 * t3;
					if (zbf.getPixel(x, y) >= z3 && z3 >= 0.0) {
						zbf.setPixel(x, y, z3);
						img.setRGB(x, y, color);
					}
				}
			}

			for (int y = Math.max((int) vecB.getY() + 1, 0); y <= Math.min(vecC.getY(), zbf.getHeight() - 1); y++) {
				double t = ((double) y - vecB.getY()) / (vecC.getY() - vecB.getY());
				double x1 = vecB.getX() * (1.0 - t) + vecC.getX() * t;
				double z1 = vecB.getZ() * (1.0 - t) + vecC.getZ() * t;

				double t2 = ((double) y - vecA.getY()) / (vecC.getY() - vecA.getY());
				double x2 = vecA.getX() * (1.0 - t2) + vecC.getX() * t2;
				double z2 = vecA.getZ() * (1.0 - t2) + vecC.getZ() * t2;

				if (x1 > x2) {
					double help = x1;
					x1 = x2;
					x2 = help;
					help = z1;
					z1 = z2;
					z2 = help;
				}

				for (int x = Math.max((int) x1 + 1, 0); x <= Math.min(x2, zbf.getWidth() - 1); x++) {
					double t3 = ((double) x - x1) / (x2 - x1);
					double z3 = z1 * (1.0 - t3) + z2 * t3;
					if (zbf.getPixel(x, y) > z3 && z3 >= 0.0) {
						zbf.setPixel(x, y, z3);
						img.setRGB(x, y, color);
					}
				}
			}
		} else {
			// vykreslení drátového modelu
			Graphics g = img.getGraphics();
			g.setColor(new Color(color));
			g.drawLine((int) vecA.getX(), (int) vecA.getY(), (int) vecB.getX(), (int) vecB.getY());
			g.drawLine((int) vecA.getX(), (int) vecA.getY(), (int) vecC.getX(), (int) vecC.getY());
			g.drawLine((int) vecC.getX(), (int) vecC.getY(), (int) vecB.getX(), (int) vecB.getY());
		}
	}

	@Override
	public BufferedImage getBufferedImage() {
		return img;
	}

	public boolean isFillOrNot() {
		return fillOrNot;
	}

	public void setFillOrNot(boolean fillOrNot) {
		this.fillOrNot = fillOrNot;
	}

	private void renderAxes(GeometricObject geoObj, int i, Mat4 finTransform) {
		// transformace
		int indexA = geoObj.getIB().get(i);
		int indexB = geoObj.getIB().get(i + 1);

		Point3D vertexA = geoObj.getVB().get(indexA);
		Point3D vertexB = geoObj.getVB().get(indexB);

		vertexA = vertexA.mul(finTransform);
		vertexB = vertexB.mul(finTransform);

		Vec3D vecA = null;
		Vec3D vecB = null;

		// oøezání w
		if (vertexA.getW() > zbf.getMinW()) {
			Point3D temp = vertexA;
			vertexA = vertexB;
			vertexB = temp;
		}

		if (vertexB.getW() < zbf.getMinW()) {
			return;
		}

		if (vertexA.getW() < zbf.getMinW()) {
			double t = (zbf.getMinW() - vertexA.getW()) / (vertexB.getW() - vertexA.getW());
			vertexA = vertexA.mul(1 - t).add(vertexB.mul(t));
		}

		// 4D -> 3D dehomog
		if (vertexA.dehomog().isPresent()) {
			vecA = vertexA.dehomog().get();
		}

		if (vertexA.dehomog().isPresent()) {
			vecB = vertexB.dehomog().get();
		}
		// oøezání objemem
		// 3D -> 2D
		if (vecA == null || vecB == null) {
			return;
		}
		if (Math.min(vecA.getX(), vecB.getX()) > 1.0 || Math.max(vecA.getX(), vecB.getX()) < -1.0
				|| Math.min(vecA.getY(), vecB.getY()) > 1.0 || Math.max(vecA.getY(), vecB.getY()) < -1.0
				|| Math.min(vecA.getZ(), vecB.getZ()) > 1.0 || Math.max(vecA.getZ(), vecB.getZ()) < -1.0) {
			return;
		}

		// upravení na okno
		vecA = vecA.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0))
				.mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));
		vecB = vecB.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0))
				.mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));
		int x1 = (int) vecA.getX();
		int y1 = (int) vecA.getY();
		double z1 = vecA.getZ();

		int x2 = (int) vecB.getX();
		int y2 = (int) vecB.getY();
		double z2 = vecB.getZ();

		// vykreslení
		double dx = x2 - x1;
		double dy = y2 - y1;

		if (Math.abs(y2 - y1) <= Math.abs(x2 - x1)) {

			// prohozeni vodicich os
			if (x2 < x1) {
				int pomoc = x2;
				x2 = x1;
				x1 = pomoc;

				pomoc = y2;
				y2 = y1;
				y1 = pomoc;

				double pomocZ = z2;
				z2 = z1;
				z1 = pomocZ;
			}

			// vypocet
			double k = (double) dy / dx;
			int int_y;
			double y = (double) y1;

			// tisk img
			for (int x = x1; x <= x2; x++) {
				int_y = (int) Math.round(y);
				// vykreslení s viditelností
				if ((x > 0 && x < img.getWidth() - 1) && (int_y > 0 && int_y < img.getHeight() - 1)) {
					double t3 = ((double) x - x1) / (x2 - x1);
					double z3 = z1 * (1.0 - t3) + z2 * t3;
					if (zbf.getPixel(x, int_y) > z3 && z3 >= 0.0) {
						zbf.setPixel(x, int_y, z3);
						img.setRGB(x, int_y, geoObj.getColor(i / 2));
					}
				}
				y += k;
			}
		} else {

			// prohozeni vodicich os
			if (y2 < y1) {
				int pomoc = x2;
				x2 = x1;
				x1 = pomoc;

				pomoc = y2;
				y2 = y1;
				y1 = pomoc;
			}

			// vypocet
			double k = (double) dx / dy;
			int int_x;
			double x = (double) x1;

			// tisk img
			for (int y = y1; y <= y2; y++) {
				int_x = (int) Math.round(x);
				// vykreslení s viditelností
				if ((int_x > 0 && int_x < img.getWidth() - 1) && (y > 0 && y < img.getHeight() - 1)) {
					double t3 = ((double) y - y1) / (y2 - y1);
					double z3 = z2 * (1.0 - t3) + z1 * t3;
					if (zbf.getPixel(int_x, y) > z3 && z3 >= 0.0) {
						zbf.setPixel(int_x, y, z3);
						img.setRGB(int_x, y, geoObj.getColor(i / 2));
					}
				}
				x += k;
			}
		}
	}
}