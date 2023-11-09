package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameOverScreen implements Screen {
    private Stage stage;
    private MainController game;
    private Skin skin;

    public GameOverScreen(MainController game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));


        Image gameover1 = new Image(new Texture("GameOver/Game Over P1.png"));
        gameover1.setPosition(0, 0);
        stage.addActor(gameover1);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setSize(300, 100);
        exitButton.setPosition(850, 50);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(exitButton);

        TextButton top10= new TextButton("Top 10", skin);
        top10.setSize (200,60);
        top10.setPosition(850, 140);
        top10.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.changeScreen(new Top10Screen(game));
                dispose();
            }
        });

        stage.addActor(top10);
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

