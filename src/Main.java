public class Main {
	public static void main(String[] args) {
			Quadrant q = new Quadrant(0.0, 0.0, 100.0);
			Quadrant nw = q.NW();
			Quadrant ne = q.NE();
			Quadrant sw = q.SW();
			Quadrant se = q.SE();
			
			Point p = nw.mid();
			System.out.println("North-West | " + p.X() + " | " + p.Y() + " | " + nw.size());
			
			p = ne.mid();
			System.out.println("North-East | " + p.X() + " | " + p.Y() + " | " + nw.size());
	
			p = sw.mid();
			System.out.println("South-West | " + p.X() + " | " + p.Y() + " | " + nw.size());
				
			p = se.mid();
			System.out.println("South-East | " + p.X() + " | " + p.Y() + " | " + nw.size());
	}
}
