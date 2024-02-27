package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.hflat.game.HFlatGame.*;

public class SongSelectScreen implements Screen {
    // Drawing utils
    HFlatGame parent;
    SpriteBatch songSelectBatch = new SpriteBatch();
    private final ShapeDrawer drawer = new ShapeDrawer(songSelectBatch);
    // Counters
    int selectedSongIndex = 0;
    int selectedDifficultyIndex = 0;
    long lastMenuAction;
    // Fonts
    BitmapFont pixelFont40 = HFlatGame.assMan.pixelFont40;
    BitmapFont serifFont12 = HFlatGame.assMan.serifFont12;
    BitmapFont serifFont20 = HFlatGame.assMan.serifFont20;

    public SongSelectScreen(HFlatGame hFlatGame) {
        this.parent = hFlatGame;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        songSelectBatch.setProjectionMatrix(parent.getCamera().combined);

        songSelectBatch.begin();

        currentSong = parent.getSongManager().getSong(selectedSongIndex);
        currentChart = currentSong.getChart(selectedDifficultyIndex);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && lastMenuAction + menuActionDelay < System.nanoTime()) {
            selectedSongIndex--;
            lastMenuAction = System.nanoTime();
            if (selectedSongIndex < 0) selectedSongIndex = parent.getSongManager().getSongCount() - 1;
            selectedDifficultyIndex = Math.min(selectedDifficultyIndex, parent.getSongManager().getSong(selectedSongIndex).getChartCount() - 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && lastMenuAction + menuActionDelay < System.nanoTime()) {
            selectedSongIndex++;
            lastMenuAction = System.nanoTime();
            if (selectedSongIndex >= parent.getSongManager().getSongCount()) selectedSongIndex = 0;
            selectedDifficultyIndex = Math.min(selectedDifficultyIndex, parent.getSongManager().getSong(selectedSongIndex).getChartCount() - 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && lastMenuAction + menuActionDelay < System.nanoTime()) {
            selectedDifficultyIndex++;
            lastMenuAction = System.nanoTime();
            if (selectedDifficultyIndex >= currentSong.getChartCount()) selectedDifficultyIndex = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && lastMenuAction + menuActionDelay < System.nanoTime()) {
            selectedDifficultyIndex--;
            lastMenuAction = System.nanoTime();
            if (selectedDifficultyIndex < 0) selectedDifficultyIndex = currentSong.getChartCount() - 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            parent.setState(HFlatGame.GameState.SONG_LOADING);
        }

        // Draw album art
        songSelectBatch.draw(currentSong.getTexture(), 25, 300, 350, 350);

        HFlatGame.drawCentredText(songSelectBatch, HFlatGame.assMan.pixelFont40, "Song Select", 690);

        // Draw difficulty backgrounds
        for (int i = 0; i < 6; i++) {
            drawer.filledRectangle(25, 240 + 10 * i, 10, 10, Color.GRAY);
            Color difficultyColour;
            try {
                difficultyColour = currentSong.getChart(i, false).getDifficultyColour();
            } catch (NullPointerException e) {
                difficultyColour = Color.GRAY;
            }
            if (i == selectedDifficultyIndex) {
                drawer.filledRectangle(28, 243 + 10 * i, 4, 4, Color.WHITE);
            }
            drawer.filledRectangle(27, 242 + 10 * i, 6, 6, difficultyColour);
        }
        drawer.filledRectangle(35, 240, 60, 60, currentChart.getDifficultyColour());
        drawer.filledRectangle(95, 240, 280, 60, Color.WHITE);

        // Draw difficulty text
        pixelFont40.draw(songSelectBatch, currentChart.getDifficultyString(), 110, 285);
        int difficulty = currentChart.getDifficulty();
        pixelFont40.draw(songSelectBatch, String.valueOf(difficulty), String.valueOf(difficulty).length() == 1 ? 52 : 45, 285);


        serifFont20.draw(songSelectBatch, currentSong.getName(), 30, 230);
        serifFont12.draw(songSelectBatch, currentSong.getSubtitle(), 30, 210);
        serifFont20.draw(songSelectBatch, currentSong.getArtist(), 30, 190);

        songSelectBatch.end();
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
        songSelectBatch.dispose();
    }
}
