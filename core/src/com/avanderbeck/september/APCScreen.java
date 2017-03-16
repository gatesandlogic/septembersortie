package com.avanderbeck.september;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.pathfinding.MoverType;

import com.avanderbeck.september.character.APC;
import com.avanderbeck.september.character.Character;
import com.avanderbeck.september.character.MapUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class APCScreen implements Screen, InputProcessor {
	
	Sortie game;
	GameScreen previousScreen;
	
	float stateTime = 0;

	OrthographicCamera camera;
	Viewport viewport;
	
	GameBoard gb;
	
	TurnManager turnQ;
	
	int mousex, mousey;
	TextureRegion cursor;
	
	int x, y;
	
	GameState state;
	
	BitmapFont droidSans;
	BitmapFont courier;
	BitmapFont droid8pt;

	int windowState = 0;
	
	List<Button> buttons;

	ActionButton dun;
	
	//mini map information
	int mmScale;
	int mmPaddingX;
	int mmPaddingY;
	int listPaddingX;
	int listPaddingY;
	
	APC apc;
	MapUnit current;
	
	
	
	public APCScreen(Sortie game, GameScreen prevScreen, APC apc)
	{
		this.game = game;
		this.previousScreen = prevScreen;
		this.apc = apc;
		
		gb = prevScreen.gb;
		turnQ = prevScreen.turnQ;
		
		this.x = apc.getX();
		this.y = apc.getY();
		
		camera = new OrthographicCamera(game.GW, game.GH);
		camera.setToOrtho(false, game.GW, game.GH);
		viewport = new FitViewport(game.GW, game.GH, camera);
		
		droidSans = game.assman.get("fonts/droidsans.fnt", BitmapFont.class);
		courier = game.assman.get("fonts/courieroutline.fnt", BitmapFont.class);
		droid8pt = game.assman.get("fonts/droid8pt.fnt", BitmapFont.class);
		
		cursor = new TextureRegion(game.assman.get("cursors.png", Texture.class), 0,0,16,16);

		buttons = new LinkedList<Button>();
		
		mmScale = 3;
		mmPaddingX = (game.GH - (16 * mmScale * 3)) / 2;
		mmPaddingY = mmPaddingX;
		
		listPaddingX =  mmPaddingX + (16 * mmScale * 3) + 10;
		listPaddingY =  mmPaddingY + (16 * mmScale * 3) - 32;

		
		if(!apc.getPassengers().isEmpty())
			current = apc.getPassengers().get(0);
		else
			current = null;

		Gdx.input.setInputProcessor(this);
		
		dun = new ActionButton(game, "Done", 8, 40){
			public void doThing()
			{
				game.setScreen(previousScreen);
				Gdx.input.setInputProcessor(previousScreen);
			}
		};

	}
	
	@Override
	public void render(float delta) {
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		
		gb.getTile(x, y).drawBG(game.batch);
		
		int bx, by;//beginning x, and y
		bx = x -1;
		by = y -1;//we want a 9 tile square over all, with x and y at the middle

		int size = 16 * mmScale;

		for(int j = 0;by < y + 2; by++, j++)
		{
			bx = x -1;//reset the x coord after each run

			for(int i = 0; bx < x + 2; bx++, i++)
			{
				Tile t = gb.getTile(bx, by);
				if(t != null)
				{
					t.draw(game.batch, mmPaddingX + (size * i), mmPaddingY + (j * size), mmScale);
					Character c = turnQ.getCharacter(bx, by);
					if(c != null)
					{
						c.draw(game.batch, mmPaddingX + (size * i), mmPaddingY + (j * size), stateTime, mmScale);
						t.focused = false;
					}
					else
						t.focused = true;
				}
			}
		}
		
		int n = 0;
		for(MapUnit l : apc.getPassengers())
		{
			int xoff = 10;
			if(current != null && current.equals(l))
				xoff = 0;
			l.drawTag(game.batch, listPaddingX + xoff,  listPaddingY - (34 * n++), false, droid8pt);
		}
		
		if(current != null)
		{
			current.drawProfile(384, 16);
			droidSans.draw(game.batch, current.getName(), 292, 355);
			courier.draw(game.batch, (int)current.hp + "/" + current.maxHP, 292, 330);
			courier.draw(game.batch, "On " + gb.getTile(current.x, current.y).terrainString(), 292, 316);
			if(gb.getTile(current.x, current.y).defense > 0)
				courier.draw(game.batch, "+" + gb.getTile(current.x, current.y).defense + " defense", 300, 302);
			
			courier.draw(game.batch, "Atk:" + current.atk, 400, 330);
			courier.draw(game.batch, "Def:" + current.getDefense(), 400, 316);
		}
		else
		{
			apc.drawProfile(384, 16);
			droidSans.draw(game.batch, apc.getName(), 292, 355);
			courier.draw(game.batch, (int)apc.hp + "/" + apc.maxHP, 292, 330);
			courier.draw(game.batch, "On " + gb.getTile(apc.x, apc.y).terrainString(), 292, 316);
			if(gb.getTile(apc.x, apc.y).defense > 0)
				courier.draw(game.batch, "+" + gb.getTile(apc.x, apc.y).defense + " defense", 300, 302);
			
			courier.draw(game.batch, "Atk:" + apc.atk, 400, 330);
			courier.draw(game.batch, "Def:" + apc.getDefense(), 400, 316);
		}
		
		dun.draw();
		
		//draw the cursor on top of everything else
		//game.batch.draw(cursor, mousex, mousey - 16);
				

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
		
		mousex = (int)(tp.x - gb.getXPadding());
		mousey = (int)(tp.y - gb.getYPadding());
		
		if(mousex > mmPaddingX && mousex < mmPaddingX + (16 * mmScale * 3) &&
				mousey > mmPaddingY && mousey < mmPaddingY + (16 * mmScale * 3))//if we're on the game board
		{
			//get the minimap location of the click, then subtract 1 to get relative location for original city
			mousex = ((mousex - mmPaddingX) / (16 * mmScale)) -1;
			mousey = ((mousey - mmPaddingY) / (16 * mmScale)) -1;
			
			
			Tile t = gb.getTile(x + mousex, y + mousey);
			if(t == null)//just a safety thing, this can possibly happen
			{
				return false;
			}
			
			if(t.focused)//if the tile is focused, place someone if possible.
			{
				if(current != null)
				{
					current.setCoords(x + mousex, y + mousey);
					turnQ.add(current);
					apc.getPassengers().remove(current);
					current = null;
				}
				else
					return false;
			}
			else//if the tile isn't focused, see if you can pick someone up.
			{

				MapUnit l = (MapUnit)turnQ.getCharacter(x + mousex, y + mousey);
				if(l != null && l.team == apc.team && l.getMoverType() == MoverType.ONFOOT)
				{
					turnQ.remove(l);
					apc.getPassengers().add(l);
					current = null;
				}
			}
		}
		else
		{
			//if we're clicking on a name tag, make them current
			int n = 0;
			for(MapUnit l : apc.getPassengers())
			{
				if(mousex > listPaddingX && mousex < listPaddingX + 74 &&
						mousey > listPaddingY - (34 * n) && mousey < listPaddingY + 32 -(34 * n))//if we're on the game board
				{
					current = l;
				}
				n++;
			}
			
			//if we click the done button, go back to the previous screen
			if(dun.inRange(mousex, mousey))
			{
				dun.doThing();
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
