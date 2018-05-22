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
			int multiplier = 3;
			bg = new Background("/Backgrounds/menubg-960.png",1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5, 0);
			
			bg2 = new Background("/Backgrounds/menubg-960.png", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 128);
			titleFont = new Font("Herculanum", Font.BOLD, 36*multiplier);
			
			fontColor = new Color(48, 0, 128);
			font = new Font("Bradley Hand", Font.BOLD, 20*multiplier);
			
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
		
		int screenWidth = 960;
		int stringWidth = g.getFontMetrics().stringWidth("Preferences");
		
		g.drawString("Preferences", screenWidth/2 - stringWidth/2 + 4, 144);
		g.setColor(titleColor);
		g.drawString("Preferences", screenWidth/2 - stringWidth/2, 140);
		
		// Draw Menu Options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			stringWidth = g.getFontMetrics().stringWidth(options[i]);
			if(i == currentChoice) { 
				g.setColor(fontColor); 
				g.drawString(options[i], screenWidth/2 - stringWidth/2, 254 + i * 100);
				g.setColor(Color.white);
			}
			
			else { 
				g.setColor(fontColor); 
			}
			
			
			if(i == 0) {
				g.drawString(options[i] + tileSetOptions[currentTileSet], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			if(i == 1) {
				g.drawString(options[i] + characterOptions[currentSkin], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			if(i == 2) {
				int result = soundSetting ? 0 : 1;
				g.drawString(options[i] + soundOptions[result], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			else {
				g.drawString(options[i], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			
		}
	}
	
	
	/*
	 * Old Draw method: 
	 * In order to center each panel of screen, the for loop needs to be redone with each two components per item list
	 * 
	 * 
	 * 
		// Draw Background
		bg.draw(g);
		bg2.draw(g);
		
		// Draw Title
		g.setFont(titleFont);
		g.setColor(Color.black);
		
		int screenWidth = 960;
		int stringWidth = g.getFontMetrics().stringWidth("Preferences");
		
		g.drawString("Preferences", screenWidth/2 - stringWidth/2 + 4, 144);
		g.setColor(titleColor);
		g.drawString("Preferences", screenWidth/2 - stringWidth/2, 140);
		
		// Draw Menu Options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			stringWidth = g.getFontMetrics().stringWidth(options[i]);
			if(i == currentChoice) { 
				if (i == 0) stringWidth = g.getFontMetrics().stringWidth(options[i] + tileSetOptions[currentTileSet]);
				if (i == 1) stringWidth = g.getFontMetrics().stringWidth(options[i] + characterOptions[currentSkin]);
				if (i == 2) stringWidth = g.getFontMetrics().stringWidth(options[i] + 1);
					
				g.setColor(fontColor); 
				g.drawString(options[i], screenWidth/2 - stringWidth/2, 254 + i * 100);
				g.setColor(Color.white);
			}
			
			else { 
				g.setColor(fontColor); 
			}
			
			
			if(i == 0) {
				stringWidth = g.getFontMetrics().stringWidth(options[i] + tileSetOptions[currentTileSet]);
				g.drawString(options[i] + tileSetOptions[currentTileSet], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			if(i == 1) {
				stringWidth = g.getFontMetrics().stringWidth(options[i] + characterOptions[currentSkin]);
				g.drawString(options[i] + characterOptions[currentSkin], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			if(i == 2) {
				int result = soundSetting ? 0 : 1;
				stringWidth = g.getFontMetrics().stringWidth(options[i] + soundOptions[result]);
				g.drawString(options[i] + soundOptions[result], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			else {
				stringWidth = g.getFontMetrics().stringWidth(options[i]);
				g.drawString(options[i], screenWidth/2 - stringWidth/2, 250 + i * 100);
			}
			
		}
	 */
	
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
