package com.avanderbeck.september.character;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.avanderbeck.september.Sortie;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PaperDoll {
	List<TextureRegion> behind;
	TextureRegion body;
	List<TextureRegion> infront;
	
	public PaperDoll(Sortie game)
	{
		Random r = new Random();
		int leftWeaponId = r.nextInt(2);
		int rightWeaponId = r.nextInt(2);

		//only one body at the moment
		body = new TextureRegion(game.assman.get("lolis/paperdoll/setbody.png", Texture.class), 0, 0, 256, 256);
		
		behind = new LinkedList<TextureRegion>();
		
		//Add behind weapon part
		behind.add(new TextureRegion(game.assman.get("lolis/paperdoll/setlefthandweapon.png", Texture.class),
				256 * leftWeaponId, 0, 256, 256));
		behind.add(new TextureRegion(game.assman.get("lolis/paperdoll/setrighthandweapon.png", Texture.class),
				256 * rightWeaponId, 0, 256, 256));
		
		//add hair next
		behind.add(new TextureRegion(game.assman.get("lolis/paperdoll/setbottomhair.png", Texture.class),
				256 * r.nextInt(3), 0, 256, 256));
		
		//Foreground parts
		infront = new LinkedList<TextureRegion>();

		//face
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/setface.png", Texture.class),
				256 * r.nextInt(2), 0, 256, 256));
		//hair
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/settophair.png", Texture.class),
				256 * r.nextInt(3), 0, 256, 256));

		//hat
		//not everyone should have a hat
		if(r.nextBoolean())
			infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/sethat.png", Texture.class),
				256 * r.nextInt(3), 0, 256, 256));
		
		//shoes
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/setshoes.png", Texture.class),
				256 * r.nextInt(3), 0, 256, 256));
		
		//pants
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/setpants.png", Texture.class),
				256 * r.nextInt(3), 0, 256, 256));
		
		//shirt
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/setshirt.png", Texture.class),
				256 * r.nextInt(3), 0, 256, 256));
		
		//Add in front weapon part
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/setlefthandweapon.png", Texture.class),
				256 * leftWeaponId, 256, 256, 256));
		infront.add(new TextureRegion(game.assman.get("lolis/paperdoll/setrighthandweapon.png", Texture.class),
				256 * rightWeaponId, 256, 256, 256));
	}
	
	public void draw(SpriteBatch batch, int x, int y)
	{
		for(TextureRegion t : behind)
			batch.draw(t, x, y);
		
		batch.draw(body, x, y);
		
		for(TextureRegion t : infront)
			batch.draw(t, x, y);
	}

}
