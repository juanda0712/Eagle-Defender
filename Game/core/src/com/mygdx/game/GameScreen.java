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
import com.mygdx.models.SongInfo;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.models.User2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.utils.SpotifyAuthenticator;
import lombok.Getter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class GameScreen implements Screen {

    boolean bulletCollided = false;
    boolean bulletCollidedcement = false;
    boolean bulletCollidedSteel = false;
    //private List<Barrera> barreras = new ArrayList<>();
    private final MainController game;
    private List<Rectangle> placedRectangles = new ArrayList<>();
    Map<Rectangle, Integer> barrierCounters = new HashMap<>();  // Mapa para mantener los contadores de las barreras
    Map<Rectangle, Integer> barrierCounterscement = new HashMap<>();  // Mapa para mantener los contadores de las barreras

    Map<Rectangle, Integer> barrierCountersSteel = new HashMap<>();  // Mapa para mantener los contadores de las barreras
    List<Rectangle> barrierRectangles = new ArrayList<>();
    List<Rectangle> barrierRectanglescement = new ArrayList<>();

    List<String> attackerSongs = new ArrayList<>();
    TextButton empezarButton;


    List<Rectangle> barrierRectanglesSteel = new ArrayList<>();
    private List<Float> waterPowerTimers = new ArrayList<>();
    private List<Float> firePowerTimers = new ArrayList<>();
    private List<Float> bombPowerTimers = new ArrayList<>();

    private int maxWaterPowerCount = 3;
    private int maxBombPowerCount = 4;

    private int maxFirePowerCount = 2;
    private Sound explosionSound;


    private List<Float> auxList = new ArrayList<Float>();
    private List<Float> fireauxList = new ArrayList<Float>();
    private List<Float> bombauxList = new ArrayList<Float>();

    private final Stage stage;
    private final OrthographicCamera camera;
    private JSONDataManager<User2> user2Manager;

    private boolean isTimerActive = false;
    private boolean isTimerActivelabel = false;
    private float elapsedTimeWater;
    private float elapsedTimeFire;
    private float elapsedTimeBomb;

    private boolean flagAux = false;
    private boolean fireflagAux = false;


    private boolean bombflagAux = false;

    private User2 user;
    private User2 user2;
    private Label usernameLabel;
    private Label username2Label;


    private final Array<Actor> arrayGUIElements = new Array<>();
    private int remainingTime = 60;
    private Label timerLabel;

    Random random = new Random();

    private int songPosition;

    Sprite playerSprite;
    Sprite bulletSprite;

    Texture bulletTexture;
    Texture player;
    Texture playerTexture;
    Texture fireTexture;
    Texture waterTexture;
    Texture bombTexture;
    private Image lineaHorizontal;
    private Texture lineaRecta;
    Image playerImage;
    Image bulletImage;
    private SpriteBatch batch;
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
    private Array<Image> woodPVP;
    private Array<Image> steelPVP;
    private Array<Image> cementPVP;
    private IAMode iaMode;
    private boolean songInfoFlag = false;

    @Getter
    private boolean placingEnabled = false;
    private boolean aguilaGodPlaced = false;
    private boolean barriersEnabled = false;
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
    private int waterPowerCount = 3;
    private int firePowerCount = 2;
    private int bombPowerCount = 4;
    private float timer = 0; // Inicializa el temporizador en 0 segundos
    private float resetInterval = 30.0f;
    private float waterCounterTimer = 0; // Contador para el WaterCounter
    private int waterCounterDrops = 0; // Contador de caídas del WaterCounter
    private int fireCounterDrops = 0; // Contador de caídas del WaterCounter
    private int bombCounterDrops = 0; // Contador de caídas del WaterCounter
    private AtomicReference<SpotifyAuthenticator> spotifyReference;

    private Boolean fire = false;
    private Boolean water = false;
    private Boolean bomb = false;

    private int timersong = 0;

    private Label waterCounterLabel;
    private Label fireCounterLabel;
    private Label bombCounterLabel;
    private List<Float> waterCounterDropsTimes = new ArrayList<>();
    private List<Float> fireCounterDropsTimes = new ArrayList<>();
    private List<Float> bombCounterDropsTimes = new ArrayList<>();

    private SongInfo songInfo;


    public GameScreen(final MainController game, JSONDataManager<User2> user2Manager, User2 user, User2 user2, CountersBarriers countersBarriers, AtomicReference<SpotifyAuthenticator> spotifyReference) {
        System.out.println(user);
        System.out.println(user2);
        this.game = game;
        this.spotifyReference = spotifyReference;
        this.elapsedTimeWater = 0;
        this.elapsedTimeFire = 0;
        this.elapsedTimeBomb = 0;
        this.user2Manager = user2Manager;
        this.user = user;
        this.user2 = user2;
        this.countersBarriers = countersBarriers;
        this.attackerSongs.add(user2.getSong1().replace(" ", "+"));
        this.attackerSongs.add(user2.getSong2().replace(" ", "+"));
        this.attackerSongs.add(user2.getSong3().replace(" ", "+"));
        songPosition = random.nextInt(3);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1720, 1080);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        woodPVP = new Array<>();
        steelPVP = new Array<>();
        cementPVP = new Array<>();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("audExplosion.mp3"));
        setupButtonsDefender();
        setupButtonsAttacker();
        //gameScreenFeatures = new GameScreenFeatures(stage, woodPVP, steelPVP, cementPVP, user, countersBarriers, woodenButton, cementButton, steelButton, eagleButton);
        batch = new SpriteBatch();
        // Crea la etiqueta (Label) para mostrar el tiempo restante
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(); // Configura el estilo de fuente
        timerLabel = new Label("Time: " + remainingTime, labelStyle);
        timerLabel.setPosition(900, 980); // Posición en la pantalla
        stage.addActor(timerLabel);
        isTimerActivelabel = true;

        timerLabel.setText("Time: " + remainingTime);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(timersong ==0) {
                    isTimerActive = true;
                    spotifyReference.get().playSong(attackerSongs.get(songPosition));
                    stage.getRoot().removeActor(empezarButton);
                    //spotifyReference.get().playSong("Billie+jean");
                }

            }
        }, 60); // 60 segundos (1 minuto)

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
        eagleCounterLabel.setPosition(460, 80);
        stage.addActor(eagleCounterLabel);


    }

    private void addWood(float x, float y) {
        if (countersBarriers.getWoodCounter() > 0 && woodenButton.isChecked()) {
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
                for (Image placedImage : woodPVP) {
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

                for (Image placedImage : cementPVP) {
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

                for (Image placedImage : steelPVP) {
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
                    woodPVP.add(newImage);
                    minusWoodCounter();
                }
            }
        }
    }

    private void minusWoodCounter() {
        if (countersBarriers.getWoodCounter() > 0) {
            countersBarriers.setWoodCounter(countersBarriers.getWoodCounter() - 1);
            //gameScreen.updateCounterLabel();
        }
    }

    //Barreras de cemento y su contador
    private void addCement(float x, float y) {
        if (countersBarriers.getCementCounter() > 0 && cementButton.isChecked()) {
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
                for (Image placedImage : woodPVP) {
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

                for (Image placedImage : cementPVP) {
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

                for (Image placedImage : steelPVP) {
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
                    cementPVP.add(newImage);
                    minusCementCounter();
                }
            }
        }
    }

    private void minusCementCounter() {
        if (countersBarriers.getCementCounter() > 0) {
            countersBarriers.setCementCounter(countersBarriers.getCementCounter() - 1);
            //gameScreen.updateCounterLabel();
        }
    }

    //Barreras de acero y su contador
    private void addSteel(float x, float y) {
        if (countersBarriers.getSteelCounter() > 0 && steelButton.isChecked()) {
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
                for (Image placedImage : woodPVP) {
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

                for (Image placedImage : cementPVP) {
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

                for (Image placedImage : steelPVP) {
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
                    steelPVP.add(newImage);
                    minusSteelCounter();
                }
            }
        }
    }

    private void minusSteelCounter() {
        if (countersBarriers.getSteelCounter() > 0) {
            countersBarriers.setSteelCounter(countersBarriers.getSteelCounter() - 1);
            //gameScreen.updateCounterLabel();
        }
    }

    private void addEagle(float x, float y) {
        if (countersBarriers.getEagleCounter() > 0 && eagleButton.isChecked()) {
            if (!aguilaGodPlaced) {
                float maxY = stage.getHeight() - 120;
                float minY = 120;
                if (x < stage.getWidth() / 2 && y >= minY && y <= maxY) {
                    Texture aguilaGodTexture = new Texture(Gdx.files.internal("assets/aguilagod.png"));
                    Image aguilaGodImage = new Image(aguilaGodTexture);
                    aguilaGodImage.setSize(80, 80);
                    aguilaGodImage.setPosition(x - (aguilaGodImage.getWidth() / 2), y - (aguilaGodImage.getHeight() / 2));
                    aguilaGodImage.setSize(aguilaGodImage.getWidth(), aguilaGodImage.getHeight());

                    boolean canPlace = true;
                    for (Image placedImage : woodPVP) {
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

                    for (Image placedImage : cementPVP) {
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

                    for (Image placedImage : steelPVP) {
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
                        woodPVP.add(aguilaGodImage);
                        //minusEagleCounter();
                        aguilaGodPlaced = true;
                    }
                }
            }
        }
    }

    public void setupButtonsAttacker() {
        Drawable fireChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/fire1.png"))));
        fireButton = new ImageButton(fireChoose);
        fireButton.setPosition(1250, 20);
        fireButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!fireButton.isChecked()) {
                    attackEnabled = false;

                } else {
                    waterButton.setChecked(false);
                    bombButton.setChecked(false);
                    bomb = false;
                    water = false;
                    fire = true;
                    attackEnabled = true;
                }
            }
        });
        stage.addActor(fireButton);

        Drawable waterChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Water12.png"))));
        waterButton = new ImageButton(waterChoose);
        waterButton.setPosition(1450, 40);
        waterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!waterButton.isChecked()) {
                    attackEnabled = false;

                } else {
                    fireButton.setChecked(false);
                    bombButton.setChecked(false);
                    bomb = false;
                    water = true;
                    fire = false;
                    attackEnabled = true;
                }
            }
        });
        stage.addActor(waterButton);

        Drawable bombChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Bomb1.png"))));
        bombButton = new ImageButton(bombChoose);
        bombButton.setPosition(1050, 40);
        bombButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!bombButton.isChecked()) {
                    attackEnabled = false;

                } else {
                    fireButton.setChecked(false);
                    waterButton.setChecked(false);
                    bomb = true;
                    water = false;
                    fire = false;
                    attackEnabled = true;
                }
            }
        });
        stage.addActor(bombButton);
    }

    private void setupUIElements() {

        setUpBackground();
        setUpLabels();
        setUpImages();
    }

    private void setUpLabels() {
        Skin skin = VisUI.getSkin();

        TextButton empezarButton = new TextButton("Start", skin);
        empezarButton.setPosition(900, 940);
        empezarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isTimerActive = true;
                spotifyReference.get().playSong(attackerSongs.get(songPosition));
                stage.getRoot().removeActor(empezarButton);
                stage.getRoot().removeActor(timerLabel);
                timersong ++;
            }
        });

        usernameLabel = new Label(user.getUsername(), skin);
        usernameLabel.setColor(Color.BLACK);
        usernameLabel.setPosition(280, 1000);

        username2Label = new Label(user2.getUsername(), skin);
        username2Label.setColor(Color.BLACK);
        username2Label.setPosition(1610, 1000);

        Label userInfo = new Label("User information:", skin);
        userInfo.setColor(Color.BLACK);
        userInfo.setPosition(200, 1020);

        Label user2Info = new Label("User information:", skin);
        user2Info.setColor(Color.BLACK);
        user2Info.setPosition(1530, 1020);



        Label usernameLabel = new Label("Username: ", skin);
        usernameLabel.setColor(Color.BLACK);
        usernameLabel.setPosition(200, 1000);

        Label username2Label = new Label("Username: ", skin);
        username2Label.setColor(Color.BLACK);
        username2Label.setPosition(1530, 1000);


        waterCounterLabel = new Label("Water Power: " + waterPowerCount, skin);
        waterCounterLabel.setPosition(1450, 80);
        stage.addActor(waterCounterLabel);
        fireCounterLabel = new Label("Fire Power: " + firePowerCount, skin);
        fireCounterLabel.setPosition(1250, 80);
        stage.addActor(fireCounterLabel);

        bombCounterLabel = new Label("Bomb Power: " + firePowerCount, skin);
        bombCounterLabel.setPosition(1050, 80);
        stage.addActor(bombCounterLabel);
        //maskImage.setPosition(posX, posY); // Ajusta la posición según tus necesidades


        stage.addActor(empezarButton);
        stage.addActor(userInfo);
        stage.addActor(user2Info);
        stage.addActor(usernameLabel);
        stage.addActor(username2Label);


        stage.addActor(this.usernameLabel);
        stage.addActor(this.username2Label);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    // Maneja el clic izquierdo como lo hacías anteriormente.
                    if (woodenButton.isChecked()) {
                        addWood(x, y);
                    } else if (cementButton.isChecked()) {
                        addCement(x, y);
                    } else if (steelButton.isChecked()) {
                        addSteel(x, y);
                    } else if (eagleButton.isChecked()) {
                        addEagle(x, y);
                    }
                }
                return true;
            }
        });
    }

    private void setUpImages() {
        Texture eagleTexture = new Texture(Gdx.files.internal("assets/aguilagod.png"));
        Texture goblinTexture = new Texture(Gdx.files.internal("assets/duendegod.png"));
        Texture userimageTexture = new Texture(Gdx.files.local("data/imgs/" + user.getImage()));
        Texture user2imageTexture = new Texture(Gdx.files.local("data/imgs/" + user2.getImage()));
        lineaRecta = new Texture("negro2.jpg");
        Texture circulo = new Texture("blanco.png");
        bulletTexture = new Texture("bala.PNG");
        playerTexture = new Texture("Idle.png");
        fireTexture = new Texture("fire1.PNG");
        waterTexture = new Texture("Water12.png");
        bombTexture = new Texture("Bomb1.png");

        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(playerX, playerY);

        bulletSprite = new Sprite(bulletTexture);
        playerImage = new Image(playerSprite);
        playerImage.setPosition(1300, 500);
        bulletImage = new Image(bulletSprite);


        Image userImage = new Image(userimageTexture);
        Image user2Image = new Image(user2imageTexture);
        Image maskImage = new Image(circulo);


        lineaHorizontal = new Image(lineaRecta);
        lineaHorizontal.setPosition(0, 920);
        lineaHorizontal.setSize(Gdx.graphics.getWidth(), 2);

        Image lineaHorizontalboton = new Image(lineaRecta);
        lineaHorizontalboton.setPosition(0, 110);
        lineaHorizontalboton.setSize(Gdx.graphics.getWidth(), 2);

        userImage.setPosition(30, 920);
        userImage.setSize(150, 150);

        user2Image.setPosition(1730, 920);
        user2Image.setSize(150, 150);

        maskImage.setSize(userImage.getWidth(), userImage.getHeight());
        //maskImage.setPosition(posX, posY); // Ajusta la posición según tus necesidades

        stage.addActor(userImage);
        stage.addActor(user2Image);
        stage.addActor(lineaHorizontal);



        stage.addActor(lineaHorizontalboton);
        stage.addActor(playerImage);
    }

    private void setUpBackground() {


        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    if (woodenButton.isChecked()) {
                        addWood(x, y);
                    } else if (cementButton.isChecked()) {
                        addCement(x, y);
                    } else if (steelButton.isChecked()) {
                        addSteel(x, y);
                    } else if (eagleButton.isChecked()) {
                        addEagle(x, y);
                    }
                }
                return true;
            }
        });

        Texture desertTexture = new Texture(Gdx.files.internal("assets/desertHighway.png"));
        Image imageBG = new Image(desertTexture);
        imageBG.setPosition(0, 110);

        imageBG.setSize(1980, 810);
        stage.addActor(imageBG);

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
        waterCounterLabel.setColor(Color.RED);
        waterCounterLabel.setText("Water Power: " + waterPowerCount);
        fireCounterLabel.setColor(Color.RED);
        fireCounterLabel.setText("Fire Power: " + firePowerCount);
        bombCounterLabel.setColor(Color.RED);
        bombCounterLabel.setText("Bomb Power: " + bombPowerCount);
    }


    private Color getColorFilterForPalette(String selectedPalette) {
        Color filter = new Color(1, 1, 1, 1); // Color inicial (sin filtro)
        selectedPalette = "Palette 1";
        switch (selectedPalette) {
            case "Palette 1":
                filter.set(0.839f, 0.725f, 0.553f, 1);
                break;
            case "Palette 2":
                filter.set(0.6157f, 0.6784f, 0.7412f, 1);
                break;
            case "Palette 3":
                filter.set(0.9451f, 0.6784f, 0.3529f, 1);
                break;
            case "Palette 4":
                filter.set(0.9490f, 0.8784f, 0.5804f, 1);
                break;
            case "Palette 5":
                filter.set(0.6039f, 0.6157f, 0.5412f, 1);
                break;
        }

        return filter;
    }

    private void applyColorFilter() {
        String selectedPalette = user.getSelectedColorPalette();
        Color filter = getColorFilterForPalette(selectedPalette);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(filter);
        batch.setColor(Color.WHITE);
        batch.end();
    }


    @Override
    public void render(float delta) {
        //Ejemplo reproduccion de una cancion
        if (this.spotifyReference.get() != null && !songInfoFlag) {
            //songInfo = this.spotifyReference.get().getSongInfo("Billie+jean");
            songInfo = this.spotifyReference.get().getSongInfo(attackerSongs.get(songPosition));
            System.out.println(songInfo);

            songInfoFlag = true;
        }
        System.out.println(attackerSongs);

        String selectedPalette = user.getSelectedColorPalette();
        timer += Gdx.graphics.getDeltaTime();

        //Color backgroundColor = new Color(0.96f, 0.96f, 0.86f, 1);
        //ScreenUtils.clear(backgroundColor);
        ScreenUtils.clear(getColorFilterForPalette(selectedPalette));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Array<User2> users = user2Manager.read();
        usernameLabel.setText(user.getUsername());
        updateCounterLabel();


        if (isTimerActivelabel) {
            elapsedTimeWater += delta;
            if (elapsedTimeWater >= 1.0f) {  // Actualizar cada segundo
                remainingTime--;
                if (remainingTime <= 0) {
                    isTimerActivelabel = false; // El tiempo ha finalizado
                }
                if (timersong == 0){
                    timerLabel.setText("Time: " + remainingTime);
                    timerLabel.setPosition(900, 980);
                    stage.addActor(timerLabel);
                    elapsedTimeWater = 0; // Reiniciar el contador de tiempo transcurrido
                }else {
                    stage.getRoot().removeActor(timerLabel);
                }
            }
        }


        if (isTimerActive) {

            elapsedTimeWater += delta;
            elapsedTimeFire += delta;
            elapsedTimeBomb += delta;
            resetInterval = (float) (30 / songInfo.getDuration()) * 60;

            // Verifica si ha pasado el tiempo necesario para aumentar el contador
            if (waterCounterDrops > 0) {
                if (Gdx.input.isKeyPressed(Input.Keys.R) && water) {
                    flagAux = false;
                    if (waterCounterDrops >= 2 && !flagAux && water) {
                        auxList.add(elapsedTimeWater);
                        flagAux = true;
                    }
                }

                if (elapsedTimeWater >= resetInterval && waterPowerCount < maxWaterPowerCount) {
                    // Aumenta el contador en 1
                    waterPowerCount++;
                    updateCounterLabel();

                    // Registra el tiempo actual en el temporizador de este Water Power
                    waterPowerTimers.add(elapsedTimeWater);

                    waterCounterDrops--; // Reduce la cantidad de caídas pendientes

                    if (auxList.isEmpty()) {
                        elapsedTimeWater = 0;
                    } else {
                        if (waterPowerCount < 3) {
                            elapsedTimeWater = elapsedTimeWater - auxList.get(0);
                            auxList.remove(0);
                            flagAux = false;
                        } else {
                            auxList.clear();
                        }

                    }
                }

            }
            // Registra el tiempo para cada Water Power independientemente
            for (int i = 0; i < waterPowerTimers.size(); i++) {
                waterPowerTimers.set(i, waterPowerTimers.get(i) + delta);
                if (waterPowerTimers.get(i) >= 5.0f) {
                    // Si ha pasado el tiempo necesario, puedes realizar alguna acción si es necesario
                }
            }

            // Verifica si ha pasado el tiempo necesario para aumentar el contador
            if (fireCounterDrops > 0) {
                if (Gdx.input.isKeyPressed(Input.Keys.R) && fire) {
                    fireflagAux = false;
                    if (fireCounterDrops >= 2 && !fireflagAux && fire) {
                        fireauxList.add(elapsedTimeFire);
                        fireflagAux = true;
                    }
                }

                if (elapsedTimeFire >= resetInterval && firePowerCount < maxFirePowerCount) {
                    // Aumenta el contador en 1
                    firePowerCount++;
                    updateCounterLabel();

                    // Registra el tiempo actual en el temporizador de este Water Power
                    firePowerTimers.add(elapsedTimeFire);

                    fireCounterDrops--; // Reduce la cantidad de caídas pendientes

                    if (fireauxList.isEmpty()) {
                        elapsedTimeFire = 0;
                    } else {
                        if (firePowerCount < 2) {
                            elapsedTimeFire = elapsedTimeFire - fireauxList.get(0);
                            fireauxList.remove(0);
                            fireflagAux = false;
                        } else {
                            fireauxList.clear();
                        }

                    }
                }

            }
            // Registra el tiempo para cada Water Power independientemente
            for (int i = 0; i < firePowerTimers.size(); i++) {
                firePowerTimers.set(i, firePowerTimers.get(i) + delta);
                if (firePowerTimers.get(i) >= 5.0f) {
                    // Si ha pasado el tiempo necesario, puedes realizar alguna acción si es necesario
                }
            }

            // Verifica si ha pasado el tiempo necesario para aumentar el contador
            if (bombCounterDrops > 0) {
                if (Gdx.input.isKeyPressed(Input.Keys.R) && bomb) {
                    bombflagAux = false;
                    if (bombCounterDrops >= 2 && !bombflagAux && bomb) {
                        bombauxList.add(elapsedTimeBomb);
                        bombflagAux = true;
                    }
                }

                if (elapsedTimeBomb >= resetInterval && bombPowerCount < maxBombPowerCount) {
                    // Aumenta el contador en 1
                    bombPowerCount++;
                    updateCounterLabel();

                    // Registra el tiempo actual en el temporizador de este Water Power
                    bombPowerTimers.add(elapsedTimeBomb);

                    bombCounterDrops--; // Reduce la cantidad de caídas pendientes

                    if (bombauxList.isEmpty()) {
                        elapsedTimeBomb = 0;
                    } else {
                        if (bombPowerCount < 4) {
                            elapsedTimeBomb = elapsedTimeBomb - bombauxList.get(0);
                            bombauxList.remove(0);
                            bombflagAux = false;
                        } else {
                            bombauxList.clear();
                        }

                    }
                }

            }
            // Registra el tiempo para cada Water Power independientemente
            for (int i = 0; i < bombPowerTimers.size(); i++) {
                bombPowerTimers.set(i, bombPowerTimers.get(i) + delta);
                if (bombPowerTimers.get(i) >= 5.0f) {
                    // Si ha pasado el tiempo necesario, puedes realizar alguna acción si es necesario
                }
            }


        }

        if (isCollide) {
            //collisionSprite.setPosition(bulletX-50, bulletY);
            //collisionSprite.setPosition(bulletX-15, bulletY);
            //collisionSprite.draw(batch);
            //Actions.removeActor(bulletImage);
            //isShooting = false;
            // Si la bala salió de la pantalla la elimina
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

        handleInput();
        stage.draw();
    }


    public void handleInput() {


        Rectangle bulletBounds = new Rectangle(bulletX, bulletY, bulletSprite.getWidth(), bulletSprite.getHeight());

        for (Image barrierImage : woodPVP) {
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
                explosionSound.play();
                stage.getRoot().removeActor(bulletImage);

                Integer currentCounter = barrierCounters.get(barrierBounds);

                if (currentCounter != null && currentCounter > 0) {
                    barrierCounters.put(barrierBounds, currentCounter - 1);
                    barrierCounters.remove(barrierBounds);
                    woodPVP.removeValue(barrierImage, true);
                    barrierImage.remove();
                    System.out.println(currentCounter);
                }

            }
            System.out.println(barrierCounters);

        }

        for (Image barrierImage : cementPVP) {
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
                explosionSound.play();
                stage.getRoot().removeActor(bulletImage);

                Integer currentCountercement = barrierCounterscement.get(barrierBounds);

                if (currentCountercement != null && currentCountercement > 0) {
                    if (bomb) {
                        barrierCounterscement.put(barrierBounds, currentCountercement - 3);
                        System.out.println(currentCountercement);
                        cementPVP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        barrierCounterscement.remove(barrierBounds);
                    } else if (fire) {
                        if (currentCountercement == 3) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 2);
                        } else if (currentCountercement < 3) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 2);
                            cementPVP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            barrierCounterscement.remove(barrierBounds);
                        }

                    } else if (water) {
                        if (currentCountercement == 3 || currentCountercement == 2) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 1);
                        } else if (currentCountercement == 1) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 1);
                            cementPVP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            barrierCounterscement.remove(barrierBounds);

                        }

                    }
                }

            }
            System.out.println(barrierCounterscement);

        }

        for (Image barrierImage : steelPVP) {
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
                explosionSound.play();
                stage.getRoot().removeActor(bulletImage);

                Integer currentCounterSteel = barrierCountersSteel.get(barrierBounds);

                if (currentCounterSteel != null && currentCounterSteel > 0) {
                    if (bomb) {
                        barrierCountersSteel.put(barrierBounds, currentCounterSteel - 2);
                        System.out.println(currentCounterSteel);
                        steelPVP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        barrierCountersSteel.remove(barrierBounds);
                    } else if (fire) {
                        barrierCountersSteel.put(barrierBounds, currentCounterSteel - 2);
                        steelPVP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        barrierCountersSteel.remove(barrierBounds);
                    } else if (water) {
                        if (currentCounterSteel == 2) {
                            barrierCountersSteel.put(barrierBounds, currentCounterSteel - 1);
                        } else if (currentCounterSteel == 1) {
                            barrierCountersSteel.put(barrierBounds, currentCounterSteel - 1);
                            steelPVP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            barrierCountersSteel.remove(barrierBounds);

                        }

                    }
                }

            }
            System.out.println(barrierCountersSteel);

        }


        if (isTimerActive) {
            if (Gdx.input.isKeyPressed(Input.Keys.R) && !isShooting) {
                bulletCollided = false;
                bulletCollidedcement = false;
                bulletCollidedSteel = false;
                // Inicia el disparo de la bala desde la posición del player.
                if (waterPowerCount > 0 && water) {
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
                    waterPowerCount--;
                    waterCounterDrops++;
                    waterCounterDropsTimes.add(elapsedTimeWater);
                    isShooting = true;
                    isCollide = false;
                } else if (firePowerCount > 0 && fire) {
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
                    firePowerCount--;
                    fireCounterDrops++;
                    fireCounterDropsTimes.add(elapsedTimeFire);
                    isShooting = true;
                    isCollide = false;
                } else if (bombPowerCount > 0 && bomb) {
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
                    bombPowerCount--;
                    bombCounterDrops++;
                    bombCounterDropsTimes.add(elapsedTimeFire);
                    isShooting = true;
                    isCollide = false;
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.W) && playerY < Gdx.graphics.getHeight() - playerSprite.getHeight()) {
                System.out.println("W");
                if (playerY + playerSprite.getHeight() + speed * Gdx.graphics.getDeltaTime() <= 920) {
                    playerY += speed * Gdx.graphics.getDeltaTime();
                    player = new Texture("Back1.png");
                    playerSprite.setTexture(new Texture("Back1.png"));
                    playerImage.setPosition(playerX, playerY);
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.S) && playerY >= 110) {
                System.out.println("S");
                playerY -= speed * Gdx.graphics.getDeltaTime();
                player = new Texture("Front2.png");
                playerSprite.setTexture(new Texture("Front2.png"));
                playerImage.setPosition(playerX, playerY);
            }


            if (Gdx.input.isKeyPressed(Input.Keys.A) && playerX > 0) {
                System.out.println("A");
                float centerX = Gdx.graphics.getWidth() / 2.0f;
                if (playerX - speed * Gdx.graphics.getDeltaTime() >= centerX) {
                    playerX -= speed * Gdx.graphics.getDeltaTime();
                    player = new Texture("WalkL3.png");
                    playerSprite.setTexture(new Texture("WalkL3.png"));
                    playerImage.setPosition(playerX, playerY);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D) && playerX < Gdx.graphics.getWidth() - playerSprite.getWidth()) {
                System.out.println("D");
                playerX += speed * Gdx.graphics.getDeltaTime();

                player = new Texture("WalkR3.png");
                playerSprite.setTexture(new Texture("WalkR3.png"));
                playerImage.setPosition(playerX, playerY);

            }

            if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                player = new Texture("Idle.png");
                playerSprite.setTexture(new Texture("Idle.png"));
            }
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