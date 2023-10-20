package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.models.User2;

public class SelectMode implements Screen {
    private final MainController game;
    private final Stage stage;
    private TextButton button1;
    private TextButton button2;
    private TextButton button3;
    private TextButton button4;
    private JSONDataManager<User2> user2Manager;
    private User2 user;

    //CountersBarriers counters = new CountersBarriers();
    //game.changeScreen(new GameScreen(game, user2Manager, user,counters));

    public SelectMode(final MainController game) {
        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        setupUIElements();
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();

        button1 = new TextButton("Button 1", skin);
        button1.setPosition(100, 300);
        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new IAMode(game));
            }
        });

        button2 = new TextButton("Button 2", skin);
        button2.setPosition(100, 200);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //

                //game.changeScreen(new GameScreen(game, user2Manager, user,counters));
            }
        });

        button3 = new TextButton("Button 3", skin);
        button3.setPosition(100, 100);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        button4 = new TextButton("Button 4", skin);
        button4.setPosition(100, 0);
        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(button4);
    }

    @Override
    public void render(float delta) {
        Color backgroundColor = new Color(0.96f, 0.96f, 0.86f, 1);
        ScreenUtils.clear(backgroundColor);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}