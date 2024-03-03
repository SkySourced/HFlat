package com.hflat.game.chart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hflat.game.HFlatGame;
import com.hflat.game.note.Judgement;
import com.hflat.game.note.Lane;
import com.hflat.game.note.Note;
import com.hflat.game.note.PlayNote;

import java.util.ArrayList;

import static com.hflat.game.HFlatGame.currentSong;
import static com.hflat.game.HFlatGame.options;

public class Play {

    ArrayList<PlayNote> notes = new ArrayList<>();
    ArrayList<PlayNote> remainingNotes = new ArrayList<>();
    Chart chart;
    HFlatGame parent;
    float rawScore = 0;
    int combo = 0;
    float scorePercentage = 0;
    int[] scores = new int[8];
    float gameTimeBars = -3f * currentSong.getBpm() * options.getMusicRate() / 60 / 4;
    double gameTimeNanos = -3 - currentSong.getOffset() * Math.pow(10, 9);
    boolean isPlaying = false;

    public Play(Chart chart, HFlatGame parent) {
        this.parent = parent;
        this.chart = chart;
        Gdx.app.debug("Play-chart" , "Chart has " + chart.getNotes().size() + " notes");
        for (Note pn : chart.getNotes()) {
            notes.add(new PlayNote(pn.getId(), pn.getLane(), pn.getBarTime(), currentSong.getBpm(), pn.getType(), pn.getColour(), this));
        }
        remainingNotes.addAll(notes);
    }

    public void update(float delta) {
        gameTimeNanos += delta * Math.pow(10, 9);
        gameTimeBars += delta * currentSong.getBpm() * options.getMusicRate() / 60 / 4;
        scorePercentage = rawScore / Judgement.MARVELLOUS.getScore() * chart.getNotes().size();

        // Calculate any missed notes
        while (remainingNotes.get(0).time < gameTimeNanos / Math.pow(10, 9) - Judgement.WAY_OFF.getTimingWindow()) {
            Gdx.app.debug("Play", "Missed note " + remainingNotes.get(0).getId() + " at " + remainingNotes.get(0).time + " (" + remainingNotes.get(0).time * Math.pow(10, 9) + ")");
            remainingNotes.get(0).setJudgement(Judgement.MISS);
            scores[7]++;
            combo = 0;
            rawScore += Judgement.MISS.getScore();
            notes.get(remainingNotes.get(0).getId()).setJudgement(Judgement.MISS);
            remainingNotes.remove(0);
        }
    }

    public int[] getJudgementScores(){
        return scores;
    }

    public float getScorePercentage(){
        return scorePercentage;
    }

    public float getRawScore(){
        return rawScore;
    }

    public int getCombo(){
        return combo;
    }

    public float getGameTimeBars() {
        return gameTimeBars;
    }

    public double getGameTimeNanos() {
        return gameTimeNanos;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void judge(Lane lane) {
        Gdx.app.debug("Play", "Judging lane " + lane + " at " + gameTimeNanos / Math.pow(10, 9));
        for (PlayNote pn : remainingNotes) {
            if (pn.getLane() == lane) {
                Judgement j = pn.judge((float) (gameTimeNanos / Math.pow(10, 9)));
                if (j == null) break; // If the note is too early to judge, don't judge it
                notes.get(pn.getId()).setJudgement(j);
                scores[j.ordinal()]++;
                rawScore += j.getScore();
                if (j.ordinal() < 4) {
                    combo++;
                }
                break;
            }
        }
    }

    public void drawNotes(SpriteBatch batch) {
        // Draw the notes
        for (PlayNote pn : remainingNotes) {
            int y = (int) ((-pn.barTime + gameTimeBars) * (options.getNoteSpeed() * options.getMusicRate() * HFlatGame.Ref.VERTICAL_ARROW_SCALAR * pn.getBpm()));
            //Gdx.app.debug("Note", "Drawing note at " + y + " (" + gameTimeBars + " - " + pn.barTime + ")" + " (" + options.getNoteSpeed() * options.getMusicRate() * HFlatGame.Ref.VERTICAL_ARROW_SCALAR * pn.getBpm() + ")");
            if (y < 0) break;
            if (y < 800) Note.drawNote(pn.colour.getTexture(), pn.getLane(), batch, y);
        }

        if (remainingNotes.size() == 0) {
            isPlaying = false;
            parent.setState(HFlatGame.GameState.SONG_SELECT);
        }
    }

    public ArrayList<PlayNote> getNotes() {
        return notes;
    }

    public ArrayList<PlayNote> getRemainingNotes() {
        return remainingNotes;
    }
}