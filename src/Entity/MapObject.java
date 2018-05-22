package Entity;

import TileMap.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;

public abstract class MapObject {
	// players, enemies, projectiles, they will all be map objects
	
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int collisionWidth;
	protected int collisionHeight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	
	// movement that determines what the object is doing
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes ("physics")
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart; // how high the object can jump
	protected double stopJumpSpeed;
	
	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		
		return r1.intersects(r2);
	}
	
	
	public Rectangle getRectangle() {
		return new Rectangle((int)x - collisionWidth, (int)y - collisionHeight, collisionWidth, collisionHeight);
	}
	
	
	public void calculateCorners(double x, double y) {
		int leftTile = (int) (x - collisionWidth / 2) / tileSize;
		int rightTile = (int) (x + collisionWidth / 2 - 1) / tileSize;
		int topTile = (int) ( y - collisionHeight / 2) / tileSize;
		int bottomTile = (int) (y + collisionHeight / 2 - 1) / tileSize;
		
		int tl = tileMap.getType(topTile,  leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile,  leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = (tl == Tile.BLOCKED);
		topRight = (tr == Tile.BLOCKED);
		bottomLeft = (bl == Tile.BLOCKED);
		bottomRight = (br == Tile.BLOCKED);
		
		
		
	}
	
	public void checkTileMapCollision() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;
		
		// destination positions
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		// 4-corner method for determining tile collision
		// y-direction
		calculateCorners(x, ydest);
		
		
		if(dy < 0) { // if we're headed upwards
			if(topLeft || topRight) { // check top two corners, and if either of these are set, we have to stop moving in the up direction and set the object just beneath the tile it just hit
				dy = 0;
				ytemp = currRow * tileSize + collisionHeight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		if(dy > 0) { // if we've landed on a tile
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - collisionHeight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		// y-direction
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + collisionWidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - collisionWidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		// if falling off a cliff
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getCollisionWidth() {
		return collisionWidth;
	}
	
	public int getCollisionHeight() {
		return collisionHeight;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		// we have to find out how far the tile map has moved in order to offset the player back onto the screen. The final position is x + xmap and y + ymap
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	
	public void setLeft(boolean b) {
		left = b;
	}
	
	public void setRight(boolean b) {
		right = b;
	}
	
	public void setUp(boolean b) {
		up = b;
	}
	
	public void setDown(boolean b) {
		down = b;
	}
	
	public void setJumping(boolean b) {
		jumping = b;
	}
	
	// only draw mapobjects if they're on the screen just like the tile map
	public boolean notOnScreen() {
		return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g) {
		
		int tempX = (int)(x + xmap - width /2);
		int tempY = (int)(y + ymap - height /2);
		
		int outBoxWidth = animation.getImage().getWidth();
		int outBoxHeight = animation.getImage().getHeight();
		
		if(facingRight) {
			g.drawImage(animation.getImage(), tempX, tempY, null);
		}
		else {
			g.drawImage(animation.getImage(),(tempX + width), tempY, -width, height, null);
		}
		g.setColor(Color.green);
		//g.drawRect(tempX, tempY, outBoxWidth, outBoxHeight);
		
		g.setColor(Color.red);
		//g.drawRect(tempX + outBoxWidth/2 - collisionWidth/2, tempY + outBoxHeight/2 - collisionHeight/2, collisionWidth, collisionHeight);
	}
	
	public int getTempX() {
		return (int)(x + xmap - width /2);
	}
	
	public int getTempY() {
		return (int)(y + ymap - height /2);
	}
	
}
