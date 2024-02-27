package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;

import static com.hflat.game.HFlatGame.drawCentredText;

public class PlayingScreen implements Screen {
    // Drawing utils
    HFlatGame parent;
    SpriteBatch playingBatch = new SpriteBatch();
    // Counters
    private static float dontGiveUpTime = 0f;
    float escapeHeldDuration;
    // Fonts
    BitmapFont serifFont12 = HFlatGame.assMan.serifFont12;

    public PlayingScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        playingBatch.setProjectionMatrix(parent.getCamera().combined);

        playingBatch.begin();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            dontGiveUpTime = 0;
            escapeHeldDuration += Gdx.graphics.getDeltaTime();
            drawCentredText(playingBatch, serifFont12, "Hold ESC to quit", 150);
            // seconds
            int escapeHeldThreshold = 2;
            if (escapeHeldDuration > escapeHeldThreshold){
                parent.setState(HFlatGame.GameState.SONG_SELECT);
            }
        } else {
            if (escapeHeldDuration > 0) dontGiveUpTime = 1.5f;
            escapeHeldDuration = 0;
        }

        if (dontGiveUpTime > 0){
            dontGiveUpTime -= Gdx.graphics.getDeltaTime();
            drawCentredText(playingBatch, serifFont12, "Don't give up!", 150);
        }
        playingBatch.end();
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
        playingBatch.dispose();
    }
}
