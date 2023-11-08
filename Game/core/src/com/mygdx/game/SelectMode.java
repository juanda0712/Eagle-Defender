package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.models.User2;
import com.mygdx.utils.SpotifyAuthenticator;

import java.util.concurrent.atomic.AtomicReference;

public class SelectMode implements Screen {
    private final MainController game;
    private final Stage stage;
    private TextButton button1;
    private TextButton button2;
    private TextButton button3;
    private TextButton button4;
    private JSONDataManager<User2> user2Manager;
    private User2 user;
    private final AtomicReference<SpotifyAuthenticator> spotifyReference = new AtomicReference<>(null);
    private Skin skin;

    //CountersBarriers counters = new CountersBarriers();
    //game.changeScreen(new GameScreen(game, user2Manager, user,counters));

    public SelectMode(final MainController game, final JSONDataManager<User2> user2Manager, User2 user) {
        skin = VisUI.getSkin();
        this.game = game;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.user2Manager = user2Manager;
        this.user = user;

        setupUIElements();
    }

    private void setupUIElements() {
        CountersBarriers countersBarriers = new CountersBarriers();
        initButtons();
        stage.addActor(button2);


        Image image = new Image(new Texture(Gdx.files.internal("helpp.png")));
        image.setVisible(false);
        Label label = new Label("Press any key to escape", skin);
        label.setVisible(false);
        button3 = new TextButton("HOW TO PLAY", skin);
        button3.setPosition((stage.getWidth() - button3.getWidth()) / 2, stage.getHeight() / 2 - 100);
        button3.setSize(200, 60);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                image.setVisible(true);
                button3.setVisible(false);
                button2.setVisible(false);
                float imageX = (stage.getWidth() - image.getWidth()) / 2;
                float imageY = (stage.getHeight() - image.getHeight()) / 2;
                image.setPosition(imageX, imageY);
                label.setVisible(true);
                float labelX = (stage.getWidth() - label.getWidth()) / 2;
                float labelY = imageY - label.getHeight() - 10;
                label.setPosition(labelX, labelY);
                label.setColor(Color.BLACK);
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        image.setVisible(false);
                        label.setVisible(false);
                        button3.setVisible(true);
                        button2.setVisible(true);
                        return true;
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        image.setVisible(false);
                        label.setVisible(false);
                        button3.setVisible(true);
                        button2.setVisible(true);
                        return true;
                    }
                });
            }
        });
        stage.addActor(image);
        stage.addActor(label);
        stage.addActor(button3);
        /*
        button4 = new TextButton("Button 4", skin);
        button4.setPosition((stage.getWidth() - button4.getWidth()) / 2, stage.getHeight() / 2 - 200);
        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Agrega la lógica para el botón 4
            }
        });*/

        //stage.addActor(button1);
        //stage.addActor(button2);
        /*stage.addActor(button3);
        stage.addActor(button4);*/
    }


    @Override
    public void render(float delta) {
        Color backgroundColor = new Color(0.96f, 0.96f, 0.86f, 1);
        ScreenUtils.clear(backgroundColor);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void initButtons() {
        button2 = new TextButton("Let's play", skin);
        button2.setPosition((stage.getWidth() - button2.getWidth()) / 2, stage.getHeight() / 2);
        button2.setSize(200, 60);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new LoginScreen(game, user2Manager, user, null));
                dispose();
            }
        });


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