package Entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames; // holds all the frames of animation
	private int currentFrame;
	
	private long startTime; // timer between each frame
	private long delay;
	
	private boolean playedOnce; 
	
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	
	public void setDelay(long d) {
		delay = d;
	}
	
	public void setFrame(int i) {
		currentFrame = i;
	}
	
	public void update() {
		
		if(delay == -1) // no animation
			return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;	// time elapsed since last frame came up (in milliseconds)
		
		if(elapsed > delay) {	// if it's greater than the delay, go to next frame and reset the timer
			currentFrame++;
			startTime = System.nanoTime();
		}
		
		// loop detection
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}
		
	}
	
	
	public int getFrame() {
		// gets current frame number
		return currentFrame;
	}
	
	public BufferedImage getImage() {
		// gets current frame image
		return frames[currentFrame];
	}
	
	
	public boolean hasPlayedOnce() {
		return playedOnce;
	}

}
