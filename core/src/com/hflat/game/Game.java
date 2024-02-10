package com.hflat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class Game extends ApplicationAdapter {
	private GameState state = GameState.LOADING;
	public ChartManager charts;
	private SpriteBatch batch;
	private Texture logo;
	private BitmapFont pixelFont20;
	private BitmapFont pixelFont12;
	private BitmapFont pixelFont40;
	private BitmapFont serifFont20;
	private int selectedSongIndex = 0;

	@Override
	public void create () {
		charts = new ChartManager(new File("charts"), this);

        batch = new SpriteBatch();
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

		pixelGenerator.dispose();
		serifGenerator.dispose();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		switch (state) {
			case LOADING:
				batch.draw(logo, (float) Gdx.graphics.getWidth() / 2 - (float) logo.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) logo.getHeight() / 2 + 70);
				drawCentredText(batch, pixelFont20, "Uses unlicensed assets!", (float) Gdx.graphics.getHeight() / 2 - 100);
				drawCentredText(batch, pixelFont12, charts.getCurrentTask(), (float) Gdx.graphics.getHeight() / 2 - 150);
				break;
			case SONG_SELECT:
				if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
					selectedSongIndex--;
					if(selectedSongIndex < 0) selectedSongIndex = charts.getChartCount() - 1;
				} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
					selectedSongIndex++;
					if(selectedSongIndex >= charts.getChartCount()) selectedSongIndex = 0;
				}
				Chart selectedChart = charts.getChart(selectedSongIndex);
				drawCentredText(batch, pixelFont40, "Song Select", (float) Gdx.graphics.getHeight() - 30);
				batch.draw(selectedChart.getTexture(), 25, 630, 350, 350);
				serifFont20.draw(batch, selectedChart.getName(), 20, 550);
				break;
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		logo.dispose();
		pixelFont20.dispose();
		pixelFont12.dispose();
		pixelFont40.dispose();
	}

	public static float getCentreTextOffset(BitmapFont font, String text) {
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, text);
		return layout.width / 2;
	}

	public static void drawCentredText(SpriteBatch batch, BitmapFont font, String text, float y) {
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, text);
		font.draw(batch, text, (float) Gdx.graphics.getWidth() /2 - layout.width / 2, y);
	}
	public void setState(GameState state) {
		this.state = state;
	}
}
