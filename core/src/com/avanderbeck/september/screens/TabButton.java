package com.avanderbeck.september.screens;

import com.avanderbeck.september.Button;
import com.avanderbeck.september.GameScreen;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.character.Character;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TabButton extends Button {
	
	TextureRegion bgcorner, bgvertborder, bghorzborder, bgcolour, logo;
	
	Sortie game;

	public TabButton(Sortie game, int x, int y, boolean focused, TextureRegion logo)
	{
		this.game = game;
		
		int f = 0; //a focus additive, for the texture region
		if(!focused)
			f = 64;
		
		bgcorner = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 + f, 0, 16, 16);
		bgvertborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 + f, 16, 16, 16);
		bghorzborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 + f, 0, 16, 16);
		bgcolour = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 + f, 16, 16, 16);
		this.logo = logo;
		
		setX(x);
		setY(y);
		setWidth(160);
		setHeight(64);
	}
	

	@Override
	public void draw(int x, int y) {
		game.batch.draw(bgcorner, x, y + getHeight() - 16);
		game.batch.draw(bgcorner, x + getWidth() , y + getHeight() - 16, -16, 16);
		
		
		for(int i = 0; i < ((getWidth()- 32) / 16); i ++)
		{
			game.batch.draw(bghorzborder, x + 16 + (16 * i), y + getHeight() - 16);
		}
		
		
		for(int i = 0; i < ((getHeight()- 16) / 16); i ++)
		{
			game.batch.draw(bgvertborder, x, y + getHeight() - 32 - (16 * i));
			game.batch.draw(bgvertborder, x + getWidth(), y + getHeight() - 32  - (16 * i), -16, 16 );
		}
		
		for(int xx = 0; xx < ((getWidth()- 32) / 16); xx++)
			for(int yy = 0; yy < ((getHeight()- 16) / 16); yy++)
				game.batch.draw(bgcolour, x + 16 + (xx*16), y + getHeight() - 32  - (16 * yy));

		game.batch.draw(logo, x + getWidth() / 2 - 32, y + getHeight() / 2 - 32);
	}


	@Override
	public void doThing() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doThing(Character c) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doThing(GameScreen gs) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doThing(Object o) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doThing(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
