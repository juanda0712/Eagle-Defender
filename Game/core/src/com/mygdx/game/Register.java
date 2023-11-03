package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.mygdx.models.User2;
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.SpotifyAuthenticator;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
//import org.opencv.imgcodecs.Imgcodecs;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class Register implements Screen {
    final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;

    private Image pfpImage;
    private TextField nameField;
    private TextField usernameField;
    private TextField emailField;
    private TextField passwordField;
    private TextField confirmPasswordField;
    private TextField songField1;
    private TextField songField2;
    private TextField songField3;
    private final JSONDataManager<User2> user2Manager;
    private Array<String> questionsArray;
    private QuestionsForm questionsForm;
    @Getter
    @Setter
    private String selectedColor = "";

    @Getter
    @Setter
    private String selectedTexture = "";
    private boolean validPassword = false;
    private boolean isValidDate = false;
    private FileHandle selectedFile;
    private final AtomicReference<SpotifyAuthenticator> spotifyReference = new AtomicReference<>(null);
    private CameraPictureActor cameraPictureActor;
    private Mat picture;
    private String pictureSlug;

    public Register(final MainController game, final JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;

        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal("register.json"));
        cameraPictureActor = new CameraPictureActor(game, user2Manager, this);
        createGUIElements();

    }
    private void createTextFields (){
        nameField = stage.getRoot().findActor("nameField");
        usernameField = stage.getRoot().findActor("usernameField");
        emailField = stage.getRoot().findActor("emailField");
        passwordField = stage.getRoot().findActor("passwordField");
        confirmPasswordField = stage.getRoot().findActor("confirmField");
        songField1 = stage.getRoot().findActor("Song1");
        songField2 = stage.getRoot().findActor("Song2");
        songField3 = stage.getRoot().findActor("Song3");

        picture = new Mat();
        //setUPGUIElements();

    }

    private void createImageButtons (){
        ImageButton btnSmoothTexture = stage.getRoot().findActor("textureSmooth");
        btnSmoothTexture.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedTexture("Smooth");
            }
        });
        ImageButton btnRockyTexture = stage.getRoot().findActor("textureRocky");
        btnRockyTexture.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedTexture("Rocky");
            }
        });
        ImageButton btnBrickedTexture = stage.getRoot().findActor("textureBricked");
        btnBrickedTexture.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedTexture("Bricked");
            }
        });
        ImageButton btnColor1 = stage.getRoot().findActor("Color1");
        btnColor1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color1");
            }
        });
        ImageButton btnColor2 = stage.getRoot().findActor("Color2");
        btnColor2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color2");
            }
        });
        ImageButton btnColor3 = stage.getRoot().findActor("Color3");
        btnColor3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color3");
            }
        });
        ImageButton btnColor4 = stage.getRoot().findActor("Color4");
        btnColor4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color4");
            }
        });
        ImageButton btnColor5 = stage.getRoot().findActor("Color5");
        btnColor5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color5");
            }
        });
        ImageButton btnColor6 = stage.getRoot().findActor("Color6");
        btnColor6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color6");
            }
        });
    }
    private void createGUIElements() {
        Table tablePfp = stage.getRoot().findActor("TablePfp");
        createTextFields();
        createImageButtons();

        questionsForm = new QuestionsForm();
        final CardDataForm cardDataForm = new CardDataForm();


        TextButton btnCreateAccount = stage.getRoot().findActor("CreateAccount");

        btnCreateAccount.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                passwordIsValid(passwordField.getText(), confirmPasswordField.getText());
                //isValidDateFormat(birthDateField.getText(), "yyyy-MM-dd");
                if (validPassword) {
                    getQuestions();
                    for (String question : questionsArray) {
                        System.out.println(question);
                    }
                    pictureSlug = generateUniqueFilename("imagen", "png");
                    String fullName = nameField.getText();
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String email = emailField.getText();
                    //String birthDate = birthDateField.getText();
                    String song1 = songField1.getText();
                    String song2 = songField2.getText();
                    String song3 = songField3.getText();
                    String selectedColor = getSelectedColor();

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

                    newUser.setSelectedColor(selectedColor);
                    newUser.setTexture(getSelectedTexture());
                    newUser.setImage(image);

                    newUser.setSelectedColor(selectedColor);
                    //newUser.setTexture();
                    newUser.setImage(pictureSlug);

                    newUser.setPetName(petName);
                    newUser.setTeacherLastname(favTeacher);
                    newUser.setFavTeam(favTeam);
                    newUser.setChildhoodNickName(childhoodNickname);
                    newUser.setFavPlace(favPlace);

                    user2Manager.create(newUser);
                    savePicture(pictureSlug);

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
                                Cell pfpCell = tablePfp.getCell(pfpImage);
                                if (pfpCell != null) {
                                    pfpCell.setActor(newPfpImage);
                                }
                            } else {
                                tablePfp.add(newPfpImage).size(180).left().padTop(5).row();
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
        btnTakePic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = generateUniqueFilename("imagen", "png");
                cameraPictureActor.takePicture();
            }
        });

        Table table = stage.getRoot().findActor("TableFaceCam");
        table.add(cameraPictureActor);
    }

    private void getQuestions() {
        questionsArray = questionsForm.getQuestions();
    }

    public static String generateUniqueFilename(String baseName, String extension) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String uniqueFilename = baseName + "_" + timestamp + "." + extension;
        return uniqueFilename;
    }

    public void setPicture(Mat picture) {
        System.out.println(picture);
        this.picture = picture;
        System.out.println(this.picture);
    }

    private void savePicture(String name) {
        String destinoPath = "data/imgs/" + name;
        FileHandle fileHandle = Gdx.files.local(destinoPath);
        if (!fileHandle.exists()) {
            if (!picture.empty()) {
                if (opencv_imgcodecs.imwrite(fileHandle.file().getAbsolutePath(), picture)) {
                    Gdx.app.log("Register", "Imagen guardada con éxito en: " + fileHandle.file().getAbsolutePath());
                } else {
                    Gdx.app.error("Register", "Error al guardar la imagen");
                }
            } else {
                Gdx.app.error("Register", "La foto es null" + picture);
            }

        } else {
            Gdx.app.error("Register", "El archivo ya existe en: " + fileHandle.file().getAbsolutePath());
        }
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

