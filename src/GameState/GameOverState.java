package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.Background;

public class GameOverState extends GameState {

	private Background bg;
	private Background bg2;

	private int currentChoice = 0;
	private String[] options = {"Retry", "Return to Menu", "Exit to Desktop"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Color subtitleColor;
	private Font subtitleFont;
	
	private Font font; // regular font
	private Color fontColor;
	
	private AudioPlayer scrollingSound;
	private AudioPlayer selectionSound;
	
	public GameOverState(GameStateManager gsm) {

		this.gsm = gsm;
		try {

			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5,  0); // moving 0.1 pixels to the left
			
			bg2 = new Background("/Backgrounds/menubg.gif", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 128);
			titleFont = new Font("Herculanum", Font.BOLD, 24);
			
			subtitleColor = Color.red;
			subtitleFont = new Font("Herculanum", Font.PLAIN, 12);
			
			fontColor = new Color(48, 0, 128);
			font = new Font("Bradley Hand", Font.BOLD, 20);
			
			scrollingSound = new AudioPlayer("/SFX/scroll.wav");
			selectionSound = new AudioPlayer("/SFX/select.wav");

		}
		catch(Exception e) {
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
		if(bg2.getX() <= -bg2.getWidth()) {
			bg2.setPosition(bg2.getWidth(), 0);
		}
	}


	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);
		bg2.draw(g);

		// draw title
		g.setFont(titleFont);
		g.setColor(Color.black);
		g.drawString("Game Over", 101, 71);
		g.setColor(titleColor);
		g.drawString("Game Over", 100, 70);
		
		g.setFont(subtitleFont);
		g.setColor(subtitleColor);
		g.drawString("You have died!", 120, 85);

		// draw menu options
		int[] xOffset = {20, -10, -10};
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(fontColor);
				g.drawString(options[i], 116 + xOffset[i], 141 + i * 25);
				g.setColor(Color.white);
			}
			else {
				g.setColor(fontColor); 
			}
			g.drawString(options[i], 115 + xOffset[i], 140 + i * 25);
		}
		
	}
	
	public void select() {
		
		if(currentChoice == 0) {
			// Retry
			gsm.setState(1);
		}
		
		if(currentChoice == 1) {
			// return to menu
			gsm.setState(0);
		}
		
		if(currentChoice == 2) {
			// exit
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
			if(currentChoice <= -1) {
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
