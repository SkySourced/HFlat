package com.hflat.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;


// links
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-rendering-asset-manager/
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-scene2d/
// https://www.gamedevelopment.blog/full-libgdx-game-tutorial-project-setup/

public class AssetsManager {
    public final AssetManager manager = new AssetManager();

    // Fonts
    FreeTypeFontGenerator pixelGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/5x5.ttf"));
    FreeTypeFontGenerator serifGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Newsreader.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    // Menu textures
    public final String defaultBanner = "defaultBanner.png";
    public final String hFlatLogo = "hFlatLogo.png";

    // Note textures
    public final String holdEnd = "hold-end.png";
    public final String holdSegment = "hold-segment.png";
    public final String jackEnd = "jack-end.png";
    public final String jackSegment = "jack-segment.png";
    public final String mine = "mine.png";
    public final String fourthNote = "note4th.png";
    public final String eighthNote = "note8th.png";
    public final String twelfthNote = "note12th.png";
    public final String sixteenthNote = "note16th.png";
    public final String twentyFourthNote = "note24th.png";
    public final String thirtySecondNote = "note32nd.png";
    public final String fortyEighthNote = "note48th.png";
    public final String sixtyFourthNote = "note64th.png";
    public final String oneHundredTwentyEighthNote = "note128th.png";
    public final String oneHundredNinetySecondNote = "note192nd.png";
    public final String targetBeat = "target-beat.png";
    public final String targetPressed = "target-pressed.png";
    public final String targetUnpressed = "target-unpressed.png";

    // Fonts


    public void queueAddMenuTextures() {
        manager.setLoader(BitmapFont.class, new FreetypeFontLoader(manager.getFileHandleResolver()));

        parameter.color = Color.BLACK;
        parameter.size = 20;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = new Color(0, 0, 0, 0.75f);
        manager.load("fonts/5x5.ttf", BitmapFont.class, new FreeTypeFontLoaderParameter());

        parameter.size = 12;
        BitmapFont pixelFont12 = pixelGenerator.generateFont(parameter);



        manager.load(hFlatLogo, Texture.class);
    }

    public void queueAddFonts() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.BLACK;
        parameter.size = 40;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        BitmapFont pixelFont40 = pixelGenerator.generateFont(parameter);

        parameter.size = 20;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        BitmapFont serifFont20 = serifGenerator.generateFont(parameter);

        parameter.size = 12;
        BitmapFont serifFont12 = serifGenerator.generateFont(parameter);
    }

    public void queueAddBulkImages() {
        manager.load(defaultBanner, Texture.class);
        manager.load(holdEnd, Texture.class);
        manager.load(holdSegment, Texture.class);
        manager.load(jackEnd, Texture.class);
        manager.load(jackSegment, Texture.class);
        manager.load(mine, Texture.class);
        manager.load(fourthNote, Texture.class);
        manager.load(eighthNote, Texture.class);
        manager.load(twelfthNote, Texture.class);
        manager.load(sixteenthNote, Texture.class);
        manager.load(twentyFourthNote, Texture.class);
        manager.load(thirtySecondNote, Texture.class);
        manager.load(fortyEighthNote, Texture.class);
        manager.load(sixtyFourthNote, Texture.class);
        manager.load(oneHundredTwentyEighthNote, Texture.class);
        manager.load(oneHundredNinetySecondNote, Texture.class);
        manager.load(targetBeat, Texture.class);
        manager.load(targetPressed, Texture.class);
        manager.load(targetUnpressed, Texture.class);
    }

}
