package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.models.User2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class GameScreen implements Screen {
    //private List<Barrera> barreras = new ArrayList<>();
    private final MainController game;

    private List<Rectangle> placedRectangles = new ArrayList<>();

    private final Stage stage;
    private final OrthographicCamera camera;
    private JSONDataManager<User2> user2Manager;

    private User2 user;
    private Label usernameLabel;
    private Label fullNameLabel;

    private final Array<Actor> arrayGUIElements = new Array<>();

    Sprite playerSprite;
    Sprite bulletSprite;

    Texture bulletTexture;
    Texture player;
    Texture playerTexture;
    Texture fireTexture;
    Texture waterTexture;
    Texture bombTexture;


    Image playerImage;
    Image bulletImage;

    SpriteBatch batch;
    float playerX = 1300;
    float playerY = 500;
    float speed = 400.0f;
    float bulletX;
    float bulletY;

    boolean isShooting = false;
    float bulletSpeed = 800.0f;
    int contador = 0;
    int contador2 = 0;
    private Array<Image> placedImages;

    private GameScreenFeatures gameScreenFeatures;
    private IAMode iaMode;

    private boolean placingEnabled = false;
    private boolean attackEnabled = false;

    boolean isCollide = false;
    private ImageButton woodenButton;
    private ImageButton cementButton;
    private ImageButton steelButton;
    private ImageButton eagleButton;

    private ImageButton waterButton;
    private ImageButton fireButton;
    private ImageButton bombButton;

    private Label woodCounterLabel;
    private Label cementCounterLabel;
    private Label steelCounterLabel;
    private Label eagleCounterLabel;
    private CountersBarriers countersBarriers;

    private int contadorBullet;


    public GameScreen(final MainController game, JSONDataManager<User2> user2Manager, User2 user, CountersBarriers countersBarriers) {
        this.game = game;
        this.user2Manager = user2Manager;
        this.user = user;
        this.countersBarriers = countersBarriers;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1720, 1080);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        placedImages = new Array<>();
        setupButtonsDefender();
        setupButtonsAttacker();
        gameScreenFeatures = new GameScreenFeatures(stage, placedImages, this, user, countersBarriers, woodenButton, cementButton, steelButton, eagleButton);
        batch = new SpriteBatch();
        setupUIElements();
    }

    public void setupButtonsDefender() {
        Skin skin = VisUI.getSkin();
        // ImageButton de barrera de madera, y su label de contador
        Drawable buttonUpWood = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/wood.jpg"))));
        Drawable buttonDownWood = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/woodD.jpg"))));
        woodenButton = new ImageButton(buttonUpWood, buttonDownWood);
        woodenButton.setPosition(10, 10);
        woodenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!woodenButton.isChecked()) {
                    placingEnabled = false;
                } else {
                    cementButton.setChecked(false);
                    steelButton.setChecked(false);
                    eagleButton.setChecked(false);
                    placingEnabled = true;
                }
            }
        });
        stage.addActor(woodenButton);
        woodCounterLabel = new Label("wood barriers: " + countersBarriers.getWoodCounter(), skin);
        woodCounterLabel.setPosition(10, 80);
        stage.addActor(woodCounterLabel);

        // ImageButton de barrera de cemento, y su label de contador
        Drawable buttonUpCement = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/cement.jpg"))));
        Drawable buttonDownCement = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/cementD.jpg"))));
        cementButton = new ImageButton(buttonUpCement, buttonDownCement);
        cementButton.setPosition(160, 10);
        cementButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!cementButton.isChecked()) {
                    placingEnabled = false;
                } else {
                    woodenButton.setChecked(false);
                    steelButton.setChecked(false);
                    eagleButton.setChecked(false);
                    placingEnabled = true;
                }
            }
        });
        stage.addActor(cementButton);
        cementCounterLabel = new Label("cement barriers: " + countersBarriers.getCementCounter(), skin);
        cementCounterLabel.setPosition(160, 80);
        stage.addActor(cementCounterLabel);

        // ImageButton de barrera de acero, y su label de contador
        Drawable buttonUpSteel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/steel.jpg"))));
        Drawable buttonDownSteel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/steelD.jpg"))));
        steelButton = new ImageButton(buttonUpSteel, buttonDownSteel);
        steelButton.setPosition(320, 10);
        steelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!steelButton.isChecked()) {
                    placingEnabled = false;
                } else {
                    woodenButton.setChecked(false);
                    cementButton.setChecked(false);
                    eagleButton.setChecked(false);
                    placingEnabled = true;
                }
            }
        });
        stage.addActor(steelButton);
        steelCounterLabel = new Label("steel barriers: " + countersBarriers.getSteelCounter(), skin);
        steelCounterLabel.setPosition(320, 80);
        stage.addActor(steelCounterLabel);

        //ImageButton de Eagle, y su label de contador
        Drawable buttonUpEagle = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/head.jpg"))));
        Drawable buttonDownEagle = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/head.jpg"))));
        eagleButton = new ImageButton(buttonUpEagle, buttonDownEagle);
        eagleButton.setPosition(460, 10);
        eagleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!eagleButton.isChecked()) {
                    placingEnabled = false;
                } else {
                    cementButton.setChecked(false);
                    steelButton.setChecked(false);
                    woodenButton.setChecked(false);
                    placingEnabled = true;
                }
            }
        });
        stage.addActor(eagleButton);
        eagleCounterLabel = new Label("Eagle: " + countersBarriers.getEagleCounter(), skin);
        eagleCounterLabel.setPosition(460, 90);
        stage.addActor(eagleCounterLabel);
    }
        public void setupButtonsAttacker(){
            Drawable fireChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/fire1.png"))));
            fireButton = new ImageButton(fireChoose);
            fireButton.setPosition(1500, 10);
            fireButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!fireButton.isChecked()) {
                        attackEnabled = false;

                    } else {
                        waterButton.setChecked(false);
                        bombButton.setChecked(false);
                        attackEnabled = true;
                    }
                }
            });
            stage.addActor(fireButton);

            Drawable waterChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Water12.png"))));
            waterButton = new ImageButton(waterChoose);
            waterButton.setPosition(1800, 10);
            waterButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!waterButton.isChecked()) {
                        attackEnabled = false;

                    } else {
                        fireButton.setChecked(false);
                        bombButton.setChecked(false);
                        attackEnabled = true;
                    }
                }
            });
            stage.addActor(waterButton);

            Drawable bombChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Bomb1.png"))));
            bombButton = new ImageButton(bombChoose);
            bombButton.setPosition(1000, 10);
            bombButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!bombButton.isChecked()) {
                        attackEnabled = false;

                    } else {
                        fireButton.setChecked(false);
                        waterButton.setChecked(false);
                        attackEnabled = true;
                    }
                }
            });
            stage.addActor(bombButton);
        }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();
        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(1600, 0);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar la pantalla a LoginScreen
                game.setScreen(new LoginScreen(game, user2Manager));
            }
        });
        fullNameLabel = new Label(user.getFullName(), skin);
        fullNameLabel.setColor(Color.BLACK);
        fullNameLabel.setPosition(280, 1000);
        usernameLabel = new Label(user.getUsername(), skin);
        usernameLabel.setColor(Color.BLACK);
        usernameLabel.setPosition(280, 980);

        Label userInfo = new Label("User information:", skin);
        userInfo.setColor(Color.BLACK);
        userInfo.setPosition(200, 1020);
        Label player2Name = new Label("unknown player 2", skin);
        player2Name.setColor(Color.BLACK);
        player2Name.setPosition(1500, 950);
        Label defenderLabel = new Label("Defender", skin);
        defenderLabel.setColor(Color.BLACK);
        defenderLabel.setPosition(1200, 550);
        Label attackerLabel = new Label("Attacker", skin);
        attackerLabel.setColor(Color.BLACK);
        attackerLabel.setPosition(1200, 200);
        Label fullnameLabel = new Label("Fullname: ", skin);
        fullnameLabel.setColor(Color.BLACK);
        fullnameLabel.setPosition(200, 1000);
        Label usernameLabel = new Label("Username: ", skin);
        usernameLabel.setColor(Color.BLACK);
        usernameLabel.setPosition(200, 980);

        Texture eagleTexture = new Texture(Gdx.files.internal("assets/aguilagod.png"));
        Texture goblinTexture = new Texture(Gdx.files.internal("assets/duendegod.png"));
        Texture userimageTexture = new Texture(Gdx.files.local("data/imgs/" + user.getImage()));
        Texture lineaRecta = new Texture("negro2.jpg");
        Texture circulo = new Texture("blanco.png");
        bulletTexture = new Texture("bala.PNG");
        playerTexture = new Texture("SSF6.png");
        fireTexture = new Texture("fire1.PNG");
        waterTexture = new Texture("Water12.png");
        bombTexture = new Texture("Bomb1.png");

        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(playerX, playerY);

        bulletSprite = new Sprite(bulletTexture);

        playerImage = new Image(playerSprite);
        playerImage.setPosition(1300, 500);
        bulletImage = new Image(bulletSprite);

        Image image1 = new Image(eagleTexture);
        image1.setPosition(1000, 600);
        Image image2 = new Image(goblinTexture);
        image2.setPosition(1000, 200);

        Image userImage = new Image(userimageTexture);
        Image maskImage = new Image(circulo);

        Image lineaVertical = new Image(lineaRecta);
        lineaVertical.setPosition(Gdx.graphics.getWidth() / 2, 0);
        lineaVertical.setSize(2, 920);

        Image lineaHorizontal = new Image(lineaRecta);
        lineaHorizontal.setPosition(0, 920);
        lineaHorizontal.setSize(Gdx.graphics.getWidth(), 2);

        Image lineaHorizontalboton = new Image(lineaRecta);
        lineaHorizontalboton.setPosition(0, 110);
        lineaHorizontalboton.setSize(Gdx.graphics.getWidth(), 2);

        userImage.setPosition(30, 920);
        userImage.setSize(150, 150);

        maskImage.setSize(userImage.getWidth(), userImage.getHeight());
        //maskImage.setPosition(posX, posY); // Ajusta la posición según tus necesidades

        stage.addActor(backButton);
        stage.addActor(userInfo);
        stage.addActor(player2Name);
        stage.addActor(defenderLabel);
        stage.addActor(attackerLabel);
        stage.addActor(fullnameLabel);
        stage.addActor(usernameLabel);
        stage.addActor(image1);
        stage.addActor(image2);
        stage.addActor(fullNameLabel);
        stage.addActor(this.usernameLabel);
        stage.addActor(userImage);
        //stage.addActor(maskImage);

        stage.addActor(lineaVertical);
        stage.addActor(lineaHorizontal);
        stage.addActor(lineaHorizontalboton);
        stage.addActor(playerImage);

        arrayGUIElements.add(fullNameLabel);
        arrayGUIElements.add(usernameLabel);
        arrayGUIElements.add(defenderLabel);
        applyColorPalette();

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    if (woodenButton.isChecked()) {
                        gameScreenFeatures.addWood(x, y);
                    } else if (cementButton.isChecked()) {
                        gameScreenFeatures.addCement(x, y);
                    } else if (steelButton.isChecked()) {
                        gameScreenFeatures.addSteel(x, y);
                    } else if (eagleButton.isChecked()) {
                        gameScreenFeatures.addEagle(x, y);
                    }
                }
                return true;
            }
        });
    }

    public boolean isPlacingEnabled() {
        return placingEnabled;
    }

    public void updateCounterLabel() {
        woodCounterLabel.setColor(Color.RED);
        woodCounterLabel.setText("wood barriers: " + countersBarriers.getWoodCounter());
        cementCounterLabel.setColor(Color.RED);
        cementCounterLabel.setText("cement barriers: " + countersBarriers.getCementCounter());
        steelCounterLabel.setColor(Color.RED);
        steelCounterLabel.setText("steel barriers: " + countersBarriers.getSteelCounter());
        eagleCounterLabel.setColor(Color.RED);
        eagleCounterLabel.setText("Eagle: " + countersBarriers.getEagleCounter());
    }


    private void applyColorPalette() {
        fullNameLabel.setColor(Color.BLUE);
        if (user.getSelectedColorPalette().equals("Palette 1")) {
            for (Actor element : arrayGUIElements) {
                if (element instanceof Label) {
                    Label label = (Label) element;
                    label.setColor(0.2f, 0.2f, 0.8f, 0.5f); // Blue with transparency
                } else if (element instanceof TextButton) {
                    TextButton button = (TextButton) element;
                    button.setColor(0.2f, 0.2f, 0.8f, 0.5f); // Blue with transparency
                }
            }
        } else if (user.getSelectedColorPalette().equals("Palette 2")) {
            for (Actor elements : arrayGUIElements) {
                elements.setColor(Color.ORANGE);
            }
        }
    }

    @Override
    public void render(float delta) {

        Color backgroundColor = new Color(0.96f, 0.96f, 0.86f, 1);
        ScreenUtils.clear(backgroundColor);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Array<User2> users = user2Manager.read();
        usernameLabel.setText(user.getUsername());
        fullNameLabel.setText(user.getFullName());
        updateCounterLabel();

        if (isShooting) {
            bulletX -= bulletSpeed * Gdx.graphics.getDeltaTime();//Donde la bala va a ser lanzada
            bulletImage.setPosition(bulletX, bulletY);
            if (fireButton.isChecked()) {
                bulletSprite.setTexture(fireTexture);
                stage.addActor(bulletImage);
            } else if (waterButton.isChecked()) {
                bulletSprite.setTexture(waterTexture);
                stage.addActor(bulletImage);
            }
            else if (bombButton.isChecked()) {
                bulletSprite.setTexture(bombTexture);
                stage.addActor(bulletImage);
            }

            // Si la bala salió de la pantalla la elimina
            if (bulletX < -bulletSprite.getWidth()) {
                isShooting = false;
            }
        }

        if (isCollide) {
            //collisionSprite.setPosition(bulletX-50, bulletY);
            //collisionSprite.setPosition(bulletX-15, bulletY);
            //collisionSprite.draw(batch);
            //isShooting = false;


            // Si la bala salió de la pantalla la elimina
            if (bulletX < -bulletSprite.getWidth()) {
                isShooting = false;
            }
        }

        handleInput();
        stage.draw();
    }


    public void handleInput() {

        Rectangle bulletBounds = new Rectangle(bulletX, bulletY, bulletSprite.getWidth(), bulletSprite.getHeight());

        for (Image barrierImage : gameScreenFeatures.getBarrierImages()) {
            float barrierX = barrierImage.getX();
            float barrierY = barrierImage.getY();
            float barrierWidth = barrierImage.getWidth();
            float barrierHeight = barrierImage.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                // Colisión detectada, aquí puedes hacer lo que necesites, por ejemplo, imprimir un mensaje
                isShooting = false; // Desactivar la bala
                isCollide = true;

            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R) && !isShooting) {
            // Inicia el disparo de la bala desde la posición del player.
            bulletX = playerX + playerSprite.getWidth();
            bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
            isShooting = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && playerY < Gdx.graphics.getHeight() - playerSprite.getHeight()) {
            System.out.println("W");
            if (playerY + playerSprite.getHeight() + speed * Gdx.graphics.getDeltaTime() <= 920) {
                playerY += speed * Gdx.graphics.getDeltaTime();
                player = new Texture("WalkR1.png");
                playerSprite.setTexture(new Texture("WalkR1.png"));
                playerImage.setPosition(playerX, playerY);
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.S) && playerY > 0) {
            System.out.println("S");
            playerY -= speed * Gdx.graphics.getDeltaTime();
            player = new Texture("WalkR2.png");
            playerSprite.setTexture(new Texture("WalkR2.png"));
            playerImage.setPosition(playerX, playerY);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.A) && playerX > 0) {
            System.out.println("A");
            float centerX = Gdx.graphics.getWidth() / 2.0f;
            if (playerX - speed * Gdx.graphics.getDeltaTime() >= centerX) {
                playerX -= speed * Gdx.graphics.getDeltaTime();
                player = new Texture("WalkR3.png");
                playerSprite.setTexture(new Texture("WalkR3.png"));
                playerImage.setPosition(playerX, playerY);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && playerX < Gdx.graphics.getWidth() - playerSprite.getWidth()) {
            System.out.println("D");
            playerX += speed * Gdx.graphics.getDeltaTime();

            player = new Texture("WalkR4.png");
            playerSprite.setTexture(new Texture("WalkR4.png"));
            playerImage.setPosition(playerX, playerY);

        }

        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            player = new Texture("SSF6.png");
            playerSprite.setTexture(new Texture("SSF6.png"));
        }

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
        stage.dispose();
    }
}