package com.avanderbeck.september.character;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.MoverType;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Team;
import com.avanderbeck.september.TurnState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class APC extends MapUnit {
	
	
	public List<Path> paths;
	AStarPathFinder astar;

	//these will be used exclusively for movement;
	Step curStep;
	Step nextStep;
	
	TextureRegion profile;
	
	private TextureRegion tag;
	
	private List<MapUnit> passengers;
	
	public APC(Sortie game, GameBoard gb, int x, int y)
	{
		super(game, gb, Team.GREEN, 6, "APC", 10);
		
		speed = 6;
		hp = 10;
		def = 8;
		atk = 0;
		range = 0;
		isRight = true;
		
		Texture lolitex = game.assman.get("lolis/mapunits/apc.png");
		
		TextureRegion [] temp = {new TextureRegion(lolitex, 0, 0, 16, 16), new TextureRegion(lolitex, 16, 0, 16, 16)};
		idle = new Animation(0.5f,temp);
		idle.setPlayMode(PlayMode.LOOP);
		
		TextureRegion [] temp2 = {new TextureRegion(lolitex, 0, 0, 16, 16), new TextureRegion(lolitex, 32, 0, 16, 16)};
		walk = new Animation(0.15f,temp2);
		walk.setPlayMode(PlayMode.LOOP);
	
		
		TextureRegion [] temp5 = {new TextureRegion(lolitex, 0, 0, 16, 16), new TextureRegion(lolitex, 48, 0, 16, 16)};
		hurt = new Animation(0.1f,temp5);
		hurt.setPlayMode(PlayMode.LOOP);
		
		
		turn = TurnState.NOTMYTURN;

		profile = new TextureRegion(game.assman.get("lolis/APCprofile.png", Texture.class), 0, 0, 256, 256);
		
		
		tag = new TextureRegion(game.assman.get("lolis/turntags.png", Texture.class), 0, 0, 64, 32);
		
		passengers = new ArrayList<MapUnit>();

	}
	
	@Override	
	public void draw(SpriteBatch batch, float stateTime) {
		draw(batch, (int)onScreenX, (int)onScreenY, stateTime, 1);
	}
	
	@Override
	public void draw(SpriteBatch batch, int x, int y, float stateTime) {
		draw(batch, x, y, stateTime, 1);
	}
	
	@Override
	public void draw(SpriteBatch batch, int x, int y, float stateTime, float scale) {
		updateTextEffects(Gdx.graphics.getDeltaTime());
		drawTextEffects();
		
		if(hurting)
		{
			if(isRight)
				batch.draw(hurt.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(hurt.getKeyFrame(stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			return;//shot characters don't care what they're doing
		}
		
		switch(turn){
		case MOVE:
			if(isRight)
				batch.draw(walk.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(walk.getKeyFrame(stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			
			break;
		default:
			if(isRight)
				batch.draw(idle.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(idle.getKeyFrame(stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			break;
		}	
	}
	
	@Override
	public void drawTag(SpriteBatch batch, int x, int y, boolean showTime, BitmapFont font) {
	
		batch.draw(tag, x, y);
		
		font.draw(batch, getName(), x + 5, y + 27);
		
		batch.draw(idle.getKeyFrame(0), x + 61, y+3, -16, 16);
		
		font.draw(batch, "HP " + (int)hp, x + 5, y + 16);
		font.draw(batch, "ATK " + atk , x + 5, y + 8);	
		font.draw(batch, "DEF " + getDefense() , x + 28, y + 8);

		if(showTime)
			font.draw(batch, (int)(turnSpeed - timeSinceTurn) + "sec", x + 28, y + 16);	
	}

	
	@Override
	public void takeTurn(float mx, float my, float delta) {
		switch(turn) //turn phases are in order they should be executed
		{
		case START:
			//We might want to do something cool here, but for now, change the state and flow into selecting move
			/*attackTimer += Gdx.graphics.getDeltaTime();
			
			if(attackTimer > salute.getFrameDuration() * 6)
			{
				attackTimer = 0;
				turn = TurnState.SELECTMOVE;
			}*/
			
			
			if(paths.isEmpty())
			{
				for(int y = 0; y < board.getHeight(); y++)
					for(int x = 0; x < board.getWidth(); x++)
						if(inRange(x, y, speed))
						{
							Path p = null;
							p = astar.findPath(this, this.x, this.y, x, y);
							if(p != null)
							{
								paths.add(p);
								board.getTile(x,y).focused = true;
							}
						}
				
				board.clearFocused();
				
				for(Path p : paths)
				{
					int x = p.getX(p.getLength() -1);
					int y = p.getY(p.getLength() - 1);
					board.getTile(x, y).focused = true;
				}
			}
			turn = TurnState.SELECTMOVE;

		
		case SELECTMOVE:
			break;
		case MOVE:
			//First decide which of the saved paths are being used.
			if(paths.size()  > 1)
			{
				Path temp = null;
				for(Path p : paths)
				{
					int x = p.getX(p.getLength() -1);
					int y = p.getY(p.getLength() - 1);
					if(x == destTileX && y == destTileY)
					{
						temp = p;
						break;
					}
				}
				paths.clear();
				paths.add(temp);
				
				curStep = paths.get(0).getStep(0);
			}
			//if there are not more steps, end move phase
			if(curStep.equals(paths.get(0).getStep(paths.get(0).getLength() -1)))
			{
				setCoords(destTileX,destTileY); //these actions happen after the loli has moved
				turn = TurnState.TARGET;
				board.clearFocused();
				paths.clear();
				break;
			}
			else
			{
				Path path = paths.get(0);
				if(nextStep == null)//next, decide which step we are on, and move to the next step.
				{
					int i = 0;
					for(; i < path.getLength() && !path.getStep(i).equals(curStep); i++);
					nextStep = path.getStep(i + 1);
				}
				
				int speed = 60;
				if(xCoordToScreen(nextStep.getX()) == Math.round(onScreenX))// May want to replace with a more forgiving rounding method
				{
					onScreenX = xCoordToScreen(nextStep.getX()); // if it's close enough, make it exact
					if(yCoordToScreen(nextStep.getY()) == Math.round(onScreenY))
					{
						setCoords(nextStep.getX(),nextStep.getY()); //these actions happen after the loli has moved
						curStep = nextStep;
						x = curStep.getX();
						y = curStep.getY();
						
						nextStep = null;
					}
					else
					{
						if(yCoordToScreen(nextStep.getY()) > onScreenY) //we need to move up
						{
							onScreenY += speed * delta;
						}
						else if(yCoordToScreen(nextStep.getY()) < onScreenY) //we need to move up
						{
							onScreenY -= speed * delta;
						}
					}
				}
				else
				{
					if(xCoordToScreen(nextStep.getX()) > onScreenX) //we need to move right
					{
						isRight = true;
						onScreenX += speed * delta;
					}
					else if(xCoordToScreen(nextStep.getX()) < onScreenX) //we need to move left
					{
						isRight = false;
						onScreenX -= speed * delta;
					}
				}
				
			}
			
			
			break;
		case TARGET:

			break;
		case ATTACK:
			turn = TurnState.END;
			break;
		case END:
			break;	
		case NOTMYTURN:
			break;
		case SELECTACTION:
			break;
		default:
			break;
		}
	}

	@Override
	public boolean inMoveRange(int x, int y) {
		if(paths.size()  > 1)
		{
			for(Path p : paths)
			{
				int px = p.getX(p.getLength() -1);
				int py = p.getY(p.getLength() - 1);
				if(px == x && py == y)
					return true;
			}
		}
		return false;
	}

	@Override
	public void drawProfile(int x, int y) {
		game.batch.draw(profile, x, y);	
	}
	
	public boolean isNPC()
	{
		return false;
	}

	@Override
	public MoverType getMoverType() {
		return MoverType.WHEELED;
	}
	
	public List<MapUnit> getPassengers()
	{
		return passengers;
	}
	
	public String getUnitType()
	{
		return "Armored Personnel Carrier";
	}
	
	@Override
	public void deploy(GameBoard gb, int x, int y) {
		board = gb;
		setCoords(x, y);
		astar = new AStarPathFinder(gb, speed, false);
		paths = new LinkedList<Path>();
	}
}
