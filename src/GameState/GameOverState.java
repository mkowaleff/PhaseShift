package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class GameOverState extends GameState {

	private Background bg;
	private Background bg2;

	private int currentChoice = 0;
	private String[] options = {"Retry", "Return to Menu", "Exit to Desktop"};
	
	private Font gameOverFont;
	private Color gameOverColor;
	
	private Font subTitleFont;
	private Color subTitleColor;
	
	private Font selectionFont;
	
	public GameOverState(GameStateManager gsm) {

		this.gsm = gsm;
		try {

			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5,  0); // moving 0.1 pixels to the left
			
			bg2 = new Background("/Backgrounds/menubg.gif", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-0.5, 0);
			
			gameOverColor 	= new Color(128, 0, 128);
			gameOverFont 	= new Font("Phosphate", Font.BOLD, 28);
			
			subTitleFont	= new Font("Century Gothic", Font.PLAIN, 12);
			subTitleColor	= Color.RED;
			
			selectionFont 	= new Font("Arial", Font.PLAIN, 12);

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
		g.setFont(gameOverFont);
		g.setColor(gameOverColor);
		g.drawString("Game Over", 100, 70);
		
		g.setFont(subTitleFont);
		g.setColor(subTitleColor);
		g.drawString("You have died!", 120, 85);

		// draw menu options
		g.setFont(selectionFont);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.GRAY);
			}
			g.drawString(options[i], 145, 140 + i * 15);
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
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice <= -1) {
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
