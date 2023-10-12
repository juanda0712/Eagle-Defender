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
    private final OrthographicCamera camera;
    private final JSONDataManager<User2> user2Manager;
    private final Array<User2> data;

    public LoginScreen(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;
        data = user2Manager.read();
        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
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
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        FileHandle selectedFile = file.first();
                        String filePath = selectedFile.path();
                        boolean usuarioValido = false;
                        double result;

                        for (User2 user : data) {
                            result = ImageComparator.comparator(filePath, user.getImage());
                            if (result > 0.8) {
                                usuarioValido = true;
                                game.changeScreen(new GameScreen(game, user2Manager, user));
                                dispose();
                                break;
                            }
                        }
                        if (!usuarioValido) {
                            System.out.println("USUARIO NO VALIDO");
                        }
                    }
                });

                stage.addActor(fileChooser.fadeIn());
                fileChooser.setVisible(true);
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

        // TABLE TO ADD ALL THE GRAPHIC ELEMENTS
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