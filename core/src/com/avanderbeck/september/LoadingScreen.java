package com.avanderbeck.september;

import com.avanderbeck.september.screens.PersonnelScreen;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class LoadingScreen implements Screen {
	
	Sortie game;
	
	public LoadingScreen(Sortie game)
	{
		this.game = game;
		
		game.assman.load("placeholders.png", Texture.class);
		
		/*_     ___    _    ____    _____ ___ _     _____ ____  
		 | |   / _ \  / \  |  _ \  |_   _|_ _| |   | ____/ ___| 
		 | |  | | | |/ _ \ | | | |   | |  | || |   |  _| \___ \ 
		 | |__| |_| / ___ \| |_| |   | |  | || |___| |___ ___) |
		 |_____\___/_/   \_\____/    |_| |___|_____|_____|____/*/
		
		game.assman.load("tilesets/terraintiles.png", Texture.class);
		game.assman.load("selectsquare.png", Texture.class);

		
		/*_     ___    _    ____    ____   ____     
		 | |   / _ \  / \  |  _ \  | __ ) / ___|___ 
		 | |  | | | |/ _ \ | | | | |  _ \| |  _/ __|
		 | |__| |_| / ___ \| |_| | | |_) | |_| \__ \
		 |_____\___/_/   \_\____/  |____/ \____|___/*/
		game.assman.load("tilesets/backgrounds/grasslandsbackground.png", Texture.class);
		game.assman.load("tilesets/backgrounds/artillerybackground.png", Texture.class);
		game.assman.load("tilesets/backgrounds/forestbackground.png", Texture.class);
		game.assman.load("tilesets/backgrounds/shorebackground.png", Texture.class);
		game.assman.load("tilesets/backgrounds/citybackground.png", Texture.class);
		game.assman.load("tilesets/backgrounds/barracksbg.png", Texture.class);


		
		/*_     ___    _    ____     ____ _   _    _    ____      
		 | |   / _ \  / \  |  _ \   / ___| | | |  / \  |  _ \ ___ 
		 | |  | | | |/ _ \ | | | | | |   | |_| | / _ \ | |_) / __|
		 | |__| |_| / ___ \| |_| | | |___|  _  |/ ___ \|  _ <\__ \
		 |_____\___/_/   \_\____/   \____|_| |_/_/   \_\_| \_\___/*/
		
		game.assman.load("lolis/mapunits/scoutloli.png", Texture.class);
		game.assman.load("lolis/mapunits/rocketloli.png", Texture.class);
		game.assman.load("lolis/mapunits/apc.png", Texture.class);


		//game.assman.load("lolis/scoutlolis.png", Texture.class);
		game.assman.load("lolis/mapunits/scoutloli2.png", Texture.class);
		game.assman.load("lolis/enemyscout.png", Texture.class);
		game.assman.load("lolis/artillerycannon.png", Texture.class);
		game.assman.load("lolis/APCprofile.png", Texture.class);

		game.assman.load("lolis/turntags.png", Texture.class);
		
		game.assman.load("lolis/paperdoll/setbody.png", Texture.class);
		game.assman.load("lolis/paperdoll/setbottomhair.png", Texture.class);
		game.assman.load("lolis/paperdoll/settophair.png", Texture.class);
		game.assman.load("lolis/paperdoll/setface.png", Texture.class);
		game.assman.load("lolis/paperdoll/setshirt.png", Texture.class);
		game.assman.load("lolis/paperdoll/setpants.png", Texture.class);
		game.assman.load("lolis/paperdoll/setshoes.png", Texture.class);
		game.assman.load("lolis/paperdoll/sethat.png", Texture.class);
		game.assman.load("lolis/paperdoll/setlefthandweapon.png", Texture.class);
		game.assman.load("lolis/paperdoll/setrighthandweapon.png", Texture.class);


		
		/*_     ___    _    ____    _____ ___  _   _ _____    
		 | |   / _ \  / \  |  _ \  |  ___/ _ \| \ | |_   _|__ 
		 | |  | | | |/ _ \ | | | | | |_ | | | |  \| | | |/ __|
		 | |__| |_| / ___ \| |_| | |  _|| |_| | |\  | | |\__ \
		 |_____\___/_/   \_\____/  |_|   \___/|_| \_| |_||___/*/
		game.assman.load("fonts/courieroutline.fnt", BitmapFont.class);
		game.assman.load("fonts/droid8pt.fnt", BitmapFont.class);
		game.assman.load("fonts/droidsans.fnt", BitmapFont.class);
		game.assman.load("fonts/proggyclean.fnt", BitmapFont.class);


	



		/* _     ___    _    ____    _____ _   _ ___ _   _  ____ ____      
		  | |   / _ \  / \  |  _ \  |_   _| | | |_ _| \ | |/ ___/ ___| ___ 
		  | |  | | | |/ _ \ | | | |   | | | |_| || ||  \| | |  _\___ \/ __|
		  | |__| |_| / ___ \| |_| |   | | |  _  || || |\  | |_| |___) \__ \
		  |_____\___/_/   \_\____/    |_| |_| |_|___|_| \_|\____|____/|___/*/
		game.assman.load("effects/explosion.png", Texture.class);
		game.assman.load("cursors.png", Texture.class);
		game.assman.load("border.png", Texture.class);
		game.assman.load("ui/uilogos.png", Texture.class);
		game.assman.load("ui/uibuttonbg.png", Texture.class);
		game.assman.load("ui/star.png", Texture.class);
		game.assman.load("ui/blackboard.png", Texture.class);
		game.assman.load("ui/scrollbar.png", Texture.class);









	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		if(game.assman.update())//when this returns true the game is done loading
		{
			//Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("blank.png")), 0, 0));
			
			game.units.add(game.charGen.getScout(game, null));
			game.units.add(game.charGen.getScout(game, null));
			game.units.add(game.charGen.getScout(game, null));
			game.units.add(game.charGen.getScout(game, null));
			game.units.add(game.charGen.getRocket(game, null));
			game.units.add(game.charGen.getRocket(game, null));
			game.units.add(game.charGen.getAPC(game, null));
			
			game.setScreen(new PersonnelScreen(game));
			//game.setScreen(new GameScreen(game));
			
		}

		/*// display loading information
      float progress = manager.getProgress()
      ... left to the reader ...*/
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
