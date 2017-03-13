package com.avanderbeck.september;

import java.util.ArrayList;
import java.util.List;

import com.avanderbeck.september.character.MapUnit;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sortie extends Game {
	
	
	public final int GW = 640;
	public final int GH = 360;
	
	public SpriteBatch batch;
	public AssetManager assman;
	
	public CharacterGenerator charGen; //ma lazor
	public List<MapUnit> units;
	public List<MapUnit> graveyard;
	
	public Cursor cursor;
	public Cursor targetCursor;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assman = new AssetManager();

		units = new ArrayList<MapUnit>();
		graveyard = new ArrayList<MapUnit>();
		
		charGen = new CharacterGenerator();
		
		this.setScreen(new LoadingScreen(this));
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assman.dispose();
	}
}
