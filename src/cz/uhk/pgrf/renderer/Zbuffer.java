package cz.uhk.pgrf.renderer;

/**
 * T��da zbufferu.
 * 
 * @author Tom� Nov�k
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

	// nastaven�/z�sk�n� W
	public double getMinW() {
		return minW;
	}

	public void setMinW(double minW) {
		this.minW = minW;
	}

	// z�sk�n� ���ky/v��ky zbufferu
	public int getWidth() {
		return buffer.length;
	}

	public int getHeight() {
		return buffer[0].length;
	}

	// nastaven�/z�sk�n� Z na dan� pozici
	public void setPixel(int x, int y, Double z) {
		buffer[x][y] = z;
	}

	public Double getPixel(int x, int y) {
		return buffer[x][y];
	}

	// nastaven� Z na defaultn� hodnotu
	public void Clear() {
		for (int i = 0; i < buffer.length; i++) {
			for (int y = 0; y < buffer[0].length; y++) {
				buffer[i][y] = 1.0;
			}
		}
	}
}
