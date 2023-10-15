package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;

public class QuestionsForm2 extends VisWindow {

    private final Array<String> questions;
    private final VisTextField petNameTextField;
    private final VisTextField favTeacherLastNameField;
    private final VisTextField favTeamField;
    private final VisTextField childhoodNicknameField;
    private final VisTextField placeField;

    private LoginScreen loginScreen;

    private User2 user;
    public interface DialogCallback {
        void displayDialog(String message);
    }

    private final Array<User2> data;

    public QuestionsForm2(final MainController game, JSONDataManager<User2> user2Manager, final User2 user, final LoginScreen loginScreen) {

        super("Questions");
        final Skin skin = VisUI.getSkin();
        questions = new Array<String>();
        data = user2Manager.read();
        this.loginScreen = loginScreen;
        Stage stage = new Stage();
        // Initialize and set up your UI elements
        petNameTextField = new VisTextField("");
        favTeacherLastNameField = new VisTextField("");
        favTeamField = new VisTextField("");
        childhoodNicknameField = new VisTextField("");
        placeField = new VisTextField("");
        TextButton btnValidate = new TextButton("Validate", skin);

        // Initialize your labels
        Label petNameLabel = new Label("What is your pet's name?", skin);
        Label favTeacherLastNameLabel = new Label("What is your favorite teacher’s last name?", skin);
        Label favTeamLabel = new Label("What is your favorite soccer team?", skin);
        Label childhoodNicknameLabel = new Label("What was your childhood nickname?", skin);
        Label placeLabel = new Label("In which place would you like to live?", skin);

        // Add your UI elements to the window
        add(petNameLabel).left().pad(10);
        row();
        add(petNameTextField).left().pad(5);
        row();

        add(favTeacherLastNameLabel).left().pad(5);
        row();
        add(favTeacherLastNameField).left().pad(5);
        row();

        add(favTeamLabel).left().pad(5);
        row();
        add(favTeamField).left().pad(5);
        row();

        add(childhoodNicknameLabel).left().pad(5);
        row();
        add(childhoodNicknameField).left().pad(5);
        row();

        add(placeLabel).left().pad(5);
        row();
        add(placeField).left().pad(5);
        row();

        add(btnValidate);

        btnValidate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                User2 foundUser = null;

                for (User2 user : data) {
                    if (questionsArray().get(0).equals(user.getPetName()) && questionsArray().get(1).equals(user.getFavTeacherLastnamne())
                            && questionsArray().get(2).equals(user.getFavTeam()) && questionsArray().get(3).equals(user.getChildhoodNickName())
                            && questionsArray().get(4).equals(user.getFavPlace())) {
                        foundUser = user;
                        break; // No need to continue searching
                    }
                }

                String message;
                if (foundUser != null) {
                    message = "Your password is " + foundUser.getPassword();
                } else {
                    message = "Try again";
                }

                // Display the message outside the loop
                loginScreen.displayDialog(message);
            }
        });




        add(btnValidate).center().pad(5);
        row();

        pack();
        center();
        setVisible(true);
        setModal(true);
        addCloseButton();
    }
    public void questions(VisTextField field) {
        questions.add(field.getText());
    }
    public Array<String> questionsArray() {
        questions(petNameTextField);
        questions(favTeacherLastNameField);
        questions(favTeamField);
        questions(childhoodNicknameField);
        questions(placeField);

        return questions;
    }


}

