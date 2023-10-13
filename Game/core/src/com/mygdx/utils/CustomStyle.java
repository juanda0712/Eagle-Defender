package com.mygdx.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTextField;

public class CustomStyle {
    public static VisTextField.VisTextFieldStyle createTextFieldStyle(BitmapFont font, Color fontColor, TextureRegionDrawable background) {
        VisTextField.VisTextFieldStyle style = new VisTextField.VisTextFieldStyle();
        style.font = font;
        style.fontColor = fontColor;
        style.background = background;
        return style;
    }

    public static VisCheckBox.VisCheckBoxStyle createCheckBoxStyle(BitmapFont font, Color fontColor, Color overFontColor) {
        VisCheckBox.VisCheckBoxStyle style = new VisCheckBox.VisCheckBoxStyle();
        style.font = font;
        style.fontColor = fontColor;
        style.overFontColor = overFontColor;
        return style;
    }
}

