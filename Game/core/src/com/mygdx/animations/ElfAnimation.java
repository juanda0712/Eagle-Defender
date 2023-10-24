package com.mygdx.animations;


import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.animations.Barrier;


public  class ElfAnimation extends InputAdapter implements Screen {


    private List<Barrier> barriers = new ArrayList<>();
    //SpriteBatch batch;

    // TextureRegion[] current_frame;
    Sprite playerSprite;

    Sprite bulletSprite;
    //Animation animation;


    Rectangle bulletRect;
    Rectangle playerRect;

    Texture defensorTexture;
    Texture atacanteTexture;
    Texture barreraTexture;
    Texture bulletTexture;
    Texture player;
    Texture playerTexture;
    Texture lineTexture;



    SpriteBatch batch;

    OrthographicCamera camera;


    float playerX = 1300;
    float playerY = 500;
    float speed = 400.0f;
    float bulletX;
    float bulletY;

    boolean isShooting = false;
    float bulletSpeed = 800.0f;
    int contador =0;
    int contador2 =0;

    public ElfAnimation() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        playerTexture = new Texture("Idle.png");
        defensorTexture = new Texture("gojo.jpg");
        atacanteTexture = new Texture("sukuna.jpg");
        barreraTexture = new Texture("Brick.png");
        bulletTexture = new Texture("bala.PNG");
        lineTexture = new Texture("negro.jpg");


        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(playerX, playerY);


        bulletSprite = new Sprite(bulletTexture);


        playerRect = new Rectangle(playerX, playerY, playerTexture.getWidth(), playerTexture.getHeight());
        bulletRect = bulletSprite.getBoundingRectangle();

    }

    @Override
    public void show() {
    }

    @Override
    public void render (float delta) {
        //Para evitar cualquier cambio de color o parpadeo:
        Gdx.gl.glClearColor(1, 1, 1, 0);
        ScreenUtils.clear(1, 1, 1, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(defensorTexture,30,920,150,150);
        batch.draw(atacanteTexture,1730,920,150,150);
        playerSprite.draw(batch);

        float centerX = Gdx.graphics.getWidth() / 2;
        float screenWidth = Gdx.graphics.getWidth();
        float yCoordinate = 920;
        batch.draw(lineTexture, centerX, 0, 2, 920);
        batch.draw(lineTexture, 0, yCoordinate, screenWidth, 2);

        //posición de la bala
        if (isShooting) {
            bulletX -= bulletSpeed * Gdx.graphics.getDeltaTime();//Donde la bala va a ser lanzada
            bulletSprite.setPosition(bulletX, bulletY);
            bulletSprite.draw(batch);

            // Si la bala salió de la pantalla la elimina
            if (bulletX < -bulletSprite.getWidth()) {
                isShooting = false;
            }
        }

        for (Barrier barrera : barriers) {
            barrera.draw(batch);
        }
        batch.end();
        handleInput();
    }

    private void handleInput() {

        for (Barrier newBarrier : barriers) {
            if (bulletSprite.getBoundingRectangle().overlaps(newBarrier.getBoundingRectangle()) && isShooting) {
                System.out.println("collided");
                isShooting = false;
                contador = contador + 1;
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
                player = new Texture("Back1.png");
                playerSprite.setTexture(new Texture("Back1.png"));
                playerSprite.setY(playerY);
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.S) && playerY > 0) {
            System.out.println("S");
            playerY -= speed * Gdx.graphics.getDeltaTime();
            player = new Texture("Front2.png");
            playerSprite.setTexture(new Texture("Front2.png"));
            playerSprite.setY(playerY);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.A) && playerX > 0) {
            System.out.println("A");
            float centerX = Gdx.graphics.getWidth() / 2.0f;
            if (playerX - speed * Gdx.graphics.getDeltaTime() >= centerX) {
                playerX -= speed * Gdx.graphics.getDeltaTime();
                player = new Texture("WalkL3.png");
                playerSprite.setTexture(new Texture("WalkL3.png"));
                playerSprite.setX(playerX);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && playerX < Gdx.graphics.getWidth() - playerSprite.getWidth()) {
            System.out.println("D");
            playerX += speed * Gdx.graphics.getDeltaTime();
            player = new Texture("WalkR3.png");
            playerSprite.setTexture(new Texture("WalkR3.png"));
            playerSprite.setX(playerX);
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            player = new Texture("Idle.png");
            playerSprite.setTexture(new Texture("Idle.png"));
        }

        if (contador2 <= 9) {
            if (Gdx.input.justTouched()) {
                // En esta parte obtiene la dirreción de la posición de mouse
                float clicX = Gdx.input.getX();
                float clicY = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (clicY <= 920 && clicX < Gdx.graphics.getWidth() / 2){
                    // Crea una nueva barrera en la posición del clic y agrégala a la lista
                    Barrier newBarrier = new Barrier(barreraTexture, clicX, clicY);
                    for (Barrier barrier : barriers) {
                        if (newBarrier.getBoundingRectangle().overlaps(newBarrier.getBoundingRectangle())) {
                            System.out.println("collided");
                            break;


                        } else {
                            System.out.println("fuck!!!");
                            barriers.add(newBarrier);
                            contador2++;
                            break;
                        }

                    }
                    if (contador2 == 0) {
                        System.out.println("fuck!!!");
                        barriers.add(newBarrier);
                        contador2++;


                    }
                }
            }

        }
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);

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

    }
}

