package com.group4;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Map extends OrthogonalTiledMapRenderer {

    public int[][] intMap;
    public Set<Actor>[][] objMap;
    final int WALL = 1;
    final int EMPTY = 0;
    final int HEAL = 2;
    MapProperties properties;

    public Map(TiledMap map, String strMap) {
        super(map);
        properties = map.getProperties();

        String lines[] = strMap.split("\\r?\\n");
        Collections.reverse(Arrays.asList(lines));

        intMap = new int[lines[0].length()][];
        objMap = new HashSet[lines[0].length()][];

        for (int i = 0; i < lines[0].length(); i++) {
            intMap[i]= new int[lines.length];
            objMap[i] = new HashSet[lines.length];

            for (int j = 0; j < lines.length ; j++) {
                intMap[i][j] = Integer.parseInt(String.valueOf(lines[j].charAt(i)));
                objMap[i][j] = new HashSet<Actor>();
            }
        }

    }

    /**
     * Determines whether a map tile empty and whether can this be walked into
     * @param x
     * @param y
     * @return boolean
     */
    public boolean Empty(int x, int y){
        if (InBounds(x,y)){
            return intMap[x][y] != WALL;
        } else{
            return false;
        }
    }

    /**
     * Determines whether a map tile empty and whether can this be walked into
     * @param x the x coordinate of rectangle
     * @param y the x coordinate of rectangle
     * @param w width of rectangle
     * @param h height of rectangle
     * @return
     */
    public boolean Empty(float x, float y, float w, float h){

        for (int i = gridPos(x); i <= gridPos(x + w); i++) {
            for (int j = gridPos(y); j <= gridPos(y + h); j++) {
                if (!Empty(i,j)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * return true if every tile they are in is a effect tile
     *
     * @param effect the effect you are looking for
     * @param x
     * @param y
     * @param w
     * @param h
     * @return boolean if the effect is
     */
    public boolean Effect(int effect,float x, float y, float w, float h){//
        for (int i = gridPos(x); i <= gridPos(x + w); i++) {
            for (int j = gridPos(y); j <= gridPos(y + h); j++) {
                if (intMap[i][j] != effect){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * return true if every tile they are in is a effect tile
     *
     * @param effect
     * @param entity
     * @return
     */
    public boolean Effect(int effect,Actor entity){//
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        return Effect(effect,x,y,w,h);
    }

    /**
     * return a set of all entities in that tile
     *
     * @param x
     * @param y
     * @return
     */
    public Set<Actor> GetEnts(int x, int y){
        if (InBounds(x,y)){
            return objMap[x][y];
        } else{
            return new HashSet<Actor>();
        }

    }

    /**
     * return a set of all entities in tiles overlapping that area
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Set<Actor> GetEnts(float x, float y, float w, float h){
        Set<Actor> ents = new HashSet<Actor>();
        for (int i = gridPos(x); i <= gridPos(x + w); i++) {
            for (int j = gridPos(y); j <= gridPos(y + h); j++) {
                if (InBounds(i, j)){
                    ents.addAll(GetEnts(i,j));
                }
            }
        }
        return ents;
    }

    /**
     * return a set of entities in the given area
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Set<Actor> InArea(float x, float y, float w, float h){
        return InArea(GetEnts(x,y,w,h),x,y,w,h);
    }

    /**
     * return a set of entities in the given area from the given set
     *
     * @param entities
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Set<Actor> InArea(Set<Actor> entities, float x, float y, float w, float h){
        Set<Actor> ents = new HashSet<Actor>();
        for (Actor ent : entities) {
            float x2 = ent.getX();
            float y2 = ent.getY();
            float w2 = ent.getWidth();
            float h2 = ent.getHeight();
            if (x >= x2 + w2 || x2 >= x + w || y <= y2 + h2 || y2 <= y + h) {
                ents.add(ent);
            }
        }
        return ents;
    }

    /**
     * return a set of all entities the given entity is touching
     *
     * @param entity
     * @return
     */
    public Set<Actor> Touching(Actor entity){
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        return InArea(x,y,w,h);
    }

    /**
     *
     * @param x
     * @param y
     * @param entity
     */
    public void Enter(int x, int y,Actor entity){
        objMap[x][y].add(entity);
    }

    /**
     *
     * @param x
     * @param y
     * @param entity
     */
    public void Leave(int x, int y,Actor entity){
        objMap[x][y].remove(entity);
    }


    /**
     * converts position in pixels to position in grid
     *
     * @param pixelPos the pixel coordinate
     * @return An integer of the grid position
     */
    public int gridPos(float pixelPos){
        return (int) pixelPos/properties.get("tilewidth", Integer.class);
    }

    /**
     * Converts a grid position into a world position
     *
     * @param gridPos
     * @return
     */
    public int worldPos(int gridPos){
        return gridPos*properties.get("tilewidth", Integer.class);
    }

    /**
     * Leave all required tiles for this entity
     *
     * @param entity
     */
    public void autoLeave(Actor entity){
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        Leave(gridPos(x),gridPos(y),entity);
        Leave(gridPos(x),gridPos(y + h),entity);
        Leave(gridPos(x + w),gridPos(y),entity);
        Leave(gridPos(x + w),gridPos(y + h),entity);
    }

    /**
     * enter all required tiles for this entity
     *
     * @param entity
     */
    public void autoEnter(Actor entity){//
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        Enter(gridPos(x),gridPos(y),entity);
        Enter(gridPos(x),gridPos(y + h),entity);
        Enter(gridPos(x + w),gridPos(y),entity);
        Enter(gridPos(x + w),gridPos(y + h),entity);
    }

    /**
     * Works out if a coordinate is within the map
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if it is, False if its not
     */
    private boolean InBounds(int x, int y){
        return 0 <= x && x < intMap.length && 0 <= y && y < intMap[0].length;
    }



}
