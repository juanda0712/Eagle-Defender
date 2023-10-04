package com.mygdx.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextField;

public class GraphicElements {
    public static VisTextField createTextField(String message, VisTextField.VisTextFieldStyle style) {
        VisTextField textField = new VisTextField("");
        textField.setMessageText(message);
        textField.setStyle(style);
        return textField;
    }

    public static TextButton createButton(String text, Skin skin, ClickListener listener) {
        TextButton button = new TextButton(text, skin);
        if (listener != null) {
            button.addListener(listener);
        }
        return button;
    }

    public static TextButton createCustomButton(String text, VisCheckBox.VisCheckBoxStyle style, ClickListener listener) {
        TextButton button = new TextButton(text, style);
        if (listener != null) {
            button.addListener(listener);
        }
        return button;
    }

    public static Label createLabel(String text, Skin skin, Color color) {
        Label label = new Label(text, skin);
        label.setColor(color);
        return label;
    }
}
