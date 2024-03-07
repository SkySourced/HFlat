package com.hflat.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        Graphics.Monitor monitor = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
        Graphics.DisplayMode desktopMode = Lwjgl3ApplicationConfiguration.getDisplayMode(monitor);

        int height = (int) (desktopMode.height * 0.9);
        float aspectRatio = 4/7f;

        // remove frame cap
        config.setForegroundFPS(0);
        config.useVsync(false);

        // set window size
        config.setWindowedMode((int) (height * aspectRatio), height);

        config.setTitle("HFlat");
        new Lwjgl3Application(new HFlatGame(), config);
    }
}
