package com.avanderbeck.september;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextEffect {

	Sortie game;
	BitmapFont font;
	
	float stateTime, TTL;
	
	float x, y;
	
	float speed;
	
	String text;
	
	public TextEffect(Sortie game, BitmapFont font, String text, float x, float y)
	{
		this.game = game;
		this.font = font;
		
		this.text = text;
		
		this.x = x;
		this.y = y;
		
		speed = 30;
		
		stateTime = 0;
		TTL = 1.5f;
	}
	
	public boolean isComplete()
	{
		if(stateTime > TTL)
			return true;
		return false;
	}
	
	public void update(float delta)
	{
		stateTime += delta;
		
		y += speed * delta;
		
	}
	
	public void draw()
	{
		if(!isComplete())
			font.draw(game.batch, text, x, y);
	}
}
