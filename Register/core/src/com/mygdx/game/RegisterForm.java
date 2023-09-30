package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class RegisterForm implements Screen {

    @Override
    public void show() {
        //Skin skin = new Skin(Gdx.files.internal("skinTextField.json"));

        Stage stage = new Stage();

        BitmapFont font = new BitmapFont();
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        //Label lblCreateAccount = new Label("Create your account by entering your personal info");

        TextField usernameField = new TextField("username", style);
        usernameField.setPosition(100, 150);

        TextField ageField = new TextField("your age", style);

        TextField emailField = new TextField("email", style);

        TextField passwordField = new TextField("password", style);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        stage.addActor(passwordField);

        TextField passwordConfirm = new TextField("Confirm password",style);

        //TextField songsField = new TextField("", style);

        Button btnUpload = new Button();

        Button btnCreateAccount = new Button();

        Table table = new Table();
        table.setFillParent(true);
        //table.add(titleLabel).colspan(2).padBottom(20).row();
        table.add(usernameField).left().row();
        //table.add(new Label("Contraseña:", skin)).right();
        table.add(passwordField).left().row();
        table.add(btnCreateAccount).colspan(2).padTop(20);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
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
}