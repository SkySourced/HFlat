package com.hflat.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import com.hflat.game.HFlatGame.GameState;

import static com.hflat.game.HFlatGame.drawCentredText;

public class LoadingScreen extends ScreenAdapter {
    HFlatGame parent;
    SpriteBatch loadingBatch = new SpriteBatch();
    Texture logo;
    BitmapFont pixelFont20 = HFlatGame.assMan.pixelFont20;
    BitmapFont pixelFont12 = HFlatGame.assMan.pixelFont12;

    public static final GameState state = HFlatGame.GameState.LOADING;

    public LoadingScreen(HFlatGame parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        super.show();
        logo = HFlatGame.assMan.manager.get(HFlatGame.assMan.hFlatLogo);
    }

    @Override
    public void render(float delta) {
        loadingBatch.setProjectionMatrix(parent.getCamera().combined);

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        loadingBatch.begin();

        loadingBatch.draw(logo, (float) Gdx.graphics.getWidth() / 2 - (float) logo.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - (float) logo.getHeight() / 2 + 70);
        drawCentredText(loadingBatch, pixelFont20, "Uses unlicensed assets!", (float) Gdx.graphics.getHeight() / 2 - 100);
        drawCentredText(loadingBatch, pixelFont12, this.parent.getSongManager().getCurrentTask(), (float) Gdx.graphics.getHeight() / 2 - 150);

        loadingBatch.end();
    }

    @Override
    public void pause() {

    }

}
