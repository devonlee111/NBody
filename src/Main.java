import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
	
	public int numBodies;
	public double kmPerPix;
	public double radius;
	public double systemMass = 1.989e30;
	public Point origin = new Point(400, 400);
	public Body[] bodies;
	private Quadrant all;
	
	public Main(int numBodies, double radius) {
		this.numBodies = numBodies;
		this.radius = radius;
		bodies = new Body[numBodies];
		kmPerPix = radius / 400;
		all = new Quadrant(0, 0, radius * 2);
		
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
		QuadTree tree = new QuadTree(all);
		for (int i = 0; i < numBodies; i++) {
			if (bodies[i].inQuad(all)) {
				tree.insert(bodies[i]);
			}
		}
		for (int i = 0; i < numBodies; i++) {
			 bodies[i].removeForces();
			 if (bodies[i].inQuad(all)) {
				 tree.updateForces(bodies[i], 2);
			 }
		}
		for (int i = 0; i < numBodies; i++) {
			bodies[i].update(10);
		}
	}
	
	// Randomly generate the required number of bodies uniformly in a circle.
	public void generateBodies() {
		for (int i = 0; i < numBodies; i++) {
			double dist = Math.sqrt(Math.random()) * radius + 1;
			double theta = Math.random() * 360;
			double x = (dist * Math.cos(theta));
			double y = (dist * Math.sin(theta));
			Body b = new Body(x, y, 0, 0, systemMass / numBodies);
			bodies[i] = b;
		}
	}
	
	public static void main(String[] args) {
		Main sim = new Main(10000, 4.545e9);
	}
}
