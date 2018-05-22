package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import TileMap.Background;

public class HelpState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {"New Game", "Tutorial", "Help", "Exit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Color creditsColor;
	private Font creditsFont;
	
	private Font font; // regular font

	public HelpState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			//bg = new Background("/Backgrounds/aieye.jpg", 1);
			bg = new Background("/Backgrounds/menubg-960.png", 1);
			bg.setVector(-0.1,  0); // moving 0.1 pixels to the left
			
			titleColor = new Color(128, 0, 128);
			titleColor = Color.BLUE;
			titleFont = new Font("Phosphate", Font.BOLD, 28);
			
			creditsColor = Color.RED;
			creditsFont = new Font("Times New Roman", Font.PLAIN, 12);
			
			font = new Font("Arial", Font.PLAIN, 12);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
}
