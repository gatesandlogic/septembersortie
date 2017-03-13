package com.avanderbeck.september.screens;

import java.util.ArrayList;
import java.util.List;

import com.avanderbeck.september.ActionButton;
import com.avanderbeck.september.GameScreen;
import com.avanderbeck.september.GameState;
import com.avanderbeck.september.Mission;
import com.avanderbeck.september.Sortie;
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

public class MissionSelectScreen implements Screen, InputProcessor {
	
	OrthographicCamera camera;
	Viewport viewport;
	
	int mousex, mousey;
	TextureRegion cursor;
	TextureRegion targetCursor;
	TextureRegion normalCursor;
	
	Sortie game;
	
	public GameState state;

	float stateTime;
	
	int windowState;
	
	TextureRegion bgcorner, bgvertborder, bghorzborder, bgcolour;
	Texture star, blackboard;
	
	BitmapFont bigFont, smallFont;

	
	/*
	 * Big ass list of buttons
	 */
	TabButton missionTab, barracksTab, medicalTab, graveyardTab;
	
	List<MissionButton> missions;
	
	ActionButton goButton;


	public MissionSelectScreen(final Sortie game) {
		
		this.game = game;
		
		camera = new OrthographicCamera(game.GW, game.GH);
		camera.setToOrtho(false, game.GW, game.GH);
		viewport = new FitViewport(game.GW, game.GH, camera);
		
		normalCursor = new TextureRegion(game.assman.get("cursors.png", Texture.class), 0,0,16,16);
		targetCursor = new TextureRegion(game.assman.get("cursors.png", Texture.class), 16,0,16,16);

		cursor = normalCursor;
		
		mousex = -10;
		mousey = -10;
		
		Gdx.input.setInputProcessor(this);
		
		
		stateTime = 0;
		state = GameState.GAMESTART;
		
		windowState = 0;
		
		missionTab = new TabButton(game, 0, game.GH - 64, true, new TextureRegion(game.assman.get("ui/uilogos.png", Texture.class), 0, 0, 64, 64));
		barracksTab = new TabButton(game, 160, game.GH - 64, false, new TextureRegion(game.assman.get("ui/uilogos.png", Texture.class), 64, 0, 64, 64)){
			public void doThing(){
				game.setScreen(new PersonnelScreen(game));
			}
		};
		medicalTab = new TabButton(game, 320, game.GH - 64, false, new TextureRegion(game.assman.get("ui/uilogos.png", Texture.class), 128, 0, 64, 64));
		graveyardTab = new TabButton(game, 480, game.GH - 64, false, new TextureRegion(game.assman.get("ui/uilogos.png", Texture.class), 196, 0, 64, 64));

		missions = new ArrayList<MissionButton>();
		
		Gson gson = new Gson();
		missions.add(new MissionButton(game, 0, game.GH - (64 * 2) , 224, 64, true,
				gson.fromJson(Gdx.files.internal("maps/mission1.json").readString(), Mission.class)));
		
		missions.add(new MissionButton(game, 4, game.GH - (64*3), 224, 64, false,
				gson.fromJson(Gdx.files.internal("maps/mission2.json").readString(), Mission.class)));
		
		missions.add(new MissionButton(game, 4, game.GH - (64*4), 224, 64, false,
				gson.fromJson(Gdx.files.internal("maps/mission3.json").readString(), Mission.class)));
		
		missions.add(new MissionButton(game, 4, game.GH - (64*5), 224, 64, false,
				gson.fromJson(Gdx.files.internal("maps/endlessmission.json").readString(), Mission.class)));
		
		star = game.assman.get("ui/star.png", Texture.class);
		blackboard = game.assman.get("ui/blackboard.png", Texture.class);

		
		
		bgcorner = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 , 0, 16, 16);
		bgvertborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 0 , 16, 16, 16);
		bghorzborder = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 , 0, 16, 16);
		bgcolour = new TextureRegion(game.assman.get("ui/uibuttonbg.png", Texture.class), 16 , 16, 16, 16);
		
		bigFont = game.assman.get("fonts/droidsans.fnt", BitmapFont.class);
		smallFont = game.assman.get("fonts/courieroutline.fnt", BitmapFont.class);
		
		
		goButton = new ActionButton(game, "Sortie!", 224 + ((640 - 224 - 64)/2), 16)
				{
					public void doThing()
					{
						Mission mission = null;
						for(MissionButton m : missions)
							if(m.isFocused())
								mission = m.getMission();
						game.setScreen(new GameScreen(game, mission));
					}
				};
	}

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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button != Input.Buttons.LEFT || pointer > 0) return false;
		Vector3 tp = new Vector3();
		camera.unproject(tp.set(screenX, screenY, 0));
		
		if(goButton.inRange(tp.x, tp.y))
			goButton.doThing();
		
		
		
		boolean onefocused = false;
		
		for(MissionButton m : missions)
		{
			if(m.inRange(tp.x, tp.y))
			{
				m.setFocus(true);
				m.setX(0);
				onefocused = true;
			}
			else
			{
				m.setFocus(false);
				m.setX(4);
			}
		}
		
		if(!onefocused)
		{
			missions.get(0).setFocus(true);
			missions.get(0).setX(0);
		}
		
		if(barracksTab.inRange(tp.x, tp.y))
			barracksTab.doThing();
		
		
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

	@Override
	public void render(float delta) {
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		Gdx.gl.glClearColor(0.294f, 0.412f, 0.184f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();

		missionTab.draw();
		barracksTab.draw();
		medicalTab.draw();
		graveyardTab.draw();

		for(MissionButton m : missions)
		{
			if(!m.isFocused())
				m.draw();
		}
		
		//draw the mission Info
		
		int x = 222;
		int y = 0;
		int w = 638 - x;
		int h = 360 - 64;
		game.batch.draw(bgcorner, x, h - 16);
		game.batch.draw(bgcorner, x, y + 24, 16, -16);
		game.batch.draw(bgcorner, x + w, h - 16, -16, 16);
		game.batch.draw(bgcorner, x + w, y + 24, -16, -16);
		

		
		for(int i = 0; i < ((w- 32) / 16); i ++)
		{
			game.batch.draw(bghorzborder, x + 16 + (16 * i), y + h - 16);
			game.batch.draw(bghorzborder, x + 16 + (16 * i), y + 24, 16, -16);
		}
				
		for(int i = 0; i < ((h - 32) / 16); i ++)
		{
			game.batch.draw(bgvertborder, x, y + h - 32 - (16 * i));
			game.batch.draw(bgvertborder, x + w, y + h - 32  - (16 * i), -16, 16 );

		}

		for(int xx = 0; xx < ((w- 32) / 16); xx++)
			for(int yy = 0; yy < ((h- 32) / 16); yy++)
				game.batch.draw(bgcolour, x + 16 + (xx*16), y + h - 32  - (16 * yy));
		
		//draw mission info
		game.batch.draw(blackboard, x + ((w - blackboard.getWidth()) / 2), y + ((h - blackboard.getHeight()) / 2));
		
		for(MissionButton m : missions)
		{
			if(m.isFocused())
			{
				m.draw();
				bigFont.draw(game.batch, m.getMission().getTitle(), x + 4, h - ((h - blackboard.getHeight()) / 4), w - 8, 1, true);
				smallFont.draw(game.batch, m.getMission().getDescription(),
						x + ((w - blackboard.getWidth()) / 2) + 6, y + ((h - blackboard.getHeight()) / 2) + blackboard.getHeight() - 16,
						blackboard.getWidth() - 16, 10, true);
			}
		}
		
		
		goButton.draw();
		
		
		//draw the cursor on top of everything else
		//game.batch.draw(cursor, mousex, mousey - 16);
				
		game.batch.end();
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

}
