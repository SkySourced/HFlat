package com.hflat.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.hflat.game.HFlatGame;

public interface IHasStaticState extends Screen {
    public HFlatGame.GameState getState();
}
