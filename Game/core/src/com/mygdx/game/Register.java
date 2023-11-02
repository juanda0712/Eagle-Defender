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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.mygdx.models.User2;
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.SpotifyAuthenticator;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import lombok.Getter;
import lombok.Setter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class Register implements Screen {
    final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;

    private Skin skin = VisUI.getSkin();
    private Image pfpImage;
    private final Table contentTable = new Table();
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
    private CameraPictureActor cameraPictureActor;

    public Register(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;

        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal("register.json"));
        cameraPictureActor = new CameraPictureActor(game, user2Manager, null, null);
        createGUIElements();
        //setUPGUIElements();
    }

    private void createGUIElements() {
        questionsForm = new QuestionsForm();
        final CardDataForm cardDataForm = new CardDataForm();

        TextField nameField = stage.getRoot().findActor("nameField");
        TextField usernameField = stage.getRoot().findActor("usernameField");
        TextField emailField = stage.getRoot().findActor("emailField");
        TextField passwordField = stage.getRoot().findActor("passwordField");
        TextField confirmPasswordField = stage.getRoot().findActor("confirmField");

        TextField songField1 = stage.getRoot().findActor("Song1");
        TextField songField2 = stage.getRoot().findActor("Song2");
        TextField songField3 = stage.getRoot().findActor("Song3");

        ImageButton textureSmooth = stage.getRoot().findActor("textureSmooth");
        ImageButton textureRocky = stage.getRoot().findActor("textureRocky");
        ImageButton textureBricked = stage.getRoot().findActor("textureBricked");
        TextButton btnCreateAccount = stage.getRoot().findActor("CreateAccount");

        btnCreateAccount.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                passwordIsValid(passwordField.getText(), confirmPasswordField.getText());
                //isValidDateFormat(birthDateField.getText(), "yyyy-MM-dd");
                if (isValidDate && validPassword) {
                    getQuestions();
                    for (String question : questionsArray) {
                        System.out.println(question);
                    }

                    String fullName = nameField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String email = emailField.getText();
                    //String birthDate = birthDateField.getText();
                    String song1 = songField1.getText();
                    String song2 = songField2.getText();
                    String song3 = songField3.getText();
                    String selectedColorPalette = getSelectedColorPalette();

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
                    //newUser.setBirthDate(birthDate);
                    newUser.setSong1(song1);
                    newUser.setSong2(song2);
                    newUser.setSong3(song3);
                    newUser.setSelectedColorPalette(selectedColorPalette);
                    //newUser.setTexture();
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

                    game.changeScreen(new SelectMode(game, user2Manager, newUser));
                    dispose();

                } else {
                    System.out.println("not valid");
                }
            }
        });

        ImageButton btnColor1 = stage.getRoot().findActor("Color1");
        btnColor1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Color1");
            }
        });
        ImageButton btnColor2 = stage.getRoot().findActor("Color2");
        btnColor2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Color2");
            }
        });
        ImageButton btnColor3 = stage.getRoot().findActor("Color3");
        btnColor3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Color3");
            }
        });
        ImageButton btnColor4 = stage.getRoot().findActor("Color4");
        btnColor4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Color4");
            }
        });
        ImageButton btnColor5 = stage.getRoot().findActor("Color5");
        btnColor5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Color5");
            }
        });
        ImageButton btnColor6 = stage.getRoot().findActor("Color6");
        btnColor6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColorPalette("Color6");
            }
        });


        TextButton btnQuestions = stage.getRoot().findActor("btnQuestions");
        btnQuestions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (questionsForm.isVisible()) {
                    stage.addActor(questionsForm);
                    questionsForm.fadeIn();
                }
            }
        });
        TextButton btnCardInfo = stage.getRoot().findActor("btnCardInfo");
        btnCardInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cardDataForm.isVisible()) {
                    stage.addActor(cardDataForm);
                    cardDataForm.fadeIn();
                }
            }
        });
        TextButton btnBrowse = stage.getRoot().findActor("btnBrowse");
        System.out.println(btnBrowse);
        btnBrowse.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        if (file.size > 0) {
                            selectedFile = file.first();
                            Texture pfpTexture = new Texture(selectedFile);
                            TextureRegion pfpRegion = new TextureRegion(pfpTexture);
                            Image newPfpImage = new Image(pfpRegion);


                            if (pfpImage != null) {
                                Cell pfpCell = contentTable.getCell(pfpImage);
                                if (pfpCell != null) {
                                    pfpCell.setActor(newPfpImage);
                                }
                            } else {
                                contentTable.add(newPfpImage).size(180).left().padTop(5).row();
                            }

                            pfpImage = newPfpImage;
                        }
                    }
                });
                stage.addActor(fileChooser.fadeIn());
                fileChooser.setVisible(true);
            }
        });

        TextButton btnTakePic = stage.getRoot().findActor("btnTakePic");

        Table table = stage.getRoot().findActor("TableFaceCam");
        table.add(cameraPictureActor);
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
        ScreenUtils.clear(1, 1, 1, 1);

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

