package com.telek.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.telek.*;
import com.telek.jtelek.*;
import com.telek.jtelek.generalUtils.*;


public class SettingsScreen implements TScreen {

    // singleton game object
    private final The2048 game;

    // rendering
    OrthographicCamera camera;
    Viewport viewport;

    // scene2d
    Stage stage;
    Table table;
    TextButton btnCloseMusic, btnGoBack;


    public SettingsScreen(final The2048 game){
        // singleton
        this.game = game;

        // rendering
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // scene2d
        stage = new Stage(viewport, this.game.batch);
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        btnCloseMusic = new TextButton(game.isMusicOn ? "MUSIC IS ON" : "MUSIC IS OFF", this.game.assetSorter.skin);
        btnCloseMusic.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
                game.isMusicOn = !game.isMusicOn;
                btnCloseMusic.setText(game.isMusicOn ? "MUSIC IS ON" : "MUSIC IS OFF");
                game.preferences.putBoolean("isMusicOn", game.isMusicOn);
                game.preferences.flush();
        }});

        btnGoBack = new TextButton("GO BACK", this.game.assetSorter.skin);
        btnGoBack.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
             game.setScreen( game.screenSorter.getScreen("mainMenuScreen") );
        }});

        table.add(btnCloseMusic).row();
        table.add(btnGoBack).padTop(25f);


    }





    @Override
    public void update(float delta) {
        stage.act(delta);
    }


    @Override
    public void render(float delta) {
        update(delta);
        flib.clearScreen(0f, 0f, 0f, 1f);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }


    @Override
    public void configure() {
        Gdx.input.setInputProcessor(null);
        Gdx.input.setInputProcessor(this.stage);
    }




    @Override
    public void dispose() {
        stage.dispose();
    }



    @Override public void pause(){}
    @Override public void show(){}
    @Override public void resume(){}
    @Override public void hide(){}

}
