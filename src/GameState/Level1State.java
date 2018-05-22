package GameState;

import TileMap.*;
import Entity.*;
import Main.GamePanel;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.awt.Point;


public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private HUD hud;
	
	private ElfPlayer player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private AudioPlayer backgroundMusic;
	
	public Level1State(GameStateManager gsm) { 
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(80);
		tileMap.loadTiles("/Tilesets/grasstileset.png");
		//tileMap.loadTiles("/Tilesets/moonTileSet.gif");
		tileMap.loadMap("/Maps/CollisionTestLevel2.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		
		
		bg = new Background("/Backgrounds/grassbg-960.png",0.1);
		//bg = new Background("/Backgrounds/moonbg1.gif", 0.1);
		
		player = new ElfPlayer(tileMap);
		player.setPosition(145, 915);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		
		backgroundMusic = new AudioPlayer("/Music/menutheme.mp3");
		backgroundMusic.stop();
		//backgroundMusic.play();
	}
	
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		//Slugger s;
		Archer archer;
		Boar boar;
		
		/*Point[] enemyLocations = {
				new Point(300, 320),
				new Point(480, 320),
				new Point(660, 320),
				new Point(840, 320),
				new Point(1020, 320),
				new Point(1200, 320)
		};*/
		Point[] enemyLocations = {
				new Point(490, 960),
				new Point(800, 960),
				new Point(1300, 960),
				new Point(1800, 960),
				new Point(2300, 960),
				new Point(2800, 960)
		};
		
		for(int i = 0; i < enemyLocations.length; i++) {
			boar = new Boar(tileMap);
			boar.setPosition(enemyLocations[i].x, enemyLocations[i].y);
			enemies.add(boar);
		}
	}
	
	public void update() { 
		
		// update player
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY() ); // keeps player at the middle of screen
		
		// set background 
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		// check if you're attacking any enemies
		player.checkAttack(enemies);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i); 
				i--;
				explosions.add(new Explosion(e.getX(), e.getY()));
			}
		}
		
		// update all explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		// Switch to game over screen
		if(player.isDead()) {
			gsm.setState(4);
		}
	}
	
	public void draw(Graphics2D g) {
		
		// clear screen
		/*g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);*/
		
		// draw bg
		bg.draw(g);
		
		
		// draw tile map
		tileMap.draw(g); 
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i ++) {
			enemies.get(i).draw(g);
		} 
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		
		// draw HUD
		hud.draw(g);
	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) { player.setLeft(true); }
		if(k == KeyEvent.VK_RIGHT) { player.setRight(true);}
		if(k == KeyEvent.VK_UP) {	player.setUp(true);}
		if(k == KeyEvent.VK_DOWN) {player.setDown(true);}
		if(k == KeyEvent.VK_SPACE) { player.setJumping(true);}
		if(k == KeyEvent.VK_E) {player.setAttackingMelee();}
		//if(k == KeyEvent.VK_R) {player.setScratching();}
		if(k == KeyEvent.VK_F) {player.setShooting();}
		if(k == KeyEvent.VK_ESCAPE) {
			int in = JOptionPane.showOptionDialog(null, "Are you sure you want to exit the game?", "Exit Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 0);
			if(in == 0) {
				System.exit(0);
			}
		}
		if(k == KeyEvent.VK_I) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) {player.setLeft(false);}
		if(k == KeyEvent.VK_RIGHT) {player.setRight(false);}
		if(k == KeyEvent.VK_UP) {player.setUp(false);}
		if(k == KeyEvent.VK_DOWN) {player.setDown(false);}
		if(k == KeyEvent.VK_SPACE) {player.setJumping(false);}
	}
}
