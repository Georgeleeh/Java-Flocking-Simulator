import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import Drawing.Canvas;
import Agent.*;
import Utils.Utils;

public class FlockingJavaAssignment {

	// list of all agents in the program
	ArrayList<Agent> agents;
	// boolean to run the game loop
	boolean continueRunning = true;
	// holds whether or not the game mode is active
	boolean gameStarted = false;
	// holds whether any flock members are active
	boolean flockingAgentsPresent = false;
	// multiplier for the simulation speed (doesn't affect agent speed)
	int simulationSpeedMultiplier = 1;
	// number of active agents which aren't flock members
	int nonFlockingAgents = 0;
	// label for the game state
	JLabel gameLabel;
	// label for player score
	JLabel scoreLabel;

	public static void main(String[] args) {
		// time between new frame calculations
		int deltaTime = 20;
		// the program itself
		new FlockingJavaAssignment(deltaTime);
	}

	public FlockingJavaAssignment(int deltaTime) {

		// Polymorphic ArrayList containing all agents
		agents = new ArrayList<Agent>();

		// FRAME SETUP

		JFrame frame = new JFrame();
		frame.setTitle("Flocking Simulator");
		frame.setSize(Utils.X_SCREEN, Utils.Y_SCREEN);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Canvas canvas = new Canvas();
		frame.add(canvas, BorderLayout.CENTER);

		JPanel upperPanel = new JPanel(new FlowLayout());
		frame.add(upperPanel, BorderLayout.NORTH);

		JPanel lowerPanel = new JPanel(new FlowLayout());
		frame.add(lowerPanel, BorderLayout.SOUTH);

		// BUTTONS & SLIDERS

		// UPPER PANEL

		JButton startGameButton = new JButton("START GAME");
		upperPanel.add(startGameButton, FlowLayout.LEFT);

		JSlider simulationSpeed = new JSlider(1, 10, 1);
		upperPanel.add(simulationSpeed);
		Hashtable<Integer, JLabel> labelTableSimulationSpeed = new Hashtable<Integer, JLabel>();
		labelTableSimulationSpeed.put(new Integer(5), new JLabel("Simulation Speed"));
		simulationSpeed.setLabelTable(labelTableSimulationSpeed);
		simulationSpeed.setMajorTickSpacing(1);
		simulationSpeed.setPaintTicks(true);
		simulationSpeed.setPaintLabels(true);

		JButton addFlockAgentButton = new JButton("Add Flock");
		upperPanel.add(addFlockAgentButton);

		JButton addRandomAgentButton = new JButton("Add Random");
		upperPanel.add(addRandomAgentButton);

		JButton addPredatorButton = new JButton("Add Predator");
		upperPanel.add(addPredatorButton);

		gameLabel = new JLabel("Ready to Play");
		upperPanel.add(gameLabel);

		// Must be manually written as playable agent doesn't exist yet
		scoreLabel = new JLabel("Score: 0");
		upperPanel.add(scoreLabel);

		// LOWER PANEL

		JSlider agentRadius = new JSlider(0, 1000, 200);
		lowerPanel.add(agentRadius);
		Hashtable<Integer, JLabel> labelTableRadius = new Hashtable<Integer, JLabel>();
		labelTableRadius.put(new Integer(500), new JLabel("Radius"));
		agentRadius.setLabelTable(labelTableRadius);
		agentRadius.setPaintLabels(true);

		JSlider agentSpeed = new JSlider(0, 500, 70);
		lowerPanel.add(agentSpeed);
		Hashtable<Integer, JLabel> labelTableAgentSpeed = new Hashtable<Integer, JLabel>();
		// set label to display at midpoint of slider
		// could be a variable but as both instances are right here, would be overkill
		labelTableAgentSpeed.put(new Integer(250), new JLabel("Agent Speed"));
		agentSpeed.setLabelTable(labelTableAgentSpeed);
		agentSpeed.setPaintLabels(true);

		JSlider agentCohesion = new JSlider(0, 10, 0);
		lowerPanel.add(agentCohesion);
		Hashtable<Integer, JLabel> labelTableCohesion = new Hashtable<Integer, JLabel>();
		labelTableCohesion.put(new Integer(5), new JLabel("Cohesion"));
		agentCohesion.setLabelTable(labelTableCohesion);
		agentCohesion.setPaintLabels(true);

		JSlider agentSeparation = new JSlider(0, 10, 0);
		lowerPanel.add(agentSeparation);
		Hashtable<Integer, JLabel> labelTableSeperation = new Hashtable<Integer, JLabel>();
		labelTableSeperation.put(new Integer(5), new JLabel("Separation"));
		agentSeparation.setLabelTable(labelTableSeperation);
		agentSeparation.setPaintLabels(true);

		JSlider agentAlignment = new JSlider(0, 10, 0);
		lowerPanel.add(agentAlignment);
		Hashtable<Integer, JLabel> labelTableAlignment = new Hashtable<Integer, JLabel>();
		labelTableAlignment.put(new Integer(5), new JLabel("Alignment"));
		agentAlignment.setLabelTable(labelTableAlignment);
		agentAlignment.setPaintLabels(true);

		// Don't add visible objects past here
		frame.setVisible(true);

		// SLIDER LISTENERS

		agentSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				for (Agent s : agents) {
					s.setSpeed((agentSpeed.getValue()));
				}
			}
		});

		simulationSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				simulationSpeedMultiplier = simulationSpeed.getValue();
			}
		});

		agentRadius.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				for (Agent s : agents) {
					s.setRadius(agentRadius.getValue());
				}
			}
		});

		agentCohesion.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				for (Agent s : agents) {
					// divide by 1000 to achieve 0-1 scaling
					s.setCohesion((double) (agentCohesion.getValue()) / (10 * deltaTime));
				}
			}
		});

		agentSeparation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				for (Agent s : agents) {
					// divide by 1000 to achieve 0-1 scaling
					s.setSeparation((double) (agentSeparation.getValue()) / (10 * deltaTime));
				}
			}
		});

		agentAlignment.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				for (Agent s : agents) {
					// divide by 10 to achieve 0-1 scaling
					s.setAlignment((double) (agentAlignment.getValue()) / 10);
				}
			}
		});

		// BUTTON LISTENERS

		// add flocking agent button anonymous class
		addFlockAgentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!gameStarted) {
					flockingAgentsPresent = true;
					agents.add(new FlockingAgent(canvas, Utils.randomInt(Utils.X_SCREEN - 400) + 200,
							Utils.randomInt(Utils.Y_SCREEN - 400) + 200));
					// when a new agent is made, initialise its speed and flocking constants to
					// match the sliders
					agents.get(agents.size() - 1).setSpeed(agentSpeed.getValue());
					agents.get(agents.size() - 1).setRadius(agentRadius.getValue());
					agents.get(agents.size() - 1).setCohesion(agentCohesion.getValue());
					agents.get(agents.size() - 1).setSeparation(agentSeparation.getValue());
					agents.get(agents.size() - 1).setAlignment(agentAlignment.getValue());
				}
			}
		});

		// add random agent button anonymous class
		addRandomAgentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!gameStarted) {
					agents.add(new RandomAgent(canvas, Utils.randomInt(Utils.X_SCREEN - 400) + 200,
							Utils.randomInt(Utils.Y_SCREEN - 400) + 200));
					// when a new agent is made, initialise its speed to match the slider
					agents.get(agents.size() - 1).setSpeed(agentSpeed.getValue());
					// increment number of agents present which don't flock
					nonFlockingAgents++;
				}
			}
		});

		// add predator agent button anonymous class
		addPredatorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!gameStarted) {
					agents.add(new Predator(canvas, Utils.randomInt(Utils.X_SCREEN - 400) + 200,
							Utils.randomInt(Utils.Y_SCREEN - 400) + 200));
					// when a new agent is made, initialise its speed to match the slider
					agents.get(agents.size() - 1).setSpeed(agentSpeed.getValue());
					// increment number of agents present which don't flock
					nonFlockingAgents++;
				}
			}
		});

		// starts the game mode
		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// used such that the button only functions once
				if (!gameStarted) {
					gameStarted = true;
					// gameLabel tells the user the game state (not started, ongoing, over)
					gameLabel.setText("Game On!");
					// add the player to the canvas
					agents.add(new PlayableAgent(canvas, 300, 300));
					// when a new agent is made, initialise its speed to match the slider
					agents.get(agents.size() - 1).setSpeed(agentSpeed.getValue());
					agents.get(agents.size() - 1).setRadius(agentRadius.getValue());
					// increment number of agents present which don't flock
					nonFlockingAgents++;
				}
			}
		});

		// KEY LISTENER

		startGameButton.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

				int key = e.getKeyCode();

				if (key == KeyEvent.VK_LEFT) {
					// last added agent should always be the playable agent
					agents.get(agents.size() - 1).turn(10);
				}

				if (key == KeyEvent.VK_RIGHT) {
					agents.get(agents.size() - 1).turn(-10);
				}

				if (key == KeyEvent.VK_UP) {
					// moving forwards is just setting the speed to be positive
					agents.get(agents.size() - 1).setSpeed(agentSpeed.getValue());
				}

				if (key == KeyEvent.VK_DOWN) {
					agents.get(agents.size() - 1).setSpeed(-agentSpeed.getValue());
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		// MAIN LOOP
		gameLoop(deltaTime);

	}

	private void gameLoop(int deltaTime) {
		// When an agent is set to be removed, the agent is copied to removedAgent
		Agent removedAgent = new Agent();
		// Holds if there is an agent to be removed
		boolean remove = false;
		// deadly radius of predators
		int killRadius = 50;

		while (continueRunning) {

			for (Agent s : agents) {
				s.undraw();
			}

			for (Agent s : agents) {
				// if there's a predator within the deadly radius of the current agent
				if (s.checkForPredator(s.searchRadius(agents, killRadius))) {
					// hold agent to be removed
					removedAgent = s;
					// triggers the agent removal process
					remove = true;
					break;
				}

				// update movements for all agents
				s.Flock(agents);
				s.update(deltaTime);
				// update score variable and write it to the screen
				s.updateScore(agents);
				scoreLabel.setText("Score: " + agents.get(agents.size() - 1).getScore());
			}

			// agent removal process
			if (remove) {
				agents.remove(removedAgent);
				remove = false;
			}

			for (Agent s : agents) {
				s.draw();
			}

			// game ending criteria:
			// game must have already started, flocking agents must have been added and then
			// all removed
			if (gameStarted & flockingAgentsPresent & agents.size() - nonFlockingAgents == 0) {
				gameLabel.setText("   YOU LOST!   ");
				continueRunning = false;
			}

			// pause to give an approximate FPS
			Utils.pause(deltaTime / simulationSpeedMultiplier);

		}
	}

}
