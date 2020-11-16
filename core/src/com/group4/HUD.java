package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Creates and loads the heads up display
 *
 * @author Robert Watts
 */
public class HUD extends Stage {

    TiledMap map;
    Player player;
    HealthBar healthBar;
    final private float heightScale = 1/7f;

    public HUD(Player player, TiledMap map){
        this.player = player;
        this.map = map;
        float scaledHeight = Gdx.graphics.getHeight() * heightScale;

        //Create the health bar and add it to the stage
        healthBar = new HealthBar(player,scaledHeight);
        healthBar.setPosition(20,20);
        this.addActor(healthBar);
    }


}
