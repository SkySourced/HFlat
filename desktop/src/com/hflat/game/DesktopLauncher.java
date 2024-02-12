package com.hflat.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// remove frame cap
		config.setForegroundFPS(0);
		config.useVsync(false);

		// set window size
		config.setWindowedMode(400, 700);

		config.setTitle("HFlat");
		new Lwjgl3Application(new Game(), config);
	}
}
