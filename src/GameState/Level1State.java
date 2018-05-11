package GameState;

import TileMap.*;
import Entity.*;
import Main.GamePanel;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Point;


public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	private HUD hud;
	
	private Player player;
	
	private ArrayList<Enemy> enemies; 
	private ArrayList<Explosion> explosions;
	
	private AudioPlayer backgroundMusic;
	
	public Level1State(GameStateManager gsm) { 
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.gif",0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		backgroundMusic = new AudioPlayer("/Music/levelsong.mp3");
		//backgroundMusic.play();
	}
	
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		
		Point[] points = {
				new Point(200, 100),
				new Point(860, 200),
				new Point(1525, 200),
				new Point(1680, 200),
				new Point(1800, 200)
		};
		
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
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
		if(k == KeyEvent.VK_W) { player.setJumping(true);}
		if(k == KeyEvent.VK_E) {player.setGliding(true);}
		if(k == KeyEvent.VK_R) {player.setScratching();}
		if(k == KeyEvent.VK_F) {player.setFiring();}
		if(k == KeyEvent.VK_ESCAPE) {System.exit(0);}
	}
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) {player.setLeft(false);}
		if(k == KeyEvent.VK_RIGHT) {player.setRight(false);}
		if(k == KeyEvent.VK_UP) {player.setUp(false);}
		if(k == KeyEvent.VK_DOWN) {player.setDown(false);}
		if(k == KeyEvent.VK_W) {player.setJumping(false);}
		if(k == KeyEvent.VK_E) {player.setGliding(false);}
	}
}
