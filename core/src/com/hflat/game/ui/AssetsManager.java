package com.hflat.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.hflat.game.note.Judgement;

import java.util.ArrayList;


// links
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-rendering-asset-manager/
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-scene2d/
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-project-setup/

public class AssetsManager {
    public final AssetManager manager = new AssetManager();

    public ArrayList<AssetLocation> assetsList = new ArrayList<>();

    // Fonts
    public BitmapFont pixelFont20;
    public BitmapFont pixelFont12;
    public BitmapFont pixelFont40;
    public BitmapFont pixelFont80;
    public BitmapFont serifFont20;
    public BitmapFont serifFont12;
    public BitmapFont marvellousFont;
    public BitmapFont fantasticFont;
    public BitmapFont excellentFont;
    public BitmapFont greatFont;
    public BitmapFont goodFont;
    public BitmapFont decentFont;
    public BitmapFont wayOffFont;
    public BitmapFont missFont;


    // Menu textures
    public final String defaultBanner = "defaultBanner.png";
    public final String hFlatLogo = "hFlatLogo.png";

    public class AssetLocation {
        public String address;

        public AssetLocation(String address){
            this.address = notes.concat(address);
            assetsList.add(this);
        }
    }

    // Note textures
    public final String notes = "notes/";
    public final AssetLocation holdEnd = new AssetLocation("hold-end.png");
    public final AssetLocation holdSegment = new AssetLocation("hold-segment.png");
    public final AssetLocation jackEnd = new AssetLocation("jack-end.png");
    public final AssetLocation jackSegment = new AssetLocation("jack-segment.png");
    public final AssetLocation mine = new AssetLocation("mine.png");
    public final AssetLocation fourthNote = new AssetLocation("note4th.png");
    public final AssetLocation eighthNote = new AssetLocation("note8th.png");
    public final AssetLocation twelfthNote = new AssetLocation("note12th.png");
    public final AssetLocation sixteenthNote = new AssetLocation("note16th.png");
    public final AssetLocation twentyFourthNote = new AssetLocation("note24th.png");
    public final AssetLocation thirtySecondNote = new AssetLocation("note32nd.png");
    public final AssetLocation fortyEighthNote = new AssetLocation("note48th.png");
    public final AssetLocation sixtyFourthNote = new AssetLocation("note64th.png");
    public final AssetLocation oneHundredTwentyEighthNote = new AssetLocation("note128th.png");
    public final AssetLocation oneHundredNinetySecondNote = new AssetLocation("note192nd.png");
    public final AssetLocation targetBeat = new AssetLocation("target-beat.png");
    public final AssetLocation targetPressed = new AssetLocation("target-pressed.png");
    public final AssetLocation targetUnpressed = new AssetLocation("target-unpressed.png");

    /**
     * Builds the fonts used in the game
     */
    private void buildFonts() {
        FreeTypeFontGenerator pixelGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/5x5.ttf"));
        FreeTypeFontGenerator serifGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Newsreader.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = 20;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);
        pixelFont20 = pixelGenerator.generateFont(parameter);
        serifFont20 = serifGenerator.generateFont(parameter);

        parameter.size = 12;
        pixelFont12 = pixelGenerator.generateFont(parameter);
        serifFont12 = serifGenerator.generateFont(parameter);

        parameter.size = 40;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        pixelFont40 = pixelGenerator.generateFont(parameter);

        parameter.size = 80;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        pixelFont80 = pixelGenerator.generateFont(parameter);

        parameter.size = 12;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        parameter.color = Judgement.MARVELLOUS.getColor();
        marvellousFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.FANTASTIC.getColor();
        fantasticFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.EXCELLENT.getColor();
        excellentFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.GREAT.getColor();
        greatFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.GOOD.getColor();
        goodFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.DECENT.getColor();
        decentFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.WAY_OFF.getColor();
        wayOffFont = pixelGenerator.generateFont(parameter);

        parameter.color = Judgement.MISS.getColor();
        missFont = pixelGenerator.generateFont(parameter);

        pixelGenerator.dispose();
        serifGenerator.dispose();
    }

    public void queueAdd() {
        manager.load(defaultBanner, Texture.class);
        manager.load(hFlatLogo, Texture.class);
        assetsList.forEach((AssetLocation asset) -> manager.load(asset.address, Texture.class));
        buildFonts();
    }
}
