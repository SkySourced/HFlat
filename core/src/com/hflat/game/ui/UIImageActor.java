package com.hflat.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class UIImageActor extends Actor {
    private Texture texture;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean showing;

    public UIImageActor(String path, float x, float y, float width, float height, boolean showing) {
        this.texture = new Texture(path);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.showing = showing;
    }

    public UIImageActor(Texture texture, float x, float y, float width, float height, boolean showing) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.showing = showing;
    }

    public UIImageActor(String path, float x, float y, boolean showing) {
        this.texture = new Texture(path);
        this.x = x;
        this.y = y;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.showing = showing;
    }

    public UIImageActor(Texture texture, float x, float y, boolean showing) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.showing = showing;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, x, y, width, height);
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public boolean isShowing() {
        return showing;
    }
}
