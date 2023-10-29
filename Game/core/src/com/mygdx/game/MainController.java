package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.Recognizer;

public class MainController extends Game {

    private LoginScreen login;
    public SpriteBatch batch;
    public JSONDataManager<User2> user2Manager;
    private Recognizer recognizer;
    private RealTimeFaceDetectionScreen faceScreen;

    @Override
    public void create() {
        user2Manager = new JSONDataManager<>("data/users2.json", User2.class);
        VisUI.load();
        batch = new SpriteBatch();
        login = new LoginScreen(this, user2Manager, null, null);
        this.setScreen(login);
    }

    public void changeScreen(Screen newScreen) {
        setScreen(newScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        login.dispose();
    }
}
