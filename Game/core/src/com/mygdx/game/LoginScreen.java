package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.mygdx.models.CountersBarriers;
import com.mygdx.models.User2;
import com.mygdx.utils.*;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.atomic.AtomicReference;


public class LoginScreen implements Screen {//QuestionsForm2.DialogCallback


    private final MainController game;
    private final AtomicReference<SpotifyAuthenticator> spotifyReference = new AtomicReference<>(null);

    private Stage stage = new Stage();
    private FaceRecognitionActor faceRecognitionActor;
    private final Skin skin = VisUI.getSkin();
    private Dialog dialog = new Dialog("", skin);
    private final OrthographicCamera camera;
    private final JSONDataManager<User2> user2Manager;

    private final QuestionsForm2 questionsForm;

    private Array<String> questionsArray;
    private final Array<User2> data;

    private User2 user;
    //private Recognizer recognizer;

    //Flow
    private User2 user1;
    private User2 user2;

    public LoginScreen(final MainController game, final JSONDataManager<User2> user2Manager, User2 user1, User2 user2) {
        questionsForm = new QuestionsForm2(game, user2Manager, user, this);
        this.game = game;
        this.user1 = user1;
        this.user2 = user2;
        this.user2Manager = user2Manager;
        data = user2Manager.read();
        //recognizer = new Recognizer(user2Manager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1720, 1080);

        Gdx.input.setInputProcessor(stage);

        faceRecognitionActor = new FaceRecognitionActor(game, user2Manager, user1, user2);

        setupUIElements();
    }

    private void setupUIElements() {

        BitmapFont font = new BitmapFont();
        TextureRegionDrawable underlineDrawable = new TextureRegionDrawable(new Texture("underline2.png"));

        // Create styles
        VisTextField.VisTextFieldStyle textFieldStyle = CustomStyle.createTextFieldStyle(font, Color.BLACK, underlineDrawable);
        VisCheckBox.VisCheckBoxStyle checkBoxStyle = CustomStyle.createCheckBoxStyle(font, Color.BLUE, Color.BLACK);

        //GRAPHIC ELEMENTS
        Label welcomeLabel = GraphicElements.createLabel("Eagle Defender", skin, Color.BLACK);
        welcomeLabel.setFontScale((float) 1.4);
        Label infoLabel = GraphicElements.createLabel("Please enter your personal info", skin, Color.BLACK);
        Label faceLabel = GraphicElements.createLabel("Already have an account? LogIn Here", skin, Color.BLACK);
        faceLabel.setFontScale((float) 1.4);

        final VisTextField usernameField = GraphicElements.createTextField("Username", textFieldStyle);
        final VisTextField passwordField = GraphicElements.createTextField("Password", textFieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton btnFacialRecognition = GraphicElements.createCustomButton("> LOGIN <", checkBoxStyle, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                faceRecognitionActor.startFaceDetection();
            }
        });
        TextButton btnRegister = GraphicElements.createCustomButton("Create Account", checkBoxStyle, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new Register(game, user2Manager));
                dispose();
            }
        });
        TextButton btnForgotPassword = GraphicElements.createCustomButton("Forgot password?", checkBoxStyle, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (questionsForm.isVisible()) {
                    stage.addActor(questionsForm);
                    questionsForm.fadeIn();
                }
            }
        });
        TextButton btnLogin = GraphicElements.createButton("Login", skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (!loginValidation(username, password)) {
                    final Dialog dialog = new Dialog("Incorrect user or password", skin);
                    dialog.show(stage);
                    dialog.setSize(280, 60);
                    dialog.button("Ok", new ClickListener() {
                        public void clicked(InputEvent event, float x, float y) {
                            dialog.remove();

                        }
                    });
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
        leftTable.add(faceRecognitionActor).center().row();
        leftTable.add(faceLabel).padTop(screenHeight / 6).padBottom(screenHeight / 37).center().row();
        leftTable.add(btnFacialRecognition).center().row();
        leftTable.row(); // Cambiar a la siguiente fila
        leftTable.add().expandY();
        leftTable.setDebug(true);


        Table rightTable = new Table();
        rightTable.setSize(leftTableWidth, leftTableHeight);
        rightTable.add(welcomeLabel).padBottom(padBottomValue).center().row();
        rightTable.add(infoLabel).padBottom(padBottomValue).center().row();
        rightTable.add(usernameField).padBottom(padBottomValue).center().row();
        rightTable.add(passwordField).padBottom(padBottomValue).center().row();
        rightTable.add(btnRegister).padBottom(padBottomValue).center().row();
        rightTable.add(btnForgotPassword).padBottom(padBottomValue).center().row();
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
                CountersBarriers countersBarriers = new CountersBarriers();

                if (user1 == null) {
                    game.changeScreen(new SelectMode(game, user2Manager, user));
                } else {
                    Thread spotifyAuthThread = new Thread(() -> {
                        SpotifyAuthenticator spotify = new SpotifyAuthenticator();
                        spotifyReference.set(spotify);
                    });

                    spotifyAuthThread.start();
                    game.changeScreen(new GameScreen(game, user2Manager, user1, user, countersBarriers, spotifyReference));
                }

                //game.changeScreen(new GameScreen(game, user2Manager, user, countersBarriers, spotifyReference));

                dispose();
                break;
            }
        }
        return validUser;
    }

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
        faceRecognitionActor.remove();
    }


    public void displayDialog(String message) {

        dialog.text(message);
        dialog.button("Ok", new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
        dialog.show(stage);

    }
}
