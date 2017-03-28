package com.avanderbeck.september.character;

import org.newdawn.slick.util.pathfinding.MoverType;

import com.avanderbeck.september.GameBoard;
import com.avanderbeck.september.Sortie;
import com.avanderbeck.september.Team;
import com.avanderbeck.september.TurnManager;
import com.avanderbeck.september.TurnState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Boss extends MapUnit {
	
	/*float stateTime = 0;
	float deathTime = 0;
	*/
	//view range for targeting, and attacking
	final private int view  = 22;

	TurnManager lolis;
	
	MapUnit target;

	private TextureRegion tag;
	private TextureRegion profile;
	
	private Animation explosion;

	public Boss(Sortie game, GameBoard gb, TurnManager turnQ, int x, int y,  int width, int height) {
		super(game, gb, Team.BLACK, 45, "Thor Morser", 80);
		
		profile = game.assman.get("lolis/artillerycannon.png");
		tag = new TextureRegion(game.assman.get("lolis/turntags.png", Texture.class), 0, 32, 64, 32);
		
		Texture temp = game.assman.get("effects/explosion.png", Texture.class);
		TextureRegion [] tempii = new TextureRegion[8];
		
		for(int i = 0; i < 8; i++)
		{
			tempii[i] = new TextureRegion(temp, 32 * i, 0, 32, 32);
		}
		explosion = new Animation(0.1f, tempii);
		
		temp = game.assman.get("lolis/mapunits/thormorser.png", Texture.class);
		tempii = new TextureRegion[2];
		for(int i = 0; i < 2; i++)
		{
			tempii[i] = new TextureRegion(temp, 32 * i, 0, 32, 32);
		}
		idle = new Animation(0.5f, tempii);
		
		
		temp = game.assman.get("lolis/mapunits/thormorser.png", Texture.class);
		tempii = new TextureRegion[7];
		for(int i = 0; i < 7; i++)
		{
			tempii[i] = new TextureRegion(temp, 32 * i + 64, 0, 32, 32);
		}
		attack = new Animation(0.1f, tempii);
		
		turn = TurnState.NOTMYTURN;
		timeSinceTurn = 0;
	}

	@Override
	public MoverType getMoverType() {
		return MoverType.WHEELED;
	}

	@Override
	public String getUnitType() {
		return "Boss";
	}

	@Override
	public void deploy(GameBoard gb, int x, int y) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(SpriteBatch batch, float stateTime) {
		updateTextEffects(Gdx.graphics.getDeltaTime());
		drawTextEffects();
		
		switch(turn)
		{
		case ATTACK:
			break;
		case NOTMYTURN:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void draw(SpriteBatch batch, int x, int y, float stateTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(SpriteBatch batch, int x, int y, float stateTime, float scale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawTag(SpriteBatch batch, int x, int y, boolean showTime, BitmapFont font) {
		// TODO Auto-generated method stub

	}

	@Override
	public void takeTurn(float mx, float my, float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawProfile(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isNPC() {
		return true;
	}

}
