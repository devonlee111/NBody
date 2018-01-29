import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame{
	
	private int numBodies;
	private double kmPerPix;
	private double radius;
	private double maxBodyMass = 5.972e26;
	private double maxBodyDensity = 5.514;
	private double minBodyDensity = .687;
	private double solarMass = 1.989e30;
	private double solarDensity = 1.408;
	private ArrayList<Body> bodies;
	private Quadrant all;
	
	public Main(int numBodies, double radius) {
		this.numBodies = numBodies;
		this.radius = radius;
		bodies = new ArrayList<Body>();
		kmPerPix = 10000000;
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
				Body b = bodies.get(i);
				// Draw a dot on the corresponding position on the screen.
				int radius = (int)((b.radius) / kmPerPix);
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
		ArrayList<Body> combined = new ArrayList<Body>();
		for (int i = 0; i < numBodies; i++) {
			if (bodies.get(i).inQuad(all)) {
				tree.insert(bodies.get(i));
			}
		}
		for (int i = 0; i < numBodies; i++) {
			 bodies.get(i).removeForces();
			 if (bodies.get(i).inQuad(all)) {
				 combined = tree.updateForces(bodies.get(i), 1);
			 }
		}
		if (!combined.isEmpty()) {
			for (int i = 0; i < combined.size(); i += 2) {
				Body combinedBody = combined.get(i).collide(combined.get(i + 1));
				bodies.remove(combined.get(i));
				bodies.remove(combined.get(i + 1));
				bodies.add(combinedBody);
				numBodies -= 1;
			}
		}
		for (int i = 0; i < numBodies; i++) {
			bodies.get(i).update(10);
		}
	}
	
	// Randomly generate the required number of bodies uniformly in a circle.
	public void generateBodies() {
		for (int i = 0; i < numBodies - 1; i++) {
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
			bodies.add(b);
		}
		Body sun = new Body (0, 0, 0, 0, solarMass, solarDensity, true);
		System.out.println(sun.radius);
		bodies.add(0, sun);
	}
	
	public static void main(String[] args) {
		Main sim = new Main(1000, 4.545e9);
	}
}
