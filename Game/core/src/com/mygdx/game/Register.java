package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.User2;
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.ImageConversion;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.SpotifyAuthenticator;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.opencv.core.CvType;
import org.mindrot.jbcrypt.BCrypt;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static org.bytedeco.javacv.Java2DFrameUtils.toBufferedImage;
import static org.opencv.imgproc.Imgproc.ellipse;

public class Register implements Screen {
    final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;

    private org.opencv.core.Mat capturedMatImage;

    public TextureRegion capturedImage;
    private TextField nameField;
    private TextField usernameField;
    private TextField emailField;
    private TextField passwordField;

    private TextField confirmPasswordField;
    private TextField birthDateField;
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
    private Set<ImageButton> clickedButtons = new HashSet<>();
    private Frame processedFrame;
    Skin skin = VisUI.getSkin();
    private final AtomicReference<SpotifyAuthenticator> spotifyReference = new AtomicReference<>(null);
    private CameraPictureActor cameraPictureActor;
    private Mat picture;
    private String pictureSlug;
    private User2 user;
    private Texture texture;
    private Table pfpTable;

    private Map<TextField, String> textFieldNames = new HashMap<>();

    private TextButton btnQuestions;

    public Register(final MainController game, final JSONDataManager<User2> user2Manager, User2 user) {
        this.game = game;
        this.user = user;
        this.user2Manager = user2Manager;

        camera = new OrthographicCamera();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal("register.json"));
        cameraPictureActor = new CameraPictureActor(game, user2Manager, this);
        createGUIElements();
        texture = new Texture(320, 240, Pixmap.Format.RGB888);
    }

    private void createTextFields() {
        nameField = stage.getRoot().findActor("nameField");
        textFieldNames.put(nameField, "Name");
        usernameField = stage.getRoot().findActor("usernameField");
        textFieldNames.put(usernameField, "Username");
        emailField = stage.getRoot().findActor("emailField");
        emailField.setName("Email");
        passwordField = stage.getRoot().findActor("passwordField");
        passwordField.setName("Password");
        confirmPasswordField = stage.getRoot().findActor("confirmField");
        confirmPasswordField.setName("Confirm password");
        birthDateField = stage.getRoot().findActor("BirthDate");
        birthDateField.setName("Birth date");
        songField1 = stage.getRoot().findActor("Song1");
        songField1.setName("Song 1");
        songField2 = stage.getRoot().findActor("Song2");
        songField2.setName("Song 2");
        System.out.println(songField2.getName());
        songField3 = stage.getRoot().findActor("Song3");
        songField3.setName("Song 3");

        picture = new Mat();
        processedFrame = new Frame();
        //setUPGUIElements();

    }

    private void createImageButtons() {
        ImageButton btnSmoothTexture = stage.getRoot().findActor("textureSmooth");
        btnSmoothTexture.setChecked(false);

        btnSmoothTexture.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedTexture("Smooth");
                clickedButtons.add(btnSmoothTexture);

            }
        });
        ImageButton btnRockyTexture = stage.getRoot().findActor("textureRocky");
        btnRockyTexture.setChecked(false);
        btnRockyTexture.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedTexture("Rocky");
                clickedButtons.add(btnRockyTexture);
            }
        });
        ImageButton btnBrickedTexture = stage.getRoot().findActor("textureBricked");
        btnBrickedTexture.setChecked(false);
        btnBrickedTexture.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedTexture("Bricked");
                clickedButtons.add(btnBrickedTexture);
            }
        });
        ImageButton btnColor1 = stage.getRoot().findActor("Color1");
        btnColor1.setChecked(false);
        btnColor1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color1");
                clickedButtons.add(btnColor1);
            }
        });
        ImageButton btnColor2 = stage.getRoot().findActor("Color2");
        btnColor2.setChecked(false);
        btnColor2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color2");
                clickedButtons.add(btnColor2);
            }
        });
        ImageButton btnColor3 = stage.getRoot().findActor("Color3");
        btnColor3.setChecked(false);
        btnColor3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color3");
                clickedButtons.add(btnColor3);
            }
        });
        ImageButton btnColor4 = stage.getRoot().findActor("Color4");
        btnColor4.setChecked(false);
        btnColor4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color4");
                clickedButtons.add(btnColor4);
            }
        });
        ImageButton btnColor5 = stage.getRoot().findActor("Color5");
        btnColor5.setChecked(false);
        btnColor5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color5");
                clickedButtons.add(btnColor5);
            }
        });
        ImageButton btnColor6 = stage.getRoot().findActor("Color6");
        btnColor6.setChecked(false);
        btnColor6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSelectedColor("Color6");
                clickedButtons.add(btnColor6);
            }
        });
    }


    private ArrayList<String> getEmptyFieldNames(TextField... textFields) {
        ArrayList<String> emptyFieldNames = new ArrayList<>();
        for (TextField textField : textFields) {
            String text = textField.getText().trim();
            if (text.isEmpty()) {
                String fieldName = textFieldNames.get(textField);
                if (fieldName != null) {
                    emptyFieldNames.add(fieldName);
                }
            }
        }
        return emptyFieldNames;
    }



    private void createGUIElements() {

        createTextFields();
        createImageButtons();

        questionsForm = new QuestionsForm();
        final CardDataForm cardDataForm = new CardDataForm();


        TextButton btnCreateAccount = stage.getRoot().findActor("CreateAccount");

        btnCreateAccount.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ArrayList<String> emptyFields = getEmptyFieldNames();
                passwordIsValid(passwordField.getText(), confirmPasswordField.getText());
                if (emptyFields.isEmpty() && questionsBtnChecked()) {
                    if (validPassword) {
                        getQuestions();
                        for (String question : questionsArray) {
                            System.out.println(question);
                        }
                        pictureSlug = generateUniqueFilename("imagen", "png");
                        String fullName = nameField.getText();
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                        String email = emailField.getText();
                        String birthDate = birthDateField.getText();
                        String song1 = songField1.getText();
                        String song2 = songField2.getText();
                        String song3 = songField3.getText();
                        String selectedColor = getSelectedColor();

                        //String image = selectedFile.name();
                        String petName = questionsArray.get(0);
                        String favTeacher = questionsArray.get(1);
                        String favTeam = questionsArray.get(2);
                        String childhoodNickname = questionsArray.get(3);
                        String favPlace = questionsArray.get(4);


                        User2 newUser = new User2();
                        newUser.setFullName(fullName);
                        newUser.setUsername(username);
                        newUser.setPassword(hashedPassword);
                        newUser.setEmail(email);
                        newUser.setBirthDate(birthDate);
                        newUser.setSong1(song1);
                        newUser.setSong2(song2);
                        newUser.setSong3(song3);

                        newUser.setSelectedColor(selectedColor);
                        newUser.setTexture(getSelectedTexture());
                        //newUser.setImage(image);

                        newUser.setSelectedColor(selectedColor);
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

                        if (user == null) {
                            game.changeScreen(new SelectMode(game, user2Manager, newUser));
                        } else {
                            CountersBarriers countersBarriers = new CountersBarriers();
                            Thread spotifyAuthThread = new Thread(() -> {
                                SpotifyAuthenticator spotify = new SpotifyAuthenticator();
                                spotifyReference.set(spotify);
                            });

                            spotifyAuthThread.start();
                            game.changeScreen(new GameScreen(game, user2Manager, user, newUser, countersBarriers, spotifyReference,0,0,0));
                        }

                        dispose();

                    }
                } else {
                    final Dialog dialog9 = new Dialog("Some info is missing", skin);
                    dialog9.show(stage);
                    dialog9.setSize(210,80 );
                    dialog9.button("Ok", new ClickListener() {
                        public void clicked(InputEvent event, float x, float y) {
                            dialog9.remove();

                        }
                    });
                }

            }
        });
        btnQuestions = stage.getRoot().findActor("btnQuestions");
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

        ImageButton btnTakePic = stage.getRoot().findActor("btnTakePic");
        btnTakePic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = generateUniqueFilename("imagen", "png");
                cameraPictureActor.takePicture();
                //updatePfpTable();
            }
        });
        TextButton btnShowPic = stage.getRoot().findActor("btnShowPic");
        btnShowPic.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updatePfpTable();
            }

        });
        Table faceCamTable = stage.getRoot().findActor("faceCamTable");
        faceCamTable.add(cameraPictureActor);

        pfpTable = stage.getRoot().findActor("TablePfp");

    }
    private boolean questionsBtnChecked(){
        return btnQuestions.isChecked();
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
        this.picture = picture;
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

    private void updatePfpTable() {
        Mat render = picture.clone();
        Size newSizeFrame = new Size(320, 240);
        opencv_imgproc.resize(render, render, newSizeFrame);
        org.bytedeco.opencv.global.opencv_imgproc.warpAffine(render, render, org.bytedeco.opencv.global.opencv_imgproc.getRotationMatrix2D(new Point2f((float) render.cols() / 2, (float) render.rows() / 2), 180, 1), render.size());
        render = applyCircularMask(render);
        processedFrame = Java2DFrameUtils.toFrame(toBufferedImage(render));
        Pixmap pixmap = ImageConversion.toPixmap(processedFrame);
        texture.draw(pixmap, 0, 0);
        Drawable imageDrawable = new TextureRegionDrawable(new TextureRegion(texture));
        Image image = new Image(imageDrawable);
        pfpTable.clear();
        pfpTable.add(image).row();
    }

    private Mat applyCircularMask(Mat input) {
        org.bytedeco.opencv.opencv_core.Mat mask = new org.bytedeco.opencv.opencv_core.Mat(input.size(), CvType.CV_8U, Scalar.BLACK);
        org.bytedeco.opencv.opencv_core.Size size = new org.bytedeco.opencv.opencv_core.Size(input.cols() / 2, input.rows() / 2);
        org.bytedeco.opencv.opencv_core.Point center = new org.bytedeco.opencv.opencv_core.Point(input.cols() / 2, input.rows() / 2);

        int thickness = org.bytedeco.opencv.global.opencv_imgproc.CV_FILLED;
        int lineType = org.bytedeco.opencv.global.opencv_imgproc.CV_AA;

        org.bytedeco.opencv.global.opencv_imgproc.ellipse(mask, center, size, 0, 0, 360, Scalar.WHITE, thickness, lineType, 0);

        org.bytedeco.opencv.opencv_core.Mat result = new org.bytedeco.opencv.opencv_core.Mat();
        input.copyTo(result, mask);

        // Encuentra el rectángulo que delimita la región no negra en la imagen resultante
        org.bytedeco.opencv.opencv_core.Rect resultRoi = org.bytedeco.opencv.global.opencv_imgproc.boundingRect(mask);

        // Recorta la región de interés (ROI) de la imagen resultante

        return new Mat(result, resultRoi);
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
            dialog2.setSize(340, 60);
            dialog2.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog2.remove();
                }
            });
        } else if (!specialCharPatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog3 = new Dialog("Password must have at least one special character", skin);
            dialog3.show(stage);
            dialog3.setSize(340, 60);
            dialog3.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog3.remove();
                }
            });
        } else if (!UpperCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog4 = new Dialog("Password must have at least one uppercase character", skin);
            dialog4.show(stage);
            dialog4.setSize(340, 60);
            dialog4.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog4.remove();
                }
            });
        } else if (!lowerCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog5 = new Dialog("Password must have at least one lowercase character", skin);
            dialog5.show(stage);
            dialog5.setSize(330, 60);
            dialog5.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog5.remove();
                }
            });
        } else if (!digitCasePatten.matcher(passwordhere).find()) {
            this.validPassword = false;
            final Dialog dialog6 = new Dialog("Password must have at least one digit character", skin);
            dialog6.show(stage);
            dialog6.setSize(330, 60);
            dialog6.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog6.remove();
                }
            });
        } else {
            this.validPassword = true;
        }
    }
    /*
    private boolean checkButtons() {
        int totalButtons = 9;
        return clickedButtons.size() == totalButtons;
    }

     */

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
        if (cameraPictureActor != null) {
            cameraPictureActor.remove();
            cameraPictureActor = null;
        }
        stage.dispose();
    }
}


