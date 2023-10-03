package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;
import com.mygdx.models.User;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;

public class MainController extends Game {

    private LoginScreen login;
    //private FormManagement register;
    public SpriteBatch batch;
    public JSONDataManager<User> userManager;
    public JSONDataManager<User2> user2Manager;

    @Override
    public void create() {
        VisUI.load();
        batch = new SpriteBatch();
        user2Manager = new JSONDataManager<>("data/users2.json", User2.class);
        //register = new FormManagement(this, userManager);
        login = new LoginScreen(this, user2Manager);
        this.setScreen(login);
    }

    // Método para cambiar de pantalla
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
        //register.dispose();
    }
}
