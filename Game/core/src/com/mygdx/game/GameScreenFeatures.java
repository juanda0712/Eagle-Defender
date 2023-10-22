package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.mygdx.models.User2;

public class GameScreenFeatures {
    private Stage stage;
    private Array<Image> placedImages;
    private GameScreen gameScreen;
    private User2 user;
    private CountersBarriers countersBarriers;
    private ImageButton woodenButton;
    private ImageButton cementButton;
    private ImageButton steelButton;
    private ImageButton eagleButton;
    private boolean aguilaGodPlaced = false;
    //private Image selectedImageToRotate;

    public GameScreenFeatures(Stage stage, Array<Image> placedImages, GameScreen gameScreen, User2 user, CountersBarriers countersBarriers, ImageButton woodenButton, ImageButton cementButton, ImageButton steelButton, ImageButton eagleButton) {
        this.stage = stage;
        this.placedImages = placedImages;
        this.gameScreen = gameScreen;
        this.user = user;
        this.countersBarriers = countersBarriers;
        this.woodenButton = woodenButton;
        this.cementButton = cementButton;
        this.steelButton = steelButton;
        this.eagleButton = eagleButton;
    }

    //Barreras de madera y su contador
    public void addWood(float x, float y) {
        if (gameScreen.isPlacingEnabled() && countersBarriers.getWoodCounter() > 0 && woodenButton.isChecked()) {
            String selectedTexture = user.getTexture();
            String imagePath = "assets/wood.jpg";
            if (x < stage.getWidth() / 2) {
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
                    minusWoodCounter();
                }
            }
        }
    }
    public void minusWoodCounter() {
        if (countersBarriers.getWoodCounter() > 0) {
            countersBarriers.setWoodCounter(countersBarriers.getWoodCounter() - 1);
            gameScreen.updateCounterLabel();
        }
    }

    //Barreras de cemento y su contador
    public void addCement(float x, float y) {
        if (gameScreen.isPlacingEnabled() && countersBarriers.getCementCounter() > 0 && cementButton.isChecked()) {
            String selectedTexture = user.getTexture();
            String imagePath = "assets/cement.jpg";
            if (x < stage.getWidth() / 2) {
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
                    minusCementCounter();
                }
            }
        }
    }
    public void minusCementCounter() {
        if (countersBarriers.getCementCounter() > 0) {
            countersBarriers.setCementCounter(countersBarriers.getCementCounter() - 1);
            gameScreen.updateCounterLabel();
        }
    }

    //Barreras de acero y su contador
    public void addSteel(float x, float y) {
        if (gameScreen.isPlacingEnabled() && countersBarriers.getSteelCounter() > 0 && steelButton.isChecked()) {
            String selectedTexture = user.getTexture();
            String imagePath = "assets/steel.jpg";
            if (x < stage.getWidth() / 2) {
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
                    minusSteelCounter();
                }
            }
        }
    }
    public void minusSteelCounter() {
        if (countersBarriers.getSteelCounter() > 0) {
            countersBarriers.setSteelCounter(countersBarriers.getSteelCounter() - 1);
            gameScreen.updateCounterLabel();
        }
    }

    public void addEagle(float x, float y) {
        if (gameScreen.isPlacingEnabled() && countersBarriers.getEagleCounter() > 0 && eagleButton.isChecked()) {
            if (!aguilaGodPlaced) {
                // Verificar que la posición x esté en la parte izquierda de la pantalla
                if (x < stage.getWidth() / 2) {
                    Texture aguilaGodTexture = new Texture(Gdx.files.internal("assets/aguilagod.png"));
                    Image aguilaGodImage = new Image(aguilaGodTexture);
                    aguilaGodImage.setPosition(x, y);
                    aguilaGodImage.setSize(150, 80);

                    boolean canPlace = true;
                    for (Image placedImage : placedImages) {
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
                        placedImages.add(aguilaGodImage);
                        minusEagleCounter();
                        aguilaGodPlaced = true;
                    }
                }
            }
        }
    }
    public void minusEagleCounter() {
        if (countersBarriers.getEagleCounter() > 0) {
            countersBarriers.setEagleCounter(countersBarriers.getEagleCounter() - 1);
            gameScreen.updateCounterLabel();
        }
    }
    public Array<Image> getBarrierImages() {
        return placedImages;
    }

}