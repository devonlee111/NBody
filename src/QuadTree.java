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

	public void insert(Body body) {
		// Check if the current node is external and contains a body. 
		// Add the body if it doesn't.
		if (this.body == null) {
			this.body = body;
			return;
		}
		
		// Check if the current node is internal or external
		//If the body is an internal node, then combine the two bodies and update the nodes children
		if (!isExternalNode()) {
			if (body.inQuad(q.NE())) {
				if (NE == null) {
					NE = new QuadTree(q.NE());
				}
				NE.insert(body);
			}
			else if (body.inQuad(q.NW())) {
				if (NW == null) {
					NW = new QuadTree(q.NW());
				}
				NW.insert(body);
			}
			else if (body.inQuad(q.SE())) {
				if (SE == null) {
					SE = new QuadTree(q.SE());
				}
				SE.insert(body);
			}
			else if (body.inQuad(q.SW())) {
				if (SW == null) {
					SW = new QuadTree(q.SW());
				}
				SW.insert(body);
			}
		}
		// If the node is an external node, then put the current body in the correct sub-quadrant and insert the new body into this quadrant.
		else {
			if (this.body.inQuad(q.NE())) {
				if (NE == null) {
					NE = new QuadTree(q.NE());
				}
				NE.insert(this.body);
			}
			else if (this.body.inQuad(q.NW())) {
				if (NW == null) {
					NW = new QuadTree(q.NW());
				}
				NW.insert(this.body);
			}
			else if (this.body.inQuad(q.SE())) {
				if (SE == null) {
					SE = new QuadTree(q.SE());
				}
				SE.insert(this.body);
			}
			else if (this.body.inQuad(q.SW())) {
				if (SW == null) {
					SW = new QuadTree(q.SW());
				}
				SW.insert(this.body);
			}
			insert(body);
		}
	}
	
	private boolean isExternalNode() {
		return (NE == null && NW == null && SE == null && SW == null);
	}
	
	public boolean isExternalNode(QuadTree node) {
		if (node.NE == null && node.NW == null && node.SE == null && node.SW == null) {
			return true;
		}
		return false;
	}
	
	public void updateForces(Body b, double theta) {
		if (isExternalNode() || q.size()/body.distanceTo(b) < theta) {
			if (body != b) {
				b.addForce(body);
			}
		}
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