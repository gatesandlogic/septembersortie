package com.avanderbeck.september.screens;

import com.avanderbeck.september.Sortie;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScrollBar {
	private int x, y, h, vh;//vh will be "virtual height", the amount of space it's scrolling accross
	private float current;
	private TextureRegion pipeEnd, pipe, bar, barStart, barEnd;
	private int barHeight;
	
	private Sortie game;
	
	public ScrollBar(Sortie game, int x, int y, int h, int vh)
	{
		this. game = game;
		this.x = x;
		this.y = y;
		this.h = h;
		setVirtualHeight(vh);
		current = 0;
		
		Texture t = game.assman.get("ui/scrollbar.png", Texture.class);
		pipeEnd = new TextureRegion(t, 0, 0, 16, 16);
		pipe = new TextureRegion(t, 0, 16, 16, 16);
		bar = new TextureRegion(t, 16, 0, 16, 16);
		barStart = new TextureRegion(t, 16, 16, 16, 8);
		barEnd = new TextureRegion(t, 16, 24, 16, 8);

	}
	
	public void draw()
	{
		//draw the background pipe
		int i = y + 16;
		while(i < y + h - 16)
		{
			game.batch.draw(pipe, x, i);
			i+= 16;
		}
		if(i + 16 < y + h)
			game.batch.draw(pipe, x, i);
		//draw the arrows for the end of the pipe
		game.batch.draw(pipeEnd, x, y + 16, 16, -16);
		game.batch.draw(pipeEnd, x, y+h-16);
		
		int topbar = (int) ((y + h - 16) - current);
		
		game.batch.draw(barStart, x, topbar);
		
		if(barHeight <= 16)
			game.batch.draw(barEnd, x, topbar - 8);
		else
		{
			topbar -= 16;
			int temp = barHeight - 16;
			while(temp > 8)
			{
				game.batch.draw(bar, x, topbar);
				topbar -= 16;
				temp -= 16;
			}
			game.batch.draw(barEnd, x, topbar+8);
		}



	}
	
	private void setBarHeight()
	{
		barHeight = (int) ((h-32) * ((h-32) / (float)vh));
	}
	
	public boolean setVirtualHeight(int vh)
	{
		//strictly speaking, if the virtual height is less than or equal to the actual height, this scrollbar is useless
		if(vh > h)
		{
			this.vh = vh;
			setBarHeight();
			return true;
		}
		this.vh = h;
		setBarHeight();
		return false;
	}
	
	public void scrollUp()
	{
		current -= 48;
		if(current < 0)
			current = 0;
	}
	
	public void scrollDown()
	{
		current += 48;
		if(current > (h - 16) - barHeight )
			current = (h - 16) - barHeight;
	}
	
	public boolean setScrollAmmount(float p)
	{
		//not 100% certain whart I'll have to do here
		current = p;
		if(current < 0)
			current = 0;
		if(current > h - barHeight - 16)
			current = h - barHeight - 16;
		return true;
	}
	
	public float getScrollAmmount()
	{
		return current;
	}
	
	public float getScrollPercent()
	{
		
		return vh * (current/ (h - 16));
	}
	
	public boolean inRangeOfUp(int x, int y)
	{
		if((x > this.x && x < this.x + 16)
				&& (y < this.y + h && y > this.y + h - 16))
			return true;
		return false;
	}
	
	public boolean inRangeOfDown(int x, int y)
	{
		if((x > this.x && x < this.x + 16)
				&& (y > this.y && y < this.y + 16))
			return true;
		return false;
	}
	
	public boolean inRangeOfBar(int x, int y)
	{
		if((x > this.x && x < this.x + 16)
				&& (y > this.y + h - current - barHeight && y < this.y + h - current))
			return true;
		return false;
	}
}
