package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import java.util.Random;

public class IAMode implements Screen {
    private final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;
    int screenWidth;
    int screenHeight;
    private Array<Image> randomIA;
    private Image goblin;
    private float goblinTimer;
    private float goblinX, goblinY;

    public IAMode(final MainController game) {
        this.game = game;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        goblinTimer = 0;
        randomIA = new Array<>();
        setupUIElements();
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();
        TextButton returnButton = new TextButton("Back", skin);
        returnButton.setPosition(1600, 500);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new SelectMode(game));
            }
        });
        stage.addActor(returnButton);

        int totalrandomIAPerType = 10;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float halfScreenWidth = screenWidth / 2;

        for (int i = 0; i < totalrandomIAPerType; i++) {
            addRandomImageIA("wood.jpg", halfScreenWidth, screenHeight);
            addRandomImageIA("cement.jpg", halfScreenWidth, screenHeight);
            addRandomImageIA("steel.jpg", halfScreenWidth, screenHeight);
        }

        goblin = new Image(new Texture("SSF6.png"));
        goblin.setPosition(screenWidth / 2, screenHeight / 2);
        stage.addActor(goblin);

        randomMovement();

    }

    private void addRandomImageIA(String imageFileName, float halfScreenWidth, float screenHeight) {
        Texture imageTexture = new Texture(imageFileName);
        TextureRegion imageRegion = new TextureRegion(imageTexture);
        TextureRegionDrawable imageDrawable = new TextureRegionDrawable(imageRegion);
        Image image = new Image(imageDrawable);

        boolean overlapped;
        float randomX, randomY;

        do {
            overlapped = false;
            randomX = MathUtils.random(0, halfScreenWidth - image.getWidth());
            randomY = MathUtils.random(0, screenHeight - image.getHeight());

            for (Image existingImage : randomIA) {
                if (existingImage.getX() < randomX + image.getWidth() &&
                        randomX < existingImage.getX() + existingImage.getWidth() &&
                        existingImage.getY() < randomY + image.getHeight() &&
                        randomY < existingImage.getY() + existingImage.getHeight()) {
                    overlapped = true;
                    break;
                }
            }
        } while (overlapped);

        image.setPosition(randomX, randomY);
        stage.addActor(image);
        randomIA.add(image);
    }

    private void randomMovement() {
        float halfScreenWidth = screenWidth / 2;
        float goblinWidth = 70;
        float goblinHeight = 87;

        goblinX = MathUtils.random(halfScreenWidth, screenWidth - goblinWidth);
        goblinY = MathUtils.random(0, screenHeight - goblinHeight);

        goblin.setPosition(goblinX, goblinY);
        goblinTimer = 2.0f;
    }

    @Override
    public void render(float delta) {
        Color backgroundColor = new Color(0.96f, 0.96f, 0.86f, 1);
        ScreenUtils.clear(backgroundColor);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        if (goblinTimer > 0) {
            goblinTimer -= delta;
        }

        if (goblinTimer <= 0) {
            goblin.setPosition(-goblin.getWidth(), -goblin.getHeight());
            goblinTimer = 2.0f;
            randomMovement();
        }

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