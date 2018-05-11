package Audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;

public class AudioPlayer {
	
	private Clip clip; // the DS that holds the audio
	
	public AudioPlayer(String filePath) {
		
		try {
			
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(filePath));
			AudioFormat baseFormat = ais.getFormat(); // mp3 compressed format
			
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, 
					baseFormat.getSampleRate(), 
					16, 
					baseFormat.getChannels(), 
					baseFormat.getChannels() * 2, 
					baseFormat.getSampleRate(), 
					false); // the format we're converting to
			
			AudioInputStream decodedAis = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(decodedAis);
		}
		
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void play() {
		if(clip == null) return; // if the clip is not there, don't play
		stop(); // stop if it is playing
		clip.setFramePosition(0);
		clip.start();
	}
	
	
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}

}
