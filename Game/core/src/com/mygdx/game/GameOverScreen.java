package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;

import java.awt.*;

public class GameOverScreen implements Screen {
    private Stage stage;
    private MainController game;
    private Skin skin;

    private Label ganador;
    private JSONDataManager<User2> user2Manager;

    public GameOverScreen(MainController game, JSONDataManager<User2> user2Manager, User2 user, User2 user2,float puntos1, float puntos2) {
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

        if(puntos1 != 0 && puntos2 !=0) {
            ganador = new Label("", skin);
            ganador.setColor(Color.BLACK);
            ganador.setPosition(530, 450);
            if (puntos1 > puntos2){
                ganador = new Label(user.getUsername() +" defeated "+ user2.getUsername(), skin);
                ganador.setPosition(530, 450);
                stage.addActor(ganador);
            }
            if (puntos2 > puntos1){
                ganador = new Label(user2.getUsername() +" defeated "+ user.getUsername(), skin);
                ganador.setPosition(530, 450);
                stage.addActor(ganador);
            }
        }else{
            System.out.println("cool");
        }

        TextButton top10= new TextButton("Top 10", skin);
        top10.setSize (200,60);
        top10.setPosition(850, 140);
        top10.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.changeScreen(new Top10Screen(game,user2Manager, user,user2));
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

