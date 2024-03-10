package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import com.hflat.game.chart.Play;
import com.hflat.game.note.Lane;
import com.hflat.game.note.Note;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.text.DecimalFormat;
import java.util.Arrays;

import static com.hflat.game.HFlatGame.*;

public class PlayingScreen implements Screen, IHasStaticState {

    // Drawing utils
    HFlatGame parent;
    SpriteBatch playingBatch;
    DecimalFormat scoreFormatter = new DecimalFormat("00.00");
    ShapeDrawer drawer;
    Play play;

    // Music
    Music track;
    float volume = 0.5f;
    boolean started = false;

    int rainbowCycle = 0;
    Color rainbowColor;

    // Counters
    private static float dontGiveUpTime = 0f; // time to show 'Don't give up!' message
    float escapeHeldDuration; // time ESC has been held
    long lastDraw = System.nanoTime();

    // Fonts
    BitmapFont serifFont12 = assMan.serifFont12;
    BitmapFont pixelFont20 = assMan.pixelFont20;
    BitmapFont[] judgementFonts12 = {assMan.marvellousFont12, assMan.fantasticFont12, assMan.excellentFont12, assMan.greatFont12, assMan.okFont12, assMan.decentFont12, assMan.wayOffFont12, assMan.missFont12};

    // Keys pressed
    boolean leftPressed;
    boolean upPressed;
    boolean downPressed;
    boolean rightPressed;
    boolean beatTick;


    public static final GameState state = GameState.PLAYING;

    public PlayingScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
        playingBatch = new SpriteBatch();
        drawer = new ShapeDrawer(playingBatch, textureRegion);
    }
    @Override
    public void show() {
//        escapeHeldDuration = 0;
        IngameInput input = new IngameInput();
        Gdx.input.setInputProcessor(input);
        play = parent.getCurrentPlay();
        String songPath = play.getChart().getSong().getPath();
        String songDirectory = String.join("/", Arrays.copyOfRange(songPath.split("\\\\"), 0, songPath.split("\\\\").length-1));
        String audioPath = songDirectory + "/" + play.getChart().getSong().getSoundPath();
        track = Gdx.audio.newMusic(Gdx.files.internal(audioPath));
    }

    @Override
    public void render(float delta) {
        if (!track.isPlaying() && play.getGameTimeNanos() > 0 && !started) {
            track.play();
            started = true;
        }

        // Update times
        play.update(delta);
        rainbowColor = new Color(java.awt.Color.HSBtoRGB(rainbowCycle, 50, 50));
        rainbowCycle += delta * 10;

        if (lastDraw - System.nanoTime() > 1/Ref.MAX_FRAMES) return;

        playingBatch.setProjectionMatrix(parent.getCamera().combined);

        playingBatch.begin();

        drawer.filledRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(0, 0, 0, options.getBackgroundFilter().getOpacity()));

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            dontGiveUpTime = 0;
            escapeHeldDuration += Gdx.graphics.getDeltaTime();
            drawCentredText(playingBatch, serifFont12, "Hold ESC to quit", 150);
            // seconds
            int escapeHeldThreshold = 1;
            if (escapeHeldDuration > escapeHeldThreshold){
                track.stop();
                parent.setState(GameState.RESULTS);
            }
        } else {
            if (escapeHeldDuration > 0) dontGiveUpTime = 1.5f;
            escapeHeldDuration = 0;
        }

        if (dontGiveUpTime > 0){
            dontGiveUpTime -= Gdx.graphics.getDeltaTime();
            drawCentredText(playingBatch, serifFont12, "Don't give up!", 150);
        }

        leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.D);
        upPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.J);
        downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.F);
        rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.K);
        beatTick = (Math.abs(play.getGameTimeBars()) * 4 % 1 >= 0.95);

        // I love ternary operators, but even I think this is too much
        Note.drawNote(assMan.manager.get(leftPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.LEFT, playingBatch);
        Note.drawNote(assMan.manager.get(downPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.DOWN, playingBatch);
        Note.drawNote(assMan.manager.get(upPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.UP, playingBatch);
        Note.drawNote(assMan.manager.get(rightPressed ? assMan.targetPressed.address : beatTick ? assMan.targetBeat.address : assMan.targetUnpressed.address), Lane.RIGHT, playingBatch);

        assMan.pixelFont40.draw(playingBatch, scoreFormatter.format(play.getScorePercentage(true)), 30, 680);

        // Combo - could be upgraded later to add colour if all marvellous/fantastic/excellent
        if (play.getCombo() > 0) drawCentredText(playingBatch, pixelFont20, String.valueOf(play.getCombo()), 350);

        for (int i = 0; i < play.getJudgementScores().length; i++) {
            int score = play.getJudgementScores()[i];
            drawRightAlignedText(playingBatch, judgementFonts12[i], String.valueOf(score), i > 3 ? 380 : 320, 690 - 12 * (i % 4));
        }

        drawer.rectangle(15, 100, 30, 300, Color.BLACK);
        drawer.filledRectangle(15, 100, 30, 300 * parent.getCurrentPlay().getLifeMeter() / 100, rainbowColor);

        play.drawNotes(playingBatch);

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
        track.dispose();
    }

    private class IngameInput extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            if (keycode == Input.Keys.LEFT || keycode == Input.Keys.D) {
                play.judge(Lane.LEFT);
            }
            if (keycode == Input.Keys.UP || keycode == Input.Keys.F) {
                play.judge(Lane.UP);
            }
            if (keycode == Input.Keys.DOWN || keycode == Input.Keys.J) {
                play.judge(Lane.DOWN);
            }
            if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.K) {
                play.judge(Lane.RIGHT);
            }
            return false;
        }
    }

    @Override
    public GameState getState() {
        return state;
    }
}
