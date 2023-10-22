package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import java.util.Random;

public class IAMode implements Screen {
    private final MainController game;
    private final Stage stage;
    private final OrthographicCamera camera;
    int screenWidth;
    int screenHeight;
    private User2 user;
    private Array<Image> randomIA;
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
    private ImageButton Defender;
    private ImageButton Attacker;
    SpriteBatch batch;
    Texture player;
    Texture playerTexture;
    Image playerImage;
    float playerX = 1300;
    float playerY = 500;
    float speed = 400.0f;

    public IAMode(final MainController game, User2 user) {
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
        batch = new SpriteBatch();
        setupMode();
        setupUIElements();
    }

    //----------------------------------------side selection-----------------------------------------
    private void setupMode() {
        // ImageButton Defender en el centro de la mitad izquierda de la pantalla
        Drawable defenderImage = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/sideD.png"))));
        Defender = new ImageButton(defenderImage);
        Defender.setPosition(0, screenHeight / 2 - Defender.getHeight() / 2);
        Defender.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                defenderSelected = true;
                Defender.remove();
                Attacker.remove();
                if (defenderSelected) {
                    setupButtonsDefender();
                    goblin = new Image(new Texture("SSF6.png"));
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
        Attacker.setPosition(screenWidth / 2, screenHeight / 2 - Attacker.getHeight() / 2);
        Attacker.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                defenderSelected = false;
                Defender.remove();
                Attacker.remove();
                if (!defenderSelected) {
                    setupButtonsAttacker();
                    showPlayer = true;
                    float halfScreenWidth = screenWidth / 2;
                    Texture aguilagodTexture = new Texture("aguilagod.png");
                    TextureRegion aguilagodRegion = new TextureRegion(aguilagodTexture);
                    TextureRegionDrawable aguilagodDrawable = new TextureRegionDrawable(aguilagodRegion);
                    aguilagodImage = new Image(aguilagodDrawable);
                    aguilagodImage.setSize(150, 80);
                    float aguilagodY;
                    float minY = 120;
                    float maxY = screenHeight - 120 - aguilagodImage.getHeight();
                    aguilagodY = MathUtils.random(minY, maxY);
                    aguilagodImage.setPosition(0, aguilagodY);
                    stage.addActor(aguilagodImage);
                    int totalRandomIAPerType = 10;
                    for (int i = 0; i < totalRandomIAPerType; i++) {
                        addRandomImageIA("wood.jpg", aguilagodImage, halfScreenWidth, screenHeight);
                        addRandomImageIA("cement.jpg", aguilagodImage, halfScreenWidth, screenHeight);
                        addRandomImageIA("steel.jpg", aguilagodImage, halfScreenWidth, screenHeight);
                    }
                }
            }
        });
        stage.addActor(Attacker);
    }

    private void setupUIElements() {
        Skin skin = VisUI.getSkin();
        TextButton returnButton = new TextButton("Back", skin);
        returnButton.setPosition(1600, 500);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeScreen(new SelectMode(game));
            }
        });
        stage.addActor(returnButton);

        //-------------------------------------
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
    }


    //-------------------------------------------defender side---------------------------------------------
    private void addRandomImageIA(String imageFileName, Image referenceImage, float halfScreenWidth, float screenHeight) {
        Texture imageTexture = new Texture(imageFileName);
        TextureRegion imageRegion = new TextureRegion(imageTexture);
        TextureRegionDrawable imageDrawable = new TextureRegionDrawable(imageRegion);
        Image image = new Image(imageDrawable);

        boolean overlapped;
        float randomX, randomY;

        do {
            overlapped = false;
            randomX = MathUtils.random(referenceImage.getX() + referenceImage.getWidth(), halfScreenWidth - image.getWidth());
            randomY = MathUtils.random(120, screenHeight - 120 - image.getHeight());

            for (Image existingImage : randomIA) {
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
        stage.addActor(image);
        randomIA.add(image);
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
                for (Image placedImage : randomIA) {
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
                    randomIA.add(newImage);
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
                for (Image placedImage : randomIA) {
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
                    randomIA.add(newImage);
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
                for (Image placedImage : randomIA) {
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
                    randomIA.add(newImage);
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
                aguilaGodImage.setSize(150, 80);

                    boolean canPlace = true;
                    for (Image placedImage : randomIA) {
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
                        randomIA.add(aguilaGodImage);
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
    private float goblinWaitTimer = 30.0f;

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


    private void manualMovement(){
        if (showPlayer) {
            if (playerTexture == null) {
                playerTexture = new Texture("SSF6.png"); // Carga la textura del jugador
            }

            Sprite playerSprite = new Sprite(playerTexture);
            playerSprite.setPosition(playerX, playerY);
            playerImage = new Image(playerSprite);
            playerImage.setPosition(1300, 500);
            stage.addActor(playerImage);

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
    }

    private void setupButtonsAttacker(){
        Drawable fireChoose = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("assets/fire1.png"))));
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


    private Array<Image> getBarriers(){
        return randomIA;
    }

    private void Hitbox(){

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
            manualMovement();
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