package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.models.User2;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    private final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;
    private JSONDataManager<User2> user2Manager;
    private Label label1;
    private User2 user;

    public GameScreen(final MainController game, JSONDataManager<User2> user2Manager, User2 user) {
        this.game = game;
        this.user2Manager = user2Manager;
        this.user = user;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 900, 800);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        setupUIElements();
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();
        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(1600, 0);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar la pantalla a LoginScreen
                game.setScreen(new LoginScreen(game, user2Manager));
            }
        });
        label1 = new Label("unknown", skin);
        label1.setColor(Color.BLACK);
        label1.setPosition(120, 950);
        Label label2 = new Label("unknown", skin);
        label2.setColor(Color.BLACK);
        label2.setPosition(1500, 950);
        Texture eagle = new Texture(Gdx.files.internal("assets/aguilagod.png"));
        Texture goblin = new Texture(Gdx.files.internal("assets/duendegod.png"));
        Texture userImage = new Texture(Gdx.files.local("data/imgs/" + user.getImage()));
        Image image1 = new Image(eagle);
        image1.setPosition(0, 400);
        Image image2 = new Image(goblin);
        image2.setPosition(1000, 400);
        Image user = new Image(userImage);
        user.setPosition(400, 0);
        stage.addActor(backButton);
        stage.addActor(label1);
        stage.addActor(label2);
        stage.addActor(image1);
        stage.addActor(image2);
        stage.addActor(user);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 2, 1, 2);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Array<User2> users = user2Manager.read();
        if (!users.isEmpty()) {
            User2 firstUser = users.get(0);
            String username = firstUser.getUsername();
            label1.setText(username);
            stage.draw();
        }
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