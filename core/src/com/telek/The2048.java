package com.telek;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.telek.screens.*;
import com.telek.telekgdx.assets.*;
import com.telek.telekgdx.screens.*;



public class The2048 extends Game {

	// telek-gdx utils
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
		initAssetSorter();

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


	private void initAssetSorter(){
		// immediately needed assets
		assetSorter.addImmediatelyNeededAsset("congratsRobot", Gdx.audio.newSound(Gdx.files.internal("congratsRobot.wav")));
		assetSorter.addImmediatelyNeededAsset("skin", new Skin(Gdx.files.internal("skins/holo-dark-hdpi/Holo-dark-hdpi.json")));
		// other asset groups
		// ...
	}


	@Override
	public void dispose() {
		super.dispose();
		this.assetSorter.disposeAll();
		this.screenSorter.dispose();
	}


}
