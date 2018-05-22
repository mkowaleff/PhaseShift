package Entity.Enemies;
import Entity.*;
import TileMap.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;

// basic enemy that moves between walls

public class Zombie extends Enemy {
	
	private boolean isDying;
	private boolean isHurting;
	
	// Melee
	private boolean isAttackingMelee;
	private int meleeDamage;
	private int meleeRange;
	
	private double dxTemp; // speed before stopping
	
	// Motion
	private boolean isMoving;
	
	// AI
	private boolean hasDetectedPlayer;
	
	// Animations
	private ArrayList<BufferedImage[]> sprites;
	// # of frames for each action
	private final int[] numFrames = {13, 12, 5, 9, 7};
	
	// Animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int HURT = 2;
	private static final int DYING = 3;
	private static final int ATTACKING = 4;
	
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Zombie(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.5;
		maxSpeed = 0.5;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 80; // dim of sprite
		height = 80;
		collisionWidth = 20;
		collisionHeight = 60;
		
		health = maxHealth = 40;
		damage = 5;
		
		hasDetectedPlayer = false;
		
		meleeDamage = 5;
		meleeRange = 40;
		
		isDying = false;
		isHurting = false;
		isMoving = true;
		
		
		// Load Sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/zombie.png"));
			/* Row:
			 * 1. Idle
			 * 2. Walking
			 * 3. Hurt
			 * 4. Dying
			 * 5. Attacking
			 */
			
			sprites = new ArrayList<BufferedImage[]>(); // 5 frames in this sprite
			
			for(int i = 0; i < 4; i++) {
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
		
		// Set Animation
		animation = new Animation();
		currentAction = WALKING;
		animation.setFrames(sprites.get(WALKING));
		animation.setDelay(100);
		right = true; // set it starting right
		facingRight = true;
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("death", new AudioPlayer("/SFX/zombieDeath.mp3"));
		sfx.put("wound1", new AudioPlayer("/SFX/zombieWound1.mp3"));
		sfx.put("wound2", new AudioPlayer("/SFX/zombieWound2.mp3"));
		sfx.put("wound3", new AudioPlayer("/SFX/zombieWound3.mp3"));
		sfx.put("wound4", new AudioPlayer("/SFX/zombieWound4.mp3"));
		sfx.put("attack1", new AudioPlayer("/SFX/zombieAttack1.mp3"));
		sfx.put("attack2", new AudioPlayer("/SFX/zombieAttack2.mp3"));
		sfx.put("attack3", new AudioPlayer("/SFX/zombieAttack3.mp3"));
		sfx.put("attack4", new AudioPlayer("/SFX/zombieAttack4.mp3"));
	}
	
	
	private void getNextPosition() {
		// helper for update function just like player
		
		// Movement
		if(!isDying && !isDead) {
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
		}
		if(isDying) {
			stop();
		}
		if(isDead) {
			stop();
		}
		if(isHurting) {
			dxTemp = dx;
			//System.out.println("dxTemp = " + dx);
			
			stop();
			//System.out.println("[GNP]: Zombie is stopping.");
			
			if(animation.hasPlayedOnce()) {
				dx = dxTemp;
				//System.out.println("[GNP]: Zombie speed: " + dx);
				isHurting = false;
			}
		}
		
		// falling
		if (falling) {
			dy += fallSpeed;
		}
		
		//if(isDead) {
			//currentAction = DYING;
			//sfx.get("death").play();
		//}
		
	}
	
	public void setDying() { isDying = true; }
	
	public void setHurting() { isHurting = true; }
	
	public void setAttackingMelee() { isAttackingMelee = true; }
	
	public void checkAttack(Player player) {
		if(isAttackingMelee) {
			if(facingRight) {
				//player.getX() >
			}
		}
	}
	public void update() {
		
		// Update Position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(currentAction == DYING) {
			if(animation.hasPlayedOnce()) {
				isDead = true;
			}
		}
		if(currentAction == HURT) {
			if(animation.hasPlayedOnce()) {
				isHurting = false;
				//System.out.println("[UPDATE] Zombie speed: " + dx);
				dx = moveSpeed;
				//System.out.println("dx = " + dxTemp);
				
			}
		}
		
		// Check Flinching (if hit)
		if(flinching) {
			setHurting();
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000; // calculate how long it's been flinching
			if(elapsed > 400) {
				flinching = false;
			}
			
			if(currentAction != HURT) {
				playWoundSound();
				System.out.println("Zombie is hurt!");
				currentAction = HURT;
				animation.setFrames(sprites.get(HURT));
				animation.setDelay(50);
				width = 80;
			}
			
		}
		
		// Set Animation
		else if(isDying) {
			if(currentAction != DYING) {
				sfx.get("death").play();
				currentAction = DYING;
				animation.setFrames(sprites.get(DYING));
				animation.setDelay(250);
				width = 80;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				width = 80;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(100);
				width = 80;
			}
		}
		
		
		
		
		
		// AI
		// Keep going in one direction until it hits a wall, then go to other direction	
		if(isMoving && !isHurting) {
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
		}
		
		// update animation
		animation.update();
				
		
	}
	
	
	public void draw(Graphics2D g) {
		// if(notOnScreen()) return; // do not draw if it's not on the screen
		setMapPosition();
		drawHealthBar(g);
		super.draw(g);
	}
	
	public void drawHealthBar(Graphics2D g) {
		Color secondaryColor;
		
		int healthBarWidth = 80;
		int HealthBarHeight = 5;
		
		int healthBarX = getTempX() + 1;
		int healthBarY = getTempY() - 15;
		
		double healthRemaining = (double)health/maxHealth;
		double drawWidth = healthRemaining * healthBarWidth;
		
		g.setColor(Color.black);
		g.fillRect(healthBarX + 2, healthBarY + 2, healthBarWidth, HealthBarHeight);
		g.setColor(Color.darkGray);
		g.fillRect(healthBarX - 1, healthBarY - 1, healthBarWidth + 2, HealthBarHeight + 2);
		g.setColor(Color.gray);
		g.fillRect(healthBarX, healthBarY, healthBarWidth, HealthBarHeight);
		
		
		
		if(healthRemaining >= 0.5) {
			
			secondaryColor = new Color(0, 102, 0);
			
			g.setColor(secondaryColor);
			g.fillRect(healthBarX, healthBarY, (int) drawWidth, HealthBarHeight);
			
			
			g.setColor(Color.green);
			g.fillRect(healthBarX, healthBarY, (int) drawWidth-1, HealthBarHeight-1);
		}
		
		else if((healthRemaining < 0.5) && (healthRemaining >= 0.25 )) {
			
			secondaryColor = new Color(204, 102, 0);
			
			g.setColor(secondaryColor);
			g.fillRect(healthBarX, healthBarY, (int) drawWidth, HealthBarHeight);
			
			g.setColor(Color.orange);
			g.fillRect(healthBarX, healthBarY, (int) drawWidth-1, HealthBarHeight-1);
		}
		
		else if(healthRemaining < 0.25) {
			
			secondaryColor = new Color(153, 0, 0);
			
			g.setColor(secondaryColor);
			g.fillRect(healthBarX, healthBarY, (int) drawWidth, HealthBarHeight);
			
			g.setColor(Color.red);
			g.fillRect(healthBarX, healthBarY, (int) drawWidth-1, HealthBarHeight-1);
		}
	}
	
	public void hit(int damage) { 
		// the enemy gets hit
		if(isDead || flinching) return;
		//playWoundSound();
		health -= damage;
		
		if(health < 0) health = 0;
		if(health == 0) {
			setDying();
			isMoving = false;
			//currentAction = DYING;
			//if(animation.hasPlayedOnce() && currentAction == DYING) isDead = true;
		}
		flinching = true;
		//System.out.println("Zombie is hit!");
		flinchTimer = System.nanoTime();
		//System.out.println("flinchTimer: " + flinchTimer);
	}
	
	private void playWoundSound() {
		Random rand = new Random();
		int n = rand.nextInt(4) + 1;
		sfx.get("wound" + n).play();
		
	}
	
	public void stop() {
		dx = 0;
	}

}
