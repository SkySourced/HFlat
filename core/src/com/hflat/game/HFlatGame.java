package com.hflat.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hflat.game.chart.Chart;
import com.hflat.game.chart.GameOptions;
import com.hflat.game.chart.Song;
import com.hflat.game.chart.SongManager;
import com.hflat.game.screens.*;
import com.hflat.game.ui.AssetsManager;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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
    private SpriteBatch batch;
    public static AssetsManager assMan = new AssetsManager();

    private Screen loadingScreen;
    private Screen songSelectScreen;
    private Screen songLoadingScreen;
    private Screen optionsScreen;
    private Screen playingScreen;
    private Screen resultsScreen;
    private Texture logo;
    private ShapeDrawer drawer;
    private BitmapFont pixelFont20;
    private BitmapFont pixelFont12;
    private BitmapFont pixelFont40;
    private BitmapFont serifFont20;
    private BitmapFont serifFont12;
    private int selectedSongIndex = 0;
    private int selectedDifficultyIndex = 0;
    private int optionSelectionIndex = 6;
    private float escapeHeldDuration;
    private float dontGiveUpTime = 0f;
    private long lastMenuAction;
    private Viewport viewport;
    NumberFormat formatter = new DecimalFormat("0.00"); // This should be renamed, but I can't think of anything good6-
    private long loadingStartTime;
    private static Song currentSong;
    private static Chart currentChart;

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

        assMan.queueAddMenuTextures();
        assMan.manager.finishLoading();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

        batch = new SpriteBatch();

        drawer = new ShapeDrawer(batch, region);

        logo = new Texture("hFlatLogo.png");



        //buildFonts();
    }

    /**
     * Builds the fonts used in the game
     */
    private void buildFonts() {
        FreeTypeFontGenerator pixelGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/5x5.ttf"));
        FreeTypeFontGenerator serifGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Newsreader.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = 20;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);
        pixelFont20 = pixelGenerator.generateFont(parameter);

        parameter.size = 12;
        pixelFont12 = pixelGenerator.generateFont(parameter);

        parameter.size = 40;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        pixelFont40 = pixelGenerator.generateFont(parameter);

        parameter.size = 20;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        serifFont20 = serifGenerator.generateFont(parameter);

        parameter.size = 12;
        serifFont12 = serifGenerator.generateFont(parameter);

        pixelGenerator.dispose();
        serifGenerator.dispose();
    }

    /**
     * Called every frame
     * Renders the game
     */
    @Override
    public void render() {
        if (songs.getSong(selectedSongIndex).getBackgroundColour() != null) {
            ScreenUtils.clear(songs.getSong(selectedSongIndex).getBackgroundColour());
        } else {
            ScreenUtils.clear(Color.WHITE);
        }

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        long menuActionDelay = (long) (Math.pow(10, 9) / 8);
        switch (state) {
            case LOADING:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
                this.setScreen(loadingScreen);

                batch.draw(logo, (float) Gdx.graphics.getWidth() / 2 - (float) logo.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) logo.getHeight() / 2 + 70);
                drawCentredText(batch, pixelFont20, "Uses unlicensed assets!", (float) Gdx.graphics.getHeight() / 2 - 100);
                drawCentredText(batch, pixelFont12, songs.getCurrentTask(), (float) Gdx.graphics.getHeight() / 2 - 150);
                break;
            case SONG_SELECT:
                if (songSelectScreen == null) songSelectScreen = new SongSelectScreen(this);
                this.setScreen(songSelectScreen);

                currentSong = songs.getSong(selectedSongIndex);
                currentChart = currentSong.getChart(selectedDifficultyIndex);

                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && lastMenuAction + menuActionDelay < System.nanoTime()) {
                    selectedSongIndex--;
                    lastMenuAction = System.nanoTime();
                    if (selectedSongIndex < 0) selectedSongIndex = songs.getSongCount() - 1;
                    selectedDifficultyIndex = Math.min(selectedDifficultyIndex, songs.getSong(selectedSongIndex).getChartCount() - 1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && lastMenuAction + menuActionDelay < System.nanoTime()) {
                    selectedSongIndex++;
                    lastMenuAction = System.nanoTime();
                    if (selectedSongIndex >= songs.getSongCount()) selectedSongIndex = 0;
                    selectedDifficultyIndex = Math.min(selectedDifficultyIndex, songs.getSong(selectedSongIndex).getChartCount() - 1);
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && lastMenuAction + menuActionDelay < System.nanoTime()) {
                    selectedDifficultyIndex++;
                    lastMenuAction = System.nanoTime();
                    if (selectedDifficultyIndex >= currentSong.getChartCount()) selectedDifficultyIndex = currentSong.getChartCount() - 1;
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && lastMenuAction + menuActionDelay < System.nanoTime()) {
                    selectedDifficultyIndex--;
                    lastMenuAction = System.nanoTime();
                    if (selectedDifficultyIndex < 0) selectedDifficultyIndex = 0;
                }

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    loadingStartTime = System.nanoTime() / 1000000;
                    setState(GameState.SONG_LOADING);
                }

                // Draw album art
                batch.draw(currentSong.getTexture(), 25, 300, 350, 350);

                drawCentredText(batch, pixelFont40, "Song Select", 690);

                // Draw difficulty backgrounds
                for (int i = 0; i < 6; i++) {
                    drawer.filledRectangle(25, 240 + 10 * i, 10, 10, Color.GRAY);
                    Color difficultyColour;
                    try {
                        difficultyColour = currentSong.getChart(i, false).getDifficultyColour();
                    } catch (NullPointerException e) {
                        difficultyColour = Color.GRAY;
                    }
                    if (i == selectedDifficultyIndex) {
                        drawer.filledRectangle(28, 243 + 10 * i, 4, 4, Color.WHITE);
                    }
                    drawer.filledRectangle(27, 242 + 10 * i, 6, 6, difficultyColour);
                }
                drawer.filledRectangle(35, 240, 60, 60, currentChart.getDifficultyColour());
                drawer.filledRectangle(95, 240, 280, 60, Color.WHITE);

                // Draw difficulty text
                pixelFont40.draw(batch, currentChart.getDifficultyString(), 110, 285);
                int difficulty = currentChart.getDifficulty();
                pixelFont40.draw(batch, String.valueOf(difficulty), String.valueOf(difficulty).length() == 1 ? 52 : 45, 285);


                serifFont20.draw(batch, currentSong.getName(), 30, 230);
                serifFont12.draw(batch, currentSong.getSubtitle(), 30, 210);
                serifFont20.draw(batch, currentSong.getArtist(), 30, 190);


                break;
            case SONG_LOADING:
                if (songLoadingScreen == null) songLoadingScreen = new SongLoadingScreen(this);
                this.setScreen(songLoadingScreen);

                // make background darkened version of difficultyColour
                long loadingTime = 3000;
                float progress = (System.nanoTime() / 1000000f - this.loadingStartTime) / (float) loadingTime;

                drawer.filledRectangle(0, 0, 400, 700, new Color(currentChart.getDifficultyColour().r, currentChart.getDifficultyColour().g, currentChart.getDifficultyColour().b, 0.5f));
                drawer.filledRectangle(0, 300, 400, 100, Color.DARK_GRAY);
                drawer.filledRectangle(0, 300, progress * 400, 100, Color.GRAY);

                drawCentredText(batch, serifFont20, currentSong.getName(), 695);
                drawCentredText(batch, pixelFont20, "press  enter  for  options", 360);

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    setState(GameState.OPTIONS);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    setState(GameState.SONG_SELECT);
                }
                if (progress >= 1) {
                    setState(GameState.PLAYING);
                }
                break;
            case OPTIONS:
                if (optionsScreen == null) optionsScreen = new OptionsScreen(this);
                this.setScreen(optionsScreen);

                drawCentredText(batch, pixelFont40, "Options", 690);
                drawer.filledRectangle(25, 25, 350, 625, new Color(0.7f, 0.7f, 0.7f, 0.7f));
                serifFont20.draw(batch, "Note speed", 40, 625);
                serifFont20.draw(batch, "Note size", 40, 575);
                serifFont20.draw(batch, "Show judgements", 40, 525);
                serifFont20.draw(batch, "Show combo", 40, 475);
                serifFont20.draw(batch, "Background filter", 40, 425);
                serifFont20.draw(batch, "Music speed", 40, 375);
                serifFont20.draw(batch, "Visual offset", 40, 325);

                int optionWidth;
                GlyphLayout layout = new GlyphLayout();

                switch (optionSelectionIndex) {
                    case 6:
                        layout.setText(serifFont20, formatter.format(options.getNoteSpeed())+"x");
                        break;
                    case 5:
                        layout.setText(serifFont20, (int) (options.getMini() * 100) + "%");
                        break;
                    case 4:
                        layout.setText(serifFont20, options.isShowJudgements() ? "On" : "Off");
                        break;
                    case 3:
                        layout.setText(serifFont20, options.isShowCombo() ? "On" : "Off");
                        break;
                    case 2:
                        layout.setText(serifFont20, options.getBackgroundFilter().toString());
                        break;
                    case 1:
                        layout.setText(serifFont20, formatter.format(options.getMusicRate())+"x");
                        break;
                    case 0:
                        layout.setText(serifFont20, options.getVisualOffset() + "ms");
                        break;
                    case -1:
                        layout.setText(pixelFont40, "play");
                    default:
                        break;
                }

                optionWidth = (int) layout.width;

                if (optionSelectionIndex >= 0) {
                    drawer.rectangle(360 - optionWidth - 7, 300 + 50 * optionSelectionIndex, optionWidth + 15, 30, Color.BLACK, 2);
                } else {
                    drawer.rectangle(200 - ((float) optionWidth / 2) - 20, 30, optionWidth + 40, 60, Color.BLACK, 2);
                }

                drawRightAlignedText(batch, serifFont20, formatter.format(options.getNoteSpeed())+"x", 360, 625);
                drawRightAlignedText(batch, serifFont20, (int) (options.getMini() * 100) + "%", 360, 575);
                drawRightAlignedText(batch, serifFont20, options.isShowJudgements() ? "On" : "Off", 360, 525);
                drawRightAlignedText(batch, serifFont20, options.isShowCombo() ? "On" : "Off", 360, 475);
                drawRightAlignedText(batch, serifFont20, options.getBackgroundFilter().toString(), 360, 425);
                drawRightAlignedText(batch, serifFont20, formatter.format(options.getMusicRate())+"x", 360, 375);
                drawRightAlignedText(batch, serifFont20, options.getVisualOffset() + "ms", 360, 325);

                drawCentredText(batch, pixelFont40, "play", 75);

                if (Gdx.input.isKeyPressed(Input.Keys.UP) && lastMenuAction + menuActionDelay < System.nanoTime()){
                    optionSelectionIndex++;
                    if (optionSelectionIndex > 6) optionSelectionIndex = -1;
                    lastMenuAction = System.nanoTime();
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && lastMenuAction + menuActionDelay < System.nanoTime()){
                    optionSelectionIndex--;
                    if (optionSelectionIndex < -1) optionSelectionIndex = 6;
                    lastMenuAction = System.nanoTime();
                }

                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && lastMenuAction + menuActionDelay < System.nanoTime()){
                    lastMenuAction = System.nanoTime();
                    switch(optionSelectionIndex){
                        case 6:
                            options.setNoteSpeed(options.getNoteSpeed() - 0.05f);
                            if (options.getNoteSpeed() < 0.05f) options.setNoteSpeed(0.05f);
                            break;
                        case 5:
                            options.setMini(options.getMini() - 0.01f);
                            if (options.getMini() < 0.01f) options.setMini(0.01f);
                            break;
                        case 4:
                            options.setShowJudgements(!options.isShowJudgements());
                            break;
                        case 3:
                            options.setShowCombo(!options.isShowCombo());
                            break;
                        case 2:
                            options.setBackgroundFilter(options.getBackgroundFilter().previous());
                            break;
                        case 1:
                            options.setMusicRate(options.getMusicRate() - 0.05f);
                            if (options.getMusicRate() < 0.05f) options.setMusicRate(0.05f);
                            break;
                        case 0:
                            options.setVisualOffset(options.getVisualOffset() - 1);
                            break;
                    }
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && lastMenuAction + menuActionDelay < System.nanoTime()){
                    switch (optionSelectionIndex){
                        case 6:
                            options.setNoteSpeed(options.getNoteSpeed() + 0.05f);
                            if (options.getNoteSpeed() > 10.0f) options.setNoteSpeed(10.0f);
                            break;
                        case 5:
                            options.setMini(options.getMini() + 0.01f);
                            if (options.getMini() > 1f) options.setMini(1f);
                            break;
                        case 4:
                            options.setShowJudgements(!options.isShowJudgements());
                            break;
                        case 3:
                            options.setShowCombo(!options.isShowCombo());
                            break;
                        case 2:
                            options.setBackgroundFilter(options.getBackgroundFilter().next());
                            break;
                        case 1:
                            options.setMusicRate(options.getMusicRate() + 0.05f);
                            if (options.getMusicRate() > 5f) options.setMusicRate(5f);
                            break;
                        case 0:
                            options.setVisualOffset(options.getVisualOffset() + 1);
                            if (options.getVisualOffset() > 1000) options.setVisualOffset(1000);
                            break;
                    }
                    lastMenuAction = System.nanoTime();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && lastMenuAction + menuActionDelay > System.nanoTime()){
                    lastMenuAction = System.nanoTime();
                    if (optionSelectionIndex == -1){
                        setState(GameState.PLAYING);
                    }
                }

                break;
            case PLAYING:
                if (playingScreen == null) playingScreen = new PlayingScreen(this);
                this.setScreen(playingScreen);

                if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
                    dontGiveUpTime = 0;
                    escapeHeldDuration += Gdx.graphics.getDeltaTime();
                    drawCentredText(batch, serifFont12, "Hold ESC to quit", 150);
                    // seconds
                    int escapeHeldThreshold = 2;
                    if (escapeHeldDuration > escapeHeldThreshold){
                        setState(GameState.SONG_SELECT);
                    }
                } else {
                    if (escapeHeldDuration > 0) dontGiveUpTime = 1.5f;
                    escapeHeldDuration = 0;
                }

                if (dontGiveUpTime > 0){
                    dontGiveUpTime -= Gdx.graphics.getDeltaTime();
                    drawCentredText(batch, serifFont12, "Don't give up!", 150);
                }
                break;
            case RESULTS:
                if (resultsScreen == null) resultsScreen = new ResultsScreen(this);
                this.setScreen(resultsScreen);
                break;
        }
        batch.end();
    }

    /**
     * Called when the application is destroyed
     * Destroys all resources
     */
    @Override
    public void dispose() {
        batch.dispose();
        logo.dispose();
        pixelFont20.dispose();
        pixelFont12.dispose();
        pixelFont40.dispose();
        serifFont12.dispose();
        serifFont20.dispose();
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

    public static class Ref {
        public static final int DENOM_ROUND_PLACES = 7;
    }
}

