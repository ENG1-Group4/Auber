package com.group4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.group4.Map;

public class GSystem extends Actor {
    int health;
    int room;
    Map map;
    static int systemsRemaining = 0;

    public GSystem(int x, int y, int w, int h, Map map, int room, int health){
        this.map = map;
        this.room = room;
        this.health = health;
        systemsRemaining += 1;
        setBounds((float) x*32,(float) y*32,(float) w*31,(float) h*31);
        map.autoEnter(this);
    }
    public GSystem(int x, int y, Map map, int room, int health){//assumes 1x1 system
        this(x,y,1,1,map,room,health);
    }
    public void onHit(Object by,int amount) {
        if /*(by instanceof Operative)*/ (true){
            health -= amount;
            if (health <= 0) {
                onDeath();
                //by.inform of death?, so operative can choose new target?, could return a bool that it was destroyed?
            } else {
                //inform game that system has been hit
                //e.g. minimap.sysDamaged(room,health);
            }
        }
    }
    public void onDeath(){
        map.autoLeave(this);
        systemsRemaining -= 1;
        //inform player that system has been destroyed
        //e.g. minimap.sysDestroyed(room);
        if (systemsRemaining == 0){
            //gameover man
        }
    }
}
