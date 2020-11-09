package com.group4;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import com.badlogic.gdx.scenes.scene2d.Actor;
public class Map {
    public int[][] intMap;
    public Set<Actor>[][] objMap;
    final int WALL = 1;
    final int EMPTY = 0;
    final int HEAL = 2;

    public Map(int[][] intMap){
        this.intMap = intMap;
        objMap = new HashSet[intMap.length][intMap[0].length];
        for (int i = 0; i < intMap.length; i++) {
            for (int j = 0; j < intMap[0].length; j++){
                objMap[i][j] = new HashSet<Actor>(); 
            }
        }
    }
    public Map(String strMap){
        String lines[] = strMap.split("\\r?\\n");
        intMap = new int[lines.length][];
        objMap = new HashSet[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            String line[] = lines[i].split("");
            intMap[i]= new int[line.length];
            objMap[i] = new HashSet[line.length];

            for (int j = 0; j < line.length; j++){
                intMap[i][j] = Integer.parseInt(line[j]);
                objMap[i][j] = new HashSet<Actor>();
            }
        }
    }
    // public Map(String strMap){//create a map from a string (for file storage etc.)
    //     pass
    // }
    public boolean Empty(int x, int y){//is the tile empty
        if (InBounds(x,y)){
            return intMap[x][y] != WALL;
        } else{
            return false;
        }
        
    }
    public boolean Empty(float x, float y, float w, float h){//are all tiles containing this area empty (can this be walked into)
        for (int i = gridPos(x); i <= gridPos(x + w); i++) {
            for (int j = gridPos(y); j <= gridPos(y + h); j++) {
                if (!Empty(i,j)){
                    return false;
                }
            }
        }
        return true;
    }
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
    public boolean Effect(int effect,Actor entity){//
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        return Effect(effect,x,y,w,h);
    }
    public Set<Actor> GetEnts(int x, int y){//return a set of all entities in that tile
        if (InBounds(x,y)){
            return objMap[x][y];
        } else{
            return new HashSet<Actor>();
        }
        
    }
    public Set<Actor> GetEnts(float x, float y, float w, float h){//return a set of all entities in tiles overlapping that area
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
    public Set<Actor> InArea(float x, float y, float w, float h){//return a set of entities in the given area
        return InArea(GetEnts(x,y,w,h),x,y,w,h);
    }
    public Set<Actor> InArea(Set<Actor> entities, float x, float y, float w, float h){//return a set of entities in the given area from the given set
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
    public Set<Actor> Touching(Actor entity){//return a set of all entities the given entity is touching
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        return InArea(x,y,w,h);
    }
    public void Enter(int x, int y,Actor entity){
        objMap[x][y].add(entity);
    }
    public void Leave(int x, int y,Actor entity){
        objMap[x][y].remove(entity);
    }
    public int gridPos(float pixelPos){//converts position in pixels to position in grid
        return (int) pixelPos/32;//(pixelPos - pixelPos%32)/32;
    }
    public void autoLeave(Actor entity){//Leave all required tiles for this entity
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        Leave(gridPos(x),gridPos(y),entity);
        Leave(gridPos(x),gridPos(y + h),entity);
        Leave(gridPos(x + w),gridPos(y),entity);
        Leave(gridPos(x + w),gridPos(y + h),entity);
    }
    public void autoEnter(Actor entity){//enter all required tiles for this entity
        float x = entity.getX();
        float y = entity.getY();
        float w = entity.getWidth();
        float h = entity.getHeight();
        Enter(gridPos(x),gridPos(y),entity);
        Enter(gridPos(x),gridPos(y + h),entity);
        Enter(gridPos(x + w),gridPos(y),entity);
        Enter(gridPos(x + w),gridPos(y + h),entity);
    }
    private boolean InBounds(int x, int y){
        return 0 <= x && x < intMap.length && 0 <= y && y < intMap[0].length;
    }
    public static void main (String[] arg) {
        int[][] mapRepr = {{1,1,1},{1,0,1},{1,0,1},{1,1,1}};
        Map x = new Map(mapRepr);
	}
}
