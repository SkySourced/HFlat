package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import com.hflat.game.note.Judgement;
import com.hflat.game.note.Lane;
import com.hflat.game.note.Note;

import static com.hflat.game.HFlatGame.*;

public class PlayingScreen implements Screen {
    // Drawing utils
    HFlatGame parent;
    SpriteBatch playingBatch;

    // Counters
    private static float dontGiveUpTime = 0f; // time to show 'Don't give up!' message
    float escapeHeldDuration; // time ESC has been held

    float rawScore = 0;
    int combo = 0;
    float scorePercentage = 0;

    // Fonts
    BitmapFont serifFont12 = HFlatGame.assMan.serifFont12;

    // Keys pressed
    boolean leftPressed;
    boolean upPressed;
    boolean downPressed;
    boolean rightPressed;

    boolean beatTick;

    float gameTimeBars = -3f * currentSong.getBpm() / 60 / 4;
    double gameTimeNanos = -3 * Math.pow(10, 9);

    public PlayingScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
        playingBatch = new SpriteBatch();
    }
    @Override
    public void show() {
        escapeHeldDuration = 0;
    }

    @Override
    public void render(float delta) {
        // Update times
        gameTimeNanos += delta * Math.pow(10, 9);
        gameTimeBars += delta * currentSong.getBpm() * options.getMusicRate() / 60 / 4;

        // Update scores - this probably doesn't need to happen every frame
        scorePercentage = rawScore / Judgement.MARVELLOUS.getScore() * currentChart.getNotes().size();

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

        leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        beatTick = (Math.abs(gameTimeBars) * 4 % 1 <= 0.1);

        // I love ternary operators, but even I think this is too much
        Note.drawNote(assMan.manager.get(leftPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.LEFT, playingBatch);
        Note.drawNote(assMan.manager.get(downPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.DOWN, playingBatch);
        Note.drawNote(assMan.manager.get(upPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.UP, playingBatch);
        Note.drawNote(assMan.manager.get(rightPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.RIGHT, playingBatch);


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
