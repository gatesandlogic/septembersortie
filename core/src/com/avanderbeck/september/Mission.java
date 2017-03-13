package com.avanderbeck.september;

public class Mission {

	private String title, description, file;
	private int difficulty;
	private int coords[][];
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int [] getCoord(int i)
	{
		if(i < coords.length)
			return coords[i];
		else
			return new int[]{-1,-1};
	}
	
	public int [][] getCoords()
	{
		return coords;
	}
	
	public int getNumUnits()
	{
		return coords.length;
	}
	
	
}
