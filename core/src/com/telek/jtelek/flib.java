package com.telek.jtelek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.telek.jtelek.box2dUtils.BodyFactory;
import com.telek.jtelek.box2dUtils.Box2DUtils;
import com.telek.jtelek.box2dUtils.TiledParser;
import com.telek.jtelek.procGenUtils.NoiseMaker.Vec2;


public class flib {


    /*  CONSIDER THESE FOR EACH PROJECT:
    - Using jpackage to export => https://github.com/raeleus/skin-composer/wiki/Deploying-libGDX-with-jpackage-and-Badass-Runtime
                                  https://www.youtube.com/watch?v=R7CMXeQ11GM
    - Creating a custom launcher for the game => https://www.youtube.com/watch?v=3l5F7f7vfTU
    - Switching to LWJGL3 =>  https://gist.github.com/crykn/eb37cb4f7a03d006b3a0ecad27292a2d
    - Use pooling
    - nebula generator => https://github.com/alyrow/Nebula-Generator

    */


    public static void initBox2D(final float PPM) {
        Box2DUtils.init(PPM);
        BodyFactory.init(PPM);
        TiledParser.init(PPM);
    }


    public static void resizeAndSetFullscreen(int screenWidth, int screenHeight, boolean willBeFullscreen) {
        Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
        if (willBeFullscreen) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }


    public static void resizeTheWindowIfNeeded(int width, int height, int minWidth, int minHeight) {
        boolean shouldBeResized = false;
        if (width < minWidth) {
            width = minWidth;
            shouldBeResized = true;
        }
        if (height < minHeight) {
            height = minHeight;
            shouldBeResized = true;
        }
        if (shouldBeResized)
            Gdx.graphics.setWindowedMode(width, height);
    }

    public static void clearScreen(){
        Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void clearScreen(float r, float g, float b, float a){
        Gdx.gl20.glClearColor(r, g, b, a);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void createAndStartThread(final Runnable runnable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(runnable);
            }
        }).start();
    }


    public static TextureRegion getRepeatedTexture(Texture texture, int row, int col) {
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, texture.getWidth() * row, texture.getHeight() * col);
        return textureRegion;
    }


    public static boolean twoSidedEquals(Object obj1, Object obj2, Object val1, Object val2) {
        return (obj1.equals(val1) && obj2.equals(val2)) || (obj1.equals(val2) && obj2.equals(val1));
    }


    /**
     * This method is A lot faster than using (int) Math.floor(x)
     */
    public static int floor(double x) {
        int xi = (int) x;
        return (x < xi) ? (xi - 1) : xi;
    }
    public static Vector2 floor(Vector2 vector) { return new Vector2( floor(vector.x), floor(vector.y) ); }
    public static Vec2 floor(Vec2 vector) { return new Vec2( floor(vector.x), floor(vector.y) ); }
    public static Vec2 floor(float x, float y) { return new Vec2( floor(x), floor(y) ); }


    public static float fract(float x) {
        return x - floor(x);
    }
    public static Vector2 fract(Vector2 vector) { return new Vector2( fract(vector.x), fract(vector.y) ); }
    public static Vec2 fract(Vec2 vector) { return new Vec2( fract(vector.x), fract(vector.y) ); }
    public static Vec2 fract(float x, float y) { return new Vec2( fract(x), fract(y) ); }

    public static float mix(float x, float y, float a){
        return (x * (1-a)) + (y * a);
    }

    /**
     *  Takes oldX from the range [oldA, oldB] and returns what it would represent if it was in range [newA, newB]
     */
    public static float changeRange(int oldA, int oldB, int newA, int newB, float oldX){
        return newA + Math.abs( ( Math.abs(oldX - oldA) / (oldB - oldA) ) * (newB - newA) );
    }

    /**
     * Takes oldX from [-1,1] and returns what it would be in [0,1]
     * */
    public static float changeRange(float oldX){
        return Math.abs( ( Math.abs(oldX + 1) / 2 ) );
    }





    /** Returns 0 as a short */
    public static short aShort() { return (short) 0; }

    /** Returns 0 as a long */
    public static long aLong() { return (long) 0; }

    /** Returns 0 as a byte */
    public static byte aByte() { return (byte) 0; }



}


/*  EXPORTING EXPLAINED:

    >>>> SETUP

    1- Gradle version has to be 6.4 at minimum. Using +6.4 might give you errors or more warnings, use the optimal one.
    2- You must have JDK14 set for gradle in project's settings (F4)
    3- in the desktop folder's build.gradle do these:

    a) add this line AT THE TOP OF THE WHOLE FILE:     plugins { id 'org.beryx.runtime' version '1.8.4' }

    b) name this line:    project.ext.mainClassName = "com.mygdx.game.desktop.DesktopLauncher"
       to this:           mainClassName = "com.mygdx.game.desktop.DesktopLauncher"

       and add this line literally below it:         def osName = System.getProperty('os.name').toLowerCase(Locale.ROOT)

    c) rename the     task run(...)    to    task runGame(...)   or something like that

    d) in the     task dist(type: Jar)   after   'with jar' line   add this line:
                              destinationDirectory = file("$buildDir/lib")
       and right after task dist(type: Jar){} ends (after } curly paranthesis)  add this line:
                              jpackageImage.dependsOn dist

    e) add these line AT THE BOTTOM OF THE WHOLE FILE:
    runtime {
        options = ['--strip-debug',
                   '--compress', '2',
                   '--no-header-files',
                   '--no-man-pages',
                   '--strip-native-commands',
                   '--vm', 'server']
        modules = ['java.base' ,
                   'java.desktop',
                   'jdk.unsupported']
        distDir = file(buildDir)

        jpackage {
            //jpackageHome = '/usr/lib/jvm/open-jdk'
            mainJar = dist.archiveFileName.get()
            if (osName.contains('windows')) {
                imageOptions = ["--icon", file("../icons/icon.ico")]
            } else if (osName.contains('linux')) {
                imageOptions = ["--icon", file("../icons/icon.png")]
            } else if (osName.contains('mac')) {
                imageOptions = ["--icon", file("../icons/icon.icns")]
            }
        }
    }

    f) create a folder called 'icons' in your projects root folder (the folder you create will be a sibling to android, core, desktop, ...)
       and put these files in there if you want your .exe to have a icon

       logo.ico   =>  WINDOWS
       logo.icns  =>  MAC
       logo.png   =>  LINUX

       (PS: you have to name all of the files below 'icon' or change the code we copy pasted in section E)

    >>>> ACTUALLY EXPORTING
    Go to your gradle section from the right section of your IDE (it's like project section on the left but it's on the right)
    Go to  <yourProjectName>  =>  desktop  =>  Tasks  =>  build  =>  jpackageImage
    And if it was successful you're done, go to projectName/desktop/build/jpackage and find your executable there to distribute!

*/
