import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;


public class Graph extends JPanel{
	private static final long serialVersionUID = 1L;
	List<Integer> coordX;
	List<Integer> coordY;
	int maxX;
	int maxY;
	JFrame frame;
	boolean linearRegression;
	boolean quadraticRegression;
	boolean nLognRegression;
	double coeffLinRegression;
	double coeffQuadRegression;
	double coeffnLognRegression;
    
    static final int marginX=100;
    static final int marginY=50;
    static final int arrowSize= 7;
    static final int radius=5;
    static final int defaultWidth=600;
    static final int defaultHeight=600;
    static final int curveThickness=2;
    
    public Graph(List<Integer> _coordX, List<Integer> _coordY) {
    	assert (_coordX.size()==_coordY.size()) : "Les deux tableaux de coordonnées n'ont pas la même taille.";
    	coordX = new ArrayList<Integer>(_coordX);
    	coordY = new ArrayList<Integer>(_coordY);
    	linearRegression = false;
    	quadraticRegression = false;
    	nLognRegression = false;
    	
    	maxX=1;
        for(int i=0;i<coordX.size();i++)
            if(coordX.get(i)>maxX)
                maxX=coordX.get(i);
        maxY=1;
        for(int i=0;i<coordY.size();i++)
            if(coordY.get(i)>maxY)
                maxY=coordY.get(i);
        

        frame =new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    	
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        //Graphic 2D class again extends Graphics class to provide control over geometry.
        //This is a fundamental class for two-dimensional shapes.
        Graphics2D g2d=(Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int width=getWidth();
        int height=getHeight();
        
        //X axis 
        drawAxis(g2d, marginX, height-marginY, width-marginX, height-marginY, maxX, "Taille" );
        //Y axis	
        drawAxis(g2d, marginX, height-marginY, marginX, marginY, maxY, "Temps");

        double scaleX=(double)(width-2*marginX)/maxX;
        double scaleY=(double)(height-2*marginY)/maxY;
        g2d.setPaint(Color.BLUE);
        //dots
        for(int i=0;i<coordX.size();i++){
            double x=marginX+scaleX*coordX.get(i);
            double y=height-marginY-scaleY*coordY.get(i);
            g2d.fill(new Ellipse2D.Double(x-radius,y-radius,2*radius,2*radius));
        }  
        if (linearRegression) {
        	int x1 = marginX; 
        	int y1 = height-marginY;
        	int x2 = width-marginX;
        	int y2 = (int) (height-marginY-scaleY*(coeffLinRegression*maxX));
        	Line2D.Double lineReg = new Line2D.Double(x1,y1,x2,y2);
        	g2d.setStroke(new BasicStroke(curveThickness));
        	g2d.setColor(Color.RED);
        	g2d.draw(lineReg);
        }
        if (quadraticRegression) {
        	g2d.setColor(Color.GREEN);
    		g2d.setStroke(new BasicStroke(curveThickness));
        	int yPrev;
        	int y=height-marginY;
        	for(int k=marginX;k<width-marginX;k++) {
        		double x = ((k-marginX)/scaleX);
        		yPrev=y;
        		y = (int) (height-marginY-scaleY*(coeffQuadRegression*x*x));
        		g2d.draw(new Line2D.Double(k-1,yPrev,k,y));
        	}
        }
        if (nLognRegression) {
        	g2d.setColor(Color.YELLOW);
    		g2d.setStroke(new BasicStroke(curveThickness));
        	int yPrev;
        	int y=height-marginY;
        	for(int k=marginX+1;k<width-marginX;k++) {
        		double x = ((k-marginX)/scaleX);
        		yPrev=y;
        		double tmp = x*Math.log(x);
        		y = (int) (height-marginY-scaleY*(coeffnLognRegression*tmp));
        		g2d.draw(new Line2D.Double(k-1,yPrev,k,y));
        	}
        }
    }
    
    private static void drawAxis(Graphics2D g, int x1, int y1, int x2, int y2, int maxValue, String legend) {
    	Line2D.Double axis = new Line2D.Double(x1,y1,x2,y2);
    	g.draw(axis);
    	int numDigits = (int) (Math.log10(maxValue));
    	double scal = maxValue/Math.pow(10,numDigits);
    	int fact = (int)(scal);
    	float incr = (fact<5)?.5f:1;
    	Polygon arrowHead = new Polygon();  
    	Font currentFont = g.getFont();
    	if (x1==x2) {
	    	arrowHead.addPoint( x2,y2-2*arrowSize);
	    	arrowHead.addPoint( x2-arrowSize,y2);
	    	arrowHead.addPoint( x2+arrowSize,y2);
	    	for(float k=incr;k<=scal;k+=incr) {
	    		int posY = (int) (y1+k*(y2-y1)/scal);
	    		g.drawString(k+"e"+numDigits,x1-40,posY+3);
	    		g.draw(new Line2D.Double(x1, posY,x1+3,posY));
	    	}
	    	AffineTransform orig = g.getTransform();
	    	g.rotate(-Math.PI/2);
	    	g.setFont(currentFont.deriveFont( 20.0f));
	    	g.drawString(legend,-(y2+y1)/2-20,x1-55);
	    	g.setTransform(orig);
	    }
    	else {//y1==y2
    		arrowHead.addPoint( x2+2*arrowSize,y2);
	    	arrowHead.addPoint( x2,y2-arrowSize);
	    	arrowHead.addPoint( x2,y2+arrowSize);
	    	for(float k=incr;k<=scal;k+=incr) {
	    		int posX = (int) (x1+k*(x2-x1)/scal);
	    		g.drawString(k+"e"+numDigits,posX-9,y1+15);
	    		g.draw(new Line2D.Double(posX,y1-3,posX,y1));
	    	}
	    	g.setFont(currentFont.deriveFont( 20.0f));
	    	g.drawString(legend,(x2+x1)/2-20,y1+40);
    	}
        g.fill(arrowHead);
        g.setFont(currentFont);
    }

    public void display(int width, int height) {
        frame.add(this);
        frame.setSize(600+2*marginX,600+2*marginY);
        frame.setLocation(200,200);
        frame.setVisible(true);
    }
    
    public void display() {
    	this.display(defaultWidth,defaultHeight);
    }
    
    public void addLine(double a) {
    	linearRegression=true;
    	coeffLinRegression = a;
    	super.repaint();
    	
    }
    
    public void addQuadratic(double a) {
    	quadraticRegression=true;
    	coeffQuadRegression = a;
    	super.repaint();
    }
    
    public void addnLogn(double a) {
    	nLognRegression=true;
    	coeffnLognRegression = a;
    	super.repaint();
    	
    }
    
}