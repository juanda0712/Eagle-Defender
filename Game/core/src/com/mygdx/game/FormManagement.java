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
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.mygdx.models.User;
import com.mygdx.utils.JSONDataManager;
import com.sun.jndi.toolkit.dir.SearchFilter;

public class FormManagement implements Screen {
    final MainController game;
    private final Stage stage;

    private SearchFilter searchBarSongs;

    private final OrthographicCamera camera;

    private final JSONDataManager<User> userManager;

    public FormManagement(final MainController game) {
        this.game = game;
        userManager = new JSONDataManager<>("data/users.json", User.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 900, 800);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = VisUI.getSkin();
        BitmapFont font = new BitmapFont();
        TextureRegionDrawable underlineDrawable = new TextureRegionDrawable(new Texture("underline2.png"));

        VisTextField.VisTextFieldStyle style = new VisTextField.VisTextFieldStyle();
        style.font = font;
        style.fontColor = Color.ORANGE;
        style.background = underlineDrawable;

        Table table = new Table();
        table.setFillParent(true);

        Label welcomeLabel = new Label("Welcome!", skin);
        welcomeLabel.setColor(Color.BLACK);

        Label infoLabel = new Label("Create your account by entering your personal info", skin);
        infoLabel.setColor(Color.BLACK);

        final VisTextField nameField = new VisTextField("", style);
        nameField.setMessageText("Name");

        final VisTextField usernameField = new VisTextField("", style);
        usernameField.setMessageText("Username");

        Label questionsLabel = new Label("Please answer these questions", skin);
        questionsLabel.setColor(Color.BLACK);

        Button btnQuestions = new TextButton("Go to questions", skin);

        btnQuestions.addListener(new ClickListener() {

            public void questionsClicked (InputEvent event, float x, float y){

            }
        });

        VisTextField ageField = new VisTextField("", style);
        ageField.setMessageText("Your age");

        final VisTextField emailField = new VisTextField("", style);
        emailField.setMessageText("Email");

        final VisTextField passwordField = new VisTextField("", style);
        passwordField.setMessageText("Password");

        final VisTextField confirmPasswordField = new VisTextField("",style);
        confirmPasswordField.setMessageText("Confirm password");

        final Label searchSongs = new Label("Select 3 favorite songs", skin);
        searchSongs.setColor(Color.BLACK);
        //searchBarSongs = new SearchFilter("Search");

        Label selectPreferedPalette = new Label("Select your preferred color palette", skin);
        selectPreferedPalette.setColor(Color.BLACK);

        Label selectAnimation = new Label("Select one animation", skin);
        selectAnimation.setColor(Color.BLACK);

        Label uploadPfp = new Label("Please upload your profile picture", skin);
        uploadPfp.setColor(Color.BLACK);

        Button btnUpload = new TextButton("Upload", skin);

        TextButton btnCreateAccount = new TextButton("Create account", skin);
        btnCreateAccount.addListener(new ClickListener() {
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

        table.add(welcomeLabel).spaceBottom(80).padBottom(10).row();
        table.add(infoLabel).padBottom(10).row();
        table.add(nameField).left().padBottom(10).row();
        table.add(usernameField).left().padBottom(10).row();
        table.add(ageField).left().padBottom(10).row();
        table.add(emailField).left().padBottom(10).row();
        table.add(passwordField).left().padBottom(10).row();
        table.add(confirmPasswordField).left().padBottom(10).row();
        table.add(searchSongs).left().padBottom(10).row();
        table.add(selectPreferedPalette).left().padBottom(10).row();
        table.add(selectAnimation).left().padBottom(10).row();
        table.add(uploadPfp).left().padBottom(10).row();
        table.add(btnUpload).left().padBottom(10).row();
        table.add(btnCreateAccount).padBottom(10).row();

        table.add(questionsLabel).right().padBottom(10).row();
        table.add(btnQuestions).right().padBottom(10).row();


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
