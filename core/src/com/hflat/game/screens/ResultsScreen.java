package com.hflat.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;

public class ResultsScreen implements Screen, IHasStaticState {
    HFlatGame parent;
    SpriteBatch resultsBatch;

    public static final HFlatGame.GameState state = HFlatGame.GameState.RESULTS;
    public ResultsScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
        resultsBatch = new SpriteBatch();
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

    @Override
    public HFlatGame.GameState getState() {
        return state;
    }
}
