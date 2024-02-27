package com.hflat.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;

public class ResultsScreen implements Screen {
    HFlatGame parent;
    SpriteBatch resultsBatch = new SpriteBatch();
    public ResultsScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        resultsBatch.setProjectionMatrix(parent.getCamera().combined);

        resultsBatch.begin();
        resultsBatch.end();
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
        resultsBatch.dispose();
    }
}
