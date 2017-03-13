package cz.uhk.pgrf.geometry;

import java.awt.image.BufferedImage;
import java.util.List;

import cz.uhk.pgrf.transforms.Mat4;
import cz.uhk.pgrf.transforms.Mat4Identity;
import cz.uhk.pgrf.transforms.Point3D;
import cz.uhk.pgrf.transforms.Vec3D;

/**
 * Zobrazovací øetìzec.
 * 
 * @author Tomáš Novák
 * @version 2016
 */ 

public class WireFrameRenderer implements Renderable {

    private BufferedImage img;
    private Zbuffer zbf;
    private Mat4 view;
    private Mat4 proj;
    private Mat4 model;
    private int count = 0;
    
    private int color;

    //nastavené matic pohledu a projekce
    @Override
    public void setBufferedImage(BufferedImage img) {
        this.img = img;
        zbf = new Zbuffer(img.getWidth(), img.getHeight());
        zbf.setMinW(0.1);
        zbf.Clear();
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
    
    //vykreslí list objektù
    @Override
    public void render(List<Objekt3D> objs) {
        for (Objekt3D obj : objs) {
            render(obj);
        }

    }
    
    //vykreslí 1 daný objekt
    @Override
    public void render(Objekt3D obj){
        for(int i = 0; i < obj.getCount();i++){
            render(obj.get(i),obj.getMat());
        }
    }
    
    //zobrazovací øetìzec
    @Override
    public void render(GeometricObject geoObj, Mat4 model) {
    	
    	count = 0;
    	zbf.Clear();
    	
    	if(geoObj.isTransferable()){
    		this.model = geoObj.getMat().mul(model);
    	} else {
    		this.model = new Mat4Identity();
    	}

        final Mat4 finTransform = this.model.mul(view).mul(proj);
        List<Integer> ints = geoObj.getIB();

        for (int i = 0; i < ints.size(); i += 3) {
        	
//        	Random r = new Random();
//        	int Low = 0;
//        	int High = 255;
//        	color = new Color(r.nextInt(High-Low) + Low, r.nextInt(High-Low) + Low, r.nextInt(High-Low) + Low);
        	color = geoObj.getColor(i - count*2);
        	count = count +1;

        	//transformace
        	
            int indexA = geoObj.getIB().get(i);
            int indexB = geoObj.getIB().get(i + 1);
            int indexC = geoObj.getIB().get(i + 2);
            
            Point3D vertexA = geoObj.getVB().get(indexA);
            Point3D vertexB = geoObj.getVB().get(indexB);
            Point3D vertexC = geoObj.getVB().get(indexC);

            vertexA = vertexA.mul(finTransform);
            vertexB = vertexB.mul(finTransform);
            vertexC = vertexC.mul(finTransform);
            
            //oøezání w  
            if (vertexA.getW() < vertexB.getW()){
                Point3D temp = vertexA;
                vertexA=vertexB;
                vertexB=temp;
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
            
            if(vertexA.getW() < zbf.getMinW()){
            	return;
            }
            
            if(vertexB.getW() < zbf.getMinW()){
                double t = (vertexA.getW()-zbf.getMinW())/(vertexA.getW()-vertexB.getW());
                Point3D x = vertexA.mul(1-t).add(vertexB.mul(t));
                
                t = (vertexA.getW() - zbf.getMinW())/(vertexA.getW()-vertexC.getW());
                Point3D y = vertexA.mul(1-t).add(vertexC.mul(t));
                draw(vertexA, x, y);
                return;
            }
            
            if(vertexC.getW() < zbf.getMinW()){
                double t = (vertexB.getW() - zbf.getMinW())/(vertexB.getW()-vertexC.getW());
                Point3D x = vertexB.mul(1-t).add(vertexC.mul(t));
                
                t = (vertexA.getW() - zbf.getMinW())/(vertexA.getW()-vertexC.getW());
                Point3D y = vertexA.mul(1-t).add(vertexC.mul(t));
                
                draw(vertexA, x, y);
                draw(vertexA, vertexB, x);
                return;
            }
            draw(vertexA, vertexB, vertexC);
        }

    }

	@Override
	public void draw(Point3D vertexA, Point3D vertexB, Point3D vertexC) {
        //4D -> 3D dehomog
        Vec3D vecA = vertexA.dehomog().get();
        Vec3D vecB = vertexB.dehomog().get();
        Vec3D vecC = vertexC.dehomog().get();

        
        // oøezání objemem
        	//3D -> 2D
        if(vecA == null || vecB == null || vecC == null){
        	return;
        }
        if (Math.min(Math.min(vecA.getX(), vecB.getX()), vecC.getX()) > 1.0 || 
            Math.max(Math.max(vecA.getX(), vecB.getX()), vecC.getX()) < -1.0 || 
            Math.min(Math.min(vecA.getY(), vecB.getY()), vecC.getY()) > 1.0 ||
            Math.max(Math.max(vecA.getY(), vecB.getY()), vecC.getY()) < -1.0 || 
            Math.min(Math.min(vecA.getZ(), vecB.getZ()), vecC.getZ()) > 1.0 || 
            Math.max(Math.max(vecA.getZ(), vecB.getZ()), vecC.getZ()) < 0.0) 
        {
        	return;
        }
            
        //upravení na okno
        vecA = vecA.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0)).mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));
        vecB = vecB.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0)).mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));
        vecC = vecC.mul(new Vec3D(1.0, -1.0, 1.0)).add(new Vec3D(1.0, 1.0, 0.0)).mul(new Vec3D((img.getWidth() - 1) / 2, (img.getHeight() - 1) / 2, 1.0));     

        //vykreslení
//        img.setRGB(500, 500, 0xff0000);
//        Graphics g = img.getGraphics();
//        g.setColor(new Color(geoObj.getColor(i/2)));
//        g.drawLine(x1, y1, x2, y2);
  
        
        
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
        
        for (int y = Math.max((int)vecA.getY() + 1, 0); y <= Math.min((int)vecB.getY(), zbf.getHeight() - 1); y++) {
        	double t = (y - vecA.getY()) / (vecB.getY() - vecA.getY());
        	double x1 = vecA.getX() * (1.0 - t) + vecB.getX() * t;
        	double z1 = vecA.getZ() * (1.0 - t) + vecB.getZ() * t;
        	
        	double t2 = (y - vecA.getY()) / (vecC.getY() - vecA.getY());
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

        	for (int x = Math.max((int)x1 + 1, 0); x <= Math.min(x2, zbf.getWidth() - 1); x++) {
        		double t3 = (x - x1) / (x2 - x1);
        		double z3 = z1 * (1.0 - t3) + z2 * t3;
        		if (zbf.getPixel(x, y) >= z3 && z3 >= 0.0) {
        			zbf.setPixel(x, y, z3);
        			img.setRGB(x, y, color);
        		}
        	}
        }
//        
//        

        for (int y = Math.max((int)vecB.getY() + 1, 0); y <= Math.min(vecC.getY(), zbf.getHeight() - 1); y++) {
        	double t = (y - vecB.getY()) / (vecC.getY() - vecA.getY());
        	double x1 = vecB.getX() * (1.0 - t) + vecC.getX() * t;
        	double z1 = vecB.getZ() * (1.0 - t) + vecC.getZ() * t;
        	
        	double t2 = (y - vecA.getY()) / (vecC.getY() - vecA.getY());
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

        	for (int x = Math.max((int)x1 + 1, 0); x <= Math.min(x2, zbf.getWidth() - 1); x++) {
        		double t3 = (x - x1) / (x2 - x1);
        		double z3 = z1 * (1.0 - t3) + z2 * t3;
        		if (zbf.getPixel(x, y) >= z3 && z3 >= 0.0) {
        			zbf.setPixel(x, y, z3);
        			img.setRGB(x, y, color);
        		}
        	}
        }
	}

	@Override
	public BufferedImage getBufferedImage() {
		return img;
	}

}