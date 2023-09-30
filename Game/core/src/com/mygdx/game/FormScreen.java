package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;

public class FormScreen implements Screen {
    final MainController game;
    private Stage stage;
    private Skin skin; // Reutiliza la instancia de skin cargada en MyGdxGame
    private Table table;
    private TextField nameField;
    private TextField emailField;
    private TextButton submitButton;

    OrthographicCamera camera;

    public FormScreen(final MainController game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Utiliza la instancia de skin cargada en MyGdxGame
        skin = VisUI.getSkin();

        table = new Table();
        table.setFillParent(true);

        nameField = new TextField("", skin);
        nameField.setMessageText("Name");

        emailField = new TextField("", skin);
        emailField.setMessageText("Email");

        submitButton = new TextButton("Submit", skin);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText();
                String email = emailField.getText();

                // Handle the form submission (e.g., validate input, send data to server)
                // For now, we'll just print the input values.
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
            }
        });

        table.add(nameField).padBottom(10).row();
        table.add(emailField).padBottom(10).row();
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

    // Implementa los métodos restantes de la interfaz Screen

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
