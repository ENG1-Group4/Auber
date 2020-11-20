package com.group4.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.group4.GSystem;
import com.group4.Operative;
import com.group4.Player;

/**
 * Creates and loads the heads up display
 *
 * @author Robert Watts
 */
public class HUD extends Stage {

    private HealthBar systemsHealthBar;
    private HealthBar operativesHealthBar;
    Player player;
    PlayerHealthBar playerHealthBar;
    NotificationWindow notificationWindow;
    final private float heightScale = 1/7f;
    final private float notificationWindowWidthScale = 3/8f;
    final private float xOffset = 10;
    final private float yOffset = 10;

    public HUD(Player player){
        this.player = player;
        float scaledHeight = Gdx.graphics.getHeight() * heightScale;
        float scaledWidth = Gdx.graphics.getWidth() * notificationWindowWidthScale;

        //Create the health bar and add it to the stage
        playerHealthBar = new PlayerHealthBar(player,scaledHeight);
        playerHealthBar.setPosition(xOffset,yOffset);
        this.addActor(playerHealthBar);

        //Create the notification window and add it to the stage
        notificationWindow = new NotificationWindow(scaledHeight, scaledWidth);
        notificationWindow.setPosition(Gdx.graphics.getWidth() - scaledWidth - xOffset, yOffset);
        this.addActor(notificationWindow);

        //Create the system health bar and add it to the stage
        systemsHealthBar = new HealthBar(50, scaledWidth, "System Health", GSystem.systemsRemaining.size());
        systemsHealthBar.setPosition(Gdx.graphics.getWidth() - scaledWidth - xOffset, yOffset + scaledHeight);
        this.addActor(systemsHealthBar);

        //Create the system health bar and add it to the stage
        operativesHealthBar = new HealthBar(50, scaledWidth, "Operatives Health", Operative.remainingOpers);
        operativesHealthBar.setPosition(Gdx.graphics.getWidth() - scaledWidth - xOffset, yOffset + scaledHeight + 50);
        this.addActor(operativesHealthBar);
    }

    /**
     * Add a success notification
     *
     * @param text the notification
     */
    public void successNotification(String text){
        notificationWindow.addNotification(text, new Color(0,1,0,1));
    }

    /**
     * Add a info notification
     *
     * @param text the notification
     */
    public void infoNotification(String text){
        notificationWindow.addNotification(text, new Color(1,1,1,1));
    }

    /**
     * Add a error notification
     *
     * @param text the notification
     */
    public void warningNotification(String text){
        notificationWindow.addNotification(text, new Color(1,0.647f,0,1));
    }


    /**
     * Add a error notification
     *
     * @param text the notification
     */
    public void errorNotification(String text){
        notificationWindow.addNotification(text, new Color(1,0,0,1));
    }

    /**
     * Draw the stage. Overridden to update the health bars every render
     */
    @Override
    public void draw(){
        super.draw();
        systemsHealthBar.setCurrentValue(GSystem.systemsRemaining.size());
        operativesHealthBar.setCurrentValue(Operative.remainingOpers);
        this.act();
    }

    public void setValues(int numOfOperatives, int numOfSystems){
        operativesHealthBar.setMaxValue(numOfOperatives);
        systemsHealthBar.setMaxValue(numOfSystems);
    }
}
