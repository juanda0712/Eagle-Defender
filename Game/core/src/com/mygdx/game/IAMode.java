package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import java.util.*;
import java.util.List;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.SongInfo;
import com.mygdx.models.User2;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.SpotifyAuthenticator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import com.mygdx.utils.SpotifyAuthenticator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class IAMode implements Screen {
    private final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;
    int screenWidth;
    int screenHeight;
    private User2 user;
    private Array<Image> randomIA;
    private Array<Image> woodSP;
    private Array<Image> cementSP;
    private Array<Image> steelSP;
    private Array<Image> bullets;
    private Image goblin;
    private float goblinTimer;
    private float goblinX, goblinY;
    private int consecutiveAttempts = 0;
    private Image aguilagodImage;
    private Image aguilaGodImage;
    private ImageButton woodenButton;
    private ImageButton cementButton;
    private ImageButton steelButton;
    private ImageButton eagleButton;
    private Label woodCounterSP;
    private Label cementCounterSP;
    private Label steelCounterSP;
    private Label eagleCounterSP;
    private int woodCounter = 10;
    private int cementCounter = 10;
    private int steelCounter = 10;
    private int eagleCounter = 1;
    private ImageButton waterButton;
    private ImageButton fireButton;
    private ImageButton bombButton;
    private boolean barriersEnabled = false;
    private boolean attacksEnabled = false;
    private boolean aguilaGodPlaced = false;
    private boolean defenderSelected = false;
    private boolean showPlayer = false;
    private int BombCollide = 0;
    private int fireCollide = 0;
    //private int waterCollide = 0;
    private ImageButton Defender;
    private ImageButton Attacker;
    SpriteBatch batch;
    private String playerTexturePath = "Idle.png";
    private Image playerImage;
    private float playerX = 0, playerY = 0;
    private float speed = 400.0f;
    private Sound explosionSound;
    private int fireCount = 0;
    private int waterCount = 0;
    private int bombCount = 0;
    private final int maxFire = 10;
    private final int maxWater = 10;
    private final int maxBomb = 10;
    private float goblinWaitTimer = 30.0f;
    Map<Rectangle, Integer> barrierCounters = new HashMap<>();
    private Map<Image, Integer> waterCollide = new HashMap<>();
    List<Rectangle> barrierRectangles = new ArrayList<>();
    Sprite bulletSprite;
    Texture bulletTexture;
    Texture fireTexture;
    Texture waterTexture;
    Texture bombTexture;
    Image bulletImage;
    float bulletX;
    float bulletY;
    boolean isShooting = false;
    boolean isCollide = false;
    float bulletSpeed = 500.0f;
    private Texture currentBulletTexture;


    public IAMode(final MainController game, User2 user) {
        System.out.println(user);
        this.game = game;
        this.user = user;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        goblinTimer = 0;
        randomIA = new Array<>();
        bullets = new Array<>();
        woodSP = new Array<>();
        cementSP = new Array<>();
        steelSP = new Array<>();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("audExplosion.mp3"));
        batch = new SpriteBatch();
        setupMode();
        setupUIElements();
    }

    //----------------------------------------side selection--------------------------------------------
    private void setupMode() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        Texture chooseTexture = new Texture(Gdx.files.internal("tk.png"));
        Image chooseImage = new Image(chooseTexture);
        chooseImage.setSize(800, 680);
        float chooseWidth = 500;
        float chooseHeight = 100;
        float xs = (screenWidth - chooseWidth) / 2;
        float ys = 0;
        chooseImage.setSize(chooseWidth, chooseHeight);
        chooseImage.setPosition(xs, ys);
        stage.addActor(chooseImage);
        Texture morfeoTexture = new Texture(Gdx.files.internal("morfeo.png"));
        Image morfeo = new Image(morfeoTexture);
        float morfeoWidth = morfeo.getWidth();
        float morfeoHeight = morfeo.getHeight();
        float xy = (screenWidth - morfeoWidth) / 2;
        float yx = (screenHeight - morfeoHeight) / 2;
        morfeo.setPosition(xy, yx);
        stage.addActor(morfeo);

        // ImageButton Defender en el centro de la mitad izquierda de la pantalla
        Drawable defenderImage = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/sideD.png"))));
        Defender = new ImageButton(defenderImage);
        float defenderWidth = Defender.getWidth();
        float defenderHeight = Defender.getHeight();
        float x = (screenWidth / 2) - (screenWidth / 4) - defenderWidth / 2;
        float y = (screenHeight - defenderHeight) / 2;
        Defender.setPosition(x, y);
        Defender.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                defenderSelected = true;
                Defender.remove();
                Attacker.remove();
                chooseImage.remove();
                morfeo.remove();
                if (defenderSelected) {
                    setupButtonsDefender();
                    goblin = new Image(new Texture("Idle.png"));
                    float goblinWidth = 70;
                    float goblinHeight = 87;
                    float minGoblinY = 120;
                    float maxGoblinY = screenHeight - 120 - goblinHeight;
                    float goblinX = MathUtils.random(screenWidth / 2, screenWidth - goblinWidth);
                    float goblinY = MathUtils.random(minGoblinY, maxGoblinY);
                    goblin.setPosition(goblinX, goblinY);
                    stage.addActor(goblin);
                }
            }
        });
        stage.addActor(Defender);

        // ImageButton Attacker en el centro de la mitad derecha de la pantalla
        Drawable attackerImage = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/sideA.png"))));
        Attacker = new ImageButton(attackerImage);
        float attackerWidth = Attacker.getWidth();
        float attackerHeight = Attacker.getHeight();
        float zx = screenWidth / 2 + (screenWidth / 4) - attackerWidth / 2;
        float zy = (screenHeight - attackerHeight) / 2;
        Attacker.setPosition(zx, zy);
        Attacker.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                defenderSelected = false;
                Defender.remove();
                Attacker.remove();
                chooseImage.remove();
                morfeo.remove();
                if (!defenderSelected) {
                    setupButtonsAttacker();
                    //showPlayer = true;
                    float halfScreenWidth = screenWidth / 2;
                    Texture aguilagodTexture = new Texture("aguilagod.png");
                    TextureRegion aguilagodRegion = new TextureRegion(aguilagodTexture);
                    TextureRegionDrawable aguilagodDrawable = new TextureRegionDrawable(aguilagodRegion);
                    aguilagodImage = new Image(aguilagodDrawable);
                    aguilagodImage.setSize(80, 80);
                    float aguilagodY;
                    float minY = 120;
                    float maxY = screenHeight - 120 - aguilagodImage.getHeight();
                    aguilagodY = MathUtils.random(minY, maxY);
                    aguilagodImage.setPosition(0, aguilagodY);
                    stage.addActor(aguilagodImage);
                    woodSP.add(aguilagodImage);
                    int totalWoodSP = 10;
                    int totalCementSP = 10;
                    int totalSteelSP = 10;
                    for (int i = 0; i < totalWoodSP; i++) {
                        addRandomImageIA("wood.jpg", aguilagodImage, halfScreenWidth, screenHeight);
                    }
                    for (int i = 0; i < totalCementSP; i++) {
                        addRandomImageIA("cement.jpg", aguilagodImage, halfScreenWidth, screenHeight);
                    }
                    for (int i = 0; i < totalSteelSP; i++) {
                        addRandomImageIA("steel.jpg", aguilagodImage, halfScreenWidth, screenHeight);
                    }
                    bulletTexture = new Texture("bala.PNG");
                    fireTexture = new Texture("Fire1.PNG");
                    waterTexture = new Texture("Water12.png");
                    bombTexture = new Texture("Bomb1.png");
                    bulletSprite = new Sprite(bulletTexture);
                    bulletImage = new Image(bulletSprite);
                }
            }
        });
        stage.addActor(Attacker);
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();

    }


    //-------------------------------------------defender side---------------------------------------------
    private void addRandomImageIA(String imageFileName, Image referenceImage, float halfScreenWidth, float screenHeight) {
        Texture imageTexture = new Texture(imageFileName);
        TextureRegion imageRegion = new TextureRegion(imageTexture);
        TextureRegionDrawable imageDrawable = new TextureRegionDrawable(imageRegion);
        Image image = new Image(imageDrawable);

        boolean overlapped;
        float randomX, randomY;

        String imageType = "unknown";
        if (imageFileName.equals("wood.jpg")) {
            imageType = "wood";
        } else if (imageFileName.equals("cement.jpg")) {
            imageType = "cement";
        } else if (imageFileName.equals("steel.jpg")) {
            imageType = "steel";
        }

        do {
            overlapped = false;
            randomX = MathUtils.random(referenceImage.getX() + referenceImage.getWidth(), halfScreenWidth - image.getWidth());
            randomY = MathUtils.random(120, screenHeight - 120 - image.getHeight());

            for (Image existingImage : woodSP) {
                if (existingImage.getX() < randomX + image.getWidth() &&
                        randomX < existingImage.getX() + existingImage.getWidth() &&
                        existingImage.getY() < randomY + image.getHeight() &&
                        randomY < existingImage.getY() + existingImage.getHeight()) {
                    overlapped = true;
                    break;
                }
            }

            for (Image existingImage : cementSP) {
                if (existingImage.getX() < randomX + image.getWidth() &&
                        randomX < existingImage.getX() + existingImage.getWidth() &&
                        existingImage.getY() < randomY + image.getHeight() &&
                        randomY < existingImage.getY() + existingImage.getHeight()) {
                    overlapped = true;
                    break;
                }
            }

            for (Image existingImage : steelSP) {
                if (existingImage.getX() < randomX + image.getWidth() &&
                        randomX < existingImage.getX() + existingImage.getWidth() &&
                        existingImage.getY() < randomY + image.getHeight() &&
                        randomY < existingImage.getY() + existingImage.getHeight()) {
                    overlapped = true;
                    break;
                }
            }
        } while (overlapped);

        image.setPosition(randomX, randomY);
        image.setName(imageFileName);
        stage.addActor(image);

        if (imageType.equals("wood")) {
            woodSP.add(image);
        } else if (imageType.equals("cement")) {
            cementSP.add(image);
        } else if (imageType.equals("steel")) {
            steelSP.add(image);
        }
    }


    private void setupButtonsDefender() {
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
                    barriersEnabled = false;
                } else {
                    cementButton.setChecked(false);
                    steelButton.setChecked(false);
                    eagleButton.setChecked(false);
                    barriersEnabled = true;
                }
            }
        });
        stage.addActor(woodenButton);
        woodCounterSP = new Label("wood barriers: ", skin); //////
        woodCounterSP.setColor(Color.RED);
        woodCounterSP.setPosition(10, 80);
        stage.addActor(woodCounterSP);

        // ImageButton de barrera de cemento, y su label de contador
        Drawable buttonUpCement = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/cement.jpg"))));
        Drawable buttonDownCement = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/cementD.jpg"))));
        cementButton = new ImageButton(buttonUpCement, buttonDownCement);
        cementButton.setPosition(160, 10);
        cementButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!cementButton.isChecked()) {
                    barriersEnabled = false;
                } else {
                    woodenButton.setChecked(false);
                    steelButton.setChecked(false);
                    eagleButton.setChecked(false);
                    barriersEnabled = true;
                }
            }
        });
        stage.addActor(cementButton);
        cementCounterSP = new Label("cement barriers: ", skin); ////////
        cementCounterSP.setColor(Color.RED);
        cementCounterSP.setPosition(160, 80);
        stage.addActor(cementCounterSP);

        // ImageButton de barrera de acero, y su label de contador
        Drawable buttonUpSteel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/steel.jpg"))));
        Drawable buttonDownSteel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/steelD.jpg"))));
        steelButton = new ImageButton(buttonUpSteel, buttonDownSteel);
        steelButton.setPosition(320, 10);
        steelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!steelButton.isChecked()) {
                    barriersEnabled = false;
                } else {
                    woodenButton.setChecked(false);
                    cementButton.setChecked(false);
                    eagleButton.setChecked(false);
                    barriersEnabled = true;
                }
            }
        });
        stage.addActor(steelButton);
        steelCounterSP = new Label("steel barriers: ", skin); ////
        steelCounterSP.setColor(Color.RED);
        steelCounterSP.setPosition(320, 80);
        stage.addActor(steelCounterSP);

        //ImageButton de Eagle, y su label de contador
        Drawable buttonUpEagle = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/head.jpg"))));
        Drawable buttonDownEagle = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/head.jpg"))));
        eagleButton = new ImageButton(buttonUpEagle, buttonDownEagle);
        eagleButton.setPosition(460, 10);
        eagleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!eagleButton.isChecked()) {
                    barriersEnabled = false;
                } else {
                    cementButton.setChecked(false);
                    steelButton.setChecked(false);
                    woodenButton.setChecked(false);
                    barriersEnabled = true;
                }
            }
        });
        stage.addActor(eagleButton);
        eagleCounterSP = new Label("Eagle: ", skin);  //////
        eagleCounterSP.setColor(Color.RED);
        eagleCounterSP.setPosition(460, 90);
        stage.addActor(eagleCounterSP);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT && barriersEnabled) {
                    if (woodenButton.isChecked()) {
                        addWoodSP(x, y);
                    } else if (cementButton.isChecked()) {
                        addCementSP(x, y);
                    } else if (steelButton.isChecked()) {
                        addSteelSP(x, y);
                    } else if (eagleButton.isChecked()) {
                        addEagleSP(x, y);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void addWoodSP(float x, float y) {
        if (barriersEnabled && woodCounter > 0 && woodenButton.isChecked()) {
            String selectedTexture = user.getTexture();
            String imagePath = "assets/wood.jpg";
            float maxY = stage.getHeight() - 120;
            float minY = 120;
            if (x < stage.getWidth() / 2 && y >= minY && y <= maxY) {
                if ("Smooth".equals(selectedTexture)) {
                    imagePath = "assets/woodSQ.jpg";
                } else if ("Rocky".equals(selectedTexture)) {
                    imagePath = "assets/woodST.jpg";
                } else if ("Bricked".equals(selectedTexture)) {
                    imagePath = "assets/woodBR.jpg";
                }
                Texture newImageTexture = new Texture(Gdx.files.internal(imagePath));
                Image newImage = new Image(newImageTexture);
                newImage.setPosition(x, y);

                boolean canPlace = true;
                for (Image placedImage : woodSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : cementSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : steelSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {
                    stage.addActor(newImage);
                    woodSP.add(newImage);
                    minusWoodCounter();
                }
            }
        }
    }

    private void minusWoodCounter() {
        if (woodCounter > 0) {
            woodCounter--;

        }
    }

    private void addCementSP(float x, float y) {
        if (barriersEnabled && cementCounter > 0 && cementButton.isChecked()) {
            String selectedTexture = user.getTexture();
            String imagePath = "assets/cement.jpg";
            float maxY = stage.getHeight() - 120;
            float minY = 120;
            if (x < stage.getWidth() / 2 && y >= minY && y <= maxY) {
                if ("Smooth".equals(selectedTexture)) {
                    imagePath = "assets/cementSQ.jpg";
                } else if ("Rocky".equals(selectedTexture)) {
                    imagePath = "assets/cementST.jpg";
                } else if ("Bricked".equals(selectedTexture)) {
                    imagePath = "assets/cementBR.jpg";
                }
                Texture newImageTexture = new Texture(Gdx.files.internal(imagePath));
                Image newImage = new Image(newImageTexture);
                newImage.setPosition(x, y);

                boolean canPlace = true;
                for (Image placedImage : woodSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : cementSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : steelSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {
                    stage.addActor(newImage);
                    cementSP.add(newImage);
                    minusCementCounter();
                }
            }
        }
    }

    private void minusCementCounter() {
        if (cementCounter > 0) {
            cementCounter--;

        }
    }

    private void addSteelSP(float x, float y) {
        if (barriersEnabled && steelCounter > 0 && steelButton.isChecked()) {
            String selectedTexture = user.getTexture();
            String imagePath = "assets/steel.jpg";
            float maxY = stage.getHeight() - 120;
            float minY = 120;
            if (x < stage.getWidth() / 2 && y >= minY && y <= maxY) {
                if ("Smooth".equals(selectedTexture)) {
                    imagePath = "assets/steelSQ.jpg";
                } else if ("Rocky".equals(selectedTexture)) {
                    imagePath = "assets/steelST.jpg";
                } else if ("Bricked".equals(selectedTexture)) {
                    imagePath = "assets/steelBR.jpg";
                }
                Texture newImageTexture = new Texture(Gdx.files.internal(imagePath));
                Image newImage = new Image(newImageTexture);
                newImage.setPosition(x, y);

                boolean canPlace = true;
                for (Image placedImage : woodSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : cementSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : steelSP) {
                    float newX = newImage.getX();
                    float newY = newImage.getY();
                    float newWidth = newImage.getWidth();
                    float newHeight = newImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {
                    stage.addActor(newImage);
                    steelSP.add(newImage);
                    minusSteelCounter();
                }
            }
        }
    }

    private void minusSteelCounter() {
        if (steelCounter > 0) {
            steelCounter--;

        }
    }

    private void addEagleSP(float x, float y) {
        if (barriersEnabled && eagleCounter > 0 && eagleButton.isChecked()) {
            float maxY = stage.getHeight() - 120;
            float minY = 120;
            if (!aguilaGodPlaced && x < stage.getWidth() / 2 && y >= minY && y <= maxY) {
                Texture aguilaGodTexture = new Texture(Gdx.files.internal("assets/aguilagod.png"));
                aguilaGodImage = new Image(aguilaGodTexture);
                aguilaGodImage.setPosition(x, y);
                aguilaGodImage.setSize(80, 80);

                boolean canPlace = true;
                for (Image placedImage : woodSP) {

                    float newX = aguilaGodImage.getX();
                    float newY = aguilaGodImage.getY();
                    float newWidth = aguilaGodImage.getWidth();
                    float newHeight = aguilaGodImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                for (Image placedImage : cementSP) {
                    float newX = aguilaGodImage.getX();
                    float newY = aguilaGodImage.getY();
                    float newWidth = aguilaGodImage.getWidth();
                    float newHeight = aguilaGodImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }


                for (Image placedImage : steelSP) {
                    float newX = aguilaGodImage.getX();
                    float newY = aguilaGodImage.getY();
                    float newWidth = aguilaGodImage.getWidth();
                    float newHeight = aguilaGodImage.getHeight();

                    float placedX = placedImage.getX();
                    float placedY = placedImage.getY();
                    float placedWidth = placedImage.getWidth();
                    float placedHeight = placedImage.getHeight();

                    if (newX < placedX + placedWidth &&
                            newX + newWidth > placedX &&
                            newY < placedY + placedHeight &&
                            newY + newHeight > placedY) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {
                    stage.addActor(aguilaGodImage);
                    woodSP.add(aguilaGodImage);
                    minusEagleCounter();
                    aguilaGodPlaced = true;
                }
            }
        }
    }


    private void minusEagleCounter() {
        if (eagleCounter > 0) {
            eagleCounter--;

        }
    }

    private boolean isBarriersEnabled() {
        return barriersEnabled;
    }

    //----------------------------------------------attacker side--------------------------------------------------

    private void randomMovement(float delta) {
        float halfScreenWidth = screenWidth / 2;
        float goblinWidth = 70;
        float goblinHeight = 87;

        if (!aguilaGodPlaced) {
            goblinWaitTimer -= delta;
            if (goblinWaitTimer <= 0) {
                aguilaGodPlaced = true;
                goblinY = aguilaGodImage.getY();
            }
        } else {
            if (consecutiveAttempts >= 2) {
                goblinY = aguilaGodImage.getY();
                consecutiveAttempts = 0;
            } else {
                if (MathUtils.randomBoolean(1f / 5f)) {
                    goblinY = aguilaGodImage.getY();
                    consecutiveAttempts = 0;
                } else {
                    goblinY = MathUtils.random(120, screenHeight - 120 - goblinHeight);
                    consecutiveAttempts++;
                }
            }

            goblinX = MathUtils.random(halfScreenWidth, screenWidth - goblinWidth);
            goblin.setPosition(goblinX, goblinY);
            goblinTimer = 2.0f;
        }
    }


    private void wasdPJ() {
        if (!showPlayer) {
            return;
        }


        if (playerImage == null) {
            Texture playerTexture = new Texture(Gdx.files.internal(playerTexturePath));
            playerImage = new Image(playerTexture);
            playerImage.setSize(70, 87);
            stage.addActor(playerImage);
            playerX = 1300;
            playerY = 500;
        }
        float maxY = stage.getHeight() - 120;
        float minY = 120;
        float centerX = stage.getWidth() / 2;
        float playerHeight = 87;
        float playerWidth = 70;

        Rectangle bulletBounds = new Rectangle(bulletX, bulletY, bulletSprite.getWidth(), bulletSprite.getHeight());

        for (Image barrierImage : woodSP) {
            float barrierX = barrierImage.getX();
            float barrierY = barrierImage.getY();
            float barrierWidth = barrierImage.getWidth();
            float barrierHeight = barrierImage.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            boolean containsBarrier = false;
            for (Rectangle existingBarrier : barrierRectangles) {
                if (existingBarrier.width == barrierWidth && existingBarrier.height == barrierHeight) {
                    containsBarrier = true;
                    break;
                }
            }

            if (!containsBarrier) {
                barrierRectangles.add(barrierBounds);
                barrierCounters.put(barrierBounds, 3);
            }

            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                isShooting = false;
                isCollide = true;

                if (bulletImage.getDrawable() instanceof TextureRegionDrawable) {
                    TextureRegionDrawable drawable = (TextureRegionDrawable) bulletImage.getDrawable();
                    Texture texture = drawable.getRegion().getTexture();

                    if (texture == fireTexture || texture == bombTexture || texture == waterTexture) {
                        woodSP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        Actions.removeActor(bulletImage);
                    }
                }

                Integer currentCounter = barrierCounters.get(barrierBounds);
                if (currentCounter != null && currentCounter > 0) {
                    barrierCounters.put(barrierBounds, currentCounter - 1);
                    System.out.println(currentCounter);
                }
            }
        }


        for (Image barrierSteel : steelSP) {
            float barrierX = barrierSteel.getX();
            float barrierY = barrierSteel.getY();
            float barrierWidth = barrierSteel.getWidth();
            float barrierHeight = barrierSteel.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            boolean containsBarrier = false;
            for (Rectangle existingBarrier : barrierRectangles) {
                if (existingBarrier.width == barrierWidth && existingBarrier.height == barrierHeight) {
                    containsBarrier = true;
                    break;
                }
            }

            if (!containsBarrier) {
                barrierRectangles.add(barrierBounds);
                barrierCounters.put(barrierBounds, 3);
            }

            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                isShooting = false;
                isCollide = true;

                if (currentBulletTexture == waterTexture) {
                    waterCount++;

                    if (waterCount >= 2) {
                        steelSP.removeValue(barrierSteel, true);
                        barrierSteel.remove();
                    }
                } else if (currentBulletTexture == fireTexture || currentBulletTexture == bombTexture) {
                    steelSP.removeValue(barrierSteel, true);
                    barrierSteel.remove();
                }

                Integer currentCounter = barrierCounters.get(barrierBounds);
                if (currentCounter != null && currentCounter > 0) {
                    barrierCounters.put(barrierBounds, currentCounter - 1);
                    System.out.println(currentCounter);
                }
            }
        }

        for (Image barrierImage : cementSP) {
            float barrierX = barrierImage.getX();
            float barrierY = barrierImage.getY();
            float barrierWidth = barrierImage.getWidth();
            float barrierHeight = barrierImage.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            boolean containsBarrier = false;
            for (Rectangle existingBarrier : barrierRectangles) {
                if (existingBarrier.width == barrierWidth && existingBarrier.height == barrierHeight) {
                    containsBarrier = true;
                    break;
                }
            }
            if (!containsBarrier) {
                barrierRectangles.add(barrierBounds);
                barrierCounters.put(barrierBounds, 3);
            }
            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                isShooting = false;
                isCollide = true;
                Integer currentCounter = barrierCounters.get(barrierBounds);
                if (currentCounter != null && currentCounter > 0) {
                    barrierCounters.put(barrierBounds, currentCounter - 1);
                    System.out.println(currentCounter);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && !isShooting) {
            bulletX = playerX + playerImage.getWidth();
            bulletY = playerY + playerImage.getHeight()/ 2 - bulletSprite.getHeight() / 2;
            isShooting = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (playerY + playerHeight + speed * Gdx.graphics.getDeltaTime() <= maxY) {
                playerY += speed * Gdx.graphics.getDeltaTime();
                playerTexturePath = "Back1.png";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (playerY >= minY) {
                playerY -= speed * Gdx.graphics.getDeltaTime();
                playerTexturePath = "Front2.png";
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && playerX > centerX) {
            if (playerX - speed * Gdx.graphics.getDeltaTime() >= centerX) {
                playerX -= speed * Gdx.graphics.getDeltaTime();
                playerTexturePath = "WalkR3.png";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (playerX + playerWidth + speed * Gdx.graphics.getDeltaTime() <= stage.getWidth()) {
                playerX += speed * Gdx.graphics.getDeltaTime();
                playerTexturePath = "WalkR3.png";
            }
        } else {
            playerTexturePath = "Idle.png";
        }
        playerImage.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(playerTexturePath)))));
        playerImage.setPosition(playerX, playerY);
    }


    private void setupButtonsAttacker(){
        showPlayer = true;
        Drawable fireChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Fire1.png"))));
        fireButton = new ImageButton(fireChoose);
        fireButton.setPosition(1500, 10);

        fireButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!fireButton.isChecked()) {
                    attacksEnabled = false;

                } else {
                    waterButton.setChecked(false);
                    bombButton.setChecked(false);
                    attacksEnabled = true;
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
                    attacksEnabled = false;

                } else {
                    fireButton.setChecked(false);
                    bombButton.setChecked(false);
                    attacksEnabled = true;
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
                    attacksEnabled = false;

                } else {
                    fireButton.setChecked(false);
                    waterButton.setChecked(false);
                    attacksEnabled = true;
                }
            }
        });
        stage.addActor(bombButton);
    }
    //------------------------------------------------------------------------------------------------------------------


    private Array<Image> getBarriers() {
        return randomIA;
    }

    private void Hitbox() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        if (defenderSelected) {
            if (goblinTimer > 0) {
                goblinTimer -= delta;
            }
            if (goblinTimer <= 0) {
                goblin.setPosition(-goblin.getWidth(), -goblin.getHeight());
                goblinTimer = 2.0f;
                randomMovement(delta);
            }
        }

        if (!defenderSelected){
            wasdPJ();
            if (isShooting) {
                bulletX -= bulletSpeed * Gdx.graphics.getDeltaTime();
                bulletImage.setPosition(bulletX, bulletY);
                Texture currentBulletTexture = bulletSprite.getTexture();
                if (fireButton.isChecked()) {
                    bulletSprite.setTexture(fireTexture);
                    stage.addActor(bulletImage);
                } else if (waterButton.isChecked()) {
                    bulletSprite.setTexture(waterTexture);
                    stage.addActor(bulletImage);
                    waterCount++;

                } else if (bombButton.isChecked()) {
                    bulletSprite.setTexture(bombTexture);
                    stage.addActor(bulletImage);
                }

                if (bulletX < -bulletSprite.getWidth()) {
                    isShooting = false;
                }
            }
            if (isCollide) {
                if (bulletX < -bulletSprite.getWidth()) {
                    isShooting = false;
                }
            }
        }
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
        stage.dispose();
    }
}