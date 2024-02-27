package com.hflat.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hflat.game.chart.Chart;
import com.hflat.game.chart.GameOptions;
import com.hflat.game.chart.Song;
import com.hflat.game.chart.SongManager;
import com.hflat.game.screens.*;
import com.hflat.game.ui.AssetsManager;

import java.io.File;

/**
 * The main game class
 * Contains the game's state and main loop
 * Imports & initialises all resources
 */
public class HFlatGame extends Game implements ApplicationListener {
    private GameState state = GameState.LOADING;
    private OrthographicCamera camera;
    public SongManager songs;
    public static GameOptions options;
    public static AssetsManager assMan = new AssetsManager();
    public static final float NOTE_SPACING = 1.0f;
    public static final long menuActionDelay = (long) (Math.pow(10, 9) / 8);

    private Screen loadingScreen;
    private Screen songSelectScreen;
    private Screen songLoadingScreen;
    private Screen optionsScreen;
    private Screen playingScreen;
    private Screen resultsScreen;
    private Viewport viewport;
    public static Song currentSong;
    public static Chart currentChart;

    /**
     * The game's state
     */
    public enum GameState {
        LOADING,
        SONG_SELECT,
        SONG_LOADING,
        OPTIONS,
        PLAYING,
        RESULTS
    }

    /**
     * Called when the application is created
     * Initialises all resources
     */
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 700);
        viewport = new FitViewport(400, 700, camera);

        songs = new SongManager(new File("charts"), this);
        options = new GameOptions();

        assMan.queueAdd();
        assMan.manager.finishLoading();
    }



    /**
     * Called every frame
     * Renders the game
     */
    @Override
    public void render() {
        if (currentSong.getBackgroundColour() != null) {
            ScreenUtils.clear(currentSong.getBackgroundColour());
        } else {
            ScreenUtils.clear(Color.WHITE);
        }

        camera.update();

        switch (state) {
            case LOADING:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
                this.setScreen(loadingScreen);

                break;
            case SONG_SELECT:
                if (songSelectScreen == null) songSelectScreen = new SongSelectScreen(this);
                this.setScreen(songSelectScreen);


                break;
            case SONG_LOADING:
                if (songLoadingScreen == null) songLoadingScreen = new SongLoadingScreen(this);
                this.setScreen(songLoadingScreen);


                break;
            case OPTIONS:
                if (optionsScreen == null) optionsScreen = new OptionsScreen(this);
                this.setScreen(optionsScreen);


                break;
            case PLAYING:
                if (playingScreen == null) playingScreen = new PlayingScreen(this);
                this.setScreen(playingScreen);


                break;
            case RESULTS:
                if (resultsScreen == null) resultsScreen = new ResultsScreen(this);
                this.setScreen(resultsScreen);
                break;
        }
    }

    /**
     * Called when the application is destroyed
     * Destroys all resources
     */
    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * Returns the offset required to centre bitmap text
     *
     * @param font The font to use
     * @param text The text to centre
     * @return The offset required to centre the text
     */
    public static float getCentreTextOffset(BitmapFont font, String text) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);
        return layout.width / 2;
    }

    /**
     * Draws centred text
     *
     * @param batch The sprite batch to draw to
     * @param font  The font to use
     * @param text  The text to draw
     * @param y     The y position to draw the text at
     */

    public static void drawCentredText(SpriteBatch batch, BitmapFont font, String text, float y) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);
        font.draw(batch, text, (float) 200 - layout.width / 2, y);
    }

    /**
     * Draws right-aligned text
     * @param batch The sprite batch to draw to
     * @param font The font to use
     * @param text The text to draw
     * @param x The x position to draw the text at (right-most point)
     * @param y The y position to draw the text at
     */
    public static void drawRightAlignedText(SpriteBatch batch, BitmapFont font, String text, float x, float y) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);
        font.draw(batch, text, x - layout.width, y);
    }

    /**
     * Update the game's state, drawing appropriate content to the screen
     *
     * @param state The new state to set
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Get the current game state
     * @return The current game state
     */
    public SongManager getSongManager() {
        return songs;
    }

    /**
     * Get the camera
     * @return The camera
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    public static class Ref {
        public static final int DENOM_ROUND_PLACES = 7;
    }
}

