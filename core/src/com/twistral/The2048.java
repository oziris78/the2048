package com.twistral;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.twistral.screens.MainMenuScreen;
import com.twistral.screens.SettingsScreen;
import com.twistral.toriagdx.assets.*;
import com.twistral.toriagdx.screens.*;
import com.twistral.screens.GameScreen;


public class The2048 extends Game {

	// toria-gdx utils
	public AssetSorter assetSorter;
	public ScreenSorter screenSorter;

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
		preferences = Gdx.app.getPreferences("twistral_the2048");
		isMusicOn = preferences.getBoolean("isMusicOn", true);

		// screensorter
		this.screenSorter = new ScreenSorter();
		this.screenSorter.putScreen("mainMenuScreen", MainMenuScreen.class, The2048.class);
		this.screenSorter.putScreen("settingsScreen", SettingsScreen.class, The2048.class);
		this.screenSorter.putScreen("gameScreen", GameScreen.class, The2048.class, int.class);

		// rendering
		this.batch = new SpriteBatch();

		// use screensorter
		this.setScreen( this.screenSorter.getScreen("mainMenuScreen", this) );
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
		this.screenSorter.disposeAll();
	}


}
