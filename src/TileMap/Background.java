package TileMap;

import java.awt.image.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

import java.awt.*;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale; // the scale by which the background moves when you're moving
	
	public Background(String s, double ms) {
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		//this.x = (x * moveScale) % GamePanel.WIDTH;
		//this.y = (y * moveScale) % GamePanel.HEIGHT;
		this.x = x * moveScale;
		this.y = y * moveScale;
	}
	
	public void setVector(double dx, double dy) {
		// for the background to automatically scroll
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
		/*if (x < 0) {
			g.drawImage(image, (int)x + GamePanel.WIDTH, (int) y, null);
		}
		if (x > 0) {
			g.drawImage(image, (int)x - GamePanel.WIDTH, (int) y, null);
		}*/
	}
}
