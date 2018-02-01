import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JFrame implements KeyListener, MouseListener {
	
	private int numBodies;
	private double kmPerPix;
	private double radius;
	private double maxBodyMass = 5.972e24;
	private double maxBodyDensity = 5.514;
	private double minBodyDensity = .687;
	private double solarMass = 1.989e30;
	private double solarDensity = 1.408;
	private ArrayList<Body> bodies;
	ArrayList<Quadrant> quadrants = new ArrayList<Quadrant>();
	private Quadrant all;
	private boolean debug = true;
	private boolean showQuadrants = false;
	private boolean showTrails = true;
	private int midX = 900;
	private int midY = 500;
	
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
		setFocusable(true);
		requestFocus();
		setVisible(true);
		
		long startTime = System.nanoTime();
		long elapsedTime = startTime;
		int frames = 0;
		int totalFrames = 0;
		int numSeconds = 0;
		while(true) {
			if (debug) {
				elapsedTime = System.nanoTime();
				if ((elapsedTime - startTime) >= 1e9) {
					startTime = System.nanoTime();
					System.out.println("current fps:\t" + frames);
					totalFrames += frames;
					numSeconds++;
					System.out.println("avg fps:\t" + totalFrames / numSeconds);
					frames = 0;
				}
			}
			space.display();
			frames++;
		}
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
				if (showTrails) {
					g.setColor(b.getColor());
					ArrayList<Point> prevPos = b.getPrevPos();
					for (int j = 0; j < prevPos.size(); j++) {
						Point pos = prevPos.get(j);
						g.fillOval((int)((pos.x / kmPerPix) - radius + midX), (int)((pos.y / kmPerPix) - radius + midY), radius * 2, radius * 2);
					}
				}
				g.setColor(Color.WHITE);
				g.fillOval((int)((b.x / kmPerPix) - radius + midX), (int)((b.y / kmPerPix) - radius + midY), radius * 2, radius * 2);

			}
			if (showQuadrants) {
				g.setColor(Color.GREEN);
				for (int i = 0; i < quadrants.size(); i++) {
					Quadrant quad = quadrants.get(i);
					g.drawLine((int)((quad.midX + (quad.size()/2)) / kmPerPix) + midX, (int)(quad.midY / kmPerPix) + midY, (int)((quad.midX - (quad.size()/2)) / kmPerPix) + midX, (int)(quad.midY / kmPerPix) + midY);
					g.drawLine((int)(quad.midX / kmPerPix) + midX, (int)((quad.midY + (quad.size()/2)) / kmPerPix) + midY, (int)(quad.midX / kmPerPix) + midX, (int)((quad.midY - (quad.size()/2)) / kmPerPix) + midY);
				}
			}
		}

		public void display() {
			// Update every body each time it is repainted.
			calculateForces();
			if (numBodies < 4000) {
				paintImmediately(0, 0, 1800, 900);
			}
			else {
				repaint();
			}
		}

	
	}

	// Create a new QuadTree with all the bodies, calculate the forces the tree enacts upon every body, and update the positions of each body.
	public void calculateForces() {
		QuadTree tree = new QuadTree(all);
		ArrayList<Body> combined = new ArrayList<Body>();
		quadrants.clear();
		for (int i = 0; i < numBodies; i++) {
			if (bodies.get(i).inQuad(all)) {
				quadrants.addAll(tree.insert(bodies.get(i)));
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
				System.out.println("collision");
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
			theta = 90;
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

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("focus");
		e.getComponent().requestFocus();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("focus");
		e.getComponent().requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		e.getComponent().requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_UP:
				kmPerPix += 10;
				System.out.println("up");
				break;
			
			case KeyEvent.VK_DOWN:
				kmPerPix -= 10;
				System.out.println("down");
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {
		Main sim = new Main(50, 4.545e9);
	}
}
