package com.group4.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.group4.AuberGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Auber - Group 4";
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;
		config.forceExit = false;
		new LwjglApplication(new AuberGame(), config);
		
	}
}