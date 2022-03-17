package com.telek.screens;


import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import com.telek.*;
import com.telek.telekgdx.screens.TScreen;
import com.telek.telekgdx.screens.TScreenUtils;

import java.util.*;


public class GameScreen implements TScreen {


    // singleton game object
    private final The2048 game;

    // rendering
    OrthographicCamera camera;
    Viewport viewport;

    // scene2d
    Stage stage;
    Table root, tableForGrid;

    // game objects
    enum State { start, won, running, over }
    private TextButton[][] grid;
    final int target = 2048;
    private boolean gameIsOver, checkingAvailableMoves;
    private int gridSize, highest, score;
    private Random rand = new Random();
    private Tile[][] tiles;
    private State gamestate = State.start;



    public GameScreen(final The2048 game, int gridSize){
        // singleton
        this.game = game;
        this.gridSize = gridSize;
        this.gameIsOver = false;
        this.grid = new TextButton[this.gridSize][this.gridSize];

        // rendering
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        // scene2d
        Skin skin = this.game.assetSorter.getResource("skin", Skin.class);
        stage = new Stage(viewport, this.game.batch);
        Gdx.input.setInputProcessor(stage);
        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.top();
        root.padTop(25f);

        // game logic
        tableForGrid = new Table();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                TextButton btn = new TextButton("", skin);
                btn.setDisabled(true); // for the looks
                tableForGrid.add(btn).width(90).center();
                grid[i][j] = btn;
            }
            tableForGrid.row();
        }
        root.add(tableForGrid).width(Value.percentWidth(1f, root)).height(Value.percentHeight(0.8f, root)).row();


        TextButton btn = new TextButton("GO BACK", skin);
        btn.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {
           game.setScreen( game.screenSorter.getScreen("mainMenuScreen") );
        }});

        root.add(btn).padRight(Value.percentWidth(0.6f, root));


        startGame();
    }




    boolean isGameOver(){
        int maxNum = 0;
        for(TextButton[] btnArr : grid){
            for(TextButton btn : btnArr){
                if(Integer.parseInt(btn.getText().toString()) == 2048) maxNum++;
                if(maxNum >= 1) return true;
            }
        }
        return false;
    }
    void updateBtnGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(tiles[i][j] != null)
                    grid[i][j].setText(String.valueOf(tiles[i][j].getValue()));
                else
                    grid[i][j].setText("0");
            }
        }
    }
    void colorGrid(){
        for(TextButton[] btnArr : grid){
            for(TextButton btn : btnArr){
                String text = btn.getText().toString();
                if(!text.matches("^[0-9]+$")) {
                    btn.setText("0");
                    continue;
                }
                int value = Integer.parseInt(text);
                if(value == 0) btn.setColor(Color.LIGHT_GRAY);
                else if(value == 2) btn.setColor(Color.GRAY);
                else if(value == 4) btn.setColor(Color.LIME);
                else if(value == 8) btn.setColor(Color.GREEN);
                else if(value == 16) btn.setColor(Color.YELLOW);
                else if(value == 32) btn.setColor(Color.CYAN);
                else if(value == 64) btn.setColor(Color.BLUE);
                else if(value == 128) btn.setColor(Color.ORANGE);
                else if(value == 256) btn.setColor(Color.RED);
                else if(value == 512) btn.setColor(Color.PINK);
                else if(value == 1024) btn.setColor(Color.GOLDENROD);
                else if(value == 2048) btn.setColor(Color.MAGENTA);
            }
        }
    }
    void startGame() {
        if (gamestate != State.running) {
            score = 0;
            highest = 0;
            gamestate = State.running;
            tiles = new Tile[gridSize][gridSize];
            addRandomTile();
            addRandomTile();
        }
    }
    boolean moveUp() { return move(0, -1, 0); }
    boolean moveDown() { return move(gridSize * gridSize - 1, 1, 0); }
    boolean moveLeft() { return move(0, 0, -1); }
    boolean moveRight() { return move(gridSize * gridSize - 1, 0, 1); }




    @Override
    public void update(float delta) {
        if(gameIsOver) return;

        stage.act(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) moveUp();
        if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) moveDown();
        if(Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) moveLeft();
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) moveRight();

        updateBtnGrid();
        colorGrid();

        if(isGameOver()) endGame();
    }

    private void endGame() {
        Gdx.input.setInputProcessor(null);
        this.gameIsOver = true;
        if(game.isMusicOn){
            Sound congratsRobot = this.game.assetSorter.getResource("congratsRobot", Sound.class);
            long id = congratsRobot.play();
            congratsRobot.setVolume(id, 0.5f);
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen( game.screenSorter.getScreen("mainMenuScreen") );
            }
        }, 3);
    }


    @Override
    public void render(float delta) {
        update(delta);
        TScreenUtils.clearScreen();

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
        this.gameIsOver = false;
    }



    @Override
    public void dispose() {
        stage.dispose();
    }


    private static class Tile {
        private boolean merged;
        private int value;

        public Tile(int val) {
            value = val;
        }

        public int getValue() {
            return value;
        }

        public void setMerged(boolean m) {
            merged = m;
        }

        public boolean canMergeWith(Tile other) {
            return !merged && other != null && !other.merged && value == other.getValue();
        }

        public int mergeWith(Tile other) {
            if (canMergeWith(other)) {
                value *= 2;
                merged = true;
                return value;
            }
            return -1;
        }
    }
    private void clearMerged() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }
    private boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }
    private void addRandomTile() {
        int pos = rand.nextInt(gridSize * gridSize);
        int row, col;
        do {
            pos = (pos + 1) % (gridSize * gridSize);
            row = pos / gridSize;
            col = pos % gridSize;
        } while (tiles[row][col] != null);

        int val = rand.nextInt(10) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
    }
    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;

        for (int i = 0; i < gridSize * gridSize; i++) {
            int j = Math.abs(countDownFrom - i);

            int r = j / gridSize;
            int c = j % gridSize;

            if (tiles[r][c] == null)
                continue;

            int nextR = r + yIncr;
            int nextC = c + xIncr;

            while (nextR >= 0 && nextR < gridSize && nextC >= 0 && nextC < gridSize) {

                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];

                if (next == null) {

                    if (checkingAvailableMoves)
                        return true;

                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;

                } else if (next.canMergeWith(curr)) {

                    if (checkingAvailableMoves)
                        return true;

                    int value = next.mergeWith(curr);
                    if (value > highest)
                        highest = value;
                    score += value;
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }

        if (moved) {
            if (highest < target) {
                clearMerged();
                addRandomTile();
                if (!movesAvailable()) {
                    gamestate = State.over;
                }
            } else if (highest == target)
                gamestate = State.won;
        }

        return moved;
    }



    @Override public void pause(){}
    @Override public void show(){}
    @Override public void resume(){}
    @Override public void hide(){}

}
