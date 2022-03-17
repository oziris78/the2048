package com.telek.jtelek.generalUtils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.HashMap;


public final class AssetSorter {

    // THE ENUM FOR SEPERATING GROUPS AND CREATING MORE THAN ONE LOADING SCREENS
    public enum GroupID {
        MAIN_GROUP
    }

    // IMMEDIATELY NEEDED ASSETS
    public Skin skin;
    public Sound congratsRobot;

    // TO-BE-LOADED ASSETS, ADD ALL OF THESE TO THE groups ARRAY AT THE END
    AssetGroup mainGroup;


    public AssetSorter() {
        // INITIALIZE THE ASSETMANAGER
        this.assetManager = new AssetManager();


        // INITIALIZE IMMEDIATELY NEEDED ASSETS
        this.skin = getSkin();
        this.congratsRobot = Gdx.audio.newSound(Gdx.files.internal("congratsRobot.wav"));


        // GROUPS ONE BY ONE
        mainGroup = new AssetGroup(GroupID.MAIN_GROUP);
        //mainGroup.add("aKeyForMe", new AssetDescriptor<>("badlogic.jpg", Texture.class));


        // ADDING ALL GROUPS TO THE ARRAY
        groups = new AssetGroup[] {mainGroup};
    }




    /*  IMMEDIATELY NEEDED ASSETS  */

    private Skin getSkin() {
        return new Skin(Gdx.files.internal("skins/holo-dark-hdpi/Holo-dark-hdpi.json"));
    }


    public void dispose() {
        // DISPOSE ALL TO-BE-LOADED ASSETS
        this.assetManager.dispose();
        // DISPOSE ALL IMMEDIATELY NEEDED ASSETS
        skin.dispose();
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*  ACTUAL FUNCTIONS AND FIELDS, DON'T TOUCH ANY OF THESE  */


    private AssetManager assetManager; // ASSETMANAGER
    private AssetGroup[] groups; // THE GROUPS ARRAY
    private AssetGroup tempGroup = null;  // Garbage variables for performance


    private static final class AssetGroup {

        private GroupID groupID;
        private HashMap<String, AssetDescriptor> descriptors;
        private boolean isQueued;

        // garbage var for performance
        AssetDescriptor temp = null;

        public AssetGroup(GroupID groupID) {
            this.descriptors = new HashMap<>();
            this.groupID = groupID;
            this.isQueued = false;
        }

        public void add(String key, AssetDescriptor assetDescriptor) {
            if (this.descriptors.containsKey(key))
                throw new RuntimeException("Invalid key: " + key);
            if (assetDescriptor == null)
                throw new RuntimeException("The assetDescriptor is null for this key: " + key);
            this.descriptors.put(key, assetDescriptor);
        }

        public void queue(AssetManager assetManager) {
            this.isQueued = true;
            for (AssetDescriptor asset : this.descriptors.values())
                assetManager.load(asset);
        }

        public AssetDescriptor getAssetDescriptor(String key) {
            temp = this.descriptors.get(key);
            if (temp == null)
                throw new RuntimeException("An asset for this key doesn't exist, key: " + key);
            return temp;
        }

        public boolean isQueued() {
            return this.isQueued;
        }

        public GroupID getGroupID() {
            return this.groupID;
        }

    }


    public <T> T getResource(GroupID groupID, String key, Class<T> aClass) {
        return this.assetManager.get(getGroup(groupID).getAssetDescriptor(key).fileName, aClass);
    }


    // LOAD TO-BE-LOADED ASSETS
    public boolean update(GroupID groupID, int milliseconds) {
        tempGroup = getGroup(groupID);
        if (!tempGroup.isQueued()) queueAssets(tempGroup);
        return this.assetManager.update(milliseconds);
    }


    public boolean update(GroupID groupID) {
        return update(groupID, 17);
    }


    private AssetGroup getGroup(GroupID groupID) {
        for (AssetGroup group : this.groups) {
            if (group.getGroupID().equals(groupID)) return group;
        }
        throw new RuntimeException("Group not found for this groupID: " + groupID);
    }

    private void queueAssets(AssetGroup assetGroup) {
        assetGroup.queue(this.assetManager);
    }

    // Getting the percentages
    public float getPercentage() {
        return this.assetManager.getProgress();
    }

    public float getPercentageInHundrents() {
        return getPercentage() * 100;
    }

    public String getPercentageString() {
        return String.format("%f %%", getPercentageInHundrents());
    }


}
