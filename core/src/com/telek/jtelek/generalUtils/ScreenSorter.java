package com.telek.jtelek.generalUtils;


import com.badlogic.gdx.Game;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


public class ScreenSorter<TGAME extends Game> {

    private class TTScreen<TYPESCREEN> {
        private final Class<TYPESCREEN> type;
        private final TScreen screen;

        private TTScreen(Class<TYPESCREEN> type, TScreen screen) {
            this.type = type; this.screen = screen;
        }

        private TScreen getScreen() { return screen; }
        private Class<TYPESCREEN> getType() { return type; }
    }


    private HashMap<String, TTScreen> screens;
    private final TGAME game;


    /*  CONSTRUCTORS  */

    public ScreenSorter(TGAME game) {
        this.game = game;
        this.screens = new HashMap<>();
    }


    /*  METHODS  */

    /** Puts an existing screen to the screensorter */
    public <T extends TScreen> void putScreen(String key, TScreen screen, Class<T> classOfScreen) {
        this.screens.put(key, new TTScreen(classOfScreen, screen));
    }


    /** Puts a null screen with the specified class to be created once and returned using the screensorter */
    public <T extends TScreen> void putScreen(String key, Class<T> classOfScreen) {
        putScreen(key, null, classOfScreen);
    }


    /** Gets the screen with the specified key from the screensorter, creates a new screen if the current existing one is null
     *  It also runs the tScreen.configure() before returning it (allowing you to reset input etc. before you get the screen) */
    public <T extends TScreen> T getScreen(String key) {
        T screen = (T) this.screens.get(key).getScreen();
        if (screen == null) {
            try {
                screen = (T) this.screens.get(key).getType().getConstructor(game.getClass()).newInstance(this.game);
            }
            catch (InstantiationException e) { e.printStackTrace(); }
            catch (InvocationTargetException e) { e.printStackTrace(); }
            catch (NoSuchMethodException e) { e.printStackTrace(); }
            catch (IllegalAccessException e) { e.printStackTrace(); }
        }
        screen.configure();
        return screen;
    }


    /** Disposes the screen found by the specified key */
    public void disposeScreen(String key) {
        if(disposeTheGivenScreen(this.screens.get(key).getScreen()))
            this.screens.remove(key);
    }


    /** Disposes all of the screens that are inside the screensorter */
    public void dispose() {
        for (TTScreen screen : this.screens.values()) disposeTheGivenScreen(screen.getScreen());
    }


    /*  HELPERS  */

    private boolean disposeTheGivenScreen(TScreen screen){
        if(screen != null) {
            screen.dispose();
            return true;
        }
        else return false;
    }

}