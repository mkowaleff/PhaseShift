package GameState;
import TileMap.Background;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

import Audio.AudioPlayer;

public class MenuState extends GameState implements MouseMotionListener {
	
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
	private AudioPlayer backgroundMusic;
	
	private int mouseX;
	private int mouseY;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			
			int multiplier = 3;
			
			bg = new Background("/Backgrounds/menubg-960.png", 1);
			bg.setPosition(0, 0);
			bg.setVector(-0.5,  0); // moving 0.1 pixels to the left
			
			scrollingSound = new AudioPlayer("/SFX/scroll.wav");
			selectionSound = new AudioPlayer("/SFX/select.wav");
			
			//bg2 = new Background("/Backgrounds/menubg.gif", 1);
			bg2 = new Background("/Backgrounds/menubg-960.png", 1);
			bg2.setPosition(bg2.getWidth(), 0);
			bg2.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 128);
			titleFont = new Font("Herculanum", Font.BOLD, 48*multiplier);
			
			creditsColor = Color.red;
			creditsFont = new Font("HanziPen TC", Font.BOLD, 6*multiplier);
			
			fontColor = new Color(48, 0, 128);
			font = new Font("Bradley Hand", Font.BOLD, 20*multiplier);
			
			backgroundMusic = new AudioPlayer("/Music/menutheme.mp3");
			backgroundMusic.play();
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
		
		//Draw Background
		bg.draw(g);
		bg2.draw(g);
		
		// Center lines
		// g.drawLine(480, 0, 480, 720);
		// g.drawLine(0, 360, 960, 360);
		
		// Draw Title
		g.setFont(titleFont);
		g.setColor(Color.black);
		
		int screenWidth = 960;
		int stringWidth = g.getFontMetrics().stringWidth("Phase Shift");
		
		g.drawString("Phase Shift", screenWidth/2 - stringWidth/2 + 4, 144);
		g.setColor(titleColor);
		g.drawString("Phase Shift", screenWidth/2 - stringWidth/2, 140);
		
		
		
		// Draw Credits
		g.setFont(creditsFont);
		g.setColor(creditsColor);
		g.drawString("by Martin Kowaleff", 700, 170);
		
		// Draw Mouse Coordinates
		//g.setColor(Color.green);
		//g.drawString(mouseX + ", " + mouseY, mouseX+50, mouseY+50);
		
		// Draw Menu Options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			stringWidth = g.getFontMetrics().stringWidth(options[i]);
			if(i == currentChoice) {
				g.setColor(fontColor);
				g.drawString(options[i], screenWidth/2 - stringWidth/2 + 4, 354 + i*75);
				g.setColor(Color.red);
				
			}
			else {
				g.setColor(fontColor);
			}
			g.drawString(options[i], screenWidth/2 - stringWidth/2, 350 + i*75);
		}
	}
	
	private void select() {
		backgroundMusic.stop();
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
			int in = JOptionPane.showOptionDialog(null, "Are you sure you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(in == 0) {
				System.exit(0);
			}
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
		
		if(k == KeyEvent.VK_ESCAPE) {
			int in = JOptionPane.showOptionDialog(null, "Are you sure you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(in == 0) {
				System.exit(0);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
	
	

}
