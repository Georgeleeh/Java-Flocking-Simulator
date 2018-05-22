package Agent;

import java.util.ArrayList;

import Drawing.*;
import Utils.Utils;

public class FlockingAgent extends Agent {

	private double Cohesion;
	private double Separation;
	private double Alignment;
	private Predator predator = new Predator();

	// CONSTRUCTORS

	/**
	 * Constructs an agent which uses flocking behaviours
	 * 
	 * @param Canvas
	 *            canvas on which the agent will be drawn
	 * @param xPosition
	 *            X coordinate at which the agent will be drawn
	 * @param yPosition
	 *            Y coordinate at which the agent will be drawn
	 */
	public FlockingAgent(Canvas canvas, int xPosition, int yPosition) {
		super(canvas, xPosition, yPosition);
		this.agentScale = 18;
		// set random initial angle
		setAngle(Math.toRadians(Utils.randomInt(360)));
	}

	// METHODS

	/**
	 * Update angle based on flocking parameters
	 * 
	 * @param allAgents
	 *            ArrayList of all agents
	 */
	public void Flock(ArrayList<Agent> allAgents) {
		// search radius for other agents
		ArrayList<Agent> foundAgents = searchRadius(allAgents, this.radius);

		if (foundAgents.size() > 0) {

			// get centre of mass and average angle for visible agents
			CoordinateWithAngle averages = calculateAverages(foundAgents);

			double cohesionAngle = calculateCohesionAngle(averages);
			// separation angle is just opposite angle to cohesion
			double SeperationAngle = Utils.angleWithinBounds(cohesionAngle + 180);
			// calculate required turn angle to match average angle
			double AlignAngle = averages.getAngle() - this.getAngle();

			// if a predator is near, ignore normal flocking behaviour and disperse
			if (checkForPredator(foundAgents)) {
				this.turn(0.1 * SeperationAngle);
			} else {
				// turn based on flock parameters
				this.turn(Cohesion * cohesionAngle);
				this.turn(Separation * SeperationAngle);
				this.turn(Alignment * AlignAngle);
			}
		}
	}

	/**
	 * Checks a list of agents and returns whether or not it contains a predator
	 * 
	 * @param foundAgents
	 *            ArrayList of agents to be checked
	 * @return boolean showing if a predator is present in the list
	 */
	public boolean checkForPredator(ArrayList<Agent> allAgents) {
		for (Agent agent : allAgents) {
			if (agent.getClass() == predator.getClass()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculate the average position and angle of every agent in the input
	 * ArrayList
	 * 
	 * @param foundAgents
	 *            ArrayList of agents to be averaged
	 * @return Average position and Angle of input agents
	 */
	public CoordinateWithAngle calculateAverages(ArrayList<Agent> foundAgents) {
		double xAverage = 0;
		double yAverage = 0;
		double aAverage = 0;

		if (foundAgents.size() > 0) {

			for (Agent agent : foundAgents) {

				// take away half of screen dimensions, to reference coordinates to the midpoint
				// of the canvas
				xAverage += agent.getPosition().getX() - Utils.X_SCREEN / 2;
				yAverage += (Utils.Y_SCREEN / 2 - agent.getPosition().getY());
				aAverage += Utils.angleWithinBounds(agent.getAngle());

			}

			xAverage = xAverage / foundAgents.size();
			yAverage = yAverage / foundAgents.size();
			aAverage = aAverage / foundAgents.size();

		}

		return new CoordinateWithAngle(xAverage, yAverage, aAverage);

	}

	/**
	 * Calculate angle to turn to face centre of mass
	 * 
	 * @param averages
	 *            average position and angle
	 * @return cohesion angle, within -179 to +180 degrees
	 */
	public double calculateCohesionAngle(CoordinateWithAngle averages) {

		double xPart = averages.getX() - (this.getPosition().getX() - Utils.X_SCREEN / 2);
		double yPart = averages.getY() - (Utils.Y_SCREEN / 2 - this.getPosition().getY());
		double TurnAngle = 90 - this.getAngle() * 180 / Math.PI + Math.toDegrees(Math.atan2(yPart, xPart));

		return Utils.angleWithinBounds(TurnAngle);
	}

	// SETTERS

	public void setSeparation(double separation) {
		Separation = separation;
	}

	public void setCohesion(double cohesion) {
		Cohesion = cohesion;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setAlignment(double alignment) {
		Alignment = alignment;
	}

}
