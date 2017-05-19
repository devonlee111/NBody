import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{
	public Main() {
		setSize(1000, 1000);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Space space = new Space();
		space.setSize(800, 800);
		
		JPanel input = new JPanel();
		input.setLayout(new FlowLayout());
		JButton runBtn = new JButton("Run");
		input.add(runBtn);
		
		add(input, BorderLayout.NORTH);
		add(space, BorderLayout.CENTER);
		setVisible(true);
	}
	
	private class Space extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.BLACK);
		}
	}
	
	public static void main(String[] args) {
		Main simulation = new Main();
	}
}
