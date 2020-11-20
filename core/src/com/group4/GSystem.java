package com.group4;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group4.HUD.HUD;

public class GSystem extends Actor {
    public static AuberGame game;
    public int health;
    int healthMax;
    String roomName;
    Map map;
    HUD hud;
    int healWait = 300;//how long before the system starts healing
    int delay = 0;
    int gridX;
    int gridY;
    int currentNotification = 0;
    public static ArrayList<GSystem> systemsRemaining = new ArrayList<GSystem>();

    public GSystem(int x, int y, int w, int h, Map map, HUD hud, String roomName, int healthMax){
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
    public GSystem(int x, int y, Map map, HUD hud, String roomName){//assumes 1x1 system
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
                hud.infoNotification("The system in " + roomName + " is being attacked.");
                currentNotification += 1;
            } else if (currentNotification == 1 && health <= 50){
                hud.warningNotification("The system in " + roomName + " is down to 50% health.");
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