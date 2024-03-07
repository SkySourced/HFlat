package com.hflat.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import com.hflat.game.chart.Play;

import java.text.DecimalFormat;

import static com.hflat.game.HFlatGame.assMan;

public class ResultsScreen implements Screen, IHasStaticState {
    HFlatGame parent;
    SpriteBatch resultsBatch;
    DecimalFormat decimalFormatter = new DecimalFormat("0000");
    DecimalFormat scoreFormatter = new DecimalFormat("00.00");
    Play play;

    BitmapFont[] judgementFonts = {assMan.marvellousFont20, assMan.fantasticFont20, assMan.excellentFont20, assMan.greatFont20, assMan.okFont20, assMan.decentFont20, assMan.wayOffFont20, assMan.missFont20};
    String[] judgementNames = {"Marvellous", "Fantastic", "Excellent", "Great", "OK", "Decent", "Way Off", "Miss"};

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

        for (int i = 0; i < judgementFonts.length; i++){
            judgementFonts[i].draw(resultsBatch, String.format("%s %s", decimalFormatter.format(play.getScores()[i]), judgementNames[i]), 50, 230 - 20 * i);
        }

        // Draw song info


        // Draw score percentage

        assMan.pixelFont40.draw(resultsBatch, scoreFormatter.format(play.getScorePercentage()),50,275);
        HFlatGame.drawCentredText(resultsBatch, assMan.pixelFont20,"Press enter",50);



        // Letter grades

        //HFlatGame.assMan.marvellousFont20.draw(resultsBatch, String.format("%s Marvellous", decimalFormatter.format(play.getScores()[0])),50,45);
        //HFlatGame.assMan.fantasticFont20.draw(resultsBatch, String.format("%s Fantastic", decimalFormatter.format(play.getScores()[1])),60,45);
        //HFlatGame.assMan.excellentFont20.draw(resultsBatch, String.format("%s Excellent", decimalFormatter.format(play.getScores()[2])),70,45);
        //HFlatGame.assMan.greatFont20.draw(resultsBatch, String.format("%s Great", decimalFormatter.format(play.getScores()[3])),80,45);
        //HFlatGame.assMan.okFont20.draw(resultsBatch, String.format("%s Okay", decimalFormatter.format(play.getScores()[4])),90,45);
        //HFlatGame.assMan.decentFont20.draw(resultsBatch, String.format("%s Decent", decimalFormatter.format(play.getScores()[5])),100,45);
        //HFlatGame.assMan.wayOffFont20.draw(resultsBatch, String.format("%s Way Off", decimalFormatter.format(play.getScores()[6])),110,45);
        //HFlatGame.assMan.missFont20.draw(resultsBatch, String.format("%s Miss", decimalFormatter.format(play.getScores()[7])),120,45);




        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) parent.setState(HFlatGame.GameState.SONG_SELECT);

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
