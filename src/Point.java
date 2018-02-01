public class Point {
	public double x;
	public double y;
	public double z;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double distTo(Point p) {
		return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2) + Math.pow(z - p.z, 2));
	}
}