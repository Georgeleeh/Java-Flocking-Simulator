package Agent;

import java.util.ArrayList;

import Drawing.Canvas;

public class PlayableAgent extends Agent {

	private int score;
	private int counter;

	public PlayableAgent() {
		super();
	}

	public PlayableAgent(Canvas myCanvas, double xPosition, double yPosition) {
		super(myCanvas, xPosition, yPosition);
		this.score = 0;
		this.counter = 0;
		this.speed = 0;
		this.agentScale = 30;
		this.radius = 200;
	}

	public void updateScore(ArrayList<Agent> allAgents) {
		ArrayList<Agent> foundAgents = searchRadius(allAgents, this.radius);

		// counter increments every frame
		if (foundAgents.size() != 0) {
			counter++;
		}
		// every hundredth call to this function, increment the score
		if ((counter % 50) == 0) {
			score += foundAgents.size();
		}

	}

	// GETTERS AND SETTERS

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
