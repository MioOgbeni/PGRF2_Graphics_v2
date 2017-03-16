package cz.uhk.pgrf.renderer;

/**
 * Tøída zbufferu.
 * 
 * @author Tomáš Novák
 * @version 2017
 */

public class Zbuffer {
	private double[][] buffer;
	private double minW;

	// konstruktor
	public Zbuffer(int w, int h) {
		buffer = new double[w][h];
		minW = 1;
	}

	// nastavení/získání W
	public double getMinW() {
		return minW;
	}

	public void setMinW(double minW) {
		this.minW = minW;
	}

	// získání šíøky/výšky zbufferu
	public int getWidth() {
		return buffer.length;
	}

	public int getHeight() {
		return buffer[0].length;
	}

	// nastavení/získání Z na dané pozici
	public void setPixel(int x, int y, Double z) {
		buffer[x][y] = z;
	}

	public Double getPixel(int x, int y) {
		return buffer[x][y];
	}

	// nastavení Z na defaultní hodnotu
	public void Clear() {
		for (int i = 0; i < buffer.length; i++) {
			for (int y = 0; y < buffer[0].length; y++) {
				buffer[i][y] = 1.0;
			}
		}
	}
}
