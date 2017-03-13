package com.avanderbeck.september;

import com.avanderbeck.september.character.APC;
import com.avanderbeck.september.character.NameGenerator;
import com.avanderbeck.september.character.PCRocketLoli;
import com.avanderbeck.september.character.PCScoutLoli;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;

public class CharacterGenerator {
	
	private NameGenerator nameGen;
	
	public CharacterGenerator()
	{
		String json = Gdx.files.internal("names.json").readString();
		Gson gson = new Gson();
		nameGen = gson.fromJson(json, NameGenerator.class);
	}
	
	public PCScoutLoli getScout(Sortie game, GameBoard gb)
	{
		return getScout(game, gb, 0, 0);
	}
	
	public PCScoutLoli getScout(Sortie game, GameBoard gb, int x, int y)
	{
		return new PCScoutLoli(game, gb, x, y, nameGen.getNewName());
	}
	
	public PCRocketLoli getRocket(Sortie game, GameBoard gb)
	{
		return getRocket(game, gb, 0, 0);
	}
	
	public PCRocketLoli getRocket(Sortie game, GameBoard gb, int x, int y)
	{
		return new PCRocketLoli(game, gb, x, y, nameGen.getNewName());
	}
	
	public APC getAPC(Sortie game, GameBoard gb)
	{
		return getAPC(game, gb, 0, 0);
	}
	
	public APC getAPC(Sortie game, GameBoard gb, int x, int y)
	{
		return new APC(game, gb, x, y);
	}
	
	
}
