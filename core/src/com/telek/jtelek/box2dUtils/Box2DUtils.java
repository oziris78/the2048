package com.telek.jtelek.box2dUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public final class Box2DUtils {


    private static float PPM;

    public static void init(float pixelsPerMeter) {
        PPM = pixelsPerMeter;
    }


    public static void moveSprite(Body body, Sprite sprite) {
        sprite.setPosition(body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2);
    }

    public static void rotateSprite(Body body, Sprite sprite) {
        sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
    }

    public static void moveAndRotateSprite(Body body, Sprite sprite) {
        moveSprite(body, sprite);
        rotateSprite(body, sprite);
    }


}
