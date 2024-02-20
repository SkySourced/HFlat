package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;

import static com.hflat.game.HFlatGame.drawCentredText;

public class LoadingScreen implements Screen {
    HFlatGame parent;
    SpriteBatch loadingBatch = new SpriteBatch();
    public LoadingScreen(HFlatGame parent) {
        this.parent = parent;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        loadingBatch.begin();

        //loadingBatch.draw(logo, (float) Gdx.graphics.getWidth() / 2 - (float) logo.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) logo.getHeight() / 2 + 70);
        //drawCentredText(loadingBatch, pixelFont20, "Uses unlicensed assets!", (float) Gdx.graphics.getHeight() / 2 - 100);
        //drawCentredText(loadingBatch, pixelFont12, this.parent.getSongManager().getCurrentTask(), (float) Gdx.graphics.getHeight() / 2 - 150);

        loadingBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
