package Entity;

import TileMap.*;

import java.util.ArrayList;
import java.util.HashMap;
import Audio.AudioPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int fire; // attack variables
	private int maxFire;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer; // flinching time
	
	// fireball
	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;
	
	// scratch attack
	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	
	// can add more if we need the player to do more stuff
	
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	// # of frames for each action
	private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm) {
		super(tm);
		
		width = 30;
		height = 30;
		collisionWidth = 20;
		collisionHeight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		fire = maxFire = 2500;
		
		fireCost = 200;
		fireBallDamage = 5;
		fireBalls = new ArrayList<FireBall>();
		
		scratchDamage = 8;
		scratchRange = 40;
		
		// load sprites
		try {
			
			//BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites2.gif"));
			
			sprites = new ArrayList<BufferedImage[]>();
			
			
			for(int i = 0; i < 7; i++) { // 7 is the number of actions
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					if(i != SCRATCHING) {
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
					}
					else {
						bi[j] = spritesheet.getSubimage(j*width*2, i*height, width*2, height); 
					}
				}
				
				sprites.add(bi);
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		sfx.put("fireball", new AudioPlayer("/SFX/fireball.mp3"));
		
		
	}
	
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getFire() {
		return fire;
	}
	
	public int getMaxFire() {
		return maxFire;
	}
	
	public void setFiring() {
		firing = true;
	}
	
	public void setScratching() {
		scratching = true;
	}
	
	public void setGliding(boolean b) {
		gliding = b;
	}
	
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i); 
			
			
			// check scratch attack
			if(scratching) {
				if(facingRight) {
					if(( e.getX() > x ) && (e.getX() < x + scratchRange) && (e.getY() > y - height / 2) && (e.getY() < y + height/2) ) {
						e.hit(scratchDamage);
					}
				}
				else {
					if( (e.getX() < x) && (e.getX() > x - scratchRange) && (e.getY() > y - height / 2) && (e.getY() < y + height / 2) ) {
						e.hit(scratchDamage);
					}
				}
			}
			
			// check fireball attack
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			
		/*	// check scratch attack
			if(scratching) {
				if(facingRight) { // if we're facing to the right, check right collision box
					// is enemy within scratch attack range?
					if( (e.getX() > x) && (e.getX() < x + scratchRange) && (e.getY() > y - height /2) && (e.getY() < y + height /2) ) {
						e.hit(scratchDamage);
					}
				}
				else {
					if( (e.getX() < x) && (e.getX() > x - scratchRange) && (e.getY() > y - height /2) && (e.getY() < y + height /2) ) {
						e.hit(scratchDamage);
					}
				}
			}
			
			// check fireball attack
			for(int j = 0; j < fireBalls.size(); j++) {
				if(fireBalls.get(j).intersects(e)) {
					e.hit(fireBallDamage);
					fireBalls.get(j).setHit();
					break;
				}
			}
			*/
			
			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
		}
			
			
	}
	
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition() {
		
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
		
		// stop
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0; // come to full stop
				}
			}
		}
		
		// player cannot attack while moving, except in air
		if((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
			dx = 0; // cannot move
			
		}
		
		// jumping
		if (jumping && !falling) {
			sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed; // the longer you hold jump button, the higher you jump
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check if attack has stopped
		if(currentAction == SCRATCHING) {
			if(animation.hasPlayedOnce()) scratching = false;
		}
		
		if(currentAction == FIREBALL) {
			if(animation.hasPlayedOnce()) firing = false; 
		}
		
		
		// fireball attack
		fire += 1;
		if(fire > maxFire) fire = maxFire;
		if(firing && currentAction != FIREBALL) {
			if(fire > fireCost) { // if we have enough energy to perform the fireBall
				fire -= fireCost;
				FireBall fb = new FireBall(tileMap, facingRight);
				fb.setPosition(x, y);
				fireBalls.add(fb);
				
			}
		}
		
		// update fireballs
		for(int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).update();
			if(fireBalls.get(i).shouldRemove()) {
				fireBalls.remove(i);
				i--;
			}
		}
		
		// check if done flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(scratching) {
			if(currentAction != SCRATCHING) {
				sfx.get("scratch").play();
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing) {
			if(currentAction != FIREBALL) {
				sfx.get("fireball").play();
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy > 0) {
			if(gliding) {
				if(currentAction != GLIDING) {
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				 animation.setFrames(sprites.get(JUMPING));
				 animation.setDelay(-1);
				 width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		// set the direction player is facing
		if (currentAction != SCRATCHING && currentAction != FIREBALL) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		// draw fireballs
		for (int i = 0; i < fireBalls.size(); i++) {
			fireBalls.get(i).draw(g);
		}
		
		// draw player
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
	}

}
