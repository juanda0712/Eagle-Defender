package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;

public class MainController extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        VisUI.load();

        batch = new SpriteBatch();
        this.setScreen(new FormManagement(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
