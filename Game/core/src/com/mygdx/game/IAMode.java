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
    boolean bulletCollided = false;
    boolean bulletCollidedcement = false;
    boolean bulletCollidedSteel = false;
    Map<Rectangle, Integer> barrierCounterscement = new HashMap<>();  // Mapa para mantener los contadores de las barreras

    Map<Rectangle, Integer> barrierCountersSteel = new HashMap<>();  // Mapa para mantener los contadores de las barreras
    List<Rectangle> barrierRectangles = new ArrayList<>();
    List<Rectangle> barrierRectanglescement = new ArrayList<>();

    List<Rectangle> barrierRectanglesSteel = new ArrayList<>();
    private int waterPowerCount = 3;
    private int firePowerCount = 2;
    private int bombPowerCount = 4;
    private Boolean fire = false;
    private Boolean water = false;
    private Boolean bomb = false;
    private float timer = 0; // Inicializa el temporizador en 0 segundos
    private final float resetInterval = 30.0f;
    private float waterCounterTimer = 0; // Contador para el WaterCounter
    private int waterCounterDrops = 0; // Contador de caídas del WaterCounter
    private int fireCounterDrops = 0; // Contador de caídas del WaterCounter
    private int bombCounterDrops = 0; // Contador de caídas del WaterCounter
    private List<Float> waterCounterDropsTimes = new ArrayList<>();
    private List<Float> fireCounterDropsTimes = new ArrayList<>();
    private List<Float> bombCounterDropsTimes = new ArrayList<>();
    private boolean isTimerActive = false;
    private boolean isTimerActivelabel = false;
    private float elapsedTimeWater;
    private float elapsedTimeFire;
    private float elapsedTimeBomb;
    private boolean flagAux = false;
    private boolean fireflagAux = false;
    private boolean bombflagAux = false;
    private int remainingTime = 60;
    private List<Float> waterPowerTimers = new ArrayList<>();
    private List<Float> firePowerTimers = new ArrayList<>();
    private List<Float> bombPowerTimers = new ArrayList<>();
    private int maxWaterPowerCount = 3;
    private int maxBombPowerCount = 4;
    private int maxFirePowerCount = 2;
    private List<Float> auxList = new ArrayList<Float>();
    private List<Float> fireauxList = new ArrayList<Float>();
    private List<Float> bombauxList = new ArrayList<Float>();
    private Label timerLabel;
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
    private AtomicReference<SpotifyAuthenticator> spotifyReference;
    private Label waterCounterLabel;
    private Label fireCounterLabel;
    private Label bombCounterLabel;
    private boolean songInfoFlag = false;
    private SongInfo songInfo;


    public IAMode(final MainController game, User2 user, AtomicReference<SpotifyAuthenticator> spotifyReference) {
        System.out.println(user);
        this.game = game;
        this.user = user;
        this.spotifyReference = spotifyReference;
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
        /*Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            isTimerActive = true;

                            spotifyReference.get().playSong("hijo+de+la+noche");

                        }
                    }, 15); // 60 segundos (1 minuto)*/
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
                    Label.LabelStyle labelStyle = new Label.LabelStyle();
                    labelStyle.font = new BitmapFont(); // Configura el estilo de fuente
                    timerLabel = new Label("Time: " + remainingTime, labelStyle);
                    timerLabel.setPosition(500, 500); // Posición en la pantalla
                    stage.addActor(timerLabel);
                    isTimerActivelabel = true;

                    timerLabel.setText("Time: " + remainingTime);

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
                    Label.LabelStyle labelStyle = new Label.LabelStyle();
                    labelStyle.font = new BitmapFont(); // Configura el estilo de fuente
                    timerLabel = new Label("Time: " + remainingTime, labelStyle);
                    timerLabel.setPosition(500, 500); // Posición en la pantalla
                    stage.addActor(timerLabel);
                    isTimerActivelabel = true;

                    timerLabel.setText("Time: " + remainingTime);

                  /*  Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            isTimerActive = true;



                            spotifyReference.get().playSong("hijo+de+la+noche");

                        }
                    }, 15); // 60 segundos (1 minuto)*/
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
            playerImage.setPosition(playerX, playerY);
        }
        float maxY = stage.getHeight() - 120;
        float minY = 120;
        float centerX = stage.getWidth() / 2;
        float playerHeight = 87;
        float playerWidth = 70;

        Rectangle bulletBounds = new Rectangle(bulletX, bulletY, bulletSprite.getWidth(), bulletSprite.getHeight());

        for (Image barrierImage : woodSP) {
            if (bulletCollided) {
                break;
            }
            float barrierX = barrierImage.getX();
            float barrierY = barrierImage.getY();
            float barrierWidth = barrierImage.getWidth();
            float barrierHeight = barrierImage.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            boolean containsBarrier = false;

            for (Rectangle existingBarrier : barrierRectangles) {
                if (existingBarrier.width == barrierWidth && existingBarrier.height == barrierHeight &&
                        existingBarrier.x == barrierX && existingBarrier.y == barrierY) {
                    containsBarrier = true;
                    break; // Salir del bucle una vez que se haya encontrado una coincidencia
                }
            }

            if (!containsBarrier) {
                // Agregar el rectángulo de la barrera a la lista y establecer el contador inicial en el mapa
                barrierRectangles.add(barrierBounds);
                barrierCounters.put(barrierBounds, 1);  // Puedes establecer el valor inicial que desees
            }

            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                // Colisión detectada, aquí puedes hacer lo que necesites, por ejemplo, imprimir un mensaje

                isShooting = false; // Desactivar la bala
                isCollide = true;
                bulletCollided = true;
                stage.getRoot().removeActor(bulletImage);

                Integer currentCounter = barrierCounters.get(barrierBounds);

                if (currentCounter != null && currentCounter > 0) {
                    barrierCounters.put(barrierBounds, currentCounter - 1);
                    barrierCounters.remove(barrierBounds);
                    woodSP.removeValue(barrierImage, true);
                    barrierImage.remove();
                    System.out.println(currentCounter);
                }

            }
            System.out.println(barrierCounters);

        }

        for (Image barrierImage : cementSP) {
            if (bulletCollidedcement) {
                break;
            }
            float barrierX = barrierImage.getX();
            float barrierY = barrierImage.getY();
            float barrierWidth = barrierImage.getWidth();
            float barrierHeight = barrierImage.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            boolean containsBarriercement = false;

            for (Rectangle existingBarrier : barrierRectanglescement) {
                if (existingBarrier.width == barrierWidth && existingBarrier.height == barrierHeight &&
                        existingBarrier.x == barrierX && existingBarrier.y == barrierY) {
                    containsBarriercement = true;
                    break; // Salir del bucle una vez que se haya encontrado una coincidencia
                }
            }

            if (!containsBarriercement) {
                // Agregar el rectángulo de la barrera a la lista y establecer el contador inicial en el mapa
                barrierRectanglescement.add(barrierBounds);
                barrierCounterscement.put(barrierBounds, 3);  // Puedes establecer el valor inicial que desees
            }

            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                // Colisión detectada, aquí puedes hacer lo que necesites, por ejemplo, imprimir un mensaje

                isShooting = false; // Desactivar la bala
                isCollide = true;
                bulletCollidedcement = true;
                stage.getRoot().removeActor(bulletImage);

                Integer currentCountercement = barrierCounterscement.get(barrierBounds);

                if (currentCountercement != null && currentCountercement > 0) {
                    if (bomb) {
                        barrierCounterscement.put(barrierBounds, currentCountercement - 3);
                        System.out.println(currentCountercement);
                        cementSP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        barrierCounterscement.remove(barrierBounds);
                    } else if (fire) {
                        if (currentCountercement == 3) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 2);
                        } else if (currentCountercement < 3) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 2);
                            cementSP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            barrierCounterscement.remove(barrierBounds);
                        }

                    } else if (water) {
                        if (currentCountercement == 3 || currentCountercement == 2) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 1);
                        } else if (currentCountercement == 1) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 1);
                            cementSP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            barrierCounterscement.remove(barrierBounds);

                        }

                    }
                }

            }
            System.out.println(barrierCounterscement);

        }

        for (Image barrierImage : steelSP) {
            if (bulletCollidedSteel) {
                break;
            }
            float barrierX = barrierImage.getX();
            float barrierY = barrierImage.getY();
            float barrierWidth = barrierImage.getWidth();
            float barrierHeight = barrierImage.getHeight();

            Rectangle barrierBounds = new Rectangle(barrierX, barrierY, barrierWidth, barrierHeight);

            boolean containsBarrierSteel = false;

            for (Rectangle existingBarrier : barrierRectanglesSteel) {
                if (existingBarrier.width == barrierWidth && existingBarrier.height == barrierHeight &&
                        existingBarrier.x == barrierX && existingBarrier.y == barrierY) {
                    containsBarrierSteel = true;
                    break; // Salir del bucle una vez que se haya encontrado una coincidencia
                }
            }

            if (!containsBarrierSteel) {
                // Agregar el rectángulo de la barrera a la lista y establecer el contador inicial en el mapa
                barrierRectanglesSteel.add(barrierBounds);
                barrierCountersSteel.put(barrierBounds, 2);  // Puedes establecer el valor inicial que desees
            }

            if (Intersector.overlaps(bulletBounds, barrierBounds)) {
                // Colisión detectada, aquí puedes hacer lo que necesites, por ejemplo, imprimir un mensaje

                isShooting = false; // Desactivar la bala
                isCollide = true;
                bulletCollidedSteel = true;
                stage.getRoot().removeActor(bulletImage);

                Integer currentCounterSteel = barrierCountersSteel.get(barrierBounds);

                if (currentCounterSteel != null && currentCounterSteel > 0) {
                    if (bomb) {
                        barrierCountersSteel.put(barrierBounds, currentCounterSteel - 2);
                        System.out.println(currentCounterSteel);
                        steelSP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        barrierCountersSteel.remove(barrierBounds);
                    } else if (fire) {
                        barrierCountersSteel.put(barrierBounds, currentCounterSteel - 2);
                        steelSP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        barrierCountersSteel.remove(barrierBounds);
                    } else if (water) {
                        if (currentCounterSteel == 2) {
                            barrierCountersSteel.put(barrierBounds, currentCounterSteel - 1);
                        } else if (currentCounterSteel == 1) {
                            barrierCountersSteel.put(barrierBounds, currentCounterSteel - 1);
                            steelSP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            barrierCountersSteel.remove(barrierBounds);

                        }

                    }
                }

            }
            System.out.println(barrierCountersSteel);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.R) && !isShooting) {
            bulletCollided = false;
            bulletCollidedcement = false;
            bulletCollidedSteel = false;
            // Inicia el disparo de la bala desde la posición del player.
            if (waterPowerCount > 0 && water) {
                bulletX = playerX + playerX;
                bulletY = playerY + playerY / 2 - bulletSprite.getHeight() / 2; //
                waterPowerCount--;
                waterCounterDrops++;
                waterCounterDropsTimes.add(elapsedTimeWater);
                isShooting = true;
                isCollide = false;
            } else if (firePowerCount > 0 && fire) {
                bulletX = playerX + playerX;
                bulletY = playerY + playerY / 2 - bulletSprite.getHeight() / 2; //
                firePowerCount--;
                fireCounterDrops++;
                fireCounterDropsTimes.add(elapsedTimeFire);
                isShooting = true;
                isCollide = false;
            } else if (bombPowerCount > 0 && bomb) {
                bulletX = playerX + playerX;
                bulletY = playerY + playerY / 2 - bulletSprite.getHeight() / 2; //
                bombPowerCount--;
                bombCounterDrops++;
                bombCounterDropsTimes.add(elapsedTimeFire);
                isShooting = true;
                isCollide = false;
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

    public void updateCounterLabel() {
       /* woodCounterLabel.setColor(Color.RED);
        woodCounterLabel.setText("wood barriers: " + countersBarriers.getWoodCounter());
        cementCounterLabel.setColor(Color.RED);
        cementCounterLabel.setText("cement barriers: " + countersBarriers.getCementCounter());
        steelCounterLabel.setColor(Color.RED);
        steelCounterLabel.setText("steel barriers: " + countersBarriers.getSteelCounter());
        eagleCounterLabel.setColor(Color.RED);
        eagleCounterLabel.setText("Eagle: " + countersBarriers.getEagleCounter());*/
        waterCounterLabel.setColor(Color.RED);
        waterCounterLabel.setText("Water Power: " + waterPowerCount);
        fireCounterLabel.setColor(Color.RED);
        fireCounterLabel.setText("Fire Power: " + firePowerCount);
        bombCounterLabel.setColor(Color.RED);
        bombCounterLabel.setText("Bomb Power: " + bombPowerCount);
    }
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        if (this.spotifyReference.get() != null && !songInfoFlag) {
            songInfo = this.spotifyReference.get().getSongInfo("hijo+de+la+noche");
            System.out.println(songInfo);
            songInfoFlag = true;
        }

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
            if (isCollide) {
                if (bulletX < -bulletSprite.getWidth()) {
                    isShooting = false;
                }
            }

            if (isShooting) {
                bulletX -= bulletSpeed * Gdx.graphics.getDeltaTime();//Donde la bala va a ser lanzada
                bulletImage.setPosition(bulletX, bulletY);
                if (fireButton.isChecked()) {
                    bulletSprite.setTexture(fireTexture);
                    stage.addActor(bulletImage);
                } else if (waterButton.isChecked()) {
                    bulletSprite.setTexture(waterTexture);
                    stage.addActor(bulletImage);
                } else if (bombButton.isChecked()) {
                    bulletSprite.setTexture(bombTexture);
                    stage.addActor(bulletImage);
                }

                if (bulletX < -bulletSprite.getWidth()) {
                    isShooting = false;
                }
            } else {
                Actions.removeActor(bulletImage);
            }
            wasdPJ();
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
