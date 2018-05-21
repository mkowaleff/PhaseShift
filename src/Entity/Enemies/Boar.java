package Entity.Enemies;
import Entity.*;
import TileMap.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;

// basic enemy that moves between walls

public class Boar extends Enemy {
	
	private BufferedImage[] sprites;
	private HashMap<String, AudioPlayer> sfx;
	
	public Boar(TileMap tm) {
		super(tm);
		
		moveSpeed = 1;
		maxSpeed = 1;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 80; // dim of sprite
		height = 80;
		collisionWidth = 40;
		collisionHeight = 30;
		
		health = maxHealth = 2;
		damage = 1;
		
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/boar.png"));
			
			sprites = new BufferedImage[8]; // 5 frames in this sprite
			
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// set animation
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(150);
		right = true; // set it starting right
		facingRight = true;
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("death", new AudioPlayer("/SFX/boarDeath.mp3"));
		sfx.put("wound1", new AudioPlayer("/SFX/boarWound1.mp3"));
		sfx.put("wound2", new AudioPlayer("/SFX/boarWound2.mp3"));
		sfx.put("wound3", new AudioPlayer("/SFX/boarWound3.mp3"));
	}
	
	
	private void getNextPosition() {
		// helper for update function just like player
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if (falling) {
			dy += fallSpeed;
		}
		
		if(dead) {
			sfx.get("death").play();
		}
		
	}
	
	
	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000; // calculate how long it's been flinching
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// Keep going in one direction until it hits a wall, then go to other direction	
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		// update animation
		animation.update();
				
		
	}
	
	
	public void draw(Graphics2D g) {
		// if(notOnScreen()) return; // do not draw if it's not on the screen
		setMapPosition();
		
		super.draw(g);
	}

}
