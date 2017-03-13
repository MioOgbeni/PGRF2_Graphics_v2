package cz.uhk.pgrf.geometry;

public class Zbuffer {
	private double [][] buffer;
	private double minW;
	
	public double getMinW() {
		return minW;
	}

	public void setMinW(double minW) {
		this.minW = minW;
	}

	public Zbuffer(int w, int h){
		buffer = new double[w][h]; 
		minW = 0.5;
	}
	
	public int getWidth() {
		return buffer.length;
	}

	public int getHeight() {
		return buffer[0].length;
	}

	public void setPixel(int x, int y, Double z) {
		buffer[x][y] = z; 
		
	}

	public Double getPixel(int x, int y) {
		return buffer[x][y];
	}

	public void Clear() {
		for(int i=0; i<buffer.length; i++){
			for(int y=0; y<buffer[0].length; y++){
				buffer[i][y] = 1.0;
			}
		}
		
	}
}
