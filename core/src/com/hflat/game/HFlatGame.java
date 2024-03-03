package com.hflat.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hflat.game.chart.*;
import com.hflat.game.screens.*;
import com.hflat.game.ui.AssetsManager;

import java.io.File;

/** The main game class. Contains the game's state & references to global utilities. Imports & initialises all resources
 */
public class HFlatGame extends Game {
    private GameState state = GameState.LOADING;
    private OrthographicCamera camera;
    public SongManager songs;
    private Play currentPlay;
    public static TextureRegion textureRegion;
    public static GameOptions options;
    public static AssetsManager assMan = new AssetsManager();
    public static final float NOTE_SPACING = 1.0f;
    public static final long menuActionDelay = (long) (Math.pow(10, 9) / 8);
    public long loadingStartTime;

    private Screen loadingScreen;
    private Screen songSelectScreen;
    private Screen songLoadingScreen;
    private Screen optionsScreen;
    private Screen playingScreen;
    private Screen resultsScreen;
    private Viewport viewport;
    public static Song currentSong;
    public static Chart currentChart;

    public Play getCurrentPlay() {
        return currentPlay;
    }

    public void setCurrentPlay(Play currentPlay) {
        this.currentPlay = currentPlay;
    }

    /**The game's state */
    public enum GameState {
        LOADING,
        SONG_SELECT,
        SONG_LOADING,
        OPTIONS,
        PLAYING,
        RESULTS
    }

    /** Called when the application is created, initialises all resources */
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 700);
        viewport = new FitViewport(400, 700, camera);

        songs = new SongManager(new File("charts"), this);
        options = new GameOptions();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        textureRegion = new TextureRegion(new Texture(pixmap));

        assMan.queueAdd();
        assMan.manager.finishLoading();
    }



    /**Called every frame, creates the various screen classes and renders them*/
    @Override
    public void render() {
        if (currentSong.getBackgroundColour() != null) {
            ScreenUtils.clear(currentSong.getBackgroundColour());
        } else {
            ScreenUtils.clear(Color.WHITE);
        }



        camera.update();


        try{
            if(((IHasStaticState) this.getScreen()).getState()  != state) {
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
        } catch(ClassCastException e){
            //Gdx.app.debug("Ha ha ha", "This is so funny...",e);
        }

        super.render();
    }

    /** Called when the application is destroyed, and destroys all resources */
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
        public static final int MAX_NOTE_SPEED = 10;
        public static final float MIN_NOTE_SPEED = 0.05f;
        public static final float STEP_NOTE_SPEED = 0.05f;
        public static final float MAX_MINI = 1f;
        public static final float MIN_MINI = 0.01f;
        public static final float STEP_MINI = 0.01f;
        public static final float MAX_MUSIC_RATE = 5f;
        public static final float MIN_MUSIC_RATE = 0.05f;
        public static final float STEP_MUSIC_RATE = 0.05f;
        public static final int MAX_VISUAL_OFFSET = 1000;
        public static final int MIN_VISUAL_OFFSET = -1000;
        public static final int STEP_VISUAL_OFFSET = 1;

        public static final int DEFAULT_ARROW_SIZE = 80;
        public static final int DEFAULT_ARROW_SPACING = 15;
        public static final float VERTICAL_ARROW_SCALAR = 1.0f;
        public static final int TARGET_ARROW_HEIGHT = 560;

        public static final int MAX_FRAMES = 144;

        public static final Color MARVELLOUS_COLOUR = color(0x3a, 0xe5, 0xfc);
        public static final Color FANTASTIC_COLOUR = color(0xed, 0xfa, 0xfc);
        public static final Color EXCELLENT_COLOUR = color(0xe2, 0xb8, 0x38);
        public static final Color GREAT_COLOUR = color(0xad, 0xf4, 0x66);
        public static final Color OK_COLOUR = color(0x6b, 0x23, 0x62);
        public static final Color DECENT_COLOUR = color(0xb0, 0x66, 0xf4);
        public static final Color WAY_OFF_COLOUR = color(0x99, 0x49, 0x1b);
        public static final Color MISS_COLOUR = color(0x99, 0x1b, 0x1d);

    }

    public static Color color(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color color(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }


}

