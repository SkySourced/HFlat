package com.hflat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
	GameState state = GameState.LOADING;
	SpriteBatch batch;
	Texture logo;
	BitmapFont pixelFont;

	@Override
	public void create () {
        try {
            Chart testChart = Chart.parseChart(new File("charts/Relative Fiction.sm"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        batch = new SpriteBatch();
		logo = new Texture("hflatlogo.png");

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/5x5.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.BLACK;
		parameter.size = 20;
		parameter.shadowOffsetX = 1;
		parameter.shadowOffsetY = 1;
		parameter.shadowColor = new Color(0, 0, 0, 0.75f);
		pixelFont = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(logo, (float) Gdx.graphics.getWidth() /2 - (float) logo.getWidth() /2, (float) Gdx.graphics.getHeight() /2 - (float) logo.getHeight() /2 + 70);
		drawCentredText(batch, pixelFont, "Uses unlicensed assets!", (float) Gdx.graphics.getHeight() /2 - 100);
		drawCentredText(batch, pixelFont, "Loading...", (float) Gdx.graphics.getHeight() /2 - 150);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		logo.dispose();
		pixelFont.dispose();
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
}
