package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import java.awt.*;

public class Arrow extends MapObject {
	
	private boolean hit; // means the fireball has hit something
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	public Arrow(TileMap tm, boolean right) {
		
		super(tm);
		
		facingRight = true;
		
		moveSpeed = 3.8;
		
		if(right) {
			dx = moveSpeed;
		}
		
		else {
			dx = -moveSpeed;
		}
		
		// Makes the arrow go down with gravity
		dy = 0.1;
		
		
		width = 30;
		height = 30;
		collisionWidth = 20;
		collisionHeight = 10;
		
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/arrow.png"));
			
			sprites = new BufferedImage[1];
			
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);	
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(sprites);
		animation.setDelay(70);
		dx = 0; // stop moving
	}
	
	
	public boolean shouldRemove() { // whether or not we should take it out of the game
		return remove;
	}
	
	
	public void update() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && !hit) {
			setHit(); // if it's not moving and it's not labeled as hit, label it as hit
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}
