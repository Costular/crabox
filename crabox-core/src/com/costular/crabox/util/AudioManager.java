package com.costular.crabox.util;

import com.badlogic.gdx.audio.Sound;
import com.costular.crabox.Cbx;

public class AudioManager {

	public float musicDefaultVolume;
	public float soundsDefaultVolume;
	
	private boolean canPlay;
	private boolean playingMusic;
	private float musicVolume;
	private float soundsVolume;
	
	public AudioManager() {
		canPlay = Cbx.getPreferences().getSound();
		playingMusic = false;
		
		musicDefaultVolume = 0.7f;
		soundsDefaultVolume = 0.4f;
		
		musicVolume = 0.7f;
		setSoundsVolume(0.4f);
	}
	
	public boolean canPlay() {
		return canPlay;
	}
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}
	public boolean isPlayingMusic() {
		return playingMusic;
	}
	public void setPlayingMusic(boolean playingMusic) {
		this.playingMusic = playingMusic;
	}

	public float getMusicVolume() {
		return musicVolume;
	}
	public void setMusicVolume(float volume) {
		if(volume > 1) {
			this.musicVolume = 1;
		}
		else if(volume < 0.1) {
			this.musicVolume = 0.1f;
		}else {
			this.musicVolume = volume;
		}
	}

	public float getSoundsVolume() {
		return soundsVolume;
	}

	public void setSoundsVolume(float soundsVolume) {
		this.soundsVolume = soundsVolume;
	}

	public void startMusic() {
		if(!canPlay) {
			return;
		}
		
		if(isPlayingMusic()) {
			Cbx.getResources().music.stop();
		}
		
		if(musicDefaultVolume != musicVolume) {
			musicVolume = musicDefaultVolume;
		}
		
		Cbx.getResources().music.setVolume(musicVolume); // En un futuro ellos elegirán el volumen.
		Cbx.getResources().music.setLooping(true);
		Cbx.getResources().music.play();
		
		setPlayingMusic(true); // Marcamos que está sonando la música
	}
	
	public void stopMusic() {		
		Cbx.getResources().music.stop();
		
		//Decir que ya no está sonando la música
		setPlayingMusic(false);
	}
	
	public void playSound(Sound sound, boolean loop) {
		if(!canPlay) {
			return;
		}
		
		if(sound == null) {
			return;
		}
		
		if(soundsVolume != soundsDefaultVolume) {
			soundsVolume = soundsDefaultVolume;
		}
		
		sound.setLooping(0, loop);
		sound.play(soundsVolume);
	}
	
	public void downMusicToStop() {
		for(int i = 0; i < 10; i++) {
			setMusicVolume(getMusicVolume()-0.1f);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stopMusic();
	}
	
	public void playJumpSound() {
		playSound(Cbx.getResources().jump, false);
	}
}
