package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
import TileMap.Background;

public class LevelSelectionState extends GameState {
	
	private Background bg;
	private Background bg2;
	
	private int currentChoice = 0;
	private String[] options = {"Tutorial", "Level 1", "Level 2", "Back to Main Menu"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font; // regular font
	private Color fontColor;
	
	private AudioPlayer scrollingSound;
	private AudioPlayer selectionSound;
	
	public LevelSelectionState(GameStateManager gsm) {
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
		g.drawString("New Game", 101, 41);
		g.setColor(titleColor);
		g.drawString("New Game", 100, 40);
		
		
		// Draw Menu Options
		int[] xOffset = {0, 5, 5, -45};
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			
			if(i == currentChoice) { 
				g.setColor(fontColor); 
				g.drawString(options[i], 116 + xOffset[i], 121 + i * 25);
				g.setColor(Color.white);
			}
			
			else { 
				g.setColor(fontColor); 
			}
			g.drawString(options[i], 115 + xOffset[i], 120 + i * 25);
			
		}
	}
	
	private void select() {
		
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.states.get("TUTORIALSTATE"));
		}
		
		if(currentChoice == 1) {
			gsm.setState(GameStateManager.states.get("LEVEL1STATE"));
		}
		
		if(currentChoice == 2) {
			gsm.setState(GameStateManager.states.get("LEVEL2STATE"));
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
