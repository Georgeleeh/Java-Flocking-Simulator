package Agent;

import Utils.Utils;
import Drawing.Canvas;

public class RandomAgent extends Agent {

	private int maxAngle;

	// CONSTRUCTOR

	// empty, to use as field for use in class comparing
	public RandomAgent() {
	};

	/**
	 * Constructs an agent which moves randomly
	 * 
	 * @param Canvas
	 *            canvas on which the agent will be drawn
	 * @param xPosition
	 *            X coordinate at which the agent will be drawn
	 * @param yPosition
	 *            Y coordinate at which the agent will be drawn
	 */
	public RandomAgent(Canvas canvas, double xPosition, double yPosition) {
		super(canvas, xPosition, yPosition);
		this.maxAngle = 45;
		this.agentScale = 22;
		setPosition(xPosition, yPosition);
	}

	// METHODS

	/**
	 * Random chance (1/100) to change the agent's angle, moves the agent based on
	 * time difference parameter
	 * 
	 * @param time
	 *            time passed since last position update
	 */
	public void update(int time) {
		// every frame has a 1/100 chance of turning the agent +/- 5 degrees
		if (Utils.randomInt(100) == 1) {
			int angle = Utils.randomIntRange(maxAngle);
			setAngle(angle);
		}
		super.update(time);
	}
}
