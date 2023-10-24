package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


import static com.badlogic.gdx.Gdx.app;

public class Top10Screen implements Screen {
    //private Game game;
    private Stage stage;
    private Skin skin;


    private AssetManager manager;

    private MainController game;
    private GameOverScreen gameOverScreen;
    //private GameScreen gameScreen;
    private Image top10;

    private TextButton retry, exit, back;




    //int score,highscore, secondscore, thirdscore, fourthscore, fifthscore, sixthscore, seventhscore, eighthscore, ninthscore, tenthscore;

    //BitmapFont scoreFont;



    public Top10Screen(final MainController game) { //agregar int, score
        super ();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin= new Skin(Gdx.files.internal("Skin/uiskin.json"));
        top10 = new Image (getManager().get("GameOver/Top10.png", Texture.class));


        top10.setPosition(0, -50);



        stage.addActor(top10);

        initButtons();


        //this.score=score;

        //Get highscore from save file
       /* Preferences prefs= Gdx.app.getPreferences("eagledefender");
        this.highscore= prefs.getInteger("Highscore", 0);
        this.secondscore= prefs.getInteger("Secondscore", 0);
        this.thirdscore= prefs.getInteger("Thirdcore", 0);
        this.fourthscore= prefs.getInteger("fourthscore", 0);
        this.fifthscore= prefs.getInteger("fifthscore", 0);
        this.sixthscore= prefs.getInteger("sixthscore", 0);
        this.seventhscore= prefs.getInteger("seventhscore", 0);
        this.eighthscore= prefs.getInteger("eighthscore", 0);
        this.ninthscore= prefs.getInteger("ninthscore", 0);
        this.tenthscore= prefs.getInteger("tenthscore", 0);

var data = JSON.parse(json);
var top10 = data.sort(function(a,b) { return a.Variable1 > b.Variable1 ? 1 : -1; })
                .slice(0, 10);


        //Check if score beats 10 highscores
        if (score>highscore) {
            prefs.putInteger("highscore",score);
              prefs.flush();
            if (score>secondscore) {
                prefs.putInteger("secondscore",score);
                  prefs.flush();
                if (score>thirdscore) {
                    prefs.putInteger("thirdscore",score);
                      prefs.flush();
                    if (score>fourthscore) {
                        prefs.putInteger("fourthscore",score);
                          prefs.flush();
                        if (score>fifthscore) {
                            prefs.putInteger("fifthscore",score);
                              prefs.flush();
                            if (score>sixthscore) {
                                prefs.putInteger("sixthscore",score);
                                  prefs.flush();
                                if (score>seventhscore) {
                                    prefs.putInteger("seventhscore",score);
                                      prefs.flush();
                                    if (score>eighthscore) {
                                        prefs.putInteger("eighthscore",score);
                                          prefs.flush();
                                        if (score>ninthscore) {
                                            prefs.putInteger("ninethscore",score);
                                              prefs.flush();
                                            if (score>tenthscore) {
                                                prefs.putInteger("tenthscore",score);
                                                  prefs.flush();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        */

        //Load textures and Fonts
        //gameOverBanner= new Texture("GameOver/Game Over P1.png");
        //scoreFont=new BitmapFont(Gdx.files.internal("skin/default.fnt"));

    }

    public void create() {
        manager= new AssetManager();
        manager.load("GameOver/Top10.png", Texture.class);
        manager.finishLoading();
    }

    public AssetManager getManager(){
        return manager;
    }


    private void initButtons() {
        retry= new TextButton("Retry", skin);
        retry.setSize (200,60);
        retry.setPosition(850, 300);
        retry.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //setScreen(new GameScreen());;
            }
        });
        stage.addActor(retry);


        exit= new TextButton("Exit", skin);
        exit.setSize (200,60);
        exit.setPosition(850, 220);
        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                app.exit();
            }
        });
        stage.addActor(exit);


        back= new TextButton("Back", skin);
        back.setSize (200,60);
        back.setPosition(850, 140);
        back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(game));
            }
        });
        stage.addActor(top10);


    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

       /* //Draw banner
        game.batch.draw(gameOverBanner, Gdx.graphics.getWidth()/2- BANNER_WIDTH/2, Gdx.graphics.getHeight()-BANNER_HEIGHT-15,BANNER_WIDTH, BANNER_HEIGHT);
        GlyphLayout scoreLayout=new GlyphLayout(scoreFont, "Score: /n", Color.WHITE,0, Align.left, false);
        scoreFont.draw (game.batch, scoreLayout,Gdx.graphics.getWidth()/2- scoreLayout.width/2, Gdx.graphics.getHeight()-BANNER_HEIGHT-15*2);
        //PARA SALÓN DE LA FAMA scoreFont.draw (game.batch, scoreLayout,Gdx.graphics.getWidth()/2- scoreLayout.width/2, Gdx.graphics.getHeight()-BANNER_HEIGHT-15*2);
        //scoreFont.draw (game.batch, scoreLayout,Gdx.graphics.getWidth()/2- scoreLayout.width/2, Gdx.graphics.getHeight()-BANNER_HEIGHT-15*2);

        game.batch.end();
        */

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
    }

}

