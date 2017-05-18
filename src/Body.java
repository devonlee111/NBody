public class Body {
	
	public double x;
	public double y;
	public double mass;
	
	public Body(double x, double y, double mass) {
		
	}
	
	public void update(double timeStep) {
		
	}
	
	public boolean inQuad(Quadrant quad) {
		return false;
	}
	
	public Point pos() {
		Point p = new Point(x, y);
		return p;
	}
}