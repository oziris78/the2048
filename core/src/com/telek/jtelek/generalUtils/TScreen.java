package com.telek.jtelek.generalUtils;

import com.badlogic.gdx.Screen;

public interface TScreen extends Screen {
    public void configure();

    public void update(float delta);
}
