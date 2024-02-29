package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.hflat.game.HFlatGame.*;

public class SongLoadingScreen implements Screen {
    SpriteBatch songLoadingBatch;
    ShapeDrawer drawer;

    HFlatGame parent;

    public SongLoadingScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
        songLoadingBatch = new SpriteBatch();
        drawer = new ShapeDrawer(songLoadingBatch, textureRegion);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        songLoadingBatch.setProjectionMatrix(parent.getCamera().combined);

        long loadingTime = 3000;
        float progress = (System.nanoTime() - parent.loadingStartTime) / (float) (loadingTime * Math.pow(10, 6));

        songLoadingBatch.begin();

        drawer.filledRectangle(0, 0, 400, 700, new Color(currentChart.getDifficultyColour().r, currentChart.getDifficultyColour().g, currentChart.getDifficultyColour().b, 0.5f));
        drawer.filledRectangle(0, 300, 400, 100, Color.DARK_GRAY);
        drawer.filledRectangle(0, 300, progress * 400, 100, Color.GRAY);

        drawCentredText(songLoadingBatch, assMan.serifFont20, currentSong.getName(), 670);
        drawCentredText(songLoadingBatch, assMan.pixelFont20, "press  enter  for  options", 360);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            parent.setState(HFlatGame.GameState.OPTIONS);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            parent.setState(HFlatGame.GameState.SONG_SELECT);
        }
        if (progress >= 1) {
            parent.setState(HFlatGame.GameState.PLAYING);
        }
        songLoadingBatch.end();
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
        songLoadingBatch.dispose();
    }
}
