package Agent;

import Drawing.Canvas;

public class Predator extends RandomAgent {

	// empty, to use as field for use in class comparing
	public Predator() {
	};

	public Predator(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
		this.agentScale = 25;
	}

}
