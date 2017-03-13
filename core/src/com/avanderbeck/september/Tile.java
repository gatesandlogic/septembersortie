package com.avanderbeck.september;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.MoverType;

import com.avanderbeck.tiledmap.TileSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
	
	Sortie game;
	
	TextureRegion tile;
	Animation orangeHL;
	Animation blueHL;

	
	Texture background;
	
	TerrainType terrain;
	
	int defense;
	
	public boolean focused, targeted, visited;
	
	float stateTime = 0;
	
	public Tile(Sortie game, TerrainType terrain, int tileindex, TileSet tiles)
	{
		this.game = game;
		
		this.terrain = terrain;
		
		focused = false;
		visited = false;
		targeted = false;
		
		//TODO: make the tile index more generic, as to take in width/height
		if(tileindex > tiles.tilecount)
			tileindex = 0;
		int cols = tiles.imagewidth / tiles.tilewidth;
		int row = tileindex / cols;
		int col = tileindex - (row * cols);

		tile = new TextureRegion(game.assman.get("tilesets/" + tiles.image, Texture.class), tiles.tilewidth * col, tiles.tilewidth * row, 16, 16);

		
		switch(terrain)
		{
		case CITY:
			background = game.assman.get("tilesets/backgrounds/citybackground.png", Texture.class);
			defense = 2;
			
			break;
		case FOREST:
			background = game.assman.get("tilesets/backgrounds/forestbackground.png", Texture.class);
			defense = 3;

			break;
		case GRASS:
			background = game.assman.get("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
			defense = 0;

			break;
		case MOUNTAIN:
			background = game.assman.get("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
			defense = 5;

			break;
		case ROAD:
			background = game.assman.get("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
			defense = 0;

			break;
		case SAND:
			background = game.assman.get("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
			defense = 0;

			break;
		case WATER:
			background = game.assman.get("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
			defense = 0;
			break;
		case SHORE:
			background = game.assman.get("tilesets/backgrounds/shorebackground.png", Texture.class);
			defense = 1;
			break;
		default:
			background = game.assman.get("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
			defense = 0;
			break;
		
		}
		
		
		TextureRegion [] temp = new TextureRegion[8];
		Texture squares = game.assman.get("selectsquare.png", Texture.class);
		for(int i = 0; i < 8; i++)
			temp[i] = new TextureRegion(squares, 16*i, 0, 16, 16);
		orangeHL = new Animation(.15f, temp);
		temp = new TextureRegion[8];
		for(int i = 0; i < 8; i++)
			temp[i] = new TextureRegion(squares, 16*i, 16, 16, 16);
		blueHL = new Animation(.15f, temp);
		
	}
	
	public void draw(SpriteBatch batch, int x, int y) {
		draw(batch, x, y, 1);
	}
	
	public void draw(SpriteBatch batch, int x, int y, float scale) {
		batch.draw(tile, x, y, 0, 0, 16, 16, scale, scale, 0);

		if(focused)
		{
			stateTime+= Gdx.graphics.getDeltaTime();
			if(stateTime > 2)
				stateTime = 0;
			batch.draw(blueHL.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
		}
		else if(targeted)
		{
			stateTime+= Gdx.graphics.getDeltaTime();
			if(stateTime > 2)
				stateTime = 0;
			batch.draw(orangeHL.getKeyFrame(stateTime), x, y, 0, 0, 16, 16, scale, scale, 0);
		}
	}
	
	public void drawBG(SpriteBatch batch)
	{
		batch.draw(background, 0, 0);
	}

	public boolean isBlocked(Mover mover) {
		
		switch(terrain)
		{
		case CITY:
			if(mover.getMoverType() == MoverType.BOAT)
				return true;
			break;
		case FOREST:
			if(mover.getMoverType() == MoverType.BOAT || mover.getMoverType() == MoverType.WHEELED)
				return true;
			break;

		case GRASS:
			if(mover.getMoverType() == MoverType.BOAT)
				return true;
			break;
		case MOUNTAIN:
			if(mover.getMoverType() != MoverType.FLYING)
				return true;
			break;

		case ROAD:
			if(mover.getMoverType() == MoverType.BOAT)
				return true;
			break;

		case SAND:
			if(mover.getMoverType() == MoverType.BOAT)
				return true;
			break;

		case SHORE:
			if(mover.getMoverType() == MoverType.BOAT)
				return true;
			break;

		case WATER:
			if(mover.getMoverType() != MoverType.BOAT || mover.getMoverType() != MoverType.FLYING)
				return true;
			break;

		default:
			break;		
		}
		
		return false;
	}

	public float getCost(Mover mover) {
		
		/*if(mover.getMoverType() == MoverType.FLYING)
			return 0;
		
		switch(terrain)
		{
		case CITY:
			return 0;
		case ROAD:
			return 0;
		default:
			return 0;
		}*/
		return 0;
	}
	
	public String getInfo()
	{
		String retval = "Tile ";

		retval += terrainString();
		
		if(focused)
			retval += " focused";
		
		return retval;
	}
	
	public String terrainString()
	{
		switch(terrain)
		{
		case CITY:
			return "City";
		case FOREST:
			return "Forest";
		case GRASS:
			return "Grass";
		case MOUNTAIN:
			return "Mountain";
		case ROAD:
			return "Road";
		case SAND:
			return "Desert";
		case SHORE:
			return "Beach";
		case WATER:
			return "Water";
		default:
			return "Other";
		}
	}
	
	public int getDefense()
	{
		return defense;
	}
}
