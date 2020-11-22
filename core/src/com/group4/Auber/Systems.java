package com.group4.Auber;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group4.Auber.HUD.HUD;

public class Systems extends Actor {
    public static AuberGame game;
    public int health;
    int healthMax;
    String roomName;
    MapRenderer map;
    HUD hud;
    int healWait = 300;//how long before the system starts healing
    int delay = 0;
    int gridX;
    int gridY;
    int currentNotification = 0;
    public static ArrayList<Systems> systemsRemaining = new ArrayList<Systems>();

    public Systems(int x, int y, int w, int h, MapRenderer map, HUD hud, String roomName, int healthMax){
        this.map = map;
        this.roomName = roomName;
        this.health = healthMax;
        this.healthMax = healthMax;
        this.hud = hud;
        gridX = x;
        gridY = y;
        systemsRemaining.add(this);
        int tilewidth = map.properties.get("tilewidth", Integer.class);
        setBounds((float) x*tilewidth,(float) y*tilewidth,(float) w*(tilewidth - 1),(float) h*(tilewidth - 1));
        map.autoEnter(this);
    }
    public Systems(int x, int y, MapRenderer map, HUD hud, String roomName){//assumes 1x1 system
        this(x,y,1,1,map, hud, roomName,100);
    }
    @Override
    public void draw(Batch batch, float parentAlpha){//just used as it is called each frame
        delay += 1;
        if (delay >= healWait && health < healthMax && delay % 10 == 0){//edit the delay % X to change rate of healing, fps/X = hps
            health += 1;
        }
    }
    public void onHit(Actor by,int amount) {
        if (by instanceof Operative){
            delay = 0;
            health -= amount;
            if (health <= 0) {
                onDeath();
            }

            if (currentNotification == 0){
                hud.infoNotification("The system in the " + roomName + " is being attacked.");
                currentNotification += 1;
            } else if (currentNotification == 1 && health <= 50){
                hud.warningNotification("The system in the " + roomName + " is down to 50% health.");
                currentNotification += 1;
            }
        }
    }
    public void onDeath(){
        map.autoLeave(this,getX(),getY(), getWidth(), getHeight());
        systemsRemaining.remove(this);
        remove(); //so its .draw() isn't called
        if (systemsRemaining.size() == 0){
            game.setScreen(new GameEndScreen(game, false));
        }
        hud.errorNotification("The system in " + roomName + " has been destroyed!");

    }
    
}