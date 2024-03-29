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

    public enum LetterGrade{
        QUINT(100,"quint"),
        QUAD(99,"quad"),
        TRIPLE(98,"triple"),
        DOUBLE(97,"double"),
        SINGLE(96,"single"),
        S_PLUS(94,"s-plus"),
        S(92,"s"),
        S_MINUS(90,"s-minus"),
        A_PLUS(89,"a-plus"),
        A(86,"a"),
        A_MINUS(83,"a-minus"),
        B_PLUS(76,"b-plus"),
        B(72,"b"),
        B_MINUS(68,"b-minus"),
        C_PLUS(64,"c-plus"),
        C(60,"c"),
        C_MINUS(55,"c-minus"),
        D(0,"d"),
        F(0,"f");

        final int minimumScore;
        final String assetPath;

        LetterGrade(int minimumScore, String assetPath){
            this.minimumScore = minimumScore;
            this.assetPath = String.format("grades/%s.png", assetPath);
        }

        public int getMinimumScore(){
            return this.minimumScore;
        }

        public String getGradeAsset(){
            return assetPath;
        }
        public static LetterGrade getGrade(float score, float life){
            if (life < 0) return F;
            if (score < 0) score = 0;
            for (LetterGrade lg : LetterGrade.values()){
                if (score >= lg.getMinimumScore()) return lg;
            }
            return F;
        }
    }

    ArrayList<PlayNote> notes = new ArrayList<>();
    ArrayList<PlayNote> remainingNotes = new ArrayList<>();
    Chart chart;
    HFlatGame parent;
    float rawScore = 0;
    int combo = 0;
    float scorePercentage = 0;
    float lifeMeter = 50f;
    int[] scores = new int[8];
    float gameTimeBars = (-3f + currentSong.getOffset()) * currentSong.getBpm() * options.getMusicRate() / 60 / 4;
    double gameTimeNanos = (-3 + currentSong.getOffset()) * Math.pow(10, 9);
    boolean isPlaying = false;

    public Play(Chart chart, HFlatGame parent) {
        this.parent = parent;
        this.chart = chart;
        Gdx.app.debug("Play-chart" , "Chart has " + chart.getNotes().size() + " notes");
        for (Note pn : chart.getNotes()) {
            notes.add(new PlayNote(pn.getId(), pn.getLane(), pn.getBarTime(), currentSong.getBpm(), pn.getType(), pn.getColour(), this));
        }
        notes.sort((o1, o2) -> (int) (o1.getTime() - o2.getTime())); // Probably not necessary
        remainingNotes.addAll(notes);
    }

    public void update(float delta) {
        gameTimeNanos += delta * Math.pow(10, 9);
        gameTimeBars += delta * currentSong.getBpm() * options.getMusicRate() / 60 / 4;
        scorePercentage = rawScore / (Judgement.MARVELLOUS.getScore() * chart.getNotes().size());

        if (remainingNotes.isEmpty()) {
            isPlaying = false;
            parent.setState(HFlatGame.GameState.SONG_SELECT);
            return;
        }

        if (lifeMeter <= 0) {
            parent.setState(HFlatGame.GameState.RESULTS);
            return;
        }

        // Calculate any missed notes

        while (remainingNotes.getFirst().getTime() / 1000f < gameTimeNanos / Math.pow(10, 9) - Judgement.WAY_OFF.getTimingWindow()) {
            Gdx.app.debug("Play", "Missed note " + remainingNotes.getFirst().getId() + " at " + remainingNotes.getFirst().getTime() + " (" + remainingNotes.getFirst().getTime() * Math.pow(10, 6) + ")");
            Gdx.app.debug("Play", "First remaining note time: " + remainingNotes.getFirst().getTime() + "ms, " + remainingNotes.getFirst().getBarTime() + " bars");
            Gdx.app.debug("Play", "Last remaining note time: " + remainingNotes.getLast().getTime() + "ms, " + remainingNotes.getLast().getBarTime() + " bars");
            Gdx.app.debug("Play", "Miss time: " + gameTimeNanos / Math.pow(10, 9) + " - " + Judgement.WAY_OFF.getTimingWindow());
            remainingNotes.getFirst().setJudgement(Judgement.MISS);
            scores[7]++;
            combo = 0;
            rawScore += Judgement.MISS.getScore();
            notes.get(remainingNotes.getFirst().getId()).setJudgement(Judgement.MISS);
            //lifeMeter += Judgement.MISS.getLifeImpact();
            remainingNotes.removeFirst();
            if (remainingNotes.isEmpty()) {
                isPlaying = false;
                parent.setState(HFlatGame.GameState.RESULTS);
                return;
            }
        }
    }

    public int[] getJudgementScores(){
        return scores;
    }

    public float getScorePercentage(boolean clampToZero){
        if (clampToZero && scorePercentage < 0) return 0f; else return scorePercentage;
    }

    public float getScorePercentage() {
        return getScorePercentage(false);
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
        //Gdx.app.debug("Play", "Judging lane " + lane + " at " + gameTimeNanos / Math.pow(10, 9));
        for (PlayNote pn : remainingNotes) {
            if (pn.getLane() == lane) {
                Judgement j = pn.judge((float) (gameTimeNanos / Math.pow(10, 9)));
                //Gdx.app.debug("Play", "Judged note " + pn.getId() + " with " + j + " at " + gameTimeNanos / Math.pow(10, 9) + " (" + j.getScore() + " points");
                if (j == null) break; // If the note is too early to judge, don't judge it
                notes.get(pn.getId()).setJudgement(j);
                scores[j.ordinal()]++;
                rawScore += j.getScore();
                lifeMeter += j.getLifeImpact();
                if (j.ordinal() < 4) {
                    combo++;
                }
                if (lifeMeter > 100) lifeMeter = 100;
                if (lifeMeter <= 0) parent.setState(HFlatGame.GameState.RESULTS);
                break;
            }
        }
    }

    public void drawNotes(SpriteBatch batch) {
        // Draw the notes
        for (PlayNote pn : remainingNotes) {
            int y = (int) ((gameTimeBars - pn.getBarTime()) * (options.getNoteSpeed() / options.getMusicRate() * HFlatGame.Ref.VERTICAL_ARROW_SCALAR * pn.getBpm()));
//            if (y < 0) break;
            //Gdx.app.debug("Note", "Drawing note at " + y + " (" + gameTimeBars + " - " + pn.getBarTime() + ")" + " (" + options.getNoteSpeed() / options.getMusicRate() * HFlatGame.Ref.VERTICAL_ARROW_SCALAR * pn.getBpm() + ")");
            if (y < 800) pn.drawNote(pn.getLane(), batch, y);
        }
    }

    public ArrayList<PlayNote> getNotes() {
        return notes;
    }

    public ArrayList<PlayNote> getRemainingNotes() {
        return remainingNotes;
    }

    public int[] getScores(){
        return this.scores;
    }

    public float getLifeMeter() {
        return lifeMeter;
    }

    public void setLifeMeter(float lifeMeter) {
        this.lifeMeter = lifeMeter;
    }

    public Chart getChart() {
        return chart;
    }
}
