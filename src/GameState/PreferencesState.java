package GameState;
import TileMap.Background;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;

public class PreferencesState extends GameState {
	
	private Background bg;
	private Background bg2;
	
	private int currentChoice = 0;
	private String[] options = {"Tileset:", "Player Skin:", "Sounds:", "Back to Main Menu"};
	
	private Color titleColor;
	private Font titleFont;
	
	private int currentTileSet;
	private int currentSkin;
	private boolean soundSetting;
	
	private Font font; // regular font
	private Color fontColor;
	
	private String[] tileSetOptions = {"Normal", "Moon", "Candy"};
	private String[] characterOptions = {"Jazz", "Lori"};
	private String[] soundOptions = {"On", "Off"};
	
	private AudioPlayer scrollingSound;
	private AudioPlayer selectionSound;
	
	public PreferencesState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/Backgrounds/menubg.gif",1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5, 0);
			
			bg2 = new Background("/Backgrounds/menubg.gif", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 128);
			titleFont = new Font("Herculanum", Font.BOLD, 24);
			
			fontColor = new Color(48, 0, 128);
			font = new Font("Bradley Hand", Font.BOLD, 20);
			
			scrollingSound = new AudioPlayer("/SFX/scroll.wav");
			selectionSound = new AudioPlayer("/SFX/select.wav");
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
		bg2.update();
		
		if(bg.getX() <= -bg.getWidth()) {
			bg.setPosition(bg.getWidth(), 0);
		}
		if(bg2.getX() <= - bg2.getWidth()) {
			bg2.setPosition(bg2.getWidth(), 0);
		}
	}
	
	
	public void draw(Graphics2D g) {
		
		// Draw Background
		bg.draw(g);
		bg2.draw(g);
		
		// Draw Title
		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString("Preferences", 101, 41);
		g.setColor(titleColor);
		g.drawString("Preferences", 100, 40);
		
		// Draw Menu Options
		g.setFont(font);
		int[] xOffset = {0, -10, 5, -20};
		for(int i = 0; i < options.length; i++) {
			
			if(i == currentChoice) { 
				g.setColor(fontColor); 
				g.drawString(options[i], 110 + xOffset[i], 121 + i * 25);
				g.setColor(Color.white);
			}
			
			else { 
				g.setColor(fontColor); 
			}
			
			
			if(i == 0) {
				g.drawString(options[i] + tileSetOptions[currentTileSet], 110 + xOffset[i], 120 + i * 25);
			}
			if(i == 1) {
				g.drawString(options[i] + characterOptions[currentSkin], 110 + xOffset[i], 120 + i * 25);
			}
			if(i == 2) {
				int result = soundSetting ? 0 : 1;
				g.drawString(options[i] + soundOptions[result], 110 + xOffset[i], 120 + i * 25);
			}
			else {
				g.drawString(options[i], 110 + xOffset[i], 120 + i * 25);
			}
			
		}
	}
	
	private void select() {
		// TODO Write select function
		
		// 0: Tileset
		// 1: Skin
		// 2: Sounds
		// 3: Back
		
		if(currentChoice == 0) {
			// Tileset
			currentTileSet++;
			if(currentTileSet == tileSetOptions.length) {
				currentTileSet = 0;
			}
		}
		
		if(currentChoice == 1) {
			// Player skin
			currentSkin++;
			if(currentSkin == characterOptions.length) {
				currentSkin = 0;
			}
		}
		
		if(currentChoice == 2) {
			// Sound
			soundSetting = !soundSetting;
		}
		
		if(currentChoice == 3) {
			gsm.setState(GameStateManager.states.get("MENUSTATE"));
		}
	}

	public void keyPressed(int k) {
		
		if(k == KeyEvent.VK_ENTER) {
			selectionSound.play();
			select();
		}
		if(k == KeyEvent.VK_UP) {
			scrollingSound.play();
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			scrollingSound.play();
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
		
	}

	public void keyReleased(int k) {}
	
	

}
