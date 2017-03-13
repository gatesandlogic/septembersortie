package com.avanderbeck.september.screens;

import java.util.LinkedList;
import java.util.List;

import com.avanderbeck.september.ActionButton;
import com.avanderbeck.september.Button;
import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.GameScreen;
import com.avanderbeck.september.GameState;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Tile;
import com.avanderbeck.september.TurnManager;
import com.avanderbeck.september.character.Character;
import com.avanderbeck.september.character.MapUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MissionSetupScreen implements Screen, InputProcessor {
	
	private Sortie game;
	GameScreen prevScreen;
	
	List<MapUnit> recruits;
	MapUnit current;
	
	float stateTime = 0;

	OrthographicCamera camera;
	Viewport viewport;
	
	GameBoard gb;
	
	TurnManager turnQ;
	
	int mousex, mousey;
	
	GameState state;
	
	BitmapFont droidSans;
	BitmapFont courier;
	BitmapFont droid8pt;

	int windowState = 0;
	
	List<Button> buttons;
	
	final int LISTX = 222;
	final int LISTY = 291;
	
	
	public MissionSetupScreen(Sortie game, GameScreen prevScreen, int [][] coords)
	{
		this.game = game;
		this.prevScreen = prevScreen;
		
		gb = prevScreen.gb;
		turnQ = prevScreen.turnQ;
		
		camera = new OrthographicCamera(game.GW, game.GH);
		camera.setToOrtho(false, game.GW, game.GH);
		viewport = new FitViewport(game.GW, game.GH, camera);
		
		droidSans = game.assman.get("fonts/droidsans.fnt", BitmapFont.class);
		courier = game.assman.get("fonts/courieroutline.fnt", BitmapFont.class);
		droid8pt = game.assman.get("fonts/droid8pt.fnt", BitmapFont.class);
		

		buttons = new LinkedList<Button>();
		
		buttons.add(new ActionButton(game, "Sortie!", LISTX + 74 + 16, 16)
				{
				});
			
		recruits = game.units;		
		
		current = recruits.get(0);

		Gdx.input.setInputProcessor(this);

	}
	
	@Override
	public void render(float delta) {


		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.begin();
		
		//TODO: draw a background
		
		//draw the game board
		gb.draw(game.batch);
		
		//draw dudes on the map
		for(Character l : turnQ.getEveryone())
		{
			if(l.getClass().getSuperclass() == MapUnit.class)
				l.draw(game.batch, (int)((((MapUnit)l).x * 16) + gb.getXPadding()), (int)((((MapUnit)l).y * 16) + gb.getYPadding() - gb.yOffset), stateTime);
		}

		//TODO:draw a list of available lolis
		int n = 0;
		for(MapUnit l : recruits)
		{
			int xoff = 10;
			if(current.equals(l))
				xoff = 0;
			l.drawTag(game.batch, LISTX + xoff,  LISTY - (34 * n++), false, droid8pt);
		}
			
		current.drawProfile(384, 16);

		droidSans.draw(game.batch, current.getName(), 292, 355);
		courier.draw(game.batch, (int)current.hp + "/" + current.maxHP, 292, 330);
		courier.draw(game.batch, "On " + gb.getTile(current.x, current.y).terrainString(), 292, 316);
		if(gb.getTile(current.x, current.y).getDefense() > 0)
			courier.draw(game.batch, "+" + gb.getTile(current.x, current.y).getDefense() + " defense", 300, 302);
		
		courier.draw(game.batch, "Atk:" + current.atk, 400, 330);
		courier.draw(game.batch, "Def:" + current.getDefense(), 400, 316);
		
		for(Button b : buttons)
			b.draw();
			
				
		//current.drawTag(game.batch, 216, 324, false, droid8pt);
				
		
		//draw the cursor on top of everything else
		//game.batch.draw(cursor, mousex, mousey - 16);
		
		//droid8pt.draw(game.batch, Gdx.graphics.getFramesPerSecond() + "fps", 2, 358);
		
		game.batch.end();
		
		update();
		
	
	}
	
	public void update()
	{
		stateTime += Gdx.graphics.getDeltaTime();
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
		// TODO Auto-generated method stub
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
		if (button != Input.Buttons.LEFT || pointer > 0) return false;// DGAF IF NOT LEFT CLICK
		Vector3 tp = new Vector3();
		camera.unproject(tp.set(screenX, screenY, 0));//GET THE LOCATION IN GAME RESOLUTION
		
		int x = (int)(tp.x - gb.getXPadding()) / 16;
		int y = (int)(tp.y - gb.getYPadding()) / 16;
		
		if(x < gb.getWidth() && y < gb.getHeight())
		{

			
			//if the chosen tile is focused, do stuff
			Tile t = gb.getTile(x, y);
			if(t == null)//just a safety thing, this can possibly happen
			{
				return false;
			}
				
			if(t.focused)
			{
				current.deploy(gb, x, y);
				turnQ.add(current);
			}

		}
		else
		{
			
			//if we aren't on the game board, first test if it's time to SORTIE!
			
			//implement sortie button that does the following
			for(Button b : buttons)
			{
				if(b.inRange(tp.x, tp.y))
				{
					game.setScreen(prevScreen);
					Gdx.input.setInputProcessor(prevScreen);
				}
			}
			
			
			//else check to see if we're working with a recruit!
			
			int n = 0;
			for(MapUnit l : recruits)
			{
				if(mousex > LISTX && mousex < LISTX + 74 &&
						mousey > LISTY - (34 * n) && mousey < LISTY + 32 -(34 * n))//if we're on the game board
				{
					current = l;
				}
				n++;
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
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

}
