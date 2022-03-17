package com.telek.jtelek.gdxaiUtils;


import com.badlogic.gdx.math.Vector2;

public final class SteeringUtils {


    // for box2d

    public static float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    public static Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }


}
