package com.group4;

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
    final private float heightScale = 1/5f;

    public HUD(Player player, TiledMap map){
        this.player = player;
        this.map = map;

        //Create the health bar and add it to the stage
        healthBar = new HealthBar(player,50);
        healthBar.setPosition(5,5);
        this.addActor(healthBar);
    }


}
