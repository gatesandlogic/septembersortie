package com.avanderbeck.september.character;

import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.GameScreen;
import com.avanderbeck.september.GameState;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Team;
import com.avanderbeck.september.TurnManager;
import com.avanderbeck.september.TurnState;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Artillery extends Character {

	TurnManager lolis;
	
	public Texture profile;
	public Animation explosion;
	public Texture background;
	
	float stateTime;
	
	GameBoard gb;
	
	TextureRegion tag;
	
	//this will be used when the map is pushed up all the way
	int artyOffset = 0;
	
	boolean killLolis = true;
	
	public Artillery(Sortie game, GameBoard gb, TurnManager lolis) {
		super(game, gb, Team.BLACK, "Artillery Barrage", 20);
		
		this.lolis = lolis;
		this.gb = gb;
		
		profile = game.assman.get("lolis/artillerycannon.png");
		background = game.assman.get("tilesets/backgrounds/artillerybackground.png");
		
		Texture temp = game.assman.get("effects/explosion.png", Texture.class);
		TextureRegion [] tempii = new TextureRegion[8];
		
		for(int i = 0; i < 8; i++)
		{
			tempii[i] = new TextureRegion(temp, 32 * i, 0, 32, 32);
		}
		explosion = new Animation(0.1f, tempii);
		
		stateTime = 0;
		timeSinceTurn = 0;
		
		tag = new TextureRegion(game.assman.get("lolis/turntags.png", Texture.class), 0, 32, 64, 32);
		}

	@Override
	public void draw(SpriteBatch batch, int x, int y, float stateTime) {
		if(turn == TurnState.TARGET)
		{
			float delay = 0.3f;
			int i = 0;
			int amount = Math.round((float)gb.getWidth() / 2);
			float offset = 32;
			if(amount > gb.getWidth() /2)
			{
				offset -= (float)16 / amount;
			}
			for(; i < amount; i++)
				if(this.stateTime - (delay * i) > 0)
				{
					if(!explosion.isAnimationFinished(this.stateTime - (delay * i)))
					{						
						if(offset*i > gb.getWidth() *  16 - 32)
							batch.draw(explosion.getKeyFrame(this.stateTime - (delay * i), false), x + (gb.getWidth() *  16) - 32, y + (16 * artyOffset));
						else
							batch.draw(explosion.getKeyFrame(this.stateTime - (delay * i), false), x + (offset*i), y + (16 * artyOffset));
					}
				}
			if(explosion.isAnimationFinished(this.stateTime - (delay * i)))
			{
				turn = TurnState.ATTACK;
				stateTime = 0;
			}
		}
	}

	@Override
	public void takeTurn(float mx, float my, float delta) {
		switch(turn)
		{
		case START:
			//a sweet ass aiming/firing animation should go here
			gb.clearFocused();
			stateTime += delta;
			if(stateTime > 1.5f)
			{
				turn = TurnState.TARGET;
				stateTime = 0;
			}
			break;//flow into the next for now
		case TARGET:
			//a sweet ass exploding animation should go here
			stateTime += delta;
			
			if(killLolis)
			{
				for(Character c : lolis.getEveryone())
				{
					if(c.getClass().getSuperclass() == MapUnit.class)
					{
						MapUnit l = (MapUnit)c;
						if(l.y < artyOffset + 2)
						{
							l.addTextEffect(((GameScreen)game.getScreen()).droid8pt, "-" + (9999 - l.getDefense()));
							l.hp -= 9999 - l.getDefense();
						}
						
					}
						
				}
				killLolis = false;
			}
			break;// flow into the next action for now	
		case ATTACK:
			//we want to move the gameboard down
			int moveammount = 2;

			if(!board.usedUp())
			{
				
				if(board.yOffset < moveammount * 16)
				{
					board.yOffset += 60 * delta;
					
				}
				else//once the board is moved down enough, cut off the last two row, and add new ones on top
				{
					//int moveammount = 5;
					
					for(Character l : lolis.getEveryone())
					{
						if(l.getClass().getSuperclass() == MapUnit.class)
							((MapUnit)l).setCoords(((MapUnit)l).x, ((MapUnit)l).y - moveammount);
					}
					board.moveBoardDown(moveammount);
					
					board.yOffset = 0;
					turn = TurnState.END;
					killLolis = true;

					/*if(board.usedUp())
						artyOffset += moveammount;*/

				}
			}
			else
			{
				artyOffset += moveammount;
				turn = TurnState.END;
				killLolis = true;

				if(artyOffset > gb.getHeight())
					((GameScreen)game.getScreen()).state = GameState.GAMEOVER;
			}

			
			break;
		case END:
			stateTime = 0;
			break;
		default:
			break;
		
		}

	}

	@Override
	public void draw(SpriteBatch batch, int x, int y, float stateTime, float scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawTag(SpriteBatch batch, int x, int y, boolean showTime, BitmapFont font) {
	
		batch.draw(tag, x, y);
		
		font.draw(batch, getName(), x + 5, y + 27);

		if(showTime)
			font.draw(batch, (int)(turnSpeed - timeSinceTurn) + "sec", x + 28, y + 16);	
	}

	@Override
	public void draw(SpriteBatch batch, float stateTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawProfile(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isNPC()
	{
		return true;
	}

}
