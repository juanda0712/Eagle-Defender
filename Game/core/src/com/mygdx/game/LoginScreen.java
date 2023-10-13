package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.mygdx.models.User2;
import com.mygdx.utils.CustomStyle;
import com.mygdx.utils.GraphicElements;
import com.mygdx.utils.JSONDataManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.utils.ImageComparator;


public class LoginScreen implements Screen {

    private final MainController game;

    private final Stage stage;
    private FaceRecognitionActor faceRecognitionActor;
    private final OrthographicCamera camera;
    private final JSONDataManager<User2> user2Manager;
    private final Array<User2> data;
    private Recognizer recognizer;

    public LoginScreen(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;
        data = user2Manager.read();
        recognizer = new Recognizer(user2Manager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1720, 1080);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        faceRecognitionActor = new FaceRecognitionActor(game, user2Manager);

        setupUIElements();
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();
        BitmapFont font = new BitmapFont();
        TextureRegionDrawable underlineDrawable = new TextureRegionDrawable(new Texture("underline2.png"));

        // Create styles
        VisTextField.VisTextFieldStyle textFieldStyle = CustomStyle.createTextFieldStyle(font, Color.BLACK, underlineDrawable);
        VisCheckBox.VisCheckBoxStyle checkBoxStyle = CustomStyle.createCheckBoxStyle(font, Color.BLUE, Color.BLACK);

        //GRAPHIC ELEMENTS
        Label welcomeLabel = GraphicElements.createLabel("Eagle Defender", skin, Color.BLACK);
        Label infoLabel = GraphicElements.createLabel("Please enter your personal info", skin, Color.BLACK);

        final VisTextField usernameField = GraphicElements.createTextField("Username", textFieldStyle);
        final VisTextField passwordField = GraphicElements.createTextField("Password", textFieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton btnFacialRecognition = GraphicElements.createCustomButton("> Facial Recognition <", checkBoxStyle, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                faceRecognitionActor.startFaceDetection();
            }
        });
        TextButton btnRegister = GraphicElements.createCustomButton("Create Account", checkBoxStyle, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new FormManagement(game, user2Manager));
                dispose();
            }
        });
        TextButton btnLogin = GraphicElements.createButton("Login", skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (!loginValidation(username, password)) {
                    System.out.println("Usuario invalido");
                }
            }
        });
      
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float leftTableWidth = screenWidth / 2;
        float leftTableHeight = screenHeight;
        float padBottomValue = screenHeight * 0.05f;

        Texture backgroundTexture = new Texture(Gdx.files.internal("loginBGpng.png")); // Reemplaza con la ubicación de tu imagen de fondo
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));


        Table leftTable = new Table();
        leftTable.setSize(leftTableWidth, leftTableHeight);
        leftTable.setBackground(backgroundDrawable);
        leftTable.add(faceRecognitionActor).size(320, 240).center().row();
        leftTable.add(btnFacialRecognition).padBottom(padBottomValue * 15).center().row();


        Table rightTable = new Table();
        rightTable.setSize(leftTableWidth, leftTableHeight);
        rightTable.add(welcomeLabel).padBottom(padBottomValue).center().row();
        rightTable.add(infoLabel).padBottom(padBottomValue).center().row();
        rightTable.add(usernameField).padBottom(padBottomValue).center().row();
        rightTable.add(passwordField).padBottom(padBottomValue).center().row();
        rightTable.add(btnRegister).padBottom(padBottomValue).center().row();
        rightTable.add(btnLogin).padBottom(padBottomValue).center().row();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setSize(screenWidth, screenHeight);

        mainTable.add(leftTable).expand().fill();
        mainTable.add(rightTable).expand().fill();

        stage.addActor(mainTable);
    }

    private boolean loginValidation(String username, String password) {
        boolean validUser = false;
        for (User2 user : data) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                validUser = true;
                game.changeScreen(new GameScreen(game, user2Manager, user));
                dispose();
                break;
            }
        }
        return validUser;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 2, 1, 2);

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