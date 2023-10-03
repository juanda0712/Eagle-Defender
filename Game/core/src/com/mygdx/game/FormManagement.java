package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.mygdx.models.User;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import com.sun.jndi.toolkit.dir.SearchFilter;

import java.util.regex.Pattern;

public class FormManagement implements Screen {
    final MainController game;
    private final Stage stage;

    private SearchFilter searchBarSongs;

    private final OrthographicCamera camera;
    String[] animations = {"Explosions", "Shock", "Transparency"};
    String[] textures = {"Smooth", "Bricked", "Rocky"};

    private final JSONDataManager<User2> user2Manager;
    private Array<String> questionsArray;
    private final QuestionsForm questionsForm;
    private boolean validPassword;

    public FormManagement(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;
        this.validPassword = false;

        questionsForm = new QuestionsForm();
        final CardDataForm cardDataForm = new CardDataForm();

        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = VisUI.getSkin();
        BitmapFont font = new BitmapFont();
        TextureRegionDrawable underlineDrawable = new TextureRegionDrawable(new Texture("underline2.png"));

        VisTextField.VisTextFieldStyle style = new VisTextField.VisTextFieldStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.background = underlineDrawable;

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
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (questionsForm.isVisible()) {
                    stage.addActor(questionsForm);
                    questionsForm.fadeIn();
                }

            }
        });

        final VisTextField birthDateField = new VisTextField("", style);
        birthDateField.setMessageText("Birth Date");

        final VisTextField emailField = new VisTextField("", style);
        emailField.setMessageText("Email");

        final VisTextField passwordField = new VisTextField("", style);
        passwordField.setMessageText("Password");


        final VisTextField confirmPasswordField = new VisTextField("", style);
        confirmPasswordField.setMessageText("Confirm password");


        final Label searchSongs = new Label("Write 3 favorite songs", skin);
        searchSongs.setColor(Color.BLACK);

        final VisTextField searchField1 = new VisTextField("song1", style);
        final VisTextField searchField2 = new VisTextField("song2", style);
        final VisTextField searchField3 = new VisTextField("song3", style);


        Label selectPreferedPalette = new Label("Select your preferred color palette", skin);
        selectPreferedPalette.setColor(Color.BLACK);

        ImageButton btnPalette1 = new ImageButton(skin);
        Texture texture = new Texture(Gdx.files.internal("Palette 1.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextureRegion imageRegion = new TextureRegion(texture);
        imageRegion.setRegionWidth(102);
        imageRegion.setRegionHeight(22);

        btnPalette1.getStyle().imageUp = new TextureRegionDrawable(imageRegion);

        ImageButton btnPalette2 = new ImageButton(skin);
        Texture texture2 = new Texture(Gdx.files.internal("Palette 2.png"));
        texture2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextureRegion imageRegion2 = new TextureRegion(texture2);
        imageRegion2.setRegionWidth(93);
        imageRegion2.setRegionHeight(22);


        Label selectAnimation = new Label("Select one animation", skin);
        selectAnimation.setColor(Color.BLACK);

        final SelectBox<String> choiceBoxAnimation = new SelectBox<>(skin);
        choiceBoxAnimation.setItems(animations);

        Label selectTexture = new Label("Select one wall texture", skin);
        selectTexture.setColor(Color.BLACK);

        final SelectBox<String> choiceBoxTexture = new SelectBox<>(skin);
        choiceBoxTexture.setItems(textures);

        Label paymentMethods = new Label("Payment options", skin);
        paymentMethods.setColor(Color.BLACK);

        TextButton btnPaymentMethods = new TextButton("Go to payment methods", skin);
        btnPaymentMethods.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cardDataForm.isVisible()) {
                    stage.addActor(cardDataForm);
                    cardDataForm.fadeIn();
                }
            }
        });

        Label uploadPfp = new Label("Please upload your profile picture", skin);
        uploadPfp.setColor(Color.BLACK);

        TextButton btnUpload = new TextButton("Upload", skin);
        btnUpload.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //loadImage();
            }
        });

        TextButton btnCreateAccount = new TextButton("Create account", skin);
        btnCreateAccount.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passwordIsValid(passwordField.getText(), confirmPasswordField.getText());

                if (!nameField.isEmpty() && !usernameField.isEmpty()) {
                    getQuestions();
                    for (String question : questionsArray) {
                        System.out.println(question);
                    }

                    String fullName = nameField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String email = emailField.getText();
                    String birthDate = birthDateField.getText();
                    String song1 = searchField1.getText();
                    String song2 = searchField2.getText();
                    String song3 = searchField3.getText();
                    String selectedColorPalette = "paleta";
                    String animation = choiceBoxAnimation.getSelected();
                    String texture = choiceBoxTexture.getSelected();
                    String image = "ruta";
                    String petName = questionsArray.get(0);
                    String favTeacher = questionsArray.get(1);
                    String favTeam = questionsArray.get(2);
                    String childhoodNickname = questionsArray.get(3);
                    String favPlace = questionsArray.get(4);


                    User2 newUser = new User2();
                    newUser.setFullName(fullName);
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.setBirthDate(birthDate);
                    newUser.setSong1(song1);
                    newUser.setSong2(song2);
                    newUser.setSong3(song3);
                    newUser.setSelectedColorPalette(selectedColorPalette);
                    newUser.setAnimation(animation);
                    newUser.setTexture(texture);
                    newUser.setImage(image);
                    newUser.setPetName(petName);
                    newUser.setFavTeacherLastnamne(favTeacher);
                    newUser.setFavTeam(favTeam);
                    newUser.setChildhoodNickName(childhoodNickname);
                    newUser.setFavPlace(favPlace);

                    user2Manager.create(newUser);

                    Array<User2> users = user2Manager.read();
                    for (User2 user : users) {
                        System.out.println(user);

                    }
                }


            }
        });


        Table contentTable = new Table();
        float screenHeight = Gdx.graphics.getHeight();
        float padBottomValue = screenHeight * 0.05f;

        // Agregar los elementos a la tabla de contenido
        contentTable.add(infoLabel).padBottom(padBottomValue).row();
        contentTable.add(nameField).left().padBottom(padBottomValue).row();
        contentTable.add(usernameField).left().padBottom(padBottomValue).row();
        contentTable.add(birthDateField).left().padBottom(padBottomValue).row();
        contentTable.add(emailField).left().padBottom(padBottomValue).row();
        contentTable.add(passwordField).left().padBottom(padBottomValue).row();
        contentTable.add(confirmPasswordField).left().padBottom(padBottomValue).row();
        contentTable.add(searchSongs).left().padBottom(padBottomValue).row();
        contentTable.add(searchField1).left().padBottom(padBottomValue).row();
        contentTable.add(searchField2).left().padBottom(padBottomValue).row();
        contentTable.add(searchField3).left().padBottom(padBottomValue).row();
        contentTable.add(selectPreferedPalette).left().padBottom(padBottomValue).row();
        contentTable.add(btnPalette1).left().padBottom(padBottomValue).row();
        contentTable.add(btnPalette2).left().padBottom(padBottomValue).row();
        contentTable.add(selectAnimation).left().padBottom(padBottomValue).row();
        contentTable.add(choiceBoxAnimation).left().padBottom(padBottomValue).row();
        contentTable.add(selectTexture).left().padBottom(padBottomValue).row();
        contentTable.add(choiceBoxTexture).left().padBottom(padBottomValue).row();
        contentTable.add(paymentMethods).left().padBottom(5).row();
        contentTable.add(btnPaymentMethods).left().padBottom(5).row();
        contentTable.add(uploadPfp).left().padBottom(padBottomValue).row();
        contentTable.add(btnUpload).left().padBottom(padBottomValue).row();
        contentTable.add(questionsLabel).right().padBottom(padBottomValue).row();
        contentTable.add(btnQuestions).left().padBottom(padBottomValue).row();
        contentTable.add(btnCreateAccount).padBottom(padBottomValue * 2).row();


        ScrollPane scrollPane = new ScrollPane(contentTable);

        Table table = new Table();
        table.setFillParent(true);

        table.add(welcomeLabel).padBottom(10).row();
        table.add(scrollPane).expandX().fillX().pad(padBottomValue).row();

        stage.addActor(table);
    }

    private void getQuestions() {
        questionsArray = questionsForm.getQuestions();
    }

    private void passwordIsValid(String passwordhere, String confirmhere) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        if (!passwordhere.equals(confirmhere)) {
            this.validPassword = false;
            System.out.println("Password and confirm password does not match");
        }
        if (passwordhere.length() < 8) {
            this.validPassword = false;
            System.out.println("Password length must have at least 8 characters");
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            System.out.println("Password must have at least one special character");
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            System.out.println("Password must have at least one uppercase character");
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            System.out.println("Password must have at least one lowercase character");
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            System.out.println("Password must have at least one digit character");
        }
        this.validPassword = true;

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
