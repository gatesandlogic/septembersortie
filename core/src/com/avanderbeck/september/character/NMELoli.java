package com.avanderbeck.september.character;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.MoverType;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.GameScreen;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Team;
import com.avanderbeck.september.TurnManager;
import com.avanderbeck.september.TurnState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class NMELoli extends MapUnit {
	
	float stateTime = 0;
	float deathTime = 0;
	
	int view;

	TurnManager lolis;
	
	MapUnit target;
	
	Animation salute;
	
	Path path;
	AStarPathFinder astar;

	//these will be used exclusively for movement;
	Step curStep;
	Step nextStep;
	
	int numSteps = 0;

	private TextureRegion tag;
	private TextureRegion profile;
	
	public NMELoli(Sortie game, GameBoard gb, TurnManager lolis, int x, int y) {
		super(game, gb, Team.BLACK, 8, "Enemy Scout", 10);
		// TODO Auto-generated constructor stub
		speed = 2;
		view = 10;
		hp = 10;
		def = 2;
		atk = 8;
		range = 4;
		isRight = true;
		
		this.lolis = lolis;
		
		astar = new AStarPathFinder(gb, view, false);//search range will be squite far, but overall movement will be determined by speed
		path = null;
		
		Texture lolitex = game.assman.get("lolis/mapunits/scoutloli2.png");
		
		TextureRegion [] temp = {new TextureRegion(lolitex, 0, 0, 16, 16), new TextureRegion(lolitex, 16, 0, 16, 16)};
		idle = new Animation(0.5f,temp);
		idle.setPlayMode(PlayMode.LOOP);
		
		TextureRegion [] temp2 = {new TextureRegion(lolitex, 0, 0, 16, 16), new TextureRegion(lolitex, 32, 0, 16, 16)};
		walk = new Animation(0.15f,temp2);
		walk.setPlayMode(PlayMode.LOOP);

		TextureRegion [] temp3 = {new TextureRegion(lolitex, 48, 0, 16, 16), new TextureRegion(lolitex, 32, 0, 16, 16)};
		attack = new Animation(0.1f,temp3);
		attack.setPlayMode(PlayMode.LOOP);
		
		TextureRegion [] temp4 = new TextureRegion[4];
		for(int i = 0; i < 4; i++)
			temp4[i] = new TextureRegion(lolitex, 64 + (16 * i), 0, 16, 16);
	
		salute = new Animation(0.1f,temp4);
		salute.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		TextureRegion [] temp5 = {new TextureRegion(lolitex, 0, 16, 16, 16), new TextureRegion(lolitex, 16, 16, 16, 16)};
		hurt = new Animation(0.1f,temp5);
		hurt.setPlayMode(PlayMode.LOOP);
		
		
		TextureRegion [] temp6 = new TextureRegion[5];
		for(int i = 0; i < 4; i++)
			temp6[i] = new TextureRegion(lolitex, 32 + (i * 16), 16, 16, 16);
		temp6[4] = new TextureRegion(lolitex, 16 * 7, 16, 16, 16);
		die = new Animation(0.15f,temp6);
		
		turn = TurnState.NOTMYTURN;
		timeSinceTurn = 0;
		
		setCoords(x, y);

		board = gb;
		
		profile = new TextureRegion(game.assman.get("lolis/enemyscout.png", Texture.class), 0, 0, 256, 256);
		
		tag = new TextureRegion(game.assman.get("lolis/turntags.png", Texture.class), 0, 32, 64, 32);

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
		
		if(hp <=  0) 
		{
			if(isRight)
				batch.draw(die.getKeyFrame(deathTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(die.getKeyFrame(deathTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			deathTime += Gdx.graphics.getDeltaTime();
			return; // dead characters don't care about their turn state
		}
		
		if(hurting)
		{
			if(isRight)
				batch.draw(hurt.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(hurt.getKeyFrame(stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			return;//shot characters don't care what they're doing
		}
		
		switch(turn){
		case ATTACK:
			if(isRight)
				batch.draw(attack.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(attack.getKeyFrame(stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			break;
					
		case MOVE:
			if(isRight)
				batch.draw(walk.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(walk.getKeyFrame(stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			
			break;
		case START:
			if(isRight)
				batch.draw(salute.getKeyFrame(this.stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(salute.getKeyFrame(this.stateTime), x + (16 * scale), y, 0, 0, -16, 16, scale, scale, 0);
			
			break;
		case END:
		case NOTMYTURN:
		case TARGET:
		case SELECTMOVE:
		case SELECTACTION:
		default:
			if(isRight)
				batch.draw(idle.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
			else
				batch.draw(idle.getKeyFrame(stateTime), x + 16, y, 0, 0, -16, 16, scale, scale, 0);
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
		stateTime += delta;
		switch(turn)
		{
		case START:
			if(stateTime > salute.getFrameDuration() * 7)
			{
				//System.out.println(x + ", " + y + "     " + onScreenX + ", " + onScreenY);
				
				//check for valid moves around loli
				List<PCScoutLoli> temp = new LinkedList<PCScoutLoli>();
				
				for(Character c : lolis.getEveryone())//first get targets
				{
					if(c.getClass() == PCScoutLoli.class)
						temp.add((PCScoutLoli)c);
				}
				
				if(temp.isEmpty())
				{
					stateTime = 0;
					turn = TurnState.END;
					break;
				}
					
					
				PCScoutLoli target = temp.get(0);
				
				for(PCScoutLoli l : temp)
				{
					if(getDistance(l.x, l.y) < getDistance(target.x, target.y))
						target = l;
				}
				
				//get the path to the target, or null if the target isn't in view range
				path = astar.findPath(this, x, y, target.x, target.y);
				
				if(path == null)//if there's no path, start gunning
				{
					stateTime = 0;
					turn = TurnState.TARGET;
					break;
				}
					
				stateTime = 0;
				turn = TurnState.SELECTMOVE;
			}
			break;
		case SELECTMOVE:
			board.clearFocused();
			//set up information to begin moving
			curStep = path.getStep(0);
			numSteps = 0;
			
			turn = TurnState.MOVE;
			//break;
		case MOVE:

			//if there are not more steps, end move phase
			if(numSteps > speed -1)
			{
				//setCoords(destTileX,destTileY); //these actions happen after the loli has moved
				turn = TurnState.TARGET;
				path = null;
				numSteps = 0;
				break;
			}
			else
			{
				if(nextStep == null)//next, decide which step we are on, and move to the next step.
				{
					int i = 0;
					for(; i < path.getLength() && !path.getStep(i).equals(curStep); i++);
					if(i < path.getLength())
						nextStep = path.getStep(i + 1);
					else
					{
						turn = TurnState.TARGET;
						path = null;
						numSteps = 0;
						break;
					}
					
					for(Character c : ((GameScreen)game.getScreen()).turnQ.getEveryone())
					{
						if(c.getClass().getSuperclass() == MapUnit.class)
						{
							if(((MapUnit)c).x == nextStep.getX() && ((MapUnit)c).y == nextStep.getY())
							{
								turn = TurnState.TARGET;
								path = null;
								numSteps = 0;
								break;
							}
						}
					}
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
						numSteps++;
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
			if(targetAvailable())
			{
				//check for valid attacks around loli
				for(int y = 0; y < board.getHeight(); y++)
					for(int x = 0; x < board.getWidth(); x++)
					{
						if(inRange(x, y, range))
							board.getTile(x, y).targeted = true;
						else
							board.getTile(x, y).targeted = false;
					}
				if(stateTime > 1)
				{
					MapUnit l = chooseTarget();
					target = l;
					l.hurting = true;
					if(atk - l.getDefense() > 0)
					{
						float loss = atk - l.getDefense();
						l.hp -= loss;
						l.addTextEffect(((GameScreen)game.getScreen()).droid8pt, "-" + loss);
					}
					if(x < l.x)
						isRight = true;
					if(x > l.x)
						isRight = false;
					turn = TurnState.ATTACK;
				}
			}
			else
			{
				stateTime = 0;
				turn = TurnState.END;
			}
			break;
		case ATTACK:
			if(stateTime > 2f)
			{
				target.hurting = false;
				stateTime = 0;
				turn = TurnState.END;
			}
			break;
		case END:
			break;
		case NOTMYTURN:
			break;
		default:
			break;
			
		}
	}
	
	private boolean targetAvailable()
	{
		for(Character loli : lolis.getEveryone())
		{
			if(loli.getClass().getSuperclass() == MapUnit.class)
				if(loli.team != team)
					if(inRange(((MapUnit)loli).x, ((MapUnit)loli).y, range))
						return true;		
		}
		return false;
	}
	
	private MapUnit chooseTarget()
	{
		MapUnit target = null;
		
		for(Character loli : lolis.getEveryone())
		{
			if(loli.getClass().getSuperclass() == MapUnit.class)
				if(loli.team != team)
					if(inRange(((MapUnit)loli).x, ((MapUnit)loli).y, range))
					{
						if(target == null)
							target = (MapUnit)loli;
						else if(Math.abs(x - ((MapUnit)loli).x) + Math.abs(y - ((MapUnit)loli).y) < Math.abs(x - target.x) + Math.abs(y - target.y))
							target = (MapUnit)loli;
					}
							
		}
		
		return target;
	}

	@Override
	public void drawProfile(int x, int y) {
		game.batch.draw(profile, x, y);
	}
	
	public boolean isNPC()
	{
		return true;
	}
	@Override
	public MoverType getMoverType() {
		return MoverType.ONFOOT;
	}
	
	public String getUnitType()
	{
		return "Enemy Scout";
	}

	@Override
	public void deploy(GameBoard gb, int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
