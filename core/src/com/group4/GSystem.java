package com.group4;

import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.group4.Map;

public class GSystem extends Actor {
    public int health;
    int room;
    Map map;
    public int x;
    public int y;
    public static ArrayList<GSystem> systemsRemaining = new ArrayList<GSystem>();

    public GSystem(int x, int y, int w, int h, Map map, int room, int health) {
        this.map = map;
        this.room = room;
        this.health = health;
        this.x = x;
        this.y = y;
        systemsRemaining.add(this);
        setBounds((float) x * 32, (float) y * 32, (float) w * 31, (float) h * 31);
        map.autoEnter(this);
    }

    public GSystem(int x, int y, Map map, int room, int health) {// assumes 1x1 system
        this(x, y, 1, 1, map, room, health);
    }

    public void onHit(Object by, int amount) {
        if /* (by instanceof Operative) */ (true) {
            this.health -= amount;
            if (this.health <= 0) {
                onDeath();
                // by.inform of death?, so operative can choose new target?, could return a bool
                // that it was destroyed?
            } else {
                // inform game that system has been hit
                // e.g. minimap.sysDamaged(room,health);
            }
        }
    }

    public void onDeath() {
        map.autoLeave(this);
        systemsRemaining.remove(this);
        // inform player that system has been destroyed
        // e.g. minimap.sysDestroyed(room);
        if (systemsRemaining.size() == 0) {
            // gameover man
        }
    }
}
