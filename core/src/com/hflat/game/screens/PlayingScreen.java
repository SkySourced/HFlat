package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.hflat.game.HFlatGame;

import static com.hflat.game.HFlatGame.*;

public class PlayingScreen implements Screen {
    // Drawing utils
    HFlatGame parent;
    SpriteBatch playingBatch;
    // Counters
    private static float dontGiveUpTime = 0f;
    float escapeHeldDuration;
    // Fonts
    BitmapFont serifFont12 = HFlatGame.assMan.serifFont12;

    // Matrices
    Affine2 left;
    Affine2 up;
    Affine2 down;
    Affine2 right;

    Vector2 noteScale;

    public PlayingScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
        playingBatch = new SpriteBatch();
    }
    @Override
    public void show() {
        noteScale = new Vector2(options.getMini(), options.getMini());
        Affine2 left = new Affine2().preRotate(270).preScale(noteScale);
        Affine2 up = new Affine2().preRotate(0).preScale(noteScale);
        Affine2 down = new Affine2().preRotate(180).preScale(noteScale);
        Affine2 right = new Affine2().preRotate(90).preScale(noteScale);
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

        // Draw target arrows
        // if pressed draw pressed arrow
        // if beat happened within last 0.1 s draw beat arrow
        // else draw normal arrow

        playingBatch.draw(assMan.manager.get(assMan.targetPressed.address), 100, 650, left);
        playingBatch.draw(assMan.manager.get(assMan.targetPressed.address), 150, 650, up);

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
