package com.avanderbeck.september;

import org.newdawn.slick.util.pathfinding.MoverType;

import com.avanderbeck.september.character.APC;
import com.avanderbeck.september.character.Artillery;
import com.avanderbeck.september.character.Character;
import com.avanderbeck.september.character.MapUnit;
import com.avanderbeck.september.character.NameGenerator;
import com.avanderbeck.september.character.PCRocketLoli;
import com.avanderbeck.september.character.PCScoutLoli;
import com.avanderbeck.september.screens.MissionSetupScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.Gson;

public class GameScreen implements Screen, InputProcessor{
	
	Sortie game;
	
	OrthographicCamera camera;
	Viewport viewport;
	
	public GameBoard gb;
	
	NameGenerator names;
	
	public TurnManager turnQ;
	Character curChar; // the currently acting character
	
	int mousex, mousey;
	TextureRegion cursor;
	TextureRegion targetCursor;
	TextureRegion normalCursor;
	
	public GameState state;
	
	BitmapFont droidSans;
	BitmapFont courier;
	public BitmapFont droid8pt;

	
	float stateTime;	
	int windowState = 0;

	private ActionButton recruitbtn;
	private ActionButton skipMoveBtn, endTurnBtn;
	private ActionButton APCexchangeBtn;
	
	boolean runSetup;
	
	Mission mission;

	public GameScreen(Sortie game, Mission mission)
	{
		this.game = game;
	
		this.mission = mission;
		
		camera = new OrthographicCamera(game.GW, game.GH);
		camera.setToOrtho(false, game.GW, game.GH);
		viewport = new FitViewport(game.GW, game.GH, camera);
		
		droidSans = game.assman.get("fonts/droidsans.fnt", BitmapFont.class);
		courier = game.assman.get("fonts/proggyclean.fnt", BitmapFont.class);
		droid8pt = game.assman.get("fonts/droid8pt.fnt", BitmapFont.class);	
		
		String json = Gdx.files.internal("names.json").readString();
				Gson gson = new Gson();
		names = gson.fromJson(json, NameGenerator.class);
		
		normalCursor = new TextureRegion(game.assman.get("cursors.png", Texture.class), 0,0,16,16);
		targetCursor = new TextureRegion(game.assman.get("cursors.png", Texture.class), 16,0,16,16);

		cursor = normalCursor;
		
		turnQ = new TurnManager(game);
		
		gb = new GameBoard(13, 22, game, turnQ);
				
		turnQ.add(new Artillery(game, gb, turnQ));
		
		turnQ.update(0);//populate the turnQ properly
		
		/*curChar = turnQ.poll();
		curChar.setTurnState(TurnState.START);*/
		
		Gdx.input.setInputProcessor(this);
		
		mousex = -10;
		mousey = -10;
		
		stateTime = 0;
		state = GameState.GAMESTART;
		
		
		recruitbtn = new ActionButton(game, "Recruit", 292, 218){
			public void doThing(int x, int y)
			{
				curChar.setTurnState(TurnState.NOTMYTURN);
				curChar.setTimeSinceTurn(0);
				
				
				if(curChar.getClass() == PCScoutLoli.class)
					((PCScoutLoli)curChar).paths.clear();
				if(curChar.getClass() == PCRocketLoli.class)
					((PCRocketLoli)curChar).paths.clear();

				turnQ.add(curChar);
				
				gb.clearFocused();
				
				curChar = null;
				
				game.setScreen(new RecruitScreen(game, (GameScreen) game.getScreen(), x, y));
			}
		};
		
		skipMoveBtn = new ActionButton(game, "SkipMove", 292, 252){
			public void doThing()
			{
				curChar.setTurnState(TurnState.TARGET);
			}
		};
		endTurnBtn = new ActionButton(game, "EndTurn", 292, 252){
			public void doThing()
			{	
				if(curChar.getClass() == PCScoutLoli.class)
					((PCScoutLoli)curChar).paths.clear();
				if(curChar.getClass() == PCRocketLoli.class)
					((PCRocketLoli)curChar).paths.clear();
				
				
				curChar.setTurnState(TurnState.NOTMYTURN);
				curChar.setTimeSinceTurn(0);
				turnQ.add(curChar);
				
				gb.clearFocused();
				
				curChar = null;
				
			}
		};
		
		APCexchangeBtn = new ActionButton(game, "(un)Load", 292, 218){
			public void doThing()
			{
				curChar.setTurnState(TurnState.NOTMYTURN);
				curChar.setTimeSinceTurn(0);
				
				((APC)curChar).paths.clear();

				turnQ.add(curChar);
				
				gb.clearFocused();
				
				game.setScreen(new APCScreen(game, (GameScreen) game.getScreen(), ((APC)curChar)));
				
				curChar = null;
			}
		};	
		
		runSetup = true;
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		
		if(runSetup)
		{
			game.setScreen(new MissionSetupScreen(game, this, mission.getCoords()));
			runSetup = false;
		}

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.begin();
		
		if(curChar != null)
		{
			if(curChar.getClass().getSuperclass() == MapUnit.class)
				gb.getTile(((MapUnit)curChar).x, ((MapUnit)curChar).y).drawBG(game.batch);
			else if(curChar.getClass() == Artillery.class)
				game.batch.draw(((Artillery)curChar).background, 0, 0);
		}
		
		gb.draw(game.batch);
		
		for(Character l : turnQ.getEveryone())
		{
			if(l.getClass().getSuperclass() == MapUnit.class)
				l.draw(game.batch, (int)((((MapUnit)l).x * 16) + gb.getXPadding()), (int)((((MapUnit)l).y * 16) + gb.getYPadding() - gb.yOffset), stateTime);
		}
		
		if(curChar != null)
		{
			if(curChar.getClass().getSuperclass() == MapUnit.class)
			{
				MapUnit loli = (MapUnit)curChar;
				loli.draw(game.batch, stateTime);
				
				loli.drawProfile(384, 16);
				
				
				droidSans.draw(game.batch, loli.getName(), 292, 355);
				courier.draw(game.batch, (int)loli.hp + "/" + loli.maxHP, 292, 330);
				courier.draw(game.batch, "On " + gb.getTile(loli.x, loli.y).terrainString(), 292, 316);
				if(gb.getTile(loli.x, loli.y).defense > 0)
					courier.draw(game.batch, "+" + gb.getTile(loli.x, loli.y).defense + " defense", 300, 302);
				
				courier.draw(game.batch, "Atk:" + loli.atk, 400, 330);
				courier.draw(game.batch, "Def:" + loli.getDefense(), 400, 316);
					
				if(!loli.isNPC())
				{
					if((loli.getTurnState() == TurnState.SELECTMOVE || loli.getTurnState() == TurnState.TARGET)
							&& gb.getTile(loli.x, loli.y).terrain == TerrainType.CITY
							&& loli.getMoverType() == MoverType.ONFOOT)
					{
						recruitbtn.draw();
					}
					
					if(loli.getTurnState() == TurnState.SELECTMOVE)
					{
						skipMoveBtn.draw();
					}
					if(loli.getTurnState() == TurnState.TARGET)
					{
						endTurnBtn.draw();
					}
					
					if(loli.getClass() == APC.class)
					{
						APCexchangeBtn.draw();
					}
						
				}
			}
			else if(curChar.getClass() == Artillery.class)
			{
				curChar.draw(game.batch, (int)gb.getXPadding(), (int)gb.getYPadding(), delta);
				
				droidSans.draw(game.batch, curChar.getName(), 292, 355);
				game.batch.draw(((Artillery)curChar).profile, 280, 0);
			}
			
			curChar.drawTag(game.batch, 216, 324, false, droid8pt);
		}

		turnQ.draw(game.batch, droid8pt, 222 , 291);
		
		
		
		Character c = turnQ.getCharacter((int)(mousex- gb.getXPadding()) / 16, (int)(mousey- gb.getYPadding()) / 16);
		
		if(c != null)
		{
			if(c.getClass().getSuperclass() == MapUnit.class)
				c.drawTag(game.batch, (int)((MapUnit)c).xCoordToScreen(((MapUnit)c).x) + 16, (int)((MapUnit)c).yCoordToScreen(((MapUnit)c).y), false, droid8pt);
		}

		
		//draw the cursor on top of everything else
		//game.batch.draw(cursor, mousex, mousey - 16);
		
		//droid8pt.draw(game.batch, Gdx.graphics.getFramesPerSecond() + "fps", 2, 358);
		
		game.batch.end();
		
		update(Gdx.graphics.getDeltaTime());
		
	}
	
	private void update(float delta)
	{
		switch(state)
		{
		case GAMESTART:
			//we might want to do something cool for the beginning of the game
			//for now just change the state and flow into active
			state = GameState.ACTIVE;
			//break;
		case ACTIVE:
		{
			stateTime += delta; //time only flows while the game is active
			
			turnQ.update(delta);
			
			if(curChar != null)
			{
				curChar.takeTurn(mousex, mousey, delta);

				
				if(curChar.getTurnState() == TurnState.TARGET)
				{
					if(!curChar.isNPC())
					{
						MapUnit loli = (MapUnit)curChar;
						//check if any lolis are in attack range
						boolean targets = false;
						for(Character l : turnQ.getEveryone())
							if(l.getClass().getSuperclass() == MapUnit.class && curChar.team != l.team)//if it's a loli not on the same team
								if(loli.inRange(((MapUnit)l).x, ((MapUnit)l).y, loli.range))
									targets = true;
						if(gb.getTile(loli.x, loli.y).terrain == TerrainType.CITY || loli.getClass() == APC.class)//if it's an APC or a loli landed on a city, allow them to act, even if there's nothing else to do
						{
							gb.clearFocused();
							targets = true;
						}
						if(!targets)
							loli.setTurnState(TurnState.END);
					}
				}
				
				if(curChar.getTurnState() == TurnState.END)
				{
					turnQ.bringOutYourDead();
					
					//This ends the turn
					curChar.setTurnState(TurnState.NOTMYTURN);
					curChar.setTimeSinceTurn(0);
					turnQ.add(curChar);
					
					for(int x = 0; x < gb.getWidth(); x++)
						for(int y = 0; y < gb.getHeight(); y++)
							gb.getTile(x, y).focused = false;
					
					curChar = null;
					
					if(turnQ.peek() != null)
					{
						curChar = turnQ.poll();
						curChar.setTurnState(TurnState.START);
					}
				}
			}
			else
			{
				if(turnQ.peek() != null)
				{
					curChar = turnQ.poll();
					curChar.setTurnState(TurnState.START);
				}
			}
			
		}
			break;
		case GAMEOVER:
			Gdx.app.exit();
			break;
		case PAUSED:
			break;
		}
	}

	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.F8)
		{
			switch(windowState)
			{
			case 0:
				Gdx.graphics.setWindowedMode(1920, 1080);
				windowState++;
				break;
			case 1:
				Gdx.graphics.setWindowedMode(1280, 720);
				windowState++;
				break;
			case 2:
				Gdx.graphics.setWindowedMode(640, 360);
				windowState++;
			case 3: 
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				windowState = 0;
			}

		}
		else if(keycode == Keys.F10)
			Gdx.app.exit();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		Vector3 tp = new Vector3();
		camera.unproject(tp.set(screenX, screenY, 0));
		
		int x = (int)(tp.x - gb.getXPadding()) / 16;
		int y = (int)(tp.y - gb.getYPadding()) / 16;
		
		if(curChar != null && !curChar.isNPC())
		{
			MapUnit loli = (MapUnit)curChar;
			
			if( x < gb.getWidth() && y < gb.getHeight())
			{
				switch(loli.getTurnState()){
				case SELECTACTION:
					
					break;
				case SELECTMOVE:
					if(loli.inMoveRange(x, y))
					{
						boolean empty = true;
						for(Character c : turnQ.getEveryone())
						{
							if(c.getClass().getSuperclass() == MapUnit.class)
								if(((MapUnit)c).x == x && ((MapUnit)c).y == y)
								{
									empty = false;
									break;
								}
						}
						if(empty)
						{
							loli.destTileX = x;
							loli.destTileY = y;
							loli.onScreenX = loli.xCoordToScreen(loli.x);
							loli.onScreenY = loli.yCoordToScreen(loli.y);
							loli.setTurnState(TurnState.MOVE);
						}
					}
					
					
					break;
				case TARGET:
					if(loli.inRange(x, y, loli.range))
						for(Character c : turnQ.getEveryone())
						{
							if(c.getClass().getSuperclass() == MapUnit.class)
							{
								MapUnit l = (MapUnit)c;
								
								if(l.x == x && l.y == y)
								{
									if(l.team == loli.team)// make sure the target isn't on the same team
										break;
									if(loli.atk - l.getDefense() > 0)
									{
										float loss = loli.atk - l.getDefense();
										l.hp -= loss;
										l.addTextEffect(droid8pt, "-" + loss);
									}
									if(loli.x < l.x)
										loli.setRight(true);
									if(loli.x > l.x)
										loli.setRight(false);
									loli.setTurnState(TurnState.ATTACK);
									l.hurting = true;
									loli.target = l;
									break;
								}
							}
						}
					break;
				default:
					break;
		
				}//end switch
			}
			else//if we didn't click on hte board
			{
				if((loli.getTurnState() == TurnState.SELECTMOVE || loli.getTurnState() == TurnState.TARGET) 
						&& gb.getTile(loli.x, loli.y).terrain == TerrainType.CITY && loli.getMoverType() == MoverType.ONFOOT)		
					if(recruitbtn.inRange(tp.x, tp.y))
						recruitbtn.doThing(loli.x, loli.y);
				
				if(loli.getTurnState() == TurnState.SELECTMOVE && skipMoveBtn.inRange(tp.x, tp.y))
				{
					skipMoveBtn.doThing();
				}
				
				if(loli.getTurnState() == TurnState.TARGET && endTurnBtn.inRange(tp.x, tp.y))
				{
					endTurnBtn.doThing();
				}
				
				if(loli.getClass() == APC.class && APCexchangeBtn.inRange(tp.x, tp.y))
				{
				//	System.out.println("button clicked");

						APCexchangeBtn.doThing();
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 tp = new Vector3();
		camera.unproject(tp.set(screenX, screenY, 0));
		//highlight a tile if it's under the mouse
		mousex = (int) tp.x;
		mousey = (int) tp.y;
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
