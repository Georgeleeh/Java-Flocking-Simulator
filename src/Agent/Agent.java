 package Agent;

import java.util.ArrayList;

import Drawing.*;
import Utils.Utils;

public class Agent {
	private Canvas myCanvas;
	private CartesianCoordinate position;
	protected double angle;
	protected int speed;
	private boolean penDown;
	protected int agentScale;
	protected int radius;

	public Agent() {
	};

	/**
	 * Constructs an agent
	 * 
	 * @param Canvas
	 *            canvas on which the agent will be drawn
	 * @param xPosition
	 *            X coordinate at which the agent will be drawn
	 * @param yPosition
	 *            Y coordinate at which the agent will be drawn
	 */
	public Agent(Canvas myCanvas, double xPosition, double yPosition) {
		this.myCanvas = myCanvas;
		this.position = new CartesianCoordinate(xPosition, yPosition);
		this.angle = 0;
		this.penDown = false;
		this.speed = 70;
		this.agentScale = 20;
		draw();
	}

	/**
	 * Moves the agent based on time difference parameter
	 * 
	 * @param time
	 *            time passed since last position update
	 */
	public void update(int time) {
		// S = D * T, converting time to milliseconds
		double distance = (double) (getSpeed() * (time * 0.001));
		move(distance);
		wrapPosition(Utils.X_SCREEN, Utils.Y_SCREEN);
	}

	/**
	 * Moves the agent i pixels in the direction of angle
	 * 
	 * @param i
	 *            the number of pixels the agent should move
	 */
	public void move(double distance) {
		// save current position
		CartesianCoordinate startPoint = new CartesianCoordinate(position.getX(), position.getY());

		// update current position with soh-cah-toa
		// reverse sin and cos due to frame coordinate system (y++ is down)
		position.setX(position.getX() + (distance * Math.sin(angle)));
		position.setY(position.getY() + (distance * Math.cos(angle)));

		// draw the movement, if pen is down
		if (penDown) {
			myCanvas.drawLineBetweenPoints(startPoint, position);
		}
	}

	/**
	 * Make Agents loop around canvas when they attempt to leave the canvas bounds.
	 * Uses buffers to allow agents to be undrawn and redrawn slightly off screen
	 * 
	 * @param maxX
	 *            canvas width
	 * @param maxY
	 *            canvas height
	 */
	protected void wrapPosition(int maxX, int maxY) {
		// set buffers (amount of pixels Agent is allowed off-canvas)
		// several buffers to account for other panels in the frame
		int xBuffer = 15;
		int yMaxBuffer = -100;
		int yMinBuffer = 10;
		maxX += xBuffer;
		maxY += yMaxBuffer;
		int minX = 0 - xBuffer;
		int minY = 0 - yMinBuffer;
		// if axis position crosses any side +- buffer, move them to opposite side +-
		// buffer
		if (position.getX() > maxX) {
			position.setX(minX);
		} else if (position.getX() < minX) {
			position.setX(maxX);
		}
		if (position.getY() > maxY) {
			position.setY(minY);
		} else if (position.getY() < minY) {
			position.setY(maxY);
		}
	}

	/**
	 * draws an agent (an isosceles triangle), keeping current position and
	 * orientation
	 */
	public void draw() {
		CartesianCoordinate Line01;
		Line01 = new CartesianCoordinate(
				position.getX() + agentScale * Math.sin(angle) - (agentScale / 2) * Math.sin(angle),
				position.getY() + agentScale * Math.cos(angle) - (agentScale / 2) * Math.cos(angle));

		CartesianCoordinate Line02;
		Line02 = new CartesianCoordinate(
				position.getX() - agentScale * Math.cos(angle) - (agentScale / 2) * Math.sin(angle),
				position.getY() + agentScale * Math.sin(angle) - (agentScale / 2) * Math.cos(angle));

		CartesianCoordinate Line03;
		Line03 = new CartesianCoordinate(
				position.getX() + agentScale * Math.cos(angle) - (agentScale / 2) * Math.sin(angle),
				position.getY() - agentScale * Math.sin(angle) - (agentScale / 2) * Math.cos(angle));

		myCanvas.drawLineBetweenPoints(Line01, Line02);
		myCanvas.drawLineBetweenPoints(Line01, Line03);
		myCanvas.drawLineBetweenPoints(Line02, Line03);
	}

	/**
	 * remove the previously drawn agent
	 */
	public void undraw() {
		myCanvas.removeMostRecentLine();
		myCanvas.removeMostRecentLine();
		myCanvas.removeMostRecentLine();
	}

	/**
	 * Turns the agent clockwise by specified number of degrees
	 * 
	 * @param i
	 *            angle by which the agent will turn;
	 */
	public void turn(double i) {
		angle += Math.toRadians(i);
	}

	/**
	 * Search for agents within a circle
	 * 
	 * @param allAgents
	 *            ArrayList of all agents
	 * @return ArrayList of all agents found within the radius
	 */
	public ArrayList<Agent> searchRadius(ArrayList<Agent> allAgents, int radius) {
		ArrayList<Agent> foundAgents = new ArrayList<Agent>();
		for (Agent agent : allAgents) {
			if (agent != this) {
				double xPart = Math.pow((agent.getPosition().getX() - this.getPosition().getX()), 2);
				double yPart = Math.pow((agent.getPosition().getY() - this.getPosition().getY()), 2);
				double xyPart = Math.sqrt(xPart + yPart);

				if (xyPart < radius) {
					foundAgents.add(agent);
				}
			}
		}
		return foundAgents;
	}

	
	public String toString() {
		return "Agent [position=" + position + ", angle=" + angle + "]";
	}
	
	// METHODS TO BE OVERRIDDEN
	
	/**
	 * Does nothing, will be overridden by FlockingAgent
	 */
	public void Flock(ArrayList<Agent> agents) {
		// Leave empty, to be overridden by a Flocking-Agent
	}

	/**
	 * Does nothing, will be overridden by PlayableAgent
	 */
	public void updateScore(ArrayList<Agent> agents) {
		// Leave empty, to be overridden by a Flocking-Agent
	}

	/**
	 * Does nothing, will be overridden by PlayableAgent
	 * 
	 * @return false, as returning true would remove the agent
	 */
	public boolean checkForPredator(ArrayList<Agent> allAgents) {
		return false;
	}

	/**
	 * Does nothing, will be overridden by PlayableAgent
	 */
	public int getScore() {
		return 0;
	}

	// GETTERS AND SETTERS

	public void putPenDown() {
		this.penDown = true;
	}

	public void putPenUp() {
		this.penDown = false;
	}

	public CartesianCoordinate getPosition() {
		return position;
	}

	public void setPosition(CartesianCoordinate position) {
		this.position = position;
	}

	public void setPosition(double xPosition, double yPosition) {
		this.position = new CartesianCoordinate(xPosition, yPosition);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	// EMPTY SETTERS, TO BE OVERRIDDEN BY FLOCKING-AGENTS

	public void setSeparation(double separation) {
	}

	public void setCohesion(double cohesion) {
	}

	public void setRadius(int radius) {
	}

	public void setAlignment(double alignment) {
	}

}
