package com.costular.crabox.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.costular.crabox.Cbx;

public class ToggleButton extends Button{

	public ToggleButton(ButtonStyle style) {
		super(style);
	}
	
	@Override
	public void setChecked(boolean bool) {
		super.setChecked(bool);
		
		Cbx.getAudio().setCanPlay(!bool);
	}
	
	
}
