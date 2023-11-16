package com.mygdx.game;

import java.util.TimerTask;
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
import com.badlogic.gdx.math.Vector2;
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
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.models.User2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.utils.SpotifyAuthenticator;
import lombok.Getter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class GameScreen implements Screen {


    boolean bulletCollided = false;
    boolean bulletCollidedcement = false;
    boolean bulletCollidedSteel = false;

    boolean aguilaDestroy = false;

    //private List<Barrera> barreras = new ArrayList<>();
    private final MainController game;

    private float targetX;
    private float targetY;

    private int contadorTurn;
    private float barrierDown;

    private List<Rectangle> placedRectangles = new ArrayList<>();
    Map<Rectangle, Integer> barrierCounters = new HashMap<>();  // Mapa para mantener los contadores de las barreras
    Map<Rectangle, Integer> barrierCounterscement = new HashMap<>();  // Mapa para mantener los contadores de las barreras

    Map<Rectangle, Integer> barrierCountersSteel = new HashMap<>();  // Mapa para mantener los contadores de las barreras
    List<Rectangle> barrierRectangles = new ArrayList<>();
    List<Rectangle> barrierRectanglescement = new ArrayList<>();

    List<String> attackerSongs = new ArrayList<>();
    List<String> defenderSongs = new ArrayList<>();
    TextButton empezarButton;


    List<Rectangle> barrierRectanglesSteel = new ArrayList<>();
    private List<Float> waterPowerTimers = new ArrayList<>();
    private List<Float> firePowerTimers = new ArrayList<>();
    private List<Float> bombPowerTimers = new ArrayList<>();
    private List<Float> woodBarrierTimers = new ArrayList<>();
    private List<Float> cementBarrierTimers = new ArrayList<>();
    private List<Float> steelBarrierTimers = new ArrayList<>();

    private int maxWaterPowerCount = 3;
    private int maxBombPowerCount = 4;

    private int maxFirePowerCount = 2;
    private int maxWoodBarrierCount = 10;
    private Sound explosionSound;

    private float barrierDefend;


    private List<Float> auxList = new ArrayList<Float>();
    private List<Float> fireauxList = new ArrayList<Float>();
    private List<Float> bombauxList = new ArrayList<Float>();

    private List<Float> woodauxList = new ArrayList<Float>();
    private List<Float> cementauxList = new ArrayList<Float>();
    private List<Float> steelauxList = new ArrayList<Float>();

    private final Stage stage;
    private final OrthographicCamera camera;
    int screenWidth;
    int screenHeight;
    private JSONDataManager<User2> user2Manager;

    private boolean isTimerActive = false;
    private boolean isTimerActivelabel = false;
    private float elapsedTimeWater;
    private float elapsedTimeFire;
    private float elapsedTimeBomb;
    private float elapsedTimeWood;
    private float elapsedTimeCement;
    private float elapsedTimeSteel;
    private float elapsedAttacker;
    private boolean flagAux = false;
    private boolean fireflagAux = false;
    private boolean bombflagAux = false;
    private boolean woodflagAux = false;
    private boolean cementflagAux = false;
    private boolean steelflagAux = false;

    private User2 user;
    private User2 user2;
    private Label usernameLabel;
    private Label username2Label;
    private Label user1PointsLabel;
    private Label user2PointsLabel;

    private float user1Points;
    private float user2Points;


    private final Array<Actor> arrayGUIElements = new Array<>();
    private int remainingTime = 60;
    private Label timerLabel;

    private int timmerAttack = 0;

    private int timeleftAttack;

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

    private Array<Image> eaglePVP;
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
    private float resetIntervalBarriers = 20.0f;
    private float waterCounterTimer = 0; // Contador para el WaterCounter
    private int waterCounterDrops = 0; // Contador de caídas del WaterCounter
    private int fireCounterDrops = 0; // Contador de caídas del WaterCounter
    private int bombCounterDrops = 0; // Contador de caídas del WaterCounter

    private int woodCounterDrops = 0; // Contador de caídas del WaterCounter
    private int cementCounterDrops = 0; // Contador de caídas del WaterCounter
    private int steelCounterDrops = 0; // Contador de caídas del WaterCounter
    private AtomicReference<SpotifyAuthenticator> spotifyReference;

    private Boolean fire = false;
    private Boolean water = false;
    private Boolean bomb = false;
    private Boolean wood = false;
    private Boolean cement = false;
    private Boolean steel = false;
    private int timersong = 0;

    private Label waterCounterLabel;
    private Label fireCounterLabel;
    private Label bombCounterLabel;
    private List<Float> waterCounterDropsTimes = new ArrayList<>();
    private List<Float> fireCounterDropsTimes = new ArrayList<>();
    private List<Float> bombCounterDropsTimes = new ArrayList<>();

    private int woodDown = 0;
    private int steelDown =0;
    private int concreteDown = 0;

    private int woodDownTimmer = 0;
    private int steelDownTimmer =0;
    private int concreteDownTimmer = 0;
    private Skin skin = VisUI.getSkin();
    private SongInfo songInfo;
    private float elapsedTimeInSong = 0;
    private Texture goTexture;
    private boolean showingGoImage = true;
    private float goImageTimer = 0;
    private static final float GO_IMAGE_DURATION = 5.0f;





    public GameScreen(final MainController game, JSONDataManager<User2> user2Manager, User2 user, User2 user2, CountersBarriers countersBarriers, AtomicReference<SpotifyAuthenticator> spotifyReference,int contadorTurnos,float pointsU1,float pointsU2) {
        System.out.println(user);
        System.out.println(user2);
        this.game = game;
        this.spotifyReference = spotifyReference;
        this.elapsedTimeWater = 0;
        this.elapsedTimeFire = 0;
        this.elapsedTimeBomb = 0;
        this.elapsedTimeWood = 0;
        this.elapsedTimeCement = 0;
        this.elapsedTimeSteel = 0;
        this.user2Manager = user2Manager;
        this.user = user;
        this.user2 = user2;
        this.countersBarriers = countersBarriers;
        this.attackerSongs.add(user2.getSong1().replace(" ", "+"));
        this.attackerSongs.add(user2.getSong2().replace(" ", "+"));
        this.attackerSongs.add(user2.getSong3().replace(" ", "+"));
        this.defenderSongs.add(user.getSong1().replace(" ", "+"));
        this.defenderSongs.add(user.getSong2().replace(" ", "+"));
        this.defenderSongs.add(user.getSong3().replace(" ", "+"));
        user2Points = pointsU2;
        user1Points = pointsU1;
        user.setPoints(user1Points);
        user2.setPoints(user2Points);
        contadorTurn = contadorTurnos;
        songPosition = random.nextInt(3);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        woodPVP = new Array<>();
        steelPVP = new Array<>();
        cementPVP = new Array<>();
        eaglePVP = new Array<>();
        countersBarriers.setWoodCounter(10);
        countersBarriers.setCementCounter(10);
        countersBarriers.setSteelCounter(10);
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("audExplosion.mp3"));
        setupButtonsDefender();
        setupButtonsAttacker();
        System.out.println(spotifyReference);
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
        //spotifyReference.get().playSong(defenderSongs.get(songPosition));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                    spotifyReference.get().playSong(defenderSongs.get(songPosition));
            }
        }, 8); // 60 segundos (1 minuto)

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
        Drawable buttonDownEagle = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/headD.jpg"))));
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
            float imageWidth = 60;
            float maxX = stage.getWidth() / 2 - imageWidth;
            float maxY = stage.getHeight() - 205;
            float minY = 120;
            if (x >= 0 && x <= maxX && y >= minY && y <= maxY) {
                if ("Smooth".equals(selectedTexture)) {
                    imagePath = "assets/wood.jpg";
                } else if ("Rocky".equals(selectedTexture)) {
                    imagePath = "assets/woodST.jpg";
                } else if ("Bricked".equals(selectedTexture)) {
                    imagePath = "assets/woodBR.jpg";
                }
                Texture newImageTexture = new Texture(Gdx.files.internal(imagePath));

                boolean canPlace = true;
                for (Image placedImage : woodPVP) {
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    Image newImage = new Image(newImageTexture);
                    newImage.setPosition(x - (newImage.getWidth() / 2), y - (newImage.getHeight() / 2));

                    newImage.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            if (button == Input.Buttons.LEFT) {
                                newImage.setOrigin(newImage.getWidth() / 2, newImage.getHeight() / 2);
                                newImage.rotateBy(45);
                            }
                            return true;
                        }
                    });

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
            float imageWidth = 60;
            float maxX = stage.getWidth() / 2 - imageWidth;
            float maxY = stage.getHeight() - 205;
            float minY = 120;
            if (x >= 0 && x <= maxX && y >= minY && y <= maxY) {
                if ("Smooth".equals(selectedTexture)) {
                    imagePath = "assets/cement.jpg";
                } else if ("Rocky".equals(selectedTexture)) {
                    imagePath = "assets/cementST.jpg";
                } else if ("Bricked".equals(selectedTexture)) {
                    imagePath = "assets/cementBR.jpg";
                }
                Texture newImageTexture = new Texture(Gdx.files.internal(imagePath));

                boolean canPlace = true;
                for (Image placedImage : woodPVP) {
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    Image newImage = new Image(newImageTexture);
                    newImage.setPosition(x - (newImage.getWidth() / 2), y - (newImage.getHeight() / 2));

                    newImage.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            if (button == Input.Buttons.LEFT) {
                                newImage.setOrigin(newImage.getWidth() / 2, newImage.getHeight() / 2);
                                newImage.rotateBy(45);
                            }
                            return true;
                        }
                    });

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
            float imageWidth = 60;
            float maxX = stage.getWidth() / 2 - imageWidth;
            float maxY = stage.getHeight() - 205;
            float minY = 120;
            if (x >= 0 && x <= maxX && y >= minY && y <= maxY) {
                if ("Smooth".equals(selectedTexture)) {
                    imagePath = "assets/steel.jpg";
                } else if ("Rocky".equals(selectedTexture)) {
                    imagePath = "assets/steelST.jpg";
                } else if ("Bricked".equals(selectedTexture)) {
                    imagePath = "assets/steelBR.jpg";
                }
                Texture newImageTexture = new Texture(Gdx.files.internal(imagePath));

                boolean canPlace = true;
                for (Image placedImage : woodPVP) {
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    float newX = x;
                    float newY = y;
                    float newWidth = newImageTexture.getWidth();
                    float newHeight = newImageTexture.getHeight();

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
                    Image newImage = new Image(newImageTexture);
                    newImage.setPosition(x - (newImage.getWidth() / 2), y - (newImage.getHeight() / 2));

                    newImage.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            if (button == Input.Buttons.LEFT) {
                                newImage.setOrigin(newImage.getWidth() / 2, newImage.getHeight() / 2);
                                newImage.rotateBy(45);
                            }
                            return true;
                        }
                    });

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
                float imageWidth = 80;
                float maxX = stage.getWidth() / 2 - imageWidth;
                float maxY = stage.getHeight() - 205;
                float minY = 160;
                if (x >= 0 && x <= maxX && y >= minY && y <= maxY) {
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
                        eaglePVP.add(aguilaGodImage);
                        minusEagleCounter();
                        aguilaGodPlaced = true;
                    }
                }
            }
        }
    }
    private void minusEagleCounter() {
        if (countersBarriers.getEagleCounter() < 0) {
            countersBarriers.setEagleCounter(countersBarriers.getEagleCounter() - 1);
        }
    }

    public void setupButtonsAttacker() {
        Drawable fireChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/fire1.png"))));
        Drawable fireChooseD = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/fire1d.png"))));
        fireButton = new ImageButton(fireChoose, fireChooseD);
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
        Drawable waterChooseD = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Water12d.png"))));
        waterButton = new ImageButton(waterChoose, waterChooseD);
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
        Drawable bombChooseD = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/Bomb1d.png"))));
        bombButton = new ImageButton(bombChoose, bombChooseD);
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
        goTexture = new Texture("go.jpeg");
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

        user2PointsLabel = new Label("Points "+user2.getUsername()+": "+user2Points, skin);
        user2PointsLabel.setColor(Color.BLACK);
        user2PointsLabel.setPosition(1530, 980);

        user1PointsLabel = new Label("Points "+user.getUsername()+": "+user1Points, skin);
        user1PointsLabel.setColor(Color.BLACK);
        user1PointsLabel.setPosition(200, 980);

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
        stage.addActor(user2PointsLabel);
        stage.addActor(user1PointsLabel);


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

        Texture desertTexture = new Texture(Gdx.files.internal("assets/desertHighway.png"));
        Image imageBG = new Image(desertTexture);
        imageBG.setPosition(0, 110);

        imageBG.setSize(1980, 810);
        stage.addActor(imageBG);

    }


    public void updateCounterLabel() {
        woodCounterLabel.setColor(Color.RED);
        woodCounterLabel.setText("Wood barriers: " + countersBarriers.getWoodCounter());
        cementCounterLabel.setColor(Color.RED);
        cementCounterLabel.setText("Cement barriers: " + countersBarriers.getCementCounter());
        steelCounterLabel.setColor(Color.RED);
        steelCounterLabel.setText("Steel barriers: " + countersBarriers.getSteelCounter());
        eagleCounterLabel.setColor(Color.RED);
        eagleCounterLabel.setText("Eagle: " + countersBarriers.getEagleCounter());
        waterCounterLabel.setColor(Color.RED);
        waterCounterLabel.setText("Water Power: " + waterPowerCount);
        fireCounterLabel.setColor(Color.RED);
        fireCounterLabel.setText("Fire Power: " + firePowerCount);
        bombCounterLabel.setColor(Color.RED);
        bombCounterLabel.setText("Bomb Power: " + bombPowerCount);
    }


    private Color getColorFilterForPalette(String selectedColor) {
        Color filter = new Color(1, 1, 1, 1); // Color inicial (sin filtro)
        //selectedPalette = "Palette 1";
        switch (selectedColor) {
            case "Color1":
                filter.set(0.839f, 0.725f, 0.553f, 1);
                break;
            case "Color2":
                filter.set(0.945f, 0.678f, 0.353f, 1);
                break;
            case "Color3":
                filter.set(0.949f, 0.878f, 0.580f, 1);
                break;
            case "Color4":
                filter.set(0.847f, 0.870f, 0.917f, 1);
                break;
            case "Color5":
                filter.set(0.658f, 0.478f, 0.313f, 1);
                break;
            case "Color6":
                filter.set(0.760f, 0.721f, 0.227f, 1);
                break;
        }

        return filter;
    }


    @Override
    public void render(float delta) {
        handleSongInfo();
        handleUIUpdates(delta);
        handleTimer(delta);
        handleCollisionAndBullet(delta);
        handleInput();
        //warning();
        stage.draw();
    }



    private void handleSongInfo() {
        if (spotifyReference.get() != null && !songInfoFlag) {
            songInfo = spotifyReference.get().getSongInfo(attackerSongs.get(songPosition));
            songInfoFlag = true;
        }

    }


    private void handleUIUpdates(float delta) {
        String selectedColor = user.getSelectedColor();
        timer += delta;

        ScreenUtils.clear(getColorFilterForPalette(selectedColor));
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.act(Math.min(delta, 1 / 30f));

        Array<User2> users = user2Manager.read();
        usernameLabel.setText(user.getUsername());
        updateCounterLabel();
    }

    private void handleTimer(float delta) {
        if (isTimerActivelabel) {
            elapsedTimeWater += delta;
            if (elapsedTimeWater >= 1.0f) {
                remainingTime--;
                if (remainingTime <= 0) {

                    isTimerActivelabel = false;

                }
                timerLabel.setText("Time: " + remainingTime);
                timerLabel.setPosition(900, 980);
                stage.addActor(timerLabel);
                elapsedTimeWater = 0;
            }
        }

        if (isTimerActive) {
            elapsedTimeInSong += delta;
            elapsedTimeWater += delta;
            elapsedTimeFire += delta;
            elapsedTimeBomb += delta;
            elapsedTimeWood += delta;
            elapsedTimeCement += delta;
            elapsedTimeSteel += delta;

            resetInterval = (float) (30 / songInfo.getDuration()) * 60;

            resetIntervalBarriers = (float) (20 / songInfo.getDuration()) * 60;

            updatePowerCounters(delta, waterCounterDrops, waterPowerCount, waterPowerTimers, elapsedTimeWater, maxWaterPowerCount, auxList, flagAux, water);
            updatePowerCounters(delta, fireCounterDrops, firePowerCount, firePowerTimers, elapsedTimeFire, maxFirePowerCount, fireauxList, fireflagAux, fire);
            updatePowerCounters(delta, bombCounterDrops, bombPowerCount, bombPowerTimers, elapsedTimeBomb, maxBombPowerCount, bombauxList, bombflagAux, bomb);

            updateBarrierCounters(delta, woodCounterDrops, countersBarriers.getWoodCounter(), woodBarrierTimers, elapsedTimeWood, maxWoodBarrierCount, woodauxList, woodflagAux, wood);
            System.out.println(countersBarriers.getWoodCounter());
            // Lógica de regeneración de balas en función de resetInterval
            if (resetInterval > 0) {
                elapsedTimeWater += delta;
                if (elapsedTimeWater >= resetInterval) {
                    // Regenera la bala aquí
                    resetBullet();
                    resetBarriers();
                    elapsedTimeWater = 0;
                }
            }
        }
    }



    private void resetBullet() {

            if (waterCounterDrops > 0) {
                if (waterPowerCount < maxWaterPowerCount) {
                    waterPowerCount++;
                    //countersBarriers.setWoodCounter(countersBarriers.getWoodCounter() + 1);
                    updateCounterLabel();
                    waterPowerTimers.add(0f); // Puedes iniciar el temporizador a 0 segundos

                    waterCounterDrops--;
                }
            }

            if (fireCounterDrops > 0) {
                if (firePowerCount < maxFirePowerCount) {
                    firePowerCount++;
                    updateCounterLabel();
                    firePowerTimers.add(0f); // Puedes iniciar el temporizador a 0 segundos

                    fireCounterDrops--;
                }
            }

            if (bombCounterDrops > 0) {
                if (bombPowerCount < maxBombPowerCount) {
                    bombPowerCount++;
                    updateCounterLabel();
                    bombPowerTimers.add(0f); // Puedes iniciar el temporizador a 0 segundos

                    bombCounterDrops--;
                }
            }

    }

    private void resetBarriers() {

        if (woodCounterDrops > 0) {
            countersBarriers.setWoodCounter(countersBarriers.getWoodCounter() + 1);
            updateCounterLabel();
            woodBarrierTimers.add(0f); // Puedes iniciar el temporizador a 0 segundos

            woodCounterDrops--;

        }

        if (cementCounterDrops > 0) {
            countersBarriers.setCementCounter(countersBarriers.getCementCounter() + 1);
            updateCounterLabel();
            cementBarrierTimers.add(0f); // Puedes iniciar el temporizador a 0 segundos

            cementCounterDrops--;

        }

        if (steelCounterDrops > 0) {
            countersBarriers.setSteelCounter(countersBarriers.getSteelCounter() + 1);
            updateCounterLabel();
            steelBarrierTimers.add(0f); // Puedes iniciar el temporizador a 0 segundos

            steelCounterDrops--;

        }

    }

    private void updatePowerCounters(float delta, int counterDrops, int powerCount, List<Float> powerTimers, float elapsedTime, int maxPowerCount, List<Float> auxList, boolean flagAux, boolean powerType) {
        if (counterDrops > 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.R) && powerType) {
                flagAux = updateFlagAux(flagAux, counterDrops, auxList, elapsedTime);
            }

            if (elapsedTime >= resetInterval && powerCount < maxPowerCount) {
                powerCount++;
                updateCounterLabel();
                powerTimers.add(elapsedTime);
                counterDrops--;

                elapsedTime = updateElapsedTime(elapsedTime, auxList, powerCount);
            }

            updatePowerTimers(powerTimers, delta);
        }
    }

    private void updateBarrierCounters(float delta, int counterDrops, int powerCount, List<Float> powerTimers, float elapsedTime, int maxPowerCount, List<Float> auxList, boolean flagAux, boolean powerType) {
        if (counterDrops > 0) {
            if (powerType) {
                flagAux = updateFlagAux(flagAux, counterDrops, auxList, elapsedTime);
            }

            if (elapsedTime >= resetInterval && powerCount < maxPowerCount) {
                powerCount++;
                updateCounterLabel();
                powerTimers.add(elapsedTime);
                counterDrops--;

                elapsedTime = updateElapsedTime(elapsedTime, auxList, powerCount);
            }

            updatePowerTimers(powerTimers, delta);
        }
    }

    private boolean updateFlagAux(boolean flagAux, int counterDrops, List<Float> auxList, float elapsedTime) {
        if (counterDrops >= 2 && !flagAux) {
            auxList.add(elapsedTime);
            flagAux = true;
        }
        return flagAux;
    }


    private float updateElapsedTime(float elapsedTime, List<Float> auxList, int powerCount) {
        if (auxList.isEmpty()) {
            elapsedTime = 0;
        } else {
            if (powerCount < 3) {
                elapsedTime -= auxList.get(0);
                auxList.remove(0);
            } else {
                auxList.clear();

            }
        }
        return elapsedTime;
    }

    private void updatePowerTimers(List<Float> powerTimers, float delta) {
        for (int i = 0; i < powerTimers.size(); i++) {
            powerTimers.set(i, powerTimers.get(i) + delta);
            if (powerTimers.get(i) >= 5.0f) {
                // Realizar alguna acción si es necesario
            }
        }
    }

    private void handleCollisionAndBullet(float delta) {
        if (isCollide && bulletX < -bulletSprite.getWidth()) {
            isShooting = false;
        }


        if (isShooting) {
            float deltaX = targetX - bulletX;
            float deltaY = targetY - bulletY;
            float angle = MathUtils.atan2(deltaY, deltaX);
            bulletX += bulletSpeed * MathUtils.cos(angle) * Gdx.graphics.getDeltaTime();    bulletY += bulletSpeed * MathUtils.sin(angle) * Gdx.graphics.getDeltaTime();
            bulletImage.setPosition(bulletX, bulletY);
            if (bulletX < -bulletSprite.getWidth() || bulletX > Gdx.graphics.getWidth() ||
                    bulletY < 0 || bulletY > Gdx.graphics.getHeight() ||            (Math.abs(deltaX) <= bulletSpeed * Gdx.graphics.getDeltaTime() &&
                    Math.abs(deltaY) <= bulletSpeed * Gdx.graphics.getDeltaTime())) {
                isShooting = false;
                bulletImage.remove();
            }
        } else {
            Actions.removeActor(bulletImage);

        }


    }

    private void updateBulletPosition(float delta) {
        /*
        if (isShooting) {
            float deltaX = targetX - bulletX;
            float deltaY = targetY - bulletY;
            float angle = MathUtils.atan2(deltaY, deltaX);
            bulletX += bulletSpeed * MathUtils.cos(angle) * Gdx.graphics.getDeltaTime();    bulletY += bulletSpeed * MathUtils.sin(angle) * Gdx.graphics.getDeltaTime();
            bulletImage.setPosition(bulletX, bulletY);
            if (bulletX < -bulletSprite.getWidth() || bulletX > Gdx.graphics.getWidth() ||
                    bulletY < 0 || bulletY > Gdx.graphics.getHeight() ||            (Math.abs(deltaX) <= bulletSpeed * Gdx.graphics.getDeltaTime() &&
                    Math.abs(deltaY) <= bulletSpeed * Gdx.graphics.getDeltaTime())) {
                isShooting = false;
                bulletImage.remove();
            }
        } else {
            Actions.removeActor(bulletImage);

        }
        */

    }

    private void updateBulletTexture() {
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
    }


    public void handleInput() {

        Rectangle bulletBounds = new Rectangle(bulletX, bulletY, bulletSprite.getWidth(), bulletSprite.getHeight());

        for (Image barrierImage : eaglePVP) {
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
                    eaglePVP.removeValue(barrierImage, true);
                    barrierImage.remove();
                    //barrierDown++;
                    aguilaDestroy = true;
                    restart();
                }

            }
            System.out.println(barrierCounters);

        }


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
                    woodDown++;
                    woodCounterDrops++;
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
                        concreteDown++;
                        cementCounterDrops++;
                        barrierCounterscement.remove(barrierBounds);
                    } else if (fire) {
                        if (currentCountercement == 3) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 2);
                        } else if (currentCountercement < 3) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 2);
                            cementPVP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            concreteDown++;
                            cementCounterDrops++;
                            barrierCounterscement.remove(barrierBounds);
                        }

                    } else if (water) {
                        if (currentCountercement == 3 || currentCountercement == 2) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 1);
                        } else if (currentCountercement == 1) {
                            barrierCounterscement.put(barrierBounds, currentCountercement - 1);
                            cementPVP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            concreteDown++;
                            cementCounterDrops++;
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
                        steelDown++;
                        steelCounterDrops++;
                        barrierCountersSteel.remove(barrierBounds);
                    } else if (fire) {
                        barrierCountersSteel.put(barrierBounds, currentCounterSteel - 2);
                        steelPVP.removeValue(barrierImage, true);
                        barrierImage.remove();
                        steelDown++;
                        steelCounterDrops++;
                        barrierCountersSteel.remove(barrierBounds);
                    } else if (water) {
                        if (currentCounterSteel == 2) {
                            barrierCountersSteel.put(barrierBounds, currentCounterSteel - 1);
                        } else if (currentCounterSteel == 1) {
                            barrierCountersSteel.put(barrierBounds, currentCounterSteel - 1);
                            steelPVP.removeValue(barrierImage, true);
                            barrierImage.remove();
                            steelDown++;
                            steelCounterDrops++;
                            barrierCountersSteel.remove(barrierBounds);

                        }

                    }
                }

            }
            System.out.println(barrierCountersSteel);

        }

        if (isTimerActive) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !isShooting) {


                if (fireButton.isChecked()) {
                    bulletSprite.setTexture(fireTexture);
                    //stage.addActor(bulletImage);
                } else if (waterButton.isChecked()) {
                    bulletSprite.setTexture(waterTexture);
                    //stage.addActor(bulletImage);
                } else if (bombButton.isChecked()) {
                    bulletSprite.setTexture(bombTexture);
                    //stage.addActor(bulletImage);
                }
                // Inicia el disparo de la bala desde la posición del player.
                if (waterPowerCount > 0 && water) {

                    targetX = Gdx.input.getX();
                    targetY = Gdx.graphics.getHeight() - Gdx.input.getY();

                    bulletCollided = false;
                    bulletCollidedcement = false;
                    bulletCollidedSteel = false;
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2;
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
                    waterPowerCount--;
                    waterCounterDrops++;
                    stage.addActor(bulletImage);
                    waterCounterDropsTimes.add(elapsedTimeWater);
                    isShooting = true;
                    isCollide = false;
                } else if (firePowerCount > 0 && fire) {
                    targetX = Gdx.input.getX();
                    targetY = Gdx.graphics.getHeight() - Gdx.input.getY();

                    bulletCollided = false;
                    bulletCollidedcement = false;
                    bulletCollidedSteel = false;
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
                    firePowerCount--;
                    fireCounterDrops++;
                    stage.addActor(bulletImage);
                    fireCounterDropsTimes.add(elapsedTimeFire);
                    isShooting = true;
                    isCollide = false;
                } else if (bombPowerCount > 0 && bomb) {
                    targetX = Gdx.input.getX();
                    targetY = Gdx.graphics.getHeight() - Gdx.input.getY();

                    bulletCollided = false;
                    bulletCollidedcement = false;
                    bulletCollidedSteel = false;
                    bulletX = playerX + playerSprite.getWidth();
                    bulletY = playerY + playerSprite.getHeight() / 2 - bulletSprite.getHeight() / 2; //
                    bombPowerCount--;
                    bombCounterDrops++;
                    stage.addActor(bulletImage);
                    bombCounterDropsTimes.add(elapsedTimeFire);
                    isShooting = true;
                    isCollide = false;
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.UP) && playerY < Gdx.graphics.getHeight() - playerSprite.getHeight()) {
                System.out.println("W");
                if (playerY + playerSprite.getHeight() + speed * Gdx.graphics.getDeltaTime() <= 920) {
                    playerY += speed * Gdx.graphics.getDeltaTime();
                    player = new Texture("Back1.png");
                    playerSprite.setTexture(new Texture("Back1.png"));
                    playerImage.setPosition(playerX, playerY);
                }
            }


            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && playerY >= 110) {
                System.out.println("S");
                playerY -= speed * Gdx.graphics.getDeltaTime();
                player = new Texture("Front2.png");
                playerSprite.setTexture(new Texture("Front2.png"));
                playerImage.setPosition(playerX, playerY);
            }


            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playerX > 0) {
                System.out.println("A");
                float centerX = Gdx.graphics.getWidth() / 2.0f;
                if (playerX - speed * Gdx.graphics.getDeltaTime() >= centerX) {
                    playerX -= speed * Gdx.graphics.getDeltaTime();
                    player = new Texture("WalkL3.png");
                    playerSprite.setTexture(new Texture("WalkL3.png"));
                    playerImage.setPosition(playerX, playerY);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playerX < Gdx.graphics.getWidth() - playerSprite.getWidth()) {
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
        if (isTimerActive && elapsedTimeInSong >= songInfo.getDuration()) {
            //game.changeScreen(new GameOverScreen(game));
            //contadorPuntos++;
            //System.out.println(contadorPuntos);
            //game.changeScreen(new GameScreen(game, user2Manager, user, user2, countersBarriers, spotifyReference));
            isTimerActive = false;
            restart();

        }
    }

    public void restart(){
        if (contadorTurn <= 1) {
            contadorTurn++;
            System.out.println(barrierDown);
            System.out.println(elapsedTimeInSong);
            System.out.println(songInfo.getDuration());
            System.out.println(barrierDefend);


            int woodCantidad = woodPVP.size;
            int cementCantidad = cementPVP.size;
            int steelCantidad = steelPVP.size;

            barrierDown = (float) ((woodDown*0.6)+(steelDown*0.8)+(concreteDown*1.0));
            barrierDefend = (float)((10-woodCantidad)*0.6+(10-steelCantidad)*0.8+(10-cementCantidad)*1.0);

            System.out.println("Parametros");
            System.out.println(barrierDown);
            System.out.println(elapsedTimeInSong);
            System.out.println(songInfo.getDuration());
            System.out.println(barrierDefend);



            if(aguilaDestroy) {
                user2Points = (float) (user2Points + (1 / ((1 / barrierDown) * 0.5) + (1 / ((elapsedTimeInSong / songInfo.getDuration()) * 0.5))));
            }else{
                user2Points = 0+user2Points;
            }

            if(barrierDefend == 0) {
                user1Points = (float) (user1Points+ (1/(1/((1/elapsedTimeInSong)*0.5))));
            }else{
                user1Points = (float) (1/((1/barrierDefend)*0.5)+(1/((1/elapsedTimeInSong)*0.5)));
            }

            System.out.println(user1Points);
            System.out.println(user2Points);

            user2Points = Math.round(user2Points);
            user1Points = Math.round(user1Points);


            game.changeScreen(new GameScreen(game, user2Manager, user2, user, countersBarriers, spotifyReference, contadorTurn,user2Points,user1Points));
        }
        if(contadorTurn == 2){
            System.out.println(user1Points);
            System.out.println(user2Points);
            game.changeScreen(new GameOverScreen(game,user2Manager, user2, user, user2Points, user1Points));
            user.setPoints(user1Points);
            user2.setPoints(user2Points);
            user2Manager.update(user);
            user2Manager.update(user2);
        }
    }
/*
    private void warning (){
        int songMidTime = (int) (songInfo.getDuration() / 2);
        if (songInfo.getDuration() - elapsedTimeInSong <= songMidTime && songInfo.getDuration() - elapsedTimeInSong > 0){
            final Dialog dialog9 = new Dialog("Queda la mitad del tiempo!", skin);
            dialog9.show(stage);
            dialog9.setSize(210,80 );
            dialog9.button("Ok", new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    dialog9.remove();
                }
            });
        }
    }

 */



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