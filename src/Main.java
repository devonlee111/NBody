import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
	
	private int numBodies;
	private double kmPerPix;
	private double radius;
	private double maxBodyMass = 5.972e24;
	private double maxBodyDensity = 5.514;
	private double minBodyDensity = .687;
	private double solarMass = 1.989e30;
	private double solarDensity = 1.408;
	private Body[] bodies;
	private Quadrant all;
	
	public Main(int numBodies, double radius) {
		this.numBodies = numBodies;
		this.radius = radius;
		bodies = new Body[numBodies];
		kmPerPix = 100000;//radius / 100;
		all = new Quadrant(0, 0, radius * 4);
		
		generateBodies();
		
		setSize(1800, 900);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Space space = new Space();
		add(space, BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	// A JPanel that is used to represent and display the space in which the bodies reside
	private class Space extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.BLACK);
			g.setColor(Color.WHITE);
			for (int i = 0; i < numBodies; i++) {
				Body b = bodies[i];
				// Draw a dot on the corresponding position on the screen.
				int radius = (int)((b.radius/1000) / kmPerPix);
				if (radius < 1) {
					radius = 1;
				}
				g.fillOval((int)((b.x / kmPerPix) - radius + 800), (int)((b.y / kmPerPix) - radius + 500), radius * 2, radius * 2);
			}
			// Update every body each time it is repainted.
			calculateForces();
			repaint();
		}
	}
	
	// Create a new QuadTree with all the bodies, calculate the forces the tree enacts upon every body, and update the positions of each body.
	public void calculateForces() {
		QuadTree tree = new QuadTree(all);
		for (int i = 0; i < numBodies; i++) {
			if (bodies[i].inQuad(all)) {
				tree.insert(bodies[i]);
			}
		}
		for (int i = 0; i < numBodies; i++) {
			 bodies[i].removeForces();
			 if (bodies[i].inQuad(all)) {
				 tree.updateForces(bodies[i], 1);
			 }
		}
		for (int i = 0; i < numBodies; i++) {
			bodies[i].update(10);
		}
	}
	
	// Randomly generate the required number of bodies uniformly in a circle.
	public void generateBodies() {
		for (int i = 0; i < numBodies; i++) {
			double mass = Math.random() * maxBodyMass + 1;
			double density = (Math.random() * (maxBodyDensity - minBodyDensity)) + minBodyDensity;
			double dist = Math.sqrt(Math.random()) * radius + 1;
			double theta = Math.random() * 360;
			double thetaV = (theta + 90) % 360;
			theta *= (Math.PI / 180);
			thetaV *= (Math.PI / 180);
			double x = (dist * Math.cos(theta));
			double y = -(dist * Math.sin(theta));
			double vel = Math.sqrt((6.67408e-11 * (solarMass + mass)) / (dist));
			double xVel = (vel * Math.cos(thetaV));
			double yVel = -(vel * Math.sin(thetaV));
			Body b = new Body(x, y, xVel, yVel, mass, density, false);
			bodies[i] = b;
		}
		bodies[0] = new Body (0, 0, 0, 0, solarMass, solarDensity, true);
	}
	
	public static void main(String[] args) {
		Main sim = new Main(10000, 4.545e9);
	}
}
