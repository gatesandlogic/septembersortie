package com.avanderbeck.september.screens;

import com.avanderbeck.september.Button;
import com.avanderbeck.september.GameScreen;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.character.Character;
import com.avanderbeck.september.character.MapUnit;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class UnitButton extends Button {
	
	TextureRegion bgcorner, bgvertborder, bghorzborder, bgcolour;
	TextureRegion ufbgcorner, ufbgvertborder, ufbghorzborder, ufbgcolour;//these buttons will actually have to swap state, so they will keep unfocused backgrounds as well
	//Texture star;
	private boolean focused;
	
	Sortie game;
	
	MapUnit unit;
	private int scrollOffSet = 0;
	
	public UnitButton(Sortie game, int x, int y, int width, int height, boolean focused, MapUnit mapunit)
	{
		this.game = game;
		
		int f = 0; //a focus additive, for the texture region
		//star = game.assman.get("ui/star.png", Texture.class);

		
		bgcorner = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 + f, 0, 16, 16);
		bgvertborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 + f, 16, 16, 16);
		bghorzborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 + f, 0, 16, 16);
		bgcolour = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 + f, 16, 16, 16);
		
		f = 64;
		ufbgcorner = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 + f, 0, 16, 16);
		ufbgvertborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 + f, 16, 16, 16);
		ufbghorzborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 + f, 0, 16, 16);
		ufbgcolour = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 + f, 16, 16, 16);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setFont(game.assman.get("fonts/courieroutline.fnt", BitmapFont.class));
		unit = mapunit;	
		
		this.focused = focused;
	}
	

	@Override
	public void draw(int x, int y) {

		y += scrollOffSet;
		//Draw the background of the button
		if(focused)
		{
			game.batch.draw(bgcorner, x, y + getHeight() - 16);
			game.batch.draw(bgcorner, x, y + 16, 16, -16);
			
			
			for(int i = 0; i < ((getWidth()- 16) / 16); i ++)
			{
				game.batch.draw(bghorzborder, x + 16 + (16 * i), y + getHeight() - 16);
				game.batch.draw(bghorzborder, x + 16 + (16 * i), y + 16, 16, -16);
			}
					
			for(int i = 0; i < ((getHeight()- 32) / 16); i ++)
				game.batch.draw(bgvertborder, x, y + getHeight() - 32 - (16 * i));
	
			for(int xx = 0; xx < ((getWidth()- 16) / 16); xx++)
				for(int yy = 0; yy < ((getHeight()- 32) / 16); yy++)
					game.batch.draw(bgcolour, x + 16 + (xx*16), y + getHeight() - 32  - (16 * yy));
		}
		else
		{
			game.batch.draw(ufbgcorner, x, y + getHeight() - 16);
			game.batch.draw(ufbgcorner, x, y + 16, 16, -16);
		
			
			for(int i = 0; i < ((getWidth()- 16) / 16); i ++)
			{
				game.batch.draw(ufbghorzborder, x + 16 + (16 * i), y + getHeight() - 16);
				game.batch.draw(ufbghorzborder, x + 16 + (16 * i), y + 16, 16, -16);
			}
					
			for(int i = 0; i < ((getHeight()- 32) / 16); i ++)
				game.batch.draw(ufbgvertborder, x, y + getHeight() - 32 - (16 * i));
	
			for(int xx = 0; xx < ((getWidth()- 16) / 16); xx++)
				for(int yy = 0; yy < ((getHeight()- 32) / 16); yy++)
					game.batch.draw(ufbgcolour, x + 16 + (xx*16), y + getHeight() - 32  - (16 * yy));
		}
		
		game.batch.draw(unit.getMapUnit(), getX()+ getWidth() - 14, y + 4, -16, 16);
				
		getFont().draw(game.batch, unit.getName(), getX() + 4, y + getFont().getCapHeight() + getHeight() - 16, getWidth() - 8, 10, true);
		getFont().draw(game.batch, unit.getUnitType(), getX() + 8, y + getFont().getCapHeight() + getHeight() - 32 , getWidth() - 8, 10, true);

		
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
	
	public boolean isFocused()
	{
		return focused;
	}
	
	public void setFocus(boolean f)
	{
		focused = f;
	}

	public MapUnit getUnit()
	{
		return unit;
	}


	public void setScrollOffset(int scrollPecent) {
		scrollOffSet  = scrollPecent;		
	}
	
	public boolean inRange(float x, float y)
	{
		if(x < getX())
			return false;
		if(y < getY() + scrollOffSet)
			return false;
		if(x > getX() + getWidth())
			return false;
		if(y > getY() + getHeight() + scrollOffSet)
			return false;
		return true;
	}
}
