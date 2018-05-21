package GameState;

import java.util.ArrayList;
import java.util.HashMap;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState; // the current state is going to be the index of the game state list
	
	//public static final int NUMBEROFGAMESTATES = 6;
	
	public static HashMap<String, Integer> states;
	public static final String[] stateNames = {
			"MENUSTATE",
			"LEVEL1STATE",
			"TUTORIALSTATE",
			"HELPSTATE",
			"GAMEOVERSTATE",
			"PREFERENCESSTATE",
			"LEVELSELECTIONSTATE"
	};
	
	
	public GameStateManager() {
		states = new HashMap<String, Integer>();
		
		for(int i = 0; i < stateNames.length; i++ ) {
			states.put(stateNames[i], i);
		}
		
		gameStates = new GameState[states.size()];
		
		currentState = states.get("MENUSTATE");
		loadState(currentState);
		
	}
	
	public void loadState(int state) {
		// have to add condition for each existing state
		if(state == states.get("MENUSTATE")) {
			gameStates[state] = new MenuState(this);
		}
		if(state == states.get("LEVEL1STATE")) {
			gameStates[state] = new Level1State(this);
		}
		if(state == states.get("TUTORIALSTATE")) {
			gameStates[state] = new TutorialState(this);
		}
		if(state == states.get("HELPSTATE")) {
			gameStates[state] = new HelpState(this);
		}
		if(state == states.get("GAMEOVERSTATE")) {
			gameStates[state] = new GameOverState(this);
		}
		if(state == states.get("PREFERENCESSTATE")) {
			gameStates[state] = new PreferencesState(this);
		}
		if(state == states.get("LEVELSELECTIONSTATE")) {
			gameStates[state] = new LevelSelectionState(this);
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
