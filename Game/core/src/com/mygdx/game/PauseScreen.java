package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

public class PauseScreen implements Screen {
    private Stage stage;
    private Texture pauseTexture;
    private Texture continueTexture;
    private Texture exitTexture;
    private Texture continueD;
    private Texture exitD;
    private final MainController game;
    public JSONDataManager<User2> user2Manager;
    private User2 user;
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();

    public PauseScreen(final MainController game, final JSONDataManager<User2> user2Manager, User2 user) {
        this.game = game;
        this.user2Manager = user2Manager;
        this.user = user;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        pauseTexture = new Texture(Gdx.files.internal("assets/pause.png"));
        continueTexture = new Texture(Gdx.files.internal("assets/cotinue.jpg"));
        exitTexture = new Texture(Gdx.files.internal("assets/exit.jpg"));
        continueD = new Texture(Gdx.files.internal("assets/cotinueD.jpg"));
        exitD = new Texture(Gdx.files.internal("assets/exitD.jpg"));

        Image pauseImage = new Image(pauseTexture);
        Table table = new Table();
        table.setFillParent(true);
        float pauseImageX = (screenWidth - pauseImage.getWidth()) / 2;
        float pauseImageY = (screenHeight - pauseImage.getHeight()) / 2;
        pauseImage.setPosition(pauseImageX, pauseImageY);
        table.addActor(pauseImage);

        Table buttonTable = new Table();
        TextButton.TextButtonStyle continueButtonStyle = new TextButton.TextButtonStyle();
        continueButtonStyle.font = new BitmapFont();
        continueButtonStyle.up = new TextureRegionDrawable(new TextureRegion(continueTexture));
        continueButtonStyle.down = new TextureRegionDrawable(new TextureRegion(continueD));

        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
        exitButtonStyle.font = new BitmapFont();
        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(exitTexture));
        exitButtonStyle.down = new TextureRegionDrawable(new TextureRegion(exitD));

        TextButton continueButton = new TextButton("Resume", continueButtonStyle);
        continueButton.setSize(251,73);
        float continueX = pauseImageX + 160;
        float continueY = pauseImageY - 0;
        continueButton.setPosition(continueX, continueY);

        TextButton exitButton = new TextButton("", exitButtonStyle);
        exitButton.setSize(155, 74);
        float exitX = pauseImageX + 2;
        float exitY = pauseImageY - 0;
        exitButton.setPosition(exitX, exitY);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                continueGame();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backSelectMode();
            }
        });

        buttonTable.addActor(continueButton);
        buttonTable.addActor(exitButton);
        table.addActor(buttonTable);
        stage.addActor(table);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        pauseTexture.dispose();
        continueTexture.dispose();
        exitTexture.dispose();
    }

    private void continueGame() {
    }

    private void backSelectMode() {
        user2Manager = new JSONDataManager<>("data/users2.json", User2.class);
        game.changeScreen(new SelectMode(game, user2Manager, user));
    }
}
