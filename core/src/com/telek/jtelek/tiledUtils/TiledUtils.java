package com.telek.jtelek.tiledUtils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;


public class TiledUtils {


    // Example:  TiledUtils.addAnimatedTileToMap(map, 0.5f, "myCoolTileset", "tile-layer", "blueFlowerAnimation", "heyyo");
    /*
     * You need to run this function once in a method like create() or show()
     *
     *  frameCount -> how many tiles are in this specific animation?
     *  durationOfEachFrame -> how much time is one frame going to take in the animation
     *  tilesetNameWithoutExt -> what is the name of the tileset that these animation frames are in? (without extension)
     *  layerNameForAnim -> the tile layer in your map that will have this animation
     *  animPropName -> you need to set a property in Tiled editor to all the animation tiles (name and value of that property should be a string)
     *                       this is the name of that property
     *  animPropValue -> this is the value of that property
     *
     */
    public static void addAnimatedTileToMap(TiledMap map, float durationOfEachFrame,
                                            String tilesetNameWithoutExt, String layerNameForAnim,
                                            String animPropName, String animPropValue) {
        Array<StaticTiledMapTile> frameTiles = new Array<>();

        Iterator<TiledMapTile> tiles = map.getTileSets().getTileSet(tilesetNameWithoutExt).iterator();
        while (tiles.hasNext()) {
            TiledMapTile tile = tiles.next();
            if (tile.getProperties().containsKey(animPropName) && tile.getProperties().get(animPropName, String.class).equals(animPropValue))
                frameTiles.add((StaticTiledMapTile) tile);
        }

        AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(durationOfEachFrame, frameTiles);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerNameForAnim);

        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell.getTile().getProperties().containsKey(animPropName)
                        && cell.getTile().getProperties().get(animPropName, String.class).equals(animPropValue)) {
                    cell.setTile(animatedTile);
                }
            }
        }
    }


    /*
     * Run this function each frame in the render() method before drawing the map (in update() method) IF ANIMATIONS AREN'T DRAWN
     */
    public static void updateTiledAnimation() {
        AnimatedTiledMapTile.updateAnimationBaseTime();
    }


    public static int getHorizontalTileCount(TiledMap map) {
        return map.getProperties().get("width", Integer.class);
    }

    public static int getVerticalTileCount(TiledMap map) {
        return map.getProperties().get("height", Integer.class);
    }

    public static int getTileSize(TiledMap map) {
        return ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth();
    }

}
