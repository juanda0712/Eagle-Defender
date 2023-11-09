package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;


import static com.badlogic.gdx.Gdx.app;

public class Top10Screen implements Screen {
    private Stage stage;
    private MainController game;
    private Skin skin;

    public Top10Screen (MainController game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("Skin/uiskin.json"));

        Image top10 = new Image(new Texture("GameOver/Top10.png"));
        top10.setPosition(0, 0);
        stage.addActor(top10);






        //int score,highscore, secondscore, thirdscore, fourthscore, fifthscore, sixthscore, seventhscore, eighthscore, ninthscore, tenthscore;

        //BitmapFont scoreFont;



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

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setSize(200, 60);
        exitButton.setPosition(850, 50);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);


        TextButton backButton= new TextButton("Back", skin);
        backButton.setSize (400, 100);
        backButton.setPosition(850, 140);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(game));
            }
        });
        stage.addActor(backButton);


    }




    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.4f, 0.1f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }
}