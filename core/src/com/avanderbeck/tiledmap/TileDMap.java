package com.avanderbeck.tiledmap;

import java.util.List;

public class TileDMap {
	public int height, width, tileheight, tilewidth;
	public float version;
	public List<Layer> layers;
	public int nextobjectid;
	public String orientation;
	public String renderorder;
	public List<TileSet> tilesets;
}
