package com.avanderbeck.september;

import com.avanderbeck.september.character.Character;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Button {
	private int x, y, w, h;
	private TextureRegion bg;
	private String txt;
	private BitmapFont fnt;
	
	public Button()
	{
		super();
	}
	
	public Button(Object o)
	{
		super();
	}
	
	public abstract void doThing();
	public abstract void doThing(Character c);
	public abstract void doThing(GameScreen gs);
	public abstract void doThing(Object o);
	public abstract void doThing(int x, int y);
	
	public void draw()
	{
		draw(getX(), getY());
	}
	
	public abstract void draw(int x, int y);
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public TextureRegion getBackground() {
		return bg;
	}

	public void setBackground(TextureRegion bg) {
		this.bg = bg;
	}

	public String getText() {
		return txt;
	}

	public void setText(String txt) {
		this.txt = txt;
	}

	public BitmapFont getFont() {
		return fnt;
	}

	public void setFont(BitmapFont fnt) {
		this.fnt = fnt;
	}
	
	public boolean inRange(float x, float y)
	{
		if(x < this.x)
			return false;
		if(y < this.y)
			return false;
		if(x > this.x + w)
			return false;
		if(y > this.y + h)
			return false;
		return true;
	}
}
