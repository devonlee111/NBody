public class Body {
	
	private final double G = 6.67408e-11; // Gravitational constant
	public double x;
	public double y;
	private double xVel;
	private double yVel;
	private double xForce;
	private double yForce;
	public double mass;
	
	public Body(double x, double y, double xVel, double yVel, double xForce, double yForce ,double mass) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		this.xForce = xForce;
	}
	
	// Update the body's position and velocity with the given timestep.
	public void update(double timestep) {
		xVel += timestep * xForce / mass;
		yVel += timestep * yForce / mass;
		x += timestep * xVel;
		y += timestep * yVel;
	}
	
	public double distanceTo(Body b) {
		double xDist = x - b.x;
		double yDist = y - b.y;
		return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
	}
	
	// Calculate the force that the other body enacts upon this body and add the forces to this body.
	public void addForce(Body b) {
		double xDist = x - b.x;
		double yDist = y - b.y;
		double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		double force = (G * mass * b.mass) / (Math.pow(dist, 2));
		xForce += force * xDist / dist;
		yForce += force * yDist/ dist;
	}
	
	// Check if this body is in the given quadrant
	public boolean inQuad(Quadrant quad) {
		return false;
	}
	
	// Combine this body with another body
	// Store the center of mass in this body and update the mass
	public void combine(Body b) {
		x = ((mass * x) + (b.mass * b.x))/(mass + b.mass);
		y = ((mass * y) + (b.mass * b.y))/(mass + b.mass);
		mass += b.mass;
	}
	
	// Return a point with the body's current positition
	public Point pos() {
		Point p = new Point(x, y);
		return p;
	}
}