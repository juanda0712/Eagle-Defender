package com.mygdx.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Elf {
    static final float WIDTH=.45f; //Check it
    static final float HEIGHT=0.6f; //Check it

    static final float DRAW_WIDTH=1.3f;
    static final float DRAW_HEIGHT=1.3f;
    static final float WALK_FRAME_DURATION=0.05f;

    static final float WALK_SPEED=3;


    static final float SHOOT_SPEED=7;

    boolean isShooting;
    boolean isAfterShoot;
    boolean isWalking_L;
    boolean isWalking_R;
    boolean isWalking_F;
    boolean isWalking_B;


    boolean didShot;

    float stateTime=0;

    Vector2 position;
    Vector2 velocity;

    public Elf (float x, float y){

        position= new Vector2 (x,y);
    }



    public void update (Body body, float delta, float accelX){
        //In general 60times per sec.
        position.x=body.getPosition().x;
        position.y=body.getPosition().y;

        velocity= body.getLinearVelocity();

        isShooting=false;

        if (didShot){
            isShooting=true;
            didShot=false;
            stateTime=0;
        }



        //if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
        // float accelX = Gdx.input.getAccelerometerX();

        //To the left
        if (accelX > 0) {
            velocity.x = -WALK_SPEED;
            velocity.y = 0;
            isWalking_L=!isShooting && !isAfterShoot && !isWalking_R && !isWalking_F && !isWalking_B;
        }
        // To the right
        else if (accelX < 0) {
            velocity.x = WALK_SPEED;
            velocity.y = 0;
            isWalking_R=!isShooting && !isAfterShoot && !isWalking_L && !isWalking_F && !isWalking_B;
        }
        // UP or Back
        else if (accelX < 0) {
            velocity.x = 0;
            velocity.y = WALK_SPEED;
            isWalking_B=!isShooting && !isAfterShoot  && !isWalking_L && !isWalking_F && !isWalking_R;
        }
        // Down
        else if (accelX == 0) {
            velocity.x = 0;
            velocity.y = -WALK_SPEED;
            isWalking_F=!isShooting && !isAfterShoot && !isWalking_L && !isWalking_B && !isWalking_R;
        }
        else{
            velocity.x=0;
            velocity.y=0;
            isWalking_L=false;
            isWalking_R=false;
            isWalking_B=false;
            isWalking_F=false;
        }
        // }

        // position.add(velocity.x * Gdx.graphics.getDeltaTime(), velocity.y * Gdx.graphics.getDeltaTime());
        body.setLinearVelocity(velocity);
        stateTime+=delta;



    }

    public void shoot(){
        if(!isAfterShoot && !isShooting && !isWalking_L && !isWalking_R && !isWalking_F && !isWalking_B){
            didShot=true;
            velocity.x = SHOOT_SPEED;
        }
    }
}