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
	public ChartManager charts;
	private SpriteBatch batch;
	private Texture logo;
	private ShapeDrawer drawer;
	private BitmapFont pixelFont20;
	private BitmapFont pixelFont12;
	private BitmapFont pixelFont40;
	private BitmapFont serifFont20;
	private BitmapFont serifFont12;
	private int selectedSongIndex = 0;
	private long lastMenuAction;

	/**
	 * The game's state
	 */
	public enum GameState {
		LOADING,
		SONG_SELECT,
		PLAYING,
		RESULTS
	}

	/**
	 * Called when the application is created
	 * Initialises all resources
	 */
    @Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		charts = new ChartManager(new File("charts"), this);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 700);
		Gdx.app.log("Debugging test","I am testing the debug");

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

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
	public void render () {
		if (charts.getChart(selectedSongIndex).getBackgroundColour() != null) {
			ScreenUtils.clear(charts.getChart(selectedSongIndex).getBackgroundColour());
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
				drawCentredText(batch, pixelFont12, charts.getCurrentTask(), (float) Gdx.graphics.getHeight() / 2 - 150);
				break;
			case SONG_SELECT:
				if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && lastMenuAction + menuActionDelay < System.nanoTime()) {
					selectedSongIndex--;
					lastMenuAction = System.nanoTime();
					if(selectedSongIndex < 0) selectedSongIndex = charts.getChartCount() - 1;
				} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && lastMenuAction + menuActionDelay < System.nanoTime()) {
					selectedSongIndex++;
					lastMenuAction = System.nanoTime();
					if(selectedSongIndex >= charts.getChartCount()) selectedSongIndex = 0;
				}
				Chart selectedChart = charts.getChart(selectedSongIndex);

				// Draw album art
				batch.draw(selectedChart.getTexture(), 25, 300, 350, 350);

				drawCentredText(batch, pixelFont40, "Song Select", 690);

				// Draw difficulty backgrounds
				drawer.filledRectangle(25, 240, 60, 60, selectedChart.getDifficultyColour());
				drawer.filledRectangle(85, 240, 290, 60, Color.WHITE);

				// Draw difficulty text
				pixelFont40.draw(batch, selectedChart.getDifficultyString(), 100, 285);
				pixelFont40.draw(batch, String.valueOf(selectedChart.getDifficulty()), 25 + Game.getCentreTextOffset(pixelFont40, String.valueOf(selectedChart.getDifficulty())), 285);


				serifFont20.draw(batch, selectedChart.getName(), 30, 230);
				serifFont12.draw(batch, selectedChart.getSubtitle(), 30, 210);
				break;
		}
		batch.end();
	}

	/**
	 * Called when the application is destroyed
	 * Destroys all resources
	 */
	@Override
	public void dispose () {
		batch.dispose();
		logo.dispose();
		pixelFont20.dispose();
		pixelFont12.dispose();
		pixelFont40.dispose();
	}

	/**
	 * Returns the offset required to centre bitmap text
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
	 * @param batch The sprite batch to draw to
	 * @param font The font to use
	 * @param text The text to draw
	 * @param y The y position to draw the text at
	 */

	public static void drawCentredText(SpriteBatch batch, BitmapFont font, String text, float y) {
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, text);
		font.draw(batch, text, (float) Gdx.graphics.getWidth() /2 - layout.width / 2, y);
	}

	/**
	 * Update the game's state, drawing appropriate content to the screen
	 * @param state The new state to set
	 */
	public void setState(GameState state) {
		this.state = state;
	}
	public class Ref{
		public static enum LogTags{
			DEBUG("Debug"),
			ERROR("Error"),
			INFO("Info"),
			OTHER_SHIT("Other shit");

			public final String literal;

			private LogTags(String literal){
				this.literal = literal;
			}
		}
	}// is this meant to be an inner class
}

