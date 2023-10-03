package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.mygdx.models.User;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.utils.ImageComparator;


public class LoginScreen implements Screen {

    private final MainController game;

    private final Stage stage;
    private final OrthographicCamera camera;
    private final JSONDataManager<User2> user2Manager;

    public LoginScreen(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 900, 800);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        setupUIElements();
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();
        BitmapFont font = new BitmapFont();
        TextureRegionDrawable underlineDrawable = new TextureRegionDrawable(new Texture("underline2.png"));

        VisTextField.VisTextFieldStyle style = new VisTextField.VisTextFieldStyle();
        style.font = font;
        style.fontColor = Color.ORANGE;
        style.background = underlineDrawable;

        VisCheckBox.VisCheckBoxStyle style2 = new VisCheckBox.VisCheckBoxStyle();
        style2.font = font;
        style2.fontColor = Color.BLUE;
        style2.overFontColor = Color.BLACK;

        Label welcomeLabel = new Label("Eagle Defender", skin);
        welcomeLabel.setColor(Color.BLACK);

        Label infoLabel = new Label("Please enter your personal info", skin);
        infoLabel.setColor(Color.BLACK);

        TextButton btnFacialRecognition = new TextButton("> Facial Recognition <", style2);
        btnFacialRecognition.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        // Se ejecuta cuando el usuario selecciona un archivo
                        FileHandle selectedFile = file.first();
                        String filePath = selectedFile.path();

                        System.out.println("RESULT: " + ImageComparator.comparator(filePath, filePath));
                    }
                });

                stage.addActor(fileChooser.fadeIn());
                fileChooser.setVisible(true);
            }
        });


        final VisTextField usernameField = new VisTextField("", style);
        usernameField.setMessageText("Username");

        final VisTextField passwordField = new VisTextField("", style);
        passwordField.setMessageText("Password");

        TextButton btnRegister = new TextButton("Create Account", style2);
        btnRegister.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("REGISTER");
                game.changeScreen(new FormManagement(game, user2Manager));
                dispose();
            }
        });

        TextButton btnLogin = new TextButton("Login", skin);
        btnLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOGIN");
                //game.changeScreen(new GameScreen(game, user2Manager));
                //dispose();
            }
        });

        // Agregar elementos a la tabla
        Table table = new Table();
        table.setFillParent(true);
        float screenHeight = Gdx.graphics.getHeight();
        float padBottomValue = screenHeight * 0.05f;

        table.add(welcomeLabel).padBottom(padBottomValue * 2).row();
        table.add(infoLabel).padBottom(padBottomValue).row();
        table.add(btnFacialRecognition).padBottom(padBottomValue).row();
        table.add(usernameField).padBottom(padBottomValue).row();
        table.add(passwordField).padBottom(padBottomValue).row();
        table.add(btnRegister).padBottom(padBottomValue).row();
        table.add(btnLogin).padBottom(padBottomValue).row();

        stage.addActor(table);
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
        // Actualiza la vista de la cámara
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