package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;


public class QuestionsForm extends VisWindow {

    public QuestionsForm  (){
        super("Questions");
        Skin skin = VisUI.getSkin();

        //setSize(400, 200);
        setPosition(400, 450);
        Label petName = new Label("What is your pet's name?", skin);
        add(petName).left().pad(10);
        row();

        VisTextField petNameTextField = new VisTextField("");
       // petNameTextField.setMessageText("Pet name");
        add(petNameTextField).left().pad(5);
        row();

        Label favTeacherLastName = new Label("What is your favorite teacher’s last name?",skin);
        add(favTeacherLastName).left().pad(5);
        row();

        VisTextField favTeacherLastNameField= new VisTextField("");
        //favTeacherLastNameField.setMessageText("Teacher's last name");
        add(favTeacherLastNameField).left().pad(5);
        row();

        Label favTeam = new Label("What is your favorite soccer team?", skin);
        add(favTeam).left().pad(5);
        row();

        VisTextField favTeamField = new VisTextField("");
        add(favTeamField).left().pad(5);
        row();

        Label childhoodNickname = new Label("What was your childhood nickname?",skin);
        add(childhoodNickname).left().pad(5);
        row();

        VisTextField childhoodNicknameField = new VisTextField("");
        add(childhoodNicknameField).left().pad(5);
        row();

        Label place = new Label("In which place would you like to live?", skin);
        add(place).left().pad(5);
        row();

        VisTextField placeField = new VisTextField("");
        add(placeField).left().pad(5);
        row();

        TextButton btnDone = new TextButton("Done!", skin);
        add(btnDone).center().pad(5);
        row();

        pack();
        center();
        setVisible(true);
        setModal(true);
        addCloseButton();
    }
}
