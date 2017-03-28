package com.avanderbeck.september;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import com.avanderbeck.september.character.NMELoli;
import com.avanderbeck.tiledmap.TileDMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.Gson;

public class GameBoard implements TileBasedMap{
	
	private int width, height;
	private Tile [][] tiles;
	private int boardXPos, boardYPos;
	
	private TextureRegion corn, vert, horz;
	
	public float yOffset = 0;
	
	Sortie game;
	
	TileDMap map;
	
	TurnManager turnQ;
	
	public GameBoard(int w, int h, Sortie game, Mission mission, TurnManager turnQ)
	{
		
		this.game = game;
		this.turnQ = turnQ;
		map = getMapFromJson(mission.getFile());
		
		boardXPos = 4;
		boardYPos = 4;

		if(!setWidth(w))
			width = 1;
		if(!setHeight(h))
			height = 1;
		
		tiles = new Tile[width][height];
		
		for(int y = 0; y < height; y++)
			for(int x = width; x > 0; x--)
				tiles[x-1][y] = getNewTile(x, y);

		corn = new TextureRegion(game.assman.get("border.png", Texture.class), 0, 0, 16, 16);
		vert = new TextureRegion(game.assman.get("border.png", Texture.class), 16, 0, 16, 16);
		horz = new TextureRegion(game.assman.get("border.png", Texture.class), 32, 0, 16, 16);

		
	}
	
	public boolean setWidth(int w)
	{
		if(w > 0)
		{
			width = w;
			return true;
		}
		return false;
	}
	public boolean setHeight(int h)
	{
		if(h > 0)
		{
			height = h;
			return true;
		}
		return false;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public float getXPadding()
	{
		return boardXPos;
	}
	
	public float getYPadding()
	{
		return boardYPos;
	}
	
	public Tile getTile(int x, int y)
	{
		if((x > -1 &&  x < width) && (y > -1 &&  y < height))
			return tiles[x][y];
		return null;
	}
	
	public void draw(SpriteBatch batch)
	{
		int inc = 16;
		int x = this.boardXPos;
		int y = this.boardYPos;
		
		for(int l = 0; l < height; l++)
		{
			for(int w = 0; w < width; w++)
			{
				tiles[w][l].draw(batch, x, (int)(y - yOffset));
				x += inc;
			}
			y += inc;
			x = this.boardXPos;
		}
		
		//draw the border
		batch.draw(corn, x - 16, y, 16, 16);
		batch.draw(corn, x - 16, y - (16*height), 16, -16);
		batch.draw(corn, x + 16 + (width * 16), y, -16, 16);
		batch.draw(corn, x + 16 + (width * 16), y - (16*height), -16, -16);
		
		for(int i = 0; i < width; i++)
		{
			batch.draw(horz, x + (i * 16), y, 16, 16);
			batch.draw(horz, x + (i * 16), y - (16*height), 16, -16);
		}
		for(int i = 0; i < height; i++)
		{
			batch.draw(vert, x - 16, y - 16 -(i * 16), 16, 16);
			batch.draw(vert, x + 16 + (width * 16) , y - 16 - (i * 16), -16, 16);
		}

	}
	
	public void moveBoardDown(int rows)// this method will be for moving the board and killing the bottom rows
	{
		for(int y = 0; y < height - rows; y++)//move existing rows down
			for(int x = 0; x < width; x++)
				tiles[x][y] = tiles[x][y + rows];
		for(int y = height - rows; y < height; y++)//and create new ones
			for(int x = width; x > 0; x--)
				tiles[x-1][y] = getNewTile(x, y);
	}
	
	public int getPixelWidth()
	{
		return width * 16;
	}
	
	public int getRightEdgeX()
	{
		return boardXPos + getPixelWidth();
	}
	
	private Tile getNewTile(int x, int y)
	{
		int nme = (int)map.layers.get(1).data.pop();
		if(nme != 0)
		{
			turnQ.add(new NMELoli(game, this, turnQ, x -1, y ));
		}
		
		return getNewTile((int)map.layers.get(0).data.pop());
	}
	
	private Tile getNewTile(int type)
	{
		switch(type)
		{
		case 1://grass
			return new Tile(game, TerrainType.GRASS, 0, map.tilesets.get(0));
		case 2:
			return new Tile(game, TerrainType.FOREST, type-1, map.tilesets.get(0));
		case 3:
			return new Tile(game, TerrainType.CITY, type-1, map.tilesets.get(0));
		case 4:
			return new Tile(game, TerrainType.MOUNTAIN, type-1, map.tilesets.get(0));
		case 5:
			return new Tile(game, TerrainType.WATER, type-1, map.tilesets.get(0));
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
			return new Tile(game, TerrainType.SHORE, type-1, map.tilesets.get(0));
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		default:
			return new Tile(game, TerrainType.ROAD, type-1, map.tilesets.get(0));

		}
	}
	
	private TileDMap getMapFromJson(String file)
	{
		TileDMap retval;
		
		String json = Gdx.files.internal("maps/" + file).readString();
		
		Gson gson = new Gson();
		retval = gson.fromJson(json, TileDMap.class);
		
		return retval;
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return getWidth();
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return getHeight();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		getTile(x, y).visited = true;
	}
	
	public void clearVisited()
	{
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				getTile(x, y).visited = false;
	}
	
	
	public void clearFocused()
	{
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
			{
				getTile(x, y).focused = false;
				getTile(x, y).targeted = false;
				getTile(x, y).stateTime = 0;

			}
	}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		return getTile(x, y).isBlocked(mover);
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		// TODO Auto-generated method stub
		return getTile(tx, ty).getCost(mover);
	}

	public boolean usedUp() {
		return map.layers.get(0).data.isEmpty();
	}
	

}
