package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class CardDataForm extends VisWindow {

    public CardDataForm (){
        super("Card data");
        Skin skin = VisUI.getSkin();

        //setSize(400, 200);
        setPosition(400, 450);
        Label cardInfo = new Label("Enter your card's information please", skin);
        add(cardInfo).left().pad(5);
        row();

        Label cardNumber = new Label("Card number", skin);
        add(cardNumber).left().pad(5);
        row();

        VisTextField cardNumberField = new VisTextField("");
        add(cardNumberField).left().pad(5);
        row();

        Label cardName = new Label("Name", skin);
        add(cardName).left().pad(5);
        row();

        VisTextField cardNameField = new VisTextField("");
        add(cardNameField).left().pad(5);
        row();

        Label cardExpiration = new Label("Expiration date", skin);
        add(cardExpiration).left().pad(5);
        row();

        VisTextField cardExpirationField = new VisTextField("");
        add(cardExpirationField).left().pad(5);
        row();

        Label cardCVS = new Label("CVS", skin);
        add(cardCVS).left().pad(5);
        row();

        VisTextField cardCVSField = new VisTextField("");
        add(cardCVSField).left().pad(5);
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
