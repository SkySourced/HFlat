package com.hflat.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.hflat.game.chart.Song;
import com.hflat.game.chart.SongManager;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.io.File;

/**
 * The main game class
 * Contains the game's state and main loop
 * Imports & initialises all resources
 */
public class Game extends ApplicationAdapter {
    private GameState state = GameState.LOADING;
    private OrthographicCamera camera;
    public SongManager songs;
    private SpriteBatch batch;
    private Texture logo;
    private ShapeDrawer drawer;
    private BitmapFont pixelFont20;
    private BitmapFont pixelFont12;
    private BitmapFont pixelFont40;
    private BitmapFont serifFont20;
    private BitmapFont serifFont12;
    private int selectedSongIndex = 0;
    private int selectedDifficultyIndex = 0;
    private long lastMenuAction;
    private Viewport viewport;

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

        songs = new SongManager(new File("charts"), this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 700);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

        viewport = new FitViewport(400, 700, camera);
        batch = new SpriteBatch();

        drawer = new ShapeDrawer(batch, region);

        logo = new Texture("hflatlogo.png");

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
        long menuActionDelay = 160000000L;
        switch (state) {
            case LOADING:
                batch.draw(logo, (float) Gdx.graphics.getWidth() / 2 - (float) logo.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) logo.getHeight() / 2 + 70);
                drawCentredText(batch, pixelFont20, "Uses unlicensed assets!", (float) Gdx.graphics.getHeight() / 2 - 100);
                drawCentredText(batch, pixelFont12, songs.getCurrentTask(), (float) Gdx.graphics.getHeight() / 2 - 150);
                break;
            case SONG_SELECT:
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
                        difficultyColour = Color.WHITE;
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

                break;
            case SONG_LOADING:
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
                break;
            case PLAYING:

                break;
            case RESULTS:
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
     * Update the game's state, drawing appropriate content to the screen
     *
     * @param state The new state to set
     */
    public void setState(GameState state) {
        this.state = state;
    }

    public static class Ref {
        public static final int DENOM_ROUND_PLACES = 7;
    }
}

