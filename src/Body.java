public class Body {
	
	private final double G = 6.67408e-11; // Gravitational constant
	public double x;
	public double y;
	private double xVel;
	private double yVel;
	private double xForce;
	private double yForce;
	public double mass;
	public double density;
	public double radius;
	public boolean stationary;
	
	public Body(double x, double y, double xVel, double yVel, double mass, double density, boolean stationary) {
		this.x = x;
		this.y = y;
		this.xVel = xVel;
		this.yVel = yVel;
		this.mass = mass;
		this.density = density;
		this.radius = Math.cbrt((mass / density) / ((4/3) * Math.PI));
		this.stationary = stationary;
	}
	
	// Update the body's position and velocity with the given timestep.
	public void update(double timestep) {
		if (!stationary) {
			xVel += timestep * (xForce / mass);
			yVel += timestep * (yForce / mass);
			x += timestep * xVel;
			y += timestep * yVel;
		}
	}
	
	// Calculate the distance from this body to another.
	public double distanceTo(Body b) {
		double xDist = b.x - x;
		double yDist = b.y - y;
		return Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
	}
	
	public void setXVel(double xVel) {
		this.xVel = xVel;
	}
	
	public void setYVel(double yVel) {	
		this.yVel = yVel;
	}
	
	// Set the forces acting upon the body to 0.
	public void removeForces() {
		xForce = 0;
		yForce = 0;
	}
	
	// Calculate the force that the other body enacts upon this body and add the forces to this body.
	public void addForce(Body b) {
		double xDist = b.x - x;
		double yDist = b.y - y;
		double dist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		double force = (G * mass * b.mass) / (Math.pow(dist, 2));
		xForce += force * xDist / dist;
		yForce += force * yDist / dist;
	}
	
	// Check if this body is in the given quadrant
	public boolean inQuad(Quadrant quad) {
		return quad.contains(x, y);
	}
	
	// Combine this body with another body
	// Store the center of mass in this body and update the mass
	public Body combine(Body b) {
		double newX = (float)(((this.mass * this.x) + (b.mass * b.x))/(this.mass + b.mass));
		double newY = (float)(((this.mass * this.y) + (b.mass * b.y))/(this.mass + b.mass));
		double newMass = this.mass + b.mass;
		return new Body(newX, newY, 0, 0, newMass, 0, false);
	}
	
	// Collide this body with another body in an inelastic collision
	public Body collide(Body b) {
		double newX = (float)(((this.mass * this.x) + (b.mass * b.x))/(this.mass + b.mass));
		double newY = (float)(((this.mass * this.y) + (b.mass * b.y))/(this.mass + b.mass));
		double newMass = this.mass + b.mass;
		double newXVel = ((this.mass * this.x) + (b.mass * b.x)) / (this.mass + b.mass);
		double newYVel = ((this.mass * this.y) + (b.mass + b.y)) / (this.mass + b.mass);
		double thisVolume = (4/3) * Math.PI * Math.pow(this.radius, 3);
		double bVolume = (4/3) * Math.PI * Math.pow(b.radius, 3);
		double newDensity = ((this.density * thisVolume) + (b.density * bVolume)) / (thisVolume + bVolume);
		boolean fixed = false;
		if (this.stationary || b.stationary) {
			fixed = true;
			if (this.stationary) {
				newX = this.x;
				newY = this.y;
			}
			else {
				newX = b.x;
				newY = b.y;
			}
		}
		return new Body(newX, newY, newXVel, newYVel, newMass, newDensity, fixed);
	}
	
	// Return a point with the body's current positition
	public Point pos() {
		Point p = new Point(x, y);
		return p;
	}
}