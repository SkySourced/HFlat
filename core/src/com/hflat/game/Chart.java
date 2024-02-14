package com.hflat.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    public static Chart parseChart(File chart) throws FileNotFoundException {
        Scanner fileReader = new Scanner(chart);
        StringBuilder file = new StringBuilder();
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            file.append(data);
        }
        fileReader.close();

        String[] fileLines = file.toString().split(";", -1);

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
            System.out.println(Arrays.toString(line.getBytes(Charset.defaultCharset())));
            while (!line.startsWith("#")){
                line = line.substring(1);
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

                for (int i = 0; i < rawNoteInfo.length; i++) rawNoteInfo[i] = rawNoteInfo[i].trim();

                stepAuthor = rawNoteInfo[2];
                difficultyString = rawNoteInfo[3];
                difficulty = Integer.parseInt(rawNoteInfo[4]);
                String noteData = rawNoteInfo[6];

                // Note parsing
                /*ArrayList<String> noteLines = new ArrayList<String>(List.of(noteData.split(",")));

                for(String block: noteLines){
                    ArrayList<String> lines = new ArrayList<String>(List.of(block.split("\n")));
                    for(String l : lines) System.out.println(l);
                }*/
                byte[] byteArray = toBytes(noteData.toCharArray());
                ArrayList<Byte> noteDataBytes = new ArrayList<Byte>();
                for(byte b: byteArray){
                    if (b != 0x20) noteDataBytes.add(b);
                }

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
                System.out.println("the array list is " + noteDataBytes.size() + " big");

                for (int i = 0; i < noteDataBytes.size(); i++) {
                    //System.out.println(noteDataBytes.get(i).toString());
                    if (noteDataBytes.get(i) == 0x2F) {
                        System.out.println("in slash loop");
                        while (i < noteDataBytes.size() && noteDataBytes.get(i) != 0x0A){
                            noteDataBytes.remove(i);
                        }
                    }
                }


            } else if (line.startsWith("#BPMS:")) {
                bpm = Float.parseFloat(line.split("=")[1]);
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

    private static Color hashColor(String s) {
        int hash = s.hashCode();
        float r = (hash & 0xFF0000) >> 16;
        float g = (hash & 0x00FF00) >> 8;
        float b = hash & 0x0000FF;
        return new Color(r / 255, g / 255, b / 255, 0.3f);
    }

    private static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }
}
