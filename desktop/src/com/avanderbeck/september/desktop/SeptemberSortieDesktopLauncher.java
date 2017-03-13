package com.avanderbeck.september.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.avanderbeck.september.Sortie;

public class SeptemberSortieDesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "September Sortie";
		
		config.width = 1280;
		config.height = 720;
		/*config.width = 640;
		config.height = 360;*/
		
		new LwjglApplication(new Sortie(), config);
	}
}
