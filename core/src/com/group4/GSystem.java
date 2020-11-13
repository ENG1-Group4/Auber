package com.group4;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.group4.Map;

public class GSystem extends Actor {
    public int health;
    int healthMax;
    int room;
    Map map;
    int healWait = 300;//how long before the system starts haling
    int delay = 0;
    public static ArrayList<GSystem> systemsRemaining = new ArrayList<GSystem>();

    public GSystem(int x, int y, int w, int h, Map map, int room, int healthMax){
        this.map = map;
        this.room = room;
        this.health = healthMax;
        this.healthMax = healthMax;
        systemsRemaining.add(this);
        setBounds((float) x*32,(float) y*32,(float) w*31,(float) h*31);
        map.autoEnter(this);
    }
    public GSystem(int x, int y, Map map, int room, int health){//assumes 1x1 system
        this(x,y,1,1,map,room,health);
    }
    @Override
    public void draw(Batch batch, float parentAlpha){//just used as it is called each frame
        delay += 1;
        if (delay >= healWait && health < healthMax && delay % 10 == 0){//edit the delay % X to change rate of healing, fps/X = hps
            health += 1;
        }
    }
    public void onHit(Object by,int amount) {
        if /*(by instanceof Operative)*/ (true){
            delay = 0;
            health -= amount;
            if (health <= 0) {
                onDeath();
            }
        }
    }
    public void onDeath(){
        map.autoLeave(this);
        systemsRemaining.remove(this);
        remove();//so its .draw() isn't called
        if (systemsRemaining.size() == 0){
            //gameover man
        }
    }
}

