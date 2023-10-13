package com.mygdx.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class General extends InputAdapter implements Screen {

    World oWorld;
    Elf elf;
    Array<Body> arrBodies;
    Box2DDebugRenderer render;
    SpriteBatch batch;
    OrthographicCamera camera;
    Texture elfTexture;
    Texture lineTexture;

    public void create (){
        Vector2 gravity = new Vector2 (0,-10f);
        oWorld= new World(gravity, true);

        arrBodies=new Array<>();

        render=new Box2DDebugRenderer();

        createElf();
    }



    private void createElf() {
        elf= new Elf(4,5);
        BodyDef bd= new BodyDef();
        bd.position.x=elf.position.x;
        bd.position.y=elf.position.y;
        bd.type= BodyDef.BodyType.DynamicBody;

        PolygonShape shape= new PolygonShape();
        shape.setAsBox(Elf.WIDTH, Elf.HEIGHT);

        FixtureDef fixDef= new FixtureDef();
        fixDef.shape=shape;
        fixDef.density=15;
        fixDef.friction=0;

        Body body= oWorld.createBody(bd);
        body.createFixture(fixDef);
        body.setUserData(elf);
        shape.dispose();

    }



    public void update (float delta) {
        float accelX=0;


        if(Gdx.input.isKeyPressed (Input.Keys.A)){
            accelX=-1;

        } else if(Gdx.input.isKeyPressed (Input.Keys.D)){
            System.out.println("D");
            accelX=1;

        } else if (Gdx.input.isKeyPressed (Input.Keys.W)){
            System.out.println("W");
            accelX=0;

        } else if(Gdx.input.isKeyPressed (Input.Keys.S)){
            System.out.println("S");
            accelX=0;
        }
        if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.R)){
            System.out.println("R");
            elf.shoot();
        }

        oWorld.step (delta, 8,6);
        oWorld.getBodies(arrBodies);
        for (Body body : arrBodies){
            if (body.getUserData() instanceof Elf){
                Elf obj = (Elf) body.getUserData();
                obj.update(body, delta, accelX);
            }
        }
    }

    //en el video hay dos cámaras****REVISAR
    public void draw (float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        ScreenUtils.clear(1, 1, 1, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(elfTexture,1730,920,150,150);
        float centerX = Gdx.graphics.getWidth() / 2;
        float screenWidth = Gdx.graphics.getWidth();
        float yCoordinate = 920;
        batch.draw(lineTexture, centerX, 0, 2, 920);
        batch.draw(lineTexture, 0, yCoordinate, screenWidth, 2);
        batch.end();
        drawElf();

    }

    private void drawElf(){
        Sprite keyFrame= AssetsElf.idle;

        if (elf.isShooting){
            keyFrame=AssetsElf.shoot;
        }
        else if (elf.isAfterShoot){
            keyFrame= AssetsElf.after_shoot;
        }
        else if (elf.isWalking_L){
            keyFrame=AssetsElf.walk_l.getKeyFrame(elf.stateTime, true);
        }
        else if (elf.isWalking_R){
            keyFrame=AssetsElf.walk_r.getKeyFrame(elf.stateTime, true);
        }
        else if (elf.isWalking_B){
            keyFrame=AssetsElf.walk_b.getKeyFrame(elf.stateTime, true);
        }
        else if (elf.isWalking_F){
            keyFrame=AssetsElf.walk_f.getKeyFrame(elf.stateTime, true);
        }

        keyFrame.setPosition(elf.position.x-Elf.DRAW_WIDTH/2, elf.position.y-Elf.DRAW_HEIGHT/2);
        keyFrame.setSize(elf.DRAW_WIDTH, elf.DRAW_HEIGHT);

        keyFrame.draw(batch);
    }







    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

    }

    /*@Override
    public void dispose(float delta) {

        AssetsElf.dispose();
    }*/

}

