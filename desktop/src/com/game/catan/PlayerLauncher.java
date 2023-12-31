package com.game.catan;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.game.catan.Map.Map;
import com.game.catan.player.CatanPlayer;

public class PlayerLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Catan");
		config.setWindowedMode(1920, 1080);
		Map map = new Map();

		new Lwjgl3Application(new CatanPlayer(map), config);

	}
}
