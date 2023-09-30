package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.User;
import com.mygdx.utils.JSONDataManager;

public class FormManagement implements Screen {
    final MainController game;
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextField nameField;
    private TextField usernameField;
    private TextField passwordField;
    private TextButton submitButton;

    OrthographicCamera camera;
    private JSONDataManager<User> userManager; // Debes tener userManager configurado previamente

    public FormManagement(final MainController game) {
        this.game = game;
        userManager = new JSONDataManager<>("data/users.json", User.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = VisUI.getSkin();

        table = new Table();
        table.setFillParent(true);

        nameField = new TextField("", skin);
        nameField.setMessageText("Full Name");

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");

        submitButton = new TextButton("Submit", skin);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String fullName = nameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Crear un nuevo usuario y guardarlo en el archivo JSON
                User newUser = new User();
                newUser.setFullName(fullName);
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setSong1("The GodFather");


                userManager.create(newUser);

                // Leer todos los usuarios y mostrarlos en la consola
                Array<User> users = userManager.read();
                for (User user : users) {
                    System.out.println("Full Name: " + user.getFullName());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Password: " + user.getPassword());
                }
            }
        });

        table.add(nameField).padBottom(10).row();
        table.add(usernameField).padBottom(10).row();
        table.add(passwordField).padBottom(10).row();
        table.add(submitButton).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

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
    }
}
