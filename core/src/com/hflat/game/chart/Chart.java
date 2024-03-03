package com.hflat.game.chart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.hflat.game.note.Lane;
import com.hflat.game.note.Note;
import com.hflat.game.note.NoteDenom;
import com.hflat.game.note.NoteType;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.hflat.game.HFlatGame.Ref.DENOM_ROUND_PLACES;

/**
 * A class to represent a chart/level
 * Contains note information as well as info about the song
 */
public class Chart {

    private Song song;
    private final int difficulty;
    private final String difficultyString;
    private final String stepAuthor;
    private final Color difficultyColour;

    private ArrayList<Note> notes;

    /**
     * Constructor for chart object
     */
    public Chart(int difficulty, String difficultyString, String stepAuthor, ArrayList<Note> notes) {
        this.difficulty = difficulty;
        this.difficultyString = difficultyString;
        this.stepAuthor = stepAuthor;
        this.notes = notes;
        this.difficultyColour = hashColor(difficultyString.toLowerCase());
    }

    /**
     * Generates a parsed chart object from a file address object
     *
     * @param data the string from the sm file with data about the notes
     * @return A parsed Chart object
     */

    public static Chart parseChart(String data, float bpm) {
        int notesAdded = 0;
        String[] rawNoteInfo = data.split(":", -1);
        if (!Objects.equals(rawNoteInfo[1].trim(), "dance-single")) return null; // only parse one panel ddr charts

        String stepAuthor = rawNoteInfo[2].trim();
        String difficultyString = rawNoteInfo[3].trim();
        int difficulty = Integer.parseInt(rawNoteInfo[4].trim());
        String noteData = rawNoteInfo[6];

        ArrayList<Note> notes = new ArrayList<>();

        // Note parsing

        byte[] byteArray = toBytes(noteData.toCharArray());
        ArrayList<Byte> noteDataBytes = new ArrayList<>();
        for (byte b : byteArray) {
            if (b != 0x20) noteDataBytes.add(b); // remove spaces
        }
        //That looks pretty good
        //Apart from the nulls
                /*
                 char - hex - dec
                    / - 2F  - 47
                    M - 4D  - 77
                    0 - 30  - 48
                    1 - 31  - 49
                    2 - 32  - 50
                    3 - 33  - 51
                    , - 2C  - 44
                space - 20  - 32

                    remove spaces (0x20)
                    remove bar comments
                        if character is / (0x2F)
                            delete characters until newline (0x0A)
                    remove all newlines
                    split by commas (0x2C)
                    theoretically then should just have a list of segments of chars 4n long
                    split into groups of 4 chars
                    figure out quantization
                 */

        for (int i = 0; i < noteDataBytes.size(); i++) {
            if (noteDataBytes.get(i) == 0x2F) { // if character is /
                while (i < noteDataBytes.size() && noteDataBytes.get(i) != 0x0A) { // delete characters until newline
                    noteDataBytes.remove(i);
                }
            }
        }

        // remove all newlines
        for (int i = 0; i < noteDataBytes.size(); i++) {
            if (noteDataBytes.get(i) == 0x0A) {
                noteDataBytes.remove(i);
                break;
            }
        }
        // join the bytes into a string (string builder discards \n (actually i dont think it does))
        StringBuilder noteDataString = new StringBuilder();
        for (byte b : noteDataBytes) {
            noteDataString.append((char) b);
        }

        // split by commas
        String[] noteDataStringArray = noteDataString.toString().split(",");

        // split bars into groups of 4 chars
        for (int barIndex = 0; barIndex < noteDataStringArray.length; barIndex++) {
            ArrayList<String> rawNotes = new ArrayList<>();
            if (noteDataStringArray[barIndex].charAt(0) == 0x0A) { // remove newline
                noteDataStringArray[barIndex] = noteDataStringArray[barIndex].substring(1);
            }
            Gdx.app.debug("Chart -bar", noteDataStringArray[barIndex]);
            //Gdx.app.debug("Chart", Arrays.toString(noteDataStringArray[barIndex].getBytes()));
            for (int i = 0; i < noteDataStringArray[barIndex].length(); i += 4) { // split into groups of 4 chars
                rawNotes.add(noteDataStringArray[barIndex].substring(i, i + 4));
            }
            //Gdx.app.debug("Chart -num notes", String.valueOf(rawNotes.size()));
            // Quantization
            for (int i = 0; i < rawNotes.size(); i++) {
                float beat = (float) Math.round((float) Math.pow(10, DENOM_ROUND_PLACES) * ((float) i + 1f) / (float) rawNotes.size()) / (float) Math.pow(10, DENOM_ROUND_PLACES);
                Gdx.app.debug("Chart -q", String.valueOf(beat));
                NoteDenom quantization = NoteDenom.fromLength(beat);
                String quantizationString = (quantization == null) ? "null" : quantization.toString();
                Gdx.app.debug("Chart -qs", quantizationString);
                for (int noteId = 0; noteId < 4; noteId++) {
                    if (rawNotes.get(i).charAt(noteId) != '0') {
                        //Gdx.app.debug("Chart -note", "Lane: " + i + ", Beat: " + beat + ", BPM: " + bpm + ", Type: " + rawNotes.get(i).charAt(noteId) + ", Quantization: " + quantizationString);
                        notes.add(new Note(
                                notesAdded,
                                Lane.fromInt(noteId),
                                barIndex + beat,
                                (int) bpm,
                                NoteType.fromChar(rawNotes.get(i).charAt(noteId)),
                                quantization
                        ));
                        notesAdded++;
                    }
                }
            }
        }
        return new Chart(difficulty, difficultyString, stepAuthor, notes);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getDifficultyString() {
        return difficultyString;
    }

    public String getStepAuthor() {
        return stepAuthor;
    }

    public Color getDifficultyColour() {
        return difficultyColour;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public Song getSong() {
        return song;
    }

    public Chart setSong(Song song) {
        return this;
    }

    /**
     * Converts a string colour code into a colour object
     *
     * @param s A colour string
     * @return A colour object
     */
    public static Color hashColor(String s) {
        if (s.length() < 5) s = s + s;
        int hash = String.valueOf(s.hashCode()).hashCode() & 0xFFFFFF;
        float r = (hash & 0xFF0000) >> 16;
        float g = (hash & 0x00FF00) >> 8;
        float b = hash & 0x0000FF;
        return new Color(r / 255, g / 255, b / 255, 0.7f);
    }

    /**
     * Converts a character array into a byte array
     *
     * @param chars An array of characters
     * @return An array of bytes
     */
    private static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }
}
