package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.SpotifyAuthenticator;
import lombok.Getter;
import lombok.Setter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class FormManagement implements Screen {
    final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;
    private Label welcomeLabel;
    private Label infoLabel;
    private VisTextField nameField;
    private VisTextField usernameField;
    private Label questionsLabel;
    private Button btnQuestions;

    private VisTextField birthDateField;
    private VisTextField emailField;
    private VisTextField passwordField;
    private VisTextField confirmPasswordField;
    private Label searchSongs;
    private VisTextField searchField1;
    private VisTextField searchField2;
    private VisTextField searchField3;
    private Label selectPreferedPalette;
    private ImageButton btnPalette1;
    private ImageButton btnPalette2;
    private ImageButton btnPalette3;
    private ImageButton btnPalette4;
    private ImageButton btnPalette5;

    private Label selectAnimation;
    private SelectBox<String> choiceBoxAnimation;
    private Label selectTexture;
    private SelectBox<String> choiceBoxTexture;
    private Label paymentMethods;
    private TextButton btnPaymentMethods;
    private String[] animations = {"Explosions", "Shock", "Transparency"};
    private String[] textures = {"Wooden", "Concrete", "Steel"};

    private Label uploadPfp;
    private TextButton btnUpload;
    private TextButton btnCreateAccount;
    private TextButton btnReturnLogin;
    private final JSONDataManager<User2> user2Manager;
    private Array<String> questionsArray;
    private QuestionsForm questionsForm;
    @Getter
    @Setter
    private String selectedColorPalette = "";
    private boolean validPassword = false;
    private boolean isValidDate = false;
    private FileHandle selectedFile;
    private final AtomicReference<SpotifyAuthenticator> spotifyReference = new AtomicReference<>(null);

    public FormManagement(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;

        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        createGUIElements();
        setUPGUIElements();
    }

    private void createGUIElements() {
        questionsForm = new QuestionsForm();
        final CardDataForm cardDataForm = new CardDataForm();
        Skin skin = VisUI.getSkin();
        BitmapFont font = new BitmapFont();
        TextureRegionDrawable underlineDrawable = new TextureRegionDrawable(new Texture("underline2.png"));

        VisTextField.VisTextFieldStyle style = new VisTextField.VisTextFieldStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.background = underlineDrawable;

        welcomeLabel = new Label("Welcome!", skin);
        welcomeLabel.setColor(Color.BLACK);

        infoLabel = new Label("Create your account by entering your personal info", skin);
        infoLabel.setColor(Color.BLACK);

        nameField = new VisTextField("", style);
        nameField.setMessageText("Name");

        usernameField = new VisTextField("", style);
        usernameField.setMessageText("Username");

        questionsLabel = new Label("Please answer these questions", skin);
        questionsLabel.setColor(Color.BLACK);

        btnQuestions = new TextButton("Go to questions", skin);
        btnQuestions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (questionsForm.isVisible()) {
                    stage.addActor(questionsForm);
                    questionsForm.fadeIn();
                }

            }
        });
        birthDateField = new VisTextField("", style);
        birthDateField.setMessageText("Birth Date");

        emailField = new VisTextField("", style);
        emailField.setMessageText("Email");

        passwordField = new VisTextField("", style);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        confirmPasswordField = new VisTextField("", style);
        confirmPasswordField.setMessageText("Confirm password");
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');


        searchSongs = new Label("Write 3 favorite songs", skin);
        searchSongs.setColor(Color.BLACK);

        searchField1 = new VisTextField("", style);
        searchField1.setMessageText("Song 1");

        searchField2 = new VisTextField("", style);
        searchField2.setMessageText("Song 2");

        searchField3 = new VisTextField("", style);
        searchField3.setMessageText("Song 3");

        selectPreferedPalette = new Label("Select your preferred color palette", skin);
        selectPreferedPalette.setColor(Color.BLACK);

        ImageButton.ImageButtonStyle style1 = new ImageButton.ImageButtonStyle();
        style1.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Colors/Palette 1.png"))));
        btnPalette1 = new ImageButton(style1);
        btnPalette1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Palette 1");
            }
        });

        ImageButton.ImageButtonStyle style2 = new ImageButton.ImageButtonStyle();
        style2.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Colors/Palette 2.png"))));
        btnPalette2 = new ImageButton(style2);
        btnPalette2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Palette 2");
            }
        });


        ImageButton.ImageButtonStyle style3 = new ImageButton.ImageButtonStyle();
        style3.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Colors/Palette 3.png"))));
        btnPalette3 = new ImageButton(style3);
        btnPalette3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Palette 3");
            }
        });


        ImageButton.ImageButtonStyle style4 = new ImageButton.ImageButtonStyle();
        style4.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Colors/Palette 4.png"))));
        btnPalette4 = new ImageButton(style4);
        btnPalette4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Palette 4");
            }
        });


        ImageButton.ImageButtonStyle style5 = new ImageButton.ImageButtonStyle();
        style5.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Colors/Palette 5.png"))));
        btnPalette5 = new ImageButton(style5);
        btnPalette5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Palette 5");
            }
        });

        selectAnimation = new Label("Select one animation", skin);
        selectAnimation.setColor(Color.BLACK);

        choiceBoxAnimation = new SelectBox<>(skin);
        choiceBoxAnimation.setItems(animations);

        selectTexture = new Label("Select one   git wall texture", skin);
        selectTexture.setColor(Color.BLACK);

        choiceBoxTexture = new SelectBox<>(skin);
        choiceBoxTexture.setItems(textures);

        paymentMethods = new Label("Payment options", skin);
        paymentMethods.setColor(Color.BLACK);

        btnPaymentMethods = new TextButton("Go to payment methods", skin);
        btnPaymentMethods.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cardDataForm.isVisible()) {
                    stage.addActor(cardDataForm);
                    cardDataForm.fadeIn();
                }
            }
        });

        uploadPfp = new Label("Please upload your profile picture", skin);
        uploadPfp.setColor(Color.BLACK);

        btnUpload = new TextButton("Upload", skin);
        btnUpload.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        selectedFile = file.first();
                    }
                });
                stage.addActor(fileChooser.fadeIn());
                fileChooser.setVisible(true);
            }
        });
        btnCreateAccount = new TextButton("Create account", skin);
        btnCreateAccount.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                passwordIsValid(passwordField.getText(), confirmPasswordField.getText());
                isValidDateFormat(birthDateField.getText(), "yyyy-MM-dd");
                if (isValidDate && validPassword) {
                    if (!nameField.isEmpty() && !usernameField.isEmpty() && !birthDateField.isEmpty() && !emailField.isEmpty() && !passwordField.isEmpty()
                            && !confirmPasswordField.isEmpty()) {
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
                        String selectedColorPalette = getSelectedColorPalette();
                        String animation = choiceBoxAnimation.getSelected();
                        String texture = choiceBoxTexture.getSelected();
                        String image = selectedFile.name();
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

                        if (selectedFile != null) {
                            String destinoPath = "data/imgs/" + selectedFile.name();
                            selectedFile.copyTo(Gdx.files.local(destinoPath));
                            System.out.println("Imagen guardada en " + destinoPath);
                        } else {
                            System.out.println("No se ha seleccionado ninguna imagen.");
                        }

                        Array<User2> users = user2Manager.read();
                        for (User2 user : users) {
                            System.out.println(user);
                        }
                        CountersBarriers counters = new CountersBarriers();

                        //Spotify
                        Thread spotifyAuthThread = new Thread(() -> {
                            SpotifyAuthenticator spotify = new SpotifyAuthenticator();
                            spotifyReference.set(spotify);
                        });

                        spotifyAuthThread.start();
                        game.changeScreen(new SelectMode(game, user2Manager, newUser));
                        dispose();
                    }
                } else {
                    System.out.println("not valid");
                }
            }
        });
        btnReturnLogin = new TextButton("Return", skin);
        btnReturnLogin.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new LoginScreen(game, user2Manager, null, null));
                dispose();
            }
        });
    }

    private void setUPGUIElements() {
        Table contentTable = new Table();
        float screenHeight = Gdx.graphics.getHeight();
        float padBottomValue = screenHeight * 0.05f;
        Array<Actor> GUIElements = new Array<>();
        GUIElements.addAll(
                welcomeLabel,
                infoLabel,
                nameField,
                usernameField,
                birthDateField,
                emailField,
                passwordField,
                confirmPasswordField,
                searchSongs,
                searchField1, searchField2, searchField3,
                selectPreferedPalette,
                btnPalette1, btnPalette2, btnPalette3, btnPalette4, btnPalette5,
                selectAnimation, choiceBoxAnimation,
                selectTexture, choiceBoxTexture,
                paymentMethods, btnPaymentMethods,
                uploadPfp, btnUpload, questionsLabel,
                btnQuestions,
                btnCreateAccount, btnReturnLogin
        );
        for (Actor element : GUIElements) {
            contentTable.add(element).left().padBottom(padBottomValue).row();
        }
        contentTable.padBottom(padBottomValue * 2);


        ScrollPane scrollPane = new ScrollPane(contentTable);

        Table table = new Table();
        table.setFillParent(true);

        table.add(scrollPane).expandX().fillX().pad(padBottomValue).row();

        stage.addActor(table);

    }


    private void getQuestions() {
        questionsArray = questionsForm.getQuestions();
    }

    private void passwordIsValid(String passwordhere, String confirmhere) {
        Skin skin = VisUI.getSkin();
        Pattern specialCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        if (!passwordhere.equals(confirmhere)) {
            this.validPassword = false;
            final Dialog dialog = new Dialog("Password confirmation does not match", skin);
            dialog.show(stage);
            dialog.setSize(280, 60);
            dialog.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog.remove();

                }
            });

        } else if (passwordhere.length() < 8) {
            this.validPassword = false;
            final Dialog dialog2 = new Dialog("Password length must have at least 8 characters", skin);
            dialog2.show(stage);
            dialog2.setSize(280, 60);
            dialog2.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog2.remove();
                }
            });
        } else if (!specialCharPatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog3 = new Dialog("Password must have at least one special character", skin);
            dialog3.show(stage);
            dialog3.setSize(280, 60);
            dialog3.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog3.remove();
                }
            });
        } else if (!UpperCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog4 = new Dialog("Password must have at least one uppercase character", skin);
            dialog4.show(stage);
            dialog4.setSize(280, 60);
            dialog4.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog4.remove();
                }
            });
        } else if (!lowerCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog5 = new Dialog("Password must have at least one lowercase character", skin);
            dialog5.show(stage);
            dialog5.setSize(280, 60);
            dialog5.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog5.remove();
                }
            });
        } else if (!digitCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog6 = new Dialog("Password must have at least one digit character", skin);
            dialog6.show(stage);
            dialog6.setSize(280, 60);
            dialog6.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog6.remove();
                }
            });
        } else {
            this.validPassword = true;
        }
    }

    private void isValidDateFormat(String date, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Deshabilitar el modo permisivo para un análisis estricto

        try {
            Date parsedDate = sdf.parse(date);
            String formattedDate = sdf.format(parsedDate);

            // Comprobar si la fecha analizada coincide exactamente con la fecha original
            isValidDate = formattedDate.equals(date);
        } catch (ParseException e) {
            isValidDate = false;
        }
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

