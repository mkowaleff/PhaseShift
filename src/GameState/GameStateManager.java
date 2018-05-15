package GameState;

import java.util.ArrayList;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState; // the current state is going to be the index of the game state list
	
	public static final int NUMBEROFGAMESTATES = 5;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int TUTORIALSTATE = 2;
	public static final int HELPSTATE = 3;
	public static final int GAMEOVERSTATE = 4;
	
	public GameStateManager() {
		gameStates = new GameState[NUMBEROFGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	public void loadState(int state) {
		// have to add condition for each existing state
		if(state == MENUSTATE) {
			gameStates[state] = new MenuState(this);
		}
		if(state == LEVEL1STATE) {
			gameStates[state] = new Level1State(this);
		}
		if(state == TUTORIALSTATE) {
			gameStates[state] = new TutorialState(this);
		}
		if(state == HELPSTATE) {
			gameStates[state] = new HelpState(this);
		}
		if(state == GAMEOVERSTATE) {
			gameStates[state] = new GameOverState(this);
		}
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	
	public void update() {
		if(gameStates[currentState] != null)
			gameStates[currentState].update();
	}

	public void draw(java.awt.Graphics2D g) {
		if(gameStates[currentState] != null)
			gameStates[currentState].draw(g);
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
}
