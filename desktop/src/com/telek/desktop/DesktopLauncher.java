package com.telek.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.telek.The2048;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// ratio is 4:3
		config.width = 800;
		config.height = 600;
		config.resizable = false;
		new LwjglApplication(new The2048(), config);
	}
}
