package com.mygdx.dragonrealms;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
//import com.mygdx.dragonrealms.DragonRealms;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowIcon("archery.png");
		config.setTitle("Dragon Realms");
		config.setAutoIconify(true);
		config.setResizable(true);
		config.setWindowedMode(1800,900);
		config.useVsync(true);
		new Lwjgl3Application(new MyGame(), config);

	}
}
