package com.avanderbeck.september.character;

import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Team;
import com.avanderbeck.september.TurnState;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Character {
	
	Sortie game;
	GameBoard board;
	
	String name;
	
	TurnState turn;
	public final Team team;
	
	float turnSpeed;
	
	float timeSinceTurn;
		
	public Character(Sortie game, GameBoard gb, Team t, String name,  float turnSpeed)
	{
		this.game = game;
		board = gb;
		team = t;
		this.name = name;
		this.turnSpeed = turnSpeed;
		timeSinceTurn = turnSpeed + 1; //make it so any new characters can immediately take a turn
	}
	
	public abstract void draw(SpriteBatch batch, float stateTime);
	public abstract void draw(SpriteBatch batch, int x, int y, float stateTime);
	public abstract void draw(SpriteBatch batch, int x, int y, float stateTime, float scale);
	public abstract void drawTag(SpriteBatch batch, int x, int y, boolean showTime, BitmapFont font);

	
	public abstract void takeTurn(float mx, float my, float delta);//pass in mouse click coordinates when a turn is taken, -1 should read as no click

	public float xCoordToScreen(int x)
	{
		return x * 16 + board.getXPadding();
	}
	
	public float yCoordToScreen(int y)
	{
		return y * 16 + board.getYPadding();
	}
	
	public String getName()
	{
		return name;
	}
	
	public abstract void drawProfile(int x, int y);
	
	public TurnState getTurnState()
	{
		return turn;
	}
	
	public void setTurnState(TurnState t)
	{
		turn = t;
	}
	
	public float getTimeSinceTurn()
	{
		return timeSinceTurn;
	}
	public float getTurnSpeed()
	{
		return turnSpeed;
	}
	public void setTimeSinceTurn(float t)
	{
		timeSinceTurn = t;
	}
	
	public abstract boolean isNPC();
}
