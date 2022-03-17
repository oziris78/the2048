package com.telek.jtelek.scene2dUtils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class Scene2DUtils {


    public static void changeFont(TextButton btn, BitmapFont font) {
        TextButton.TextButtonStyle style = btn.getStyle();
        style.font = font;
        btn.setStyle(style);
    }

    public static void changeFont(Label label, BitmapFont font) {
        Label.LabelStyle style = label.getStyle();
        style.font = font;
        label.setStyle(style);
    }


}