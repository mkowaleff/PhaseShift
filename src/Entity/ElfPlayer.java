package Entity;

import TileMap.*;

import java.util.ArrayList;
import java.util.HashMap;
import Audio.AudioPlayer;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ElfPlayer extends MapObject {
	
	// Player stuff
	private int health;
	private int maxHealth;
	private int stamina; // attack variables
	private int maxStamina;
	private boolean isDead;
	private boolean flinching;
	private long flinchTimer; // flinching time
	
	// Arrow
	private boolean isShooting;
	private int costToShoot;
	private int rangedDamage;
	private ArrayList<Arrow> arrows;
	
	// Melee
	private boolean isAttackingMelee;
	private int meleeDamage;
	private int meleeRange;
	
	
	// can add more if we need the player to do more stuff
	
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	// # of frames for each action
	private final int[] numFrames = {7, 10, 10, 6, 6, 5};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int SHOOTING = 2;
	private static final int MELEE = 3;
	private static final int DYING = 4;
	private static final int JUMPING = IDLE;
	private static final int FALLING = IDLE;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public ElfPlayer(TileMap tm) {
		super(tm);
		
		width = 80;
		height = 80;
		collisionWidth = 20;
		collisionHeight = 59;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		stamina = maxStamina = 2500;
		
		costToShoot = 200;
		rangedDamage = 5;
		arrows = new ArrayList<Arrow>();
		
		meleeDamage = 8;
		meleeRange = 40;
		
		isDead = false;
		
		// load sprites
		try {
			
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/grandelf.png"));
			/* Row:
			 * 1. Idle
			 * 2. Walking
			 * 3. Shooting
			 * 4. Melee
			 * 5. Dying
			 */
			
			sprites = new ArrayList<BufferedImage[]>();
			
			
			for(int i = 0; i < 4; i++) { // 5 is the number of actions
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(j*width, i*height, width, height);
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
		sfx.put("arrow1", new AudioPlayer("/SFX/arrow1.mp3"));
		sfx.put("arrow2", new AudioPlayer("/SFX/arrow2.mp3"));
		sfx.put("arrow3", new AudioPlayer("/SFX/arrow3.mp3"));
		sfx.put("knife1", new AudioPlayer("/SFX/knife1.mp3"));
		sfx.put("knife2", new AudioPlayer("/SFX/knife2.mp3"));
		sfx.put("knife3", new AudioPlayer("/SFX/knife3.mp3"));
		
		
	}
	
	
	public boolean isDead() {
		return isDead;
	}
	
	public void setDead() {
		isDead = true;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getStamina() {
		return stamina;
	}
	
	public int getMaxStamina() {
		return maxStamina;
	}
	
	public void setShooting() {
		isShooting = true;
	}
	
	public void setAttackingMelee() {
		isAttackingMelee = true;
	}
	
	
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i); 
			
			// check scratch attack
						if(isAttackingMelee) {
							if(facingRight) {
								if(( e.getX() > x ) && (e.getX() < x + meleeRange) && (e.getY() > y - height / 2) && (e.getY() < y + height/2) ) {
									e.hit(meleeDamage);
								}
							}
							else {
								if( (e.getX() < x) && (e.getX() > x - meleeRange) && (e.getY() > y - height / 2) && (e.getY() < y + height / 2) ) {
									e.hit(meleeDamage);
								}
							}
						}
			
			
			// check ranged attack
			for(int j = 0; j < arrows.size(); j++) {
				if(arrows.get(j).intersects(e)) {
					e.hit(rangedDamage);
					arrows.get(j).setHit();
					break;
				}
			}
			
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
		if(health == 0) isDead = true;
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
		if((currentAction == SHOOTING) && !(jumping || falling)) {
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
			dy += fallSpeed;
			
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
		if(currentAction == SHOOTING) {
			if(animation.hasPlayedOnce()) isShooting = false; 
		}
		if(currentAction == MELEE) {
			if(animation.hasPlayedOnce()) isAttackingMelee = false;
		}
		
		
		// ranged attack
		stamina += 1;
		if(stamina > maxStamina) stamina = maxStamina;
		if(isShooting && currentAction != SHOOTING) {
			if(stamina > costToShoot) { // if we have enough energy to perform the fireBall
				stamina -= costToShoot;
				Arrow fb = new Arrow(tileMap, facingRight);
				fb.setPosition(x, y);
				arrows.add(fb);
				
			}
		}
		
		// update arrows
		for(int i = 0; i < arrows.size(); i++) {
			arrows.get(i).update();
			if(arrows.get(i).shouldRemove()) {
				arrows.remove(i);
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
		
		// Set Animation
		else if(isShooting) {
			if(currentAction != SHOOTING) {
				playShootingSound();
				currentAction = SHOOTING;
				animation.setFrames(sprites.get(SHOOTING));
				animation.setDelay(35);
				width = 80;
			}
		}
		else if(isAttackingMelee) {
			if(currentAction != MELEE) {
				playMeleeSound();
				currentAction = MELEE;
				animation.setFrames(sprites.get(MELEE));
				animation.setDelay(100);
				width = 80;
			}
		}
		/*else if(dy > 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 80;
			}
		}*/
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				 animation.setFrames(sprites.get(JUMPING));
				 animation.setDelay(-1);
				 width = 80;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 80;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 80;
			}
		}
		
		animation.update();
		
		// set the direction player is facing
		if (currentAction != SHOOTING) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	
	private void playMeleeSound() {
		Random rand = new Random();
		int n = rand.nextInt(3) + 1;
		sfx.get("knife" + n).play();
		
	}


	private void playShootingSound() {
		Random rand = new Random();
		int n = rand.nextInt(3) + 1;
		sfx.get("arrow" + n).play();
	}


	public void draw(Graphics2D g) {
		setMapPosition();
		
		// draw projectiles
		for (int i = 0; i < arrows.size(); i++) {
			arrows.get(i).draw(g);
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
