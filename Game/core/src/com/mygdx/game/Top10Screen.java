package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.badlogic.gdx.Gdx.app;

public class Top10Screen implements Screen {
    private Stage stage;
    private MainController game;
    private Skin skin;

    public Top10Screen (MainController game, JSONDataManager<User2> user2Manager, User2 user, User2 user2) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        Image top10 = new Image(new Texture("GameOver/Top10.png"));
        top10.setPosition(0, 0);
        stage.addActor(top10);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setSize(200, 60);
        exitButton.setPosition(850, 50);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);

        TextButton backButton= new TextButton("Back", skin);
        backButton.setSize (400, 100);
        backButton.setPosition(850, 140);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(game,user2Manager,user2,user,0,0));
            }
        });
        stage.addActor(backButton);
    }



    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.4f, 0.1f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}