import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
	
	private int numBodies;
	private double kmPerPix;
	private double radius;
	private double maxBodyMass = 1.898e27; 
	private double solarMass = 1.989e30;
	private double systemMass = 0;
	private Point origin = new Point(400, 400);
	private Body[] bodies;
	private Quadrant all;
	
	public Main(int numBodies, double radius) {
		this.numBodies = numBodies;
		this.radius = radius;
		bodies = new Body[numBodies];
		kmPerPix = radius / 400;
		all = new Quadrant(0, 0, radius * 4);
		
		generateBodies();
		
		setSize(900, 900);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Space space = new Space();
		
		JPanel input = new JPanel();
		input.setLayout(new FlowLayout());
		JButton runBtn = new JButton("Test");
		input.add(runBtn);
		
		add(input, BorderLayout.NORTH);
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
				g.fillOval((int)(b.x / kmPerPix) + 400, (int)(b.y / kmPerPix) + 400, 2, 2);
			}
			// Update every body each time it is repainted.
			calculateForces();
			repaint();
		}
	}
	
	// Create a new QuadTree with all the bodies, calculate the forces the tree enacts upon every body, and update the positions of each body.
	public void calculateForces() {
		//System.out.println(bodies[1].distanceTo(bodies[0]));
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
		for (int i = 1; i < numBodies; i++) {
			double mass = Math.random() * maxBodyMass + 1;
			systemMass += mass;
			double dist = Math.sqrt(Math.random()) * radius + 1;
			//dist = radius;
			double theta = Math.random() * 360;
			double thetaV = (theta + 90) % 360;
			//System.out.println(theta + " | " + thetaV);
			theta *= (Math.PI / 180);
			thetaV *= (Math.PI / 180);
			double x = (dist * Math.cos(theta));
			double y = -(dist * Math.sin(theta)); 	
			double vel = Math.sqrt((6.67408e-11 * (solarMass + mass)) / dist);
			double xVel = (vel * Math.cos(thetaV));
			double yVel = -(vel * Math.sin(thetaV));
			//System.out.println(theta + " | " + thetaV + ": " + xVel + ", " + yVel);
			Body b = new Body(x, y, xVel, yVel, mass, false);
			bodies[i] = b;
		}
		bodies[0] = new Body (0, 0, 0, 0, solarMass, true);
	}
	
	public static void main(String[] args) {
		Main sim = new Main(10000, 4.545e9);
	}
}
