package GameState;
import TileMap.Background;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	
	private Background bg;
	private Background bg2;
	
	private int currentChoice = 0;
	private String[] options = {"New Game", "Tutorial", "Help", "Exit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Color creditsColor;
	private Font creditsFont;
	
	private Font font; // regular font
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			//bg = new Background("/Backgrounds/aieye.jpg", 1);
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5,  0); // moving 0.1 pixels to the left
			
			bg2 = new Background("/Backgrounds/menubg.gif", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-1, 0);
			
			titleColor = new Color(128, 0, 128);
			titleFont = new Font("Phosphate", Font.BOLD, 28);
			
			creditsColor = Color.RED;
			creditsFont = new Font("Times New Roman", Font.PLAIN, 12);
			
			font = new Font("Arial", Font.PLAIN, 12);
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
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Phase Shift", 100, 70);
		
		g.setColor(creditsColor);
		g.setFont(creditsFont);
		g.drawString("by Martin Kowaleff", 120, 85);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.red);
			}
			else {
				g.setColor(Color.gray);
			}
			g.drawString(options[i], 145, 140+i*15);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			// start
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			// tutorial
			gsm.setState(GameStateManager.TUTORIALSTATE);
		}
		if(currentChoice == 2) {
			// help
			gsm.setState(GameStateManager.HELPSTATE);
		}
		if(currentChoice == 3) {
			// exit
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	
	public void keyReleased(int k) {}
	
	

}
