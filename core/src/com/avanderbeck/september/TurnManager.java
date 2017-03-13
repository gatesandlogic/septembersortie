package com.avanderbeck.september;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.avanderbeck.september.character.Character;
import com.avanderbeck.september.character.MapUnit;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TurnManager {
	
	Queue<Character> turnQ;
	List<Character> waiting;
	
	TextureRegion [] turnTag;
	

	public TurnManager(Sortie game)
	{
		turnQ = new LinkedList<Character>();
		waiting = new LinkedList<Character>();
		
		turnTag = new TextureRegion[5];
		for(int i = 0; i < 5; i++)
			turnTag[i] = new TextureRegion(game.assman.get("lolis/turntags.png", Texture.class), 0, 32 * i, 64, 32);
	}
	
	public boolean add(Character c)
	{
		return waiting.add(c);
	}

	public Character poll() {
		return turnQ.poll();
	}
	
	public List<Character> getEveryone()
	{
		List<Character> temp = new LinkedList<Character>();
		
		temp.addAll(turnQ);
		temp.addAll(waiting);
	
		return temp;
	}

	public boolean remove(Character c) {
		if(waiting.remove(c))
			return true;
		if(turnQ.remove(c))
			return true;
		return false;
	}
	
	public void update(float delta)
	{
		for(Character c : waiting)// add a character to the Q if their turn is up
		{
			if(c.getTimeSinceTurn() > c.getTurnSpeed())
			{
				//if(!turnQ.contains(c))
				turnQ.add(c);
			}
			else
			{
				c.setTimeSinceTurn(c.getTimeSinceTurn() + delta);
			}
		}
		
		for(Character c : turnQ) // If their turn is up, remove them from waiting
		{
			waiting.remove(c);
		}
	}
	
	public void draw(SpriteBatch batch, BitmapFont font, int x, int y)
	{
		int lineHeight = 33;
		int i = 0;
		
		for(Character c : turnQ)
		{
			c.drawTag(batch, x, y - (lineHeight * i++), false, font);
		}
		i++;
		for(Character c : waiting)
		{
			c.drawTag(batch, x, y - (lineHeight * i++), true, font);
		}
	}

	public Object peek() {
		return turnQ.peek();
	}
	
	
	public Character getCharacter(int x, int y)
	{
		for(Character c : getEveryone())
		{
			if(c.getClass().getSuperclass() == MapUnit.class)
			{
				if(((MapUnit)c).x == x && ((MapUnit)c).y == y)
					return c;
			}
		}
		return null;
	}
	
	public void bringOutYourDead()
	{
		List<Character> temp = new LinkedList<Character>();
		
		for(Character c : turnQ)
		{
			if(c.getClass().getSuperclass() == MapUnit.class && (((MapUnit)c).hp < 1 || ((MapUnit)c).y < 0))
				temp.add(c);
		}
		for(Character c : waiting)
		{
			if(c.getClass().getSuperclass() == MapUnit.class && (((MapUnit)c).hp < 1 || ((MapUnit)c).y < 0))
				temp.add(c);
		}
		
		for(Character c : temp)
		{
			turnQ.remove(c);
			waiting.remove(c);
		}
	}
	
	

}
