package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import static com.badlogic.gdx.Gdx.app;

public class Top10Screen implements Screen {
    private Stage stage;
    private MainController game;
    private Skin skin;
    private JSONDataManager<User2> user2Manager;

    public Top10Screen(MainController game, JSONDataManager<User2> user2Manager, User2 user, User2 user2) {
        this.game = game;
        this.user2Manager = user2Manager;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        Image top10 = new Image(new Texture("GameOver/Top10.png"));
        top10.setPosition(0, 0);
        stage.addActor(top10);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setSize(200, 60);
        exitButton.setPosition(1000, 50);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);

        TextButton backButton = new TextButton("Back", skin);
        backButton.setSize(400, 100);
        backButton.setPosition(850, 50);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(game, user2Manager, user2, user, 0, 0));
            }
        });
        stage.addActor(backButton);

        Array<User2> userArray = user2Manager.read();
        List<User2> userList = new ArrayList<>();
        for (User2 currentUser : userArray) {
            userList.add(currentUser);
        }

        userList.sort(Comparator.comparing(User2::getPoints).reversed());

        for (int i = 0; i < Math.min(10, userList.size()); i++) {
            User2 currentUser = userList.get(i);
            System.out.println("Posición " + (i + 1) + ": " + currentUser.getUsername() + " - Puntos: " + currentUser.getPoints());
        }

        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Posición", skin)).pad(10);
        table.add(new Label("Usuario", skin)).pad(10);
        table.add(new Label("Puntos", skin)).pad(10);
        table.row();

        boolean userEnteredTop10 = false;
        boolean user2EnteredTop10 = false;

        for (int i = 0; i < Math.min(10, userList.size()); i++) {
            User2 currentUser = userList.get(i);

            Label positionLabel = new Label(String.valueOf(i + 1), skin);
            Label usernameLabel = new Label(currentUser.getUsername(), skin);
            Label pointsLabel = new Label(String.valueOf(currentUser.getPoints()), skin);
            Label topLabel = new Label("", skin);

            if (user.getPoints() > currentUser.getPoints()) {
                userEnteredTop10 = true;
            }

            if (user2.getPoints() > currentUser.getPoints()) {
                user2EnteredTop10 = true;
            }

            table.add(positionLabel).pad(10);
            table.add(usernameLabel).pad(10);
            table.add(pointsLabel).pad(10);
            table.add(topLabel).pad(10);
            table.row();
        }

        Label messageLabel = new Label("", skin);

        if (userEnteredTop10 && user2EnteredTop10) {
            messageLabel.setText(user.getUsername() + " y " + user2.getUsername() + " han entrado al Top 10");
        } else if (userEnteredTop10) {
            messageLabel.setText(user.getUsername() + " ha entrado al Top 10");
        } else if (user2EnteredTop10) {
            messageLabel.setText(user2.getUsername() + " ha entrado al Top 10");
        } else {
            messageLabel.setText("Ninguno de los dos jugadores ha entrado al Top 10");
        }

        table.row();
        table.add(messageLabel).colspan(4).padTop(20);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.4f, 0.1f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }


    @Override
    public void show() {
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