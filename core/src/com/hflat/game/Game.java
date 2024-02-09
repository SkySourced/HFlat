package com.hflat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	GameState state = GameState.LOADING;
	SpriteBatch batch;
	Texture logo;
	BitmapFont pixelFont;

	@Override
	public void create () {
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
		pixelFont.draw(batch, "Uses unlicensed assets!", 50, (float) Gdx.graphics.getHeight() /2 - 50);
		pixelFont.draw(batch, "Loading...", 140, (float) Gdx.graphics.getHeight() /2 - 80);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		logo.dispose();
		pixelFont.dispose();
	}
}
