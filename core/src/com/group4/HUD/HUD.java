package com.group4.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.group4.GSystem;
import com.group4.Player;

/**
 * Creates and loads the heads up display
 *
 * @author Robert Watts
 */
public class HUD extends Stage {

    Player player;
    PlayerHealthBar playerHealthBar;
    NotificationWindow notificationWindow;
    final private float heightScale = 2/7f;
    final private float notificationWindowWidthScale = 3/8f;
    final private float xOffset = 10;
    final private float yOffset = 10;
    final private BitmapFont font = new BitmapFont();

    public HUD(Player player, GSystem systems){
        this.player = player;
        float scaledHeight = Gdx.graphics.getHeight() * heightScale;
        float scaledWidth = Gdx.graphics.getWidth() * notificationWindowWidthScale;

        //Create the health bar and add it to the stage
        playerHealthBar = new PlayerHealthBar(player,scaledHeight/2, font);
        playerHealthBar.setPosition(xOffset,yOffset);
        this.addActor(playerHealthBar);

        notificationWindow = new NotificationWindow(scaledHeight, scaledWidth, font);
        notificationWindow.setPosition(Gdx.graphics.getWidth() - scaledWidth - xOffset, yOffset);
        this.addActor(notificationWindow);
    }
}
