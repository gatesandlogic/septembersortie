package com.avanderbeck.september.character;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.pathfinding.Mover;

import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Team;
import com.avanderbeck.september.TextEffect;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MapUnit extends Character implements Mover{

	Animation idle;
	Animation walk;
	Animation attack;
	Animation hurt;
	Animation die;
	
	boolean isRight;
	public boolean hurting = false;
	
	public int x, y;
	
	public int speed;
	public int range;
	
	public float hp;
	public final int maxHP;
	
	public int def;
	public int atk;
	
	String name;
	
	public MapUnit target;
	
	List<TextEffect> textEffects;
	
	//these will be used in the movement phase
	public int destTileX;
	public int destTileY;
	
	public float onScreenX;
	public float onScreenY;
	
	public MapUnit(Sortie game, GameBoard gb, Team t, int turnTime, String name, int maxHP) {
		super(game, gb, t, name, turnTime);
		this.maxHP = maxHP;
		textEffects = new LinkedList<TextEffect>();
	}

	public void setCoords(int x, int y)
	{
		this.x = x;
		this.y = y;
		onScreenX = xCoordToScreen(x);
		onScreenY = yCoordToScreen(y);
	}

	public int getDistance(int x, int y)
	{
		return Math.abs(this.x - x) + Math.abs(this.y - y);
	}
	
	public boolean inRange(int x, int y, int range)
	{
		return getDistance(x, y) <= range;
	}
	
	public boolean inMoveRange(int x, int y)
	{
		return inRange(x, y, speed);
	}
	
	public boolean inAttackRange(int x, int y)
	{
		return inRange(x, y, range);
	}
	
	public void addTextEffect(BitmapFont font, String text)
	{
		textEffects.add(new TextEffect(game, font, text, onScreenX, onScreenY + 16));
	}
	private void pruneTextEffects()
	{
		List<TextEffect> temp = new LinkedList<TextEffect>();
		for(TextEffect te : textEffects)
			if(te.isComplete())
				temp.add(te);
		for(TextEffect te : temp)
			textEffects.remove(te);
	}
	public void updateTextEffects(float delta)
	{
		pruneTextEffects();
		for(TextEffect te : textEffects)
			te.update(delta);
	}
	
	public void drawTextEffects()
	{
		for(TextEffect te : textEffects)
			te.draw();
	}
	
	public int getDefense()
	{
		if(board != null)
			return def + board.getTile(x, y).getDefense();
		else
			return def;
	}
	
	public boolean isRight()
	{
		return isRight;
	}
	
	public void setRight(boolean r)
	{
		isRight = r;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	public TextureRegion getMapUnit()
	{
		return idle.getKeyFrame(0);
	}
	
	public abstract String getUnitType();
	
	public abstract void deploy(GameBoard gb, int x, int y);
}
