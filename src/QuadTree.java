public class QuadTree {

	private Body body;
	private Quadrant q;
	private QuadTree NE;
	private QuadTree NW;
	private QuadTree SE;
	private QuadTree SW;
	
	public QuadTree(Quadrant q) {
		this.q = q;
	}

	// Insert a new body into the tree and update all internal bodies.
	public void insert(Body body) {
		// Check if the current node is external and contains a body. 
		// Add the body if it doesn't.
		if (this.body == null) {
			this.body = body;
		}
		
		// Check if the current node is internal or external
		// If the body is an internal node, then combine the two bodies and update sub-quadrants
		// Create the correct sub-quadrant if the correct sub-quadrant does not exist.
		else if (!isExternalNode()) {
			this.body = this.body.combine(this.body, body);
			Quadrant northeast = q.NE();
			if (body.inQuad(northeast)) {
				if (NE == null) {
					NE = new QuadTree(northeast);
				}
				NE.insert(body);
			}
			else {
				Quadrant northwest = q.NW();
				if (body.inQuad(northwest)) {
					if (NW == null) {
						NW = new QuadTree(northwest);
					}
					NW.insert(body);
				}
				else {
					Quadrant southeast = q.SE();
					if (body.inQuad(southeast)) {
						if (SE == null) {
							SE = new QuadTree(southeast);
						}
						SE.insert(body);
					}
					else {
						Quadrant southwest = q.SW();
						if (body.inQuad(southwest)) {
							if (SW == null) {
								SW = new QuadTree(southwest);
							}
							SW.insert(body);
						}
					}
				}
			}
		}
		// If the node is an external node, then put the current body in the correct sub-quadrant and insert the new body into this quadrant.
		// Create the correct sub-quadrant if the correct sub-quadrant does not exist.
		else {
			Quadrant northeast = q.NE();
			if (this.body.inQuad(northeast)) {
				if (NE == null) {
					NE = new QuadTree(q.NE());
				}
				NE.insert(this.body);
			}
			else {
				Quadrant northwest = q.NW();
				if (this.body.inQuad(northwest)) {
					if (NW == null) {
						NW = new QuadTree(q.NW());
					}
					NW.insert(this.body);
				}
				else {
					Quadrant southeast = q.SE();
					if (this.body.inQuad(southeast)) {
						if (SE == null) {
							SE = new QuadTree(q.SE());
						}
						SE.insert(this.body);
					}
					else {
						Quadrant southwest = q.SW();
						if (this.body.inQuad(southwest)) {
							if (SW == null) {
								SW = new QuadTree(q.SW());
							}
							SW.insert(this.body);
						}
						else {
							System.out.println("failed to insert into sub-quadrant");
						}
					}
				}
			}
			insert(body);
		}
	}
	
	// Check if this node is an external node.
	private boolean isExternalNode() {
		if (NE == null && NW == null && SE == null && SW == null) {
			return true;
		}
		return false;
	}
	
	// Check if the given node is an external node.
	public boolean isExternalNode(QuadTree node) {
		if (node.NE == null && node.NW == null && node.SE == null && node.SW == null) {
			return true;
		}
		return false;
	}
	
	// Given a body calculate the forces that all other bodies in the tree enact upon it.
	public void updateForces(Body b, double theta) {
		// Calculate the force one of the bodies in the tree enacts on the given body directly if you reach an external node.
		if (isExternalNode()) {
			if (body != b) {
				b.addForce(body);
			}
		}
		// If the internal node is sufficiently far away from the given body approximate the forces on the other body using the center of mass.
		else if (q.size() / body.distanceTo(b) < theta) {
			b.addForce(body);
		}
		// Recursively calculate the forces the bodies in each sub-quadrant enacts upon the given body.
		else {
			if (NE != null) {
				NE.updateForces(b, theta);
			}
			if (NW != null) {
				NW.updateForces(b, theta);
			}
			if (SE != null) {
				SE.updateForces(b, theta);
			}
			if (SW != null) {
				SW.updateForces(b, theta);
			}			
		}
	}
}