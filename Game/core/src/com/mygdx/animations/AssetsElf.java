package com.mygdx.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class AssetsElf extends InputAdapter {
    // static SpriteBatch batch;
    SpriteBatch batch;
    OrthographicCamera camera;
    static Sprite shoot;
    static Sprite after_shoot;
    static Sprite idle;
    static Animation<Sprite> walk_f;
    static Animation<Sprite> walk_b;
    static Animation<Sprite> walk_r;
    static Animation<Sprite> walk_l;

    static TextureAtlas atlas;

    public AssetsElf() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        batch = new SpriteBatch();
    }

    public static void load() {

        atlas = new TextureAtlas(Gdx.files.internal("assets/Animation/Elf2.atlas"));
        shoot = atlas.createSprite("Shoot1");
        after_shoot = atlas.createSprite("Shoot2");
        idle = atlas.createSprite("Idle");

        walk_f = new Animation<>(
                Elf.WALK_FRAME_DURATION,
                atlas.createSprite("Front1"),
                atlas.createSprite("Front2"),
                atlas.createSprite("Front3"));


        walk_b = new Animation<>(
                Elf.WALK_FRAME_DURATION,
                atlas.createSprite("Back1"),
                atlas.createSprite("Back2"),
                atlas.createSprite("Back3"),
                atlas.createSprite("Back4"));


        walk_l = new Animation<>(
                Elf.WALK_FRAME_DURATION,
                atlas.createSprite("WalkL1"),
                atlas.createSprite("WalkL2"),
                atlas.createSprite("WalkL3"),
                atlas.createSprite("WalkL4"));


        walk_r = new Animation<>(
                Elf.WALK_FRAME_DURATION,
                atlas.createSprite("WalkR1"),
                atlas.createSprite("WalkR2"),
                atlas.createSprite("WalkR3"),
                atlas.createSprite("WalkR4"));


    }


    /* @Override
     public void resize(int width, int height) {
         camera.setToOrtho(false, width, height);

     }*/
    public static void dispose() {
        atlas.dispose();

    }
}