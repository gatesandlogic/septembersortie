package com.avanderbeck.september;

import com.avanderbeck.september.character.Character;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ActionButton extends Button {
	
	protected Sortie game;


	public ActionButton(Sortie game, String text, int x, int y) {
		super();
		setX(x);
		setY(y);
		setWidth(64);
		setHeight(32);
		setFont(game.assman.get("fonts/courieroutline.fnt", BitmapFont.class));
		setText(text);
		setBackground(new TextureRegion(game.assman.get("lolis/turntags.png", Texture.class),
				0, 0, getWidth(), getHeight()));
		
		this.game = game;
	}

	@Override
	public void doThing() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doThing(Character c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doThing(GameScreen gs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doThing(Object o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(int x, int y) {
		game.batch.draw(getBackground(), x, y);
		getFont().draw(game.batch, getText(), x + 2, y + 28, 60, 1, false);
	}

	@Override
	public void doThing(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
