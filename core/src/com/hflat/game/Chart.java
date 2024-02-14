package com.hflat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


/**
 * A class to represent a chart/level
 * Contains note information as well as info about the song
 * */
public class Chart {
    private final String name;
    private final String subtitle;
    private final String artist;
    private final String soundPath;
    private final String bgPath;
    private final String path;
    private final float offset;
    private final Texture banner;
    private final int difficulty;
    private final String difficultyString;
    private final String stepAuthor;
    private final float bpm;
    private final Color backgroundColour;
    private final Color difficultyColour;

    private ArrayList<Note> notes;

    /**
     * Constructor for chart object
     * */
    public Chart(String name, String subtitle, String artist, String soundPath, String bgPath, String path, int difficulty, String difficultyString, String stepAuthor, float bpm, float offset, String bannerPath) {
        if (bannerPath == null || bannerPath.equals("charts/")) bannerPath = "defaultbanner.png";

        this.name = name;
        this.subtitle = subtitle;
        this.artist = artist;
        this.difficulty = difficulty;
        this.soundPath = soundPath;
        this.bgPath = bgPath;
        this.path = path;
        this.difficultyString = difficultyString;
        this.stepAuthor = stepAuthor;
        this.bpm = bpm;
        this.offset = offset;
        this.notes = new ArrayList<>();
        this.banner = new Texture(bannerPath);
        this.backgroundColour = hashColor(name + artist);
        this.difficultyColour = hashColor(difficultyString.toLowerCase());
    }

    /**
     * Generates a parsed chart object from a file address object
     * @return A parsed Chart object
     * @param chart The File object of the chart file
     * */
    public static Chart parseChart(File chart) throws IOException {

        String file = Files.readString(chart.toPath(), Charset.defaultCharset());

        String[] fileLines = file.split(";", -1);

        String name = "";
        String subtitle = "";
        String artist = "";
        String soundPath = "";
        String path = chart.getPath();
        int difficulty = 0;
        float bpm = 0;
        float offset = 0;
        String difficultyString = "";
        String stepAuthor = "";
        String bgPath = "";
        String banner = "";

        for (String line : fileLines) {
            line = line.replace("\r", "");
            //Gdx.app.debug("Chart -bytes", Arrays.toString(line.getBytes(Charset.defaultCharset())));
            while (!line.startsWith("#")){
                try {
                    line = line.substring(1);
                } catch (StringIndexOutOfBoundsException e) {
                    break;
                }
            }
            if (line.startsWith("#TITLE:")) {
                name = line.substring(7);
            } else if (line.startsWith("#SUBTITLE:")) {
                subtitle = line.substring(10);
            } else if (line.startsWith("#ARTIST:")) {
                artist = line.substring(8);
            } else if (line.startsWith("#BANNER:")) {
                banner = "charts/" + line.substring(8);
            } else if (line.startsWith("#MUSIC:")) {
                soundPath = line.substring(7);
            } else if (line.startsWith("#OFFSET:")){
                offset = Float.parseFloat(line.substring(8));
            } else if (line.startsWith("#BACKGROUND:")){
                bgPath = line.substring(12);
            } else if (line.startsWith("#NOTES:")) {
                String[] rawNoteInfo = line.split(":", -1);

                stepAuthor = rawNoteInfo[2].trim();
                difficultyString = rawNoteInfo[3].trim();
                difficulty = Integer.parseInt(rawNoteInfo[4].trim());
                String noteData = rawNoteInfo[6];

                // Note parsing

                byte[] byteArray = toBytes(noteData.toCharArray());
                ArrayList<Byte> noteDataBytes = new ArrayList<>();
                for(byte b: byteArray){
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
                        while (i < noteDataBytes.size() && noteDataBytes.get(i) != 0x0A){ // delete characters until newline
                            noteDataBytes.remove(i);
                        }
                    }
                }

                // remove all newlines
                for (int i = 0; i < noteDataBytes.size(); i++) {
                    if (noteDataBytes.get(i) == 0x0A) {
                        noteDataBytes.remove(i);
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
                for(String bar : noteDataStringArray){
                    ArrayList<String> notes = new ArrayList<>();
                    if (bar.charAt(0) == 0x0A) { // remove newline
                        bar = bar.substring(1);
                    }
                    Gdx.app.debug("Chart -bar", bar);
//                    Gdx.app.debug("Chart", Arrays.toString(bar.getBytes()));
                    for(int i = 0; i < bar.length(); i += 4) { // split into groups of 4 chars
                        notes.add(bar.substring(i, i+4));
                    }
                    Gdx.app.debug("Chart -num notes", String.valueOf(notes.size()));
                    // Quantization
                    for (int i = 0; i < notes.size(); i++) {
                        float beat = (float) Math.round((float) Math.pow(10, 6) * ((float)i+1f)/(float)notes.size())/ (float) Math.pow(10, 6); // TODO: There is a problem where triplets are being rounded differently to the calculation in NoteDenom so they are not being recognized
                        Gdx.app.debug("Chart -q", String.valueOf(beat));
                        NoteDenom quantization = NoteDenom.fromLength(beat);
                        String quantizationString = (quantization == null) ? "null" : quantization.toString();
                        Gdx.app.debug("Chart -qs", quantizationString);
                    }
                }

            } else if (line.startsWith("#BPMS:")) {
                bpm = Float.parseFloat(line.split("=")[1].split(",")[0]);
            }
        }

        return new Chart(name, subtitle, artist, soundPath, bgPath, path, difficulty, difficultyString, stepAuthor, bpm, offset, banner);
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getArtist() {
        return artist;
    }


    public int getDifficulty() {
        return difficulty;
    }

    public String getPath() {
        return path;
    }

    public String getDifficultyString() {
        return difficultyString;
    }

    public String getStepAuthor() {
        return stepAuthor;
    }

    public float getBpm() {
        return bpm;
    }

    public Texture getTexture() {
        return banner;
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public Color getDifficultyColour() {
        return difficultyColour;
    }

    /**
     * Converts a string colour code into a colour object
     * @return A colour object
     * @param s A colour string
     * */
    private static Color hashColor(String s) {
        int hash = s.hashCode();
        float r = (hash & 0xFF0000) >> 16;
        float g = (hash & 0x00FF00) >> 8;
        float b = hash & 0x0000FF;
        return new Color(r / 255, g / 255, b / 255, 0.3f);
    }

    /**
     * Converts a character array into a byte array
     * @return An array of bytes
     * @param chars An array of characters
     * */
    private static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }
}
