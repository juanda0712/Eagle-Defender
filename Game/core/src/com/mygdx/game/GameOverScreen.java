package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.models.User2;
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.JSONDataManager;


import static com.badlogic.gdx.Gdx.app;

public class GameOverScreen implements Screen {
    private Stage stage;


    private MainController game;

    private Skin skin;

    private AssetManager manager;


    private Image gameover1, gameover2, gameover3, gameover4, gameover5;

    private TextButton retry, exit, top10;
    private Texture myTexture;
    private TextureRegion myTextureRegion;
    private TextureRegionDrawable myTexRegionDrawable;
    private ImageButton facebook;

    private JSONDataManager<User2> user2Manager;
    private CountersBarriers countersBarriers;


    private User2 user;

    //int score,highscore, secondscore, thirdscore, fourthscore, fifthscore, sixthscore, seventhscore, eighthscore, ninthscore, tenthscore;
    //BitmapFont scoreFont;

    public GameOverScreen(MainController game) { //agregar int, score
        super();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));
        gameover1 = new Image(getManager().get("GameOver/Game Over P1.png", Texture.class));
        gameover2 = new Image(getManager().get("GameOver/Game Over P2.png", Texture.class));
        gameover3 = new Image(getManager().get("GameOver/Game Over P3.png", Texture.class));
        gameover4 = new Image(getManager().get("GameOver/Game Over P4.png", Texture.class));
        gameover5 = new Image(getManager().get("GameOver/Game Over P5.png", Texture.class));

        gameover1.setPosition(0, -50);
        stage.addActor(gameover1);

        initButtons();

    }


    public void create() {
        manager = new AssetManager();
        manager.load("GameOver/Game Over P1.png", Texture.class);
        manager.load("GameOver/Game Over P2.png", Texture.class);
        manager.load("GameOver/Game Over P3.png", Texture.class);
        manager.load("GameOver/Game Over P.png", Texture.class);
        manager.load("GameOver/Game Over P5.png", Texture.class);
        manager.finishLoading();
    }

    public AssetManager getManager() {
        return manager;
    }


    private void initButtons() {
        /*retry = new TextButton("Retry", skin);
        retry.setSize(200, 60);
        retry.setPosition(850, 300);
        retry.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, user2Manager, user, countersBarriers, null));
            }
        });
        stage.addActor(retry);

         */


        exit = new TextButton("Exit", skin);
        exit.setSize(200, 60);
        exit.setPosition(850, 220);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.exit();
            }
        });
        stage.addActor(exit);

        /*top10 = new TextButton("Top 10", skin);
        top10.setSize(200, 60);
        top10.setPosition(850, 140);
        top10.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Top10Screen(game));
            }
        });
        stage.addActor(top10);


        myTexture = new Texture(Gdx.files.internal("GameOver/Fbkp.png"));
        myTextureRegion = new TextureRegion(myTexture);
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        facebook = new ImageButton(myTexRegionDrawable); //Set the button up
        facebook.setPosition(930, 80);
        facebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //setScreen(new Facebook (this));
            }
        });
        stage.addActor(facebook);

         */

    }

    /*

        private Image getImageForPalette(String selectedPalette) {
            Image gameover = new Image(); // Initialize Image without a drawable
            TextureRegion gameoverTextureRegion = null;



        private Image getImageForPalette (String selectedPalette){
            Image gameover = new Image(gameover1); // Imagen1 inicial (sin filtro)
            if "Palette 1"{
                gameoverTextureRegion = gameover1;
                break;
                case "Palette 2":
                    gameoverTextureRegion = gameover2;
                    break;
                break;
                case "Palette 3":
                    gameoverTextureRegion = gameover3;
                    break;
                case "Palette 4":
                    gameoverTextureRegion = gameover4;
                    break;
                case "Palette 5":
                    gameoverTextureRegion = gameover5;
                    break;
            }
            return gameover;
        }

        private void applyImage () {
            String selectedPalette = user.getSelectedColorPalette();
            Image gameover = getImageForPalette(selectedPalette);

            gameover.setPosition(0, -50);
            stage.addActor(gameover);
        }
    */
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.4f, 0.1f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //ScreenUtils.clear(getImageForPalette(selectedPalette));
        stage.act();
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        stage.dispose();
        skin.dispose();
    }}
