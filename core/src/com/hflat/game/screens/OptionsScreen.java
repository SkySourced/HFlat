package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.hflat.game.HFlatGame.*;

public class OptionsScreen implements Screen {
    // Drawing utils
    HFlatGame parent;
    SpriteBatch optionsBatch;
    ShapeDrawer drawer;
    NumberFormat formatter = new DecimalFormat("0.00"); // This should be renamed, but I can't think of anything good
    // Counters
    int optionSelectionIndex = 0;
    long lastMenuAction = 0;

    public OptionsScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
        optionsBatch = new SpriteBatch();
        drawer = new ShapeDrawer(optionsBatch, textureRegion);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        parent.getCamera().update();
        optionsBatch.setProjectionMatrix(parent.getCamera().combined);

        optionsBatch.begin();

        drawCentredText(optionsBatch, assMan.pixelFont40, "Options", 690);
        drawer.filledRectangle(25, 25, 350, 625, new Color(0.7f, 0.7f, 0.7f, 0.7f));
        assMan.serifFont20.draw(optionsBatch, "Note speed", 40, 625);
        assMan.serifFont20.draw(optionsBatch, "Note size", 40, 575);
        assMan.serifFont20.draw(optionsBatch, "Show judgements", 40, 525);
        assMan.serifFont20.draw(optionsBatch, "Show combo", 40, 475);
        assMan.serifFont20.draw(optionsBatch, "Background filter", 40, 425);
        assMan.serifFont20.draw(optionsBatch, "Music speed", 40, 375);
        assMan.serifFont20.draw(optionsBatch, "Visual offset", 40, 325);

        int optionWidth;
        GlyphLayout layout = new GlyphLayout();

        switch (optionSelectionIndex) {
            case 6:
                layout.setText(assMan.serifFont20, formatter.format(options.getNoteSpeed())+"x");
                break;
            case 5:
                layout.setText(assMan.serifFont20, (int) (options.getMini() * 100) + "%");
                break;
            case 4:
                layout.setText(assMan.serifFont20, options.isShowJudgements() ? "On" : "Off");
                break;
            case 3:
                layout.setText(assMan.serifFont20, options.isShowCombo() ? "On" : "Off");
                break;
            case 2:
                layout.setText(assMan.serifFont20, options.getBackgroundFilter().toString());
                break;
            case 1:
                layout.setText(assMan.serifFont20, formatter.format(options.getMusicRate())+"x");
                break;
            case 0:
                layout.setText(assMan.serifFont20, options.getVisualOffset() + "ms");
                break;
            case -1:
                layout.setText(assMan.pixelFont40, "play");
            default:
                break;
        }

        optionWidth = (int) layout.width;

        if (optionSelectionIndex >= 0) {
            drawer.rectangle(360 - optionWidth - 7, 300 + 50 * optionSelectionIndex, optionWidth + 15, 30, Color.BLACK, 2);
        } else {
            drawer.rectangle(200 - ((float) optionWidth / 2) - 20, 30, optionWidth + 40, 60, Color.BLACK, 2);
        }

        drawRightAlignedText(optionsBatch, assMan.serifFont20, formatter.format(options.getNoteSpeed())+"x", 360, 625);
        drawRightAlignedText(optionsBatch, assMan.serifFont20, (int) (options.getMini() * 100) + "%", 360, 575);
        drawRightAlignedText(optionsBatch, assMan.serifFont20, options.isShowJudgements() ? "On" : "Off", 360, 525);
        drawRightAlignedText(optionsBatch, assMan.serifFont20, options.isShowCombo() ? "On" : "Off", 360, 475);
        drawRightAlignedText(optionsBatch, assMan.serifFont20, options.getBackgroundFilter().toString(), 360, 425);
        drawRightAlignedText(optionsBatch, assMan.serifFont20, formatter.format(options.getMusicRate())+"x", 360, 375);
        drawRightAlignedText(optionsBatch, assMan.serifFont20, options.getVisualOffset() + "ms", 360, 325);

        drawCentredText(optionsBatch, assMan.pixelFont40, "play", 75);

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && lastMenuAction + menuActionDelay < System.nanoTime()){
            optionSelectionIndex++;
            if (optionSelectionIndex > 6) optionSelectionIndex = -1;
            lastMenuAction = System.nanoTime();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && lastMenuAction + menuActionDelay < System.nanoTime()){
            optionSelectionIndex--;
            if (optionSelectionIndex < -1) optionSelectionIndex = 6;
            lastMenuAction = System.nanoTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && lastMenuAction + menuActionDelay < System.nanoTime()){
            lastMenuAction = System.nanoTime();
            switch (optionSelectionIndex) {
                case 6 -> {
                    options.setNoteSpeed(options.getNoteSpeed() - 0.05f);
                    if (options.getNoteSpeed() < 0.05f) options.setNoteSpeed(0.05f);
                }
                case 5 -> {
                    options.setMini(options.getMini() - 0.01f);
                    if (options.getMini() < 0.01f) options.setMini(0.01f);
                }
                case 4 -> options.setShowJudgements(!options.isShowJudgements());
                case 3 -> options.setShowCombo(!options.isShowCombo());
                case 2 -> options.setBackgroundFilter(options.getBackgroundFilter().previous());
                case 1 -> {
                    options.setMusicRate(options.getMusicRate() - 0.05f);
                    if (options.getMusicRate() < 0.05f) options.setMusicRate(0.05f);
                }
                case 0 -> options.setVisualOffset(options.getVisualOffset() - 1);
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && lastMenuAction + menuActionDelay < System.nanoTime()){
            switch (optionSelectionIndex) {
                case 6 -> {
                    options.setNoteSpeed(options.getNoteSpeed() + 0.05f);
                    if (options.getNoteSpeed() > 10.0f) options.setNoteSpeed(10.0f);
                }
                case 5 -> {
                    options.setMini(options.getMini() + 0.01f);
                    if (options.getMini() > 1f) options.setMini(1f);
                }
                case 4 -> options.setShowJudgements(!options.isShowJudgements());
                case 3 -> options.setShowCombo(!options.isShowCombo());
                case 2 -> options.setBackgroundFilter(options.getBackgroundFilter().next());
                case 1 -> {
                    options.setMusicRate(options.getMusicRate() + 0.05f);
                    if (options.getMusicRate() > 5f) options.setMusicRate(5f);
                }
                case 0 -> {
                    options.setVisualOffset(options.getVisualOffset() + 1);
                    if (options.getVisualOffset() > 1000) options.setVisualOffset(1000);
                }
            }
            lastMenuAction = System.nanoTime();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && lastMenuAction + menuActionDelay > System.nanoTime()){
            lastMenuAction = System.nanoTime();
            if (optionSelectionIndex == -1){
                parent.setState(HFlatGame.GameState.PLAYING);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            parent.setState(HFlatGame.GameState.SONG_SELECT);
        }

        optionsBatch.end();
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
        optionsBatch.dispose();
    }
}
