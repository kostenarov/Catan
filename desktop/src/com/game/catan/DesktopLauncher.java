package com.game.catan;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.catan.player.CatanPlayer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Catan");
		config.setWindowedMode(800, 600);
		new Lwjgl3Application(new CatanPlayer(), config);

	}
}
