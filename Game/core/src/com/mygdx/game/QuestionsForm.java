package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
import lombok.Getter;


@Getter
public class QuestionsForm extends VisWindow {

    private final Array<String> questions;


    public QuestionsForm() {


        super("Questions");
        Skin skin = VisUI.getSkin();
        questions = new Array<String>();

        //setSize(400, 200);
        setPosition(400, 450);
        Label petName = new Label("What is your pet's name?", skin);
        add(petName).left().pad(10);
        row();

        final VisTextField petNameTextField = new VisTextField("");
        // petNameTextField.setMessageText("Pet name");
        add(petNameTextField).left().pad(5);
        row();

        Label favTeacherLastName = new Label("What is your favorite teacher’s last name?", skin);
        add(favTeacherLastName).left().pad(5);
        row();

        final VisTextField favTeacherLastNameField = new VisTextField("");
        //favTeacherLastNameField.setMessageText("Teacher's last name");
        add(favTeacherLastNameField).left().pad(5);
        row();

        Label favTeam = new Label("What is your favorite soccer team?", skin);
        add(favTeam).left().pad(5);
        row();

        final VisTextField favTeamField = new VisTextField("");
        add(favTeamField).left().pad(5);
        row();

        Label childhoodNickname = new Label("What was your childhood nickname?", skin);
        add(childhoodNickname).left().pad(5);
        row();

        final VisTextField childhoodNicknameField = new VisTextField("");
        add(childhoodNicknameField).left().pad(5);
        row();

        Label place = new Label("In which place would you like to live?", skin);
        add(place).left().pad(5);
        row();

        final VisTextField placeField = new VisTextField("");
        add(placeField).left().pad(5);
        row();

        TextButton btnDone = new TextButton("Done!", skin);
        btnDone.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                questions.clear();
                questionsList(petNameTextField);
                questionsList(favTeacherLastNameField);
                questionsList(favTeamField);
                questionsList(childhoodNicknameField);
                questionsList(placeField);
                remove();
            }
        });


        add(btnDone).center().pad(5);
        row();

        pack();
        center();
        setVisible(true);
        setModal(true);
        addCloseButton();
    }

    public void questionsList(VisTextField field) {
        questions.add(field.getText());
    }

}