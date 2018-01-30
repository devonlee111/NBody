public class Quadrant {
	
	public double midX;
	public double midY;
	public double size;
	
	// Create new quadrant with side length size and midpoint (midX, midY)
	public Quadrant(double midX, double midY, double size) {
		this.midX = midX;
		this.midY = midY;
		this.size = size;
	}
	
	// Return the side length of the quadrant
	public double size() {
		return size;
	}
	
	// Check if this quadrant contains the specified body
	public boolean contains(double x, double y) {
		if (x <= midX + size / 2 && x >= midX - size / 2 && y <= midY + size / 2 && y >= midY - size / 2 ) {
			return true;
		}
		return false;
	}
	
	// Return a point with (x, y) coordinates at the center of the quadrant
	public Point mid() {
		Point p = new Point(midY, midX);
		return p;
	}
	
	// Create Upper Right sub-quadrant
	public Quadrant NE() {
		Quadrant q = new Quadrant(midX + size/4, midY + size/4, size/2);
		return q;
	}
	
	// Create Upper Left sub-quadrant
	public Quadrant NW() {
		Quadrant q = new Quadrant(midX - size/4, midY + size/4, size/2);
		return q;
	}
	
	// Create Lower Right sub-quadrant
	public Quadrant SE() {
		Quadrant q = new Quadrant(midX + size/4, midY - size/4, size/2);
		return q;
	}
	
	// Create Lower Left sub-quadrant
	public Quadrant SW() {
		Quadrant q = new Quadrant(midX - size/4, midY - size/4, size/2);
		return q;
	}
}