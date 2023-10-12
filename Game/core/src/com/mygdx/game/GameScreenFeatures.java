package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class GameScreenFeatures {
    private Stage stage;
    private Array<Image> placedImages;
    Rectangle playerRect;


    public GameScreenFeatures(Stage stage, Array<Image> placedImages) {
        this.stage = stage;
        this.placedImages = placedImages;

    }

    public void AddImage(float x, float y) {
        if (x < stage.getWidth() / 2) {
            Texture newImageTexture = new Texture(Gdx.files.internal("assets/b7.jpg"));
            Image newImage = new Image(newImageTexture);


            newImage.setPosition(x, y);

            boolean canPlace = true;
            for (Image placedImage : placedImages) {
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
                placedImages.add(newImage);



            }
        }
    }
}