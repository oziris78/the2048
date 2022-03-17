package com.telek.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.rafaskoberg.gdx.typinglabel.*;
import com.telek.*;
import com.telek.jtelek.*;
import com.telek.jtelek.generalUtils.*;


public class MainMenuScreen implements TScreen {

    // singleton game object
    private final The2048 game;

    // rendering
    OrthographicCamera camera;
    Viewport viewport;

    // scene2d
    Stage stage;
    Table table;
    TypingLabel lbTitle;
    TextButton btn4x4, btn5x5, btn6x6, btnSettings, btnQuit;


    public MainMenuScreen(The2048 game){
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

        table.top();

        lbTitle = new TypingLabel("{SPEED=0.25}THE 2048 GAME", this.game.assetSorter.skin);
        table.add(lbTitle).padTop(10f).row();

        btn4x4 = new TextButton("PLAY 4X4", this.game.assetSorter.skin);
        table.add(btn4x4).padTop(35f).row();
        btn5x5 = new TextButton("PLAY 5X5", this.game.assetSorter.skin);
        table.add(btn5x5).padTop(5f).row();
        btn6x6 = new TextButton("PLAY 6X6", this.game.assetSorter.skin);
        table.add(btn6x6).padTop(5f).row();
        btnSettings = new TextButton("SETTINGS", this.game.assetSorter.skin);
        table.add(btnSettings).padTop(5f).row();
        btnQuit = new TextButton("QUIT", this.game.assetSorter.skin);
        table.add(btnQuit).padTop(5f).row();
        addInputListeners();
    }



    private void addInputListeners(){
        btn4x4.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
            setAndChangeToGameScreen(4);
        }});
        btn5x5.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
            setAndChangeToGameScreen(5);
        }});
        btn6x6.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
            setAndChangeToGameScreen(6);
        }});
        btnSettings.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
            game.setScreen( game.screenSorter.getScreen("settingsScreen") );
        }});
        btnQuit.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
            Gdx.app.exit();
        }});
    }


    private void setAndChangeToGameScreen(int gridSize){
        game.screenSorter.disposeScreen("gameScreen");
        game.screenSorter.putScreen("gameScreen", new GameScreen(game, gridSize), GameScreen.class);
        game.setScreen( game.screenSorter.getScreen("gameScreen") );
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
