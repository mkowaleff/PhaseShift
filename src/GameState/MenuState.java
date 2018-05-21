package GameState;
import TileMap.Background;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;

public class MenuState extends GameState {
	
	private Background bg;
	private Background bg2;
	
	private int currentChoice = 0;
	private String[] options = {"New Game", "Tutorial", "Preferences", "Help", "Exit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Color creditsColor;
	private Font creditsFont;
	
	private Font font; // regular font
	private Color fontColor;
	
	private AudioPlayer scrollingSound;
	private AudioPlayer selectionSound;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			//bg = new Background("/Backgrounds/menubg.gif", 1);
			bg = new Background("/Backgrounds/menubg.png", 1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5,  0); // moving 0.1 pixels to the left
			
			scrollingSound = new AudioPlayer("/SFX/scroll.wav");
			selectionSound = new AudioPlayer("/SFX/select.wav");
			
			//bg2 = new Background("/Backgrounds/menubg.gif", 1);
			bg2 = new Background("/Backgrounds/menubg.png", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 128);
			titleFont = new Font("Herculanum", Font.BOLD, 48);
			
			creditsColor = Color.red;
			creditsFont = new Font("Times New Roman", Font.PLAIN, 12);
			
			fontColor = new Color(48, 0, 128);
			font = new Font("Bradley Hand", Font.BOLD, 24);
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
		
		//draw background
		bg.draw(g);
		bg2.draw(g);
		
		// draw title
		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString("Phase Shift", 32, 72);
		g.setColor(titleColor);
		g.drawString("Phase Shift", 30, 70);
		
		
		g.setFont(creditsFont);
		g.setColor(creditsColor);
		g.drawString("by Martin Kowaleff", 200, 85);
		
		// draw menu options
		int[] xOffset = {0, 10, 0, 35, 35};
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(fontColor);
				g.drawString(options[i], 116 + xOffset[i], 121+i*25);
				g.setColor(Color.white);
				
			}
			else {
				g.setColor(fontColor);
			}
			g.drawString(options[i], 115 + xOffset[i], 120+i*25);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			// NEW GAME
			gsm.setState(GameStateManager.states.get("LEVELSELECTIONSTATE"));
		}
		if(currentChoice == 1) {
			// TUTORIAL
			gsm.setState(GameStateManager.states.get("TUTORIALSTATE"));
		}
		if(currentChoice == 2) {
			// PREFERENCES
			gsm.setState(GameStateManager.states.get("PREFERENCESSTATE"));
		}
		if(currentChoice == 3) {
			// HELP
			gsm.setState(GameStateManager.states.get("HELPSTATE"));
		}
		if(currentChoice == 4) {
			// EXIT
			System.exit(0);
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
