package com.telek;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.telek.jtelek.generalUtils.AssetSorter;
import com.telek.jtelek.generalUtils.ScreenSorter;
import com.telek.screens.GameScreen;
import com.telek.screens.MainMenuScreen;
import com.telek.screens.SettingsScreen;

public class The2048 extends Game {

	// jtelek utils
	public AssetSorter assetSorter;
	public ScreenSorter<The2048> screenSorter;

	// saving
	public Preferences preferences;
	public boolean isMusicOn;

	// rendering utils
	public SpriteBatch batch;


	@Override
	public void create() {
		// assetsorter
		this.assetSorter = new AssetSorter();

		// general control
		preferences = Gdx.app.getPreferences("telek_the2048");
		isMusicOn = preferences.getBoolean("isMusicOn", true);

		// screensorter
		this.screenSorter = new ScreenSorter<>(this);
		this.screenSorter.putScreen("mainMenuScreen", MainMenuScreen.class);
		this.screenSorter.putScreen("settingsScreen", SettingsScreen.class);
		this.screenSorter.putScreen("gameScreen", GameScreen.class);

		// rendering
		this.batch = new SpriteBatch();

		// use screensorter
		this.setScreen( this.screenSorter.getScreen("mainMenuScreen") );
	}

	@Override
	public void dispose() {
		super.dispose();
		this.assetSorter.dispose();
		this.screenSorter.dispose();
	}
}
