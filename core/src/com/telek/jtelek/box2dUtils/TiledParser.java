package com.telek.jtelek.box2dUtils;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;


public class TiledParser {


    private static float PPM;

    public static void init(float pixelsPerMeter) {
        PPM = pixelsPerMeter;
    }


    public static void parseTiledObjectLayer(World world, MapObjects objects, Object userData) {
        Body body;

        for (MapObject object : objects) {
            MyPair pair = createShapeAndBodyDefinition(object);
            BodyDef bDef = pair.getBodyDef();
            Shape shape = pair.getShape();

            body = world.createBody(bDef);
            body.createFixture(shape, 1.0f);
            body.setUserData(userData);

            shape.dispose();
        }

    }


    /////////////////////////////////////////////////////////
    //////////////////////  HELPERS  ////////////////////////
    /////////////////////////////////////////////////////////


    private static class MyPair {
        private Shape shape;
        private BodyDef bodyDef;

        public MyPair(Shape shape, BodyDef bodyDef) {
            this.shape = shape;
            this.bodyDef = bodyDef;
        }

        public Shape getShape() {
            return shape;
        }

        public BodyDef getBodyDef() {
            return bodyDef;
        }
    }


    private static MyPair createShapeAndBodyDefinition(MapObject object) {
        if (object instanceof PolygonMapObject)
            return createPolyLine((PolygonMapObject) object);
        if (object instanceof RectangleMapObject)
            return createBox((RectangleMapObject) object);
        if (object instanceof EllipseMapObject)
            return createCircle((EllipseMapObject) object);
        else return null;
    }


    private static MyPair createCircle(EllipseMapObject object) {
        Ellipse ellipse = object.getEllipse();
        if (ellipse.width != ellipse.height) // force it to be a circle if it's not a circle
            ellipse.height = ellipse.width;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(ellipse.width / 2f / PPM);

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(ellipse.x / PPM, ellipse.y / PPM);

        return new MyPair(circleShape, bDef);
    }


    private static MyPair createBox(RectangleMapObject object) {
        Rectangle rectangle = object.getRectangle();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.getWidth() / 2f / PPM, rectangle.getHeight() / 2f / PPM);

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(
                (rectangle.getX() + rectangle.getWidth() / 2f) / PPM,
                (rectangle.getY() + rectangle.getHeight() / 2f) / PPM
        );

        return new MyPair(polygonShape, bDef);
    }


    private static MyPair createPolyLine(PolygonMapObject object) {
        float[] vertices = object.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < worldVertices.length; i++)
            worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);

        ChainShape cs = new ChainShape();

        if (firstAndLastAreSame(worldVertices)) {
            cs.createChain(worldVertices);
        } else {  // must add the last one manually
            Vector2[] newWorldVertices = new Vector2[worldVertices.length + 1];

            for (int i = 0; i < worldVertices.length; i++) newWorldVertices[i] = worldVertices[i];
            Vector2 firstVertex = worldVertices[0];
            newWorldVertices[newWorldVertices.length - 1] = new Vector2(firstVertex.x, firstVertex.y);

            cs.createChain(newWorldVertices);
        }

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        return new MyPair(cs, bDef);
    }


    private static boolean firstAndLastAreSame(Vector2[] worldVertices) {
        return worldVertices[0].equals(worldVertices[worldVertices.length - 1]);
    }


}
