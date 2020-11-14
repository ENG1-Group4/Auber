package com.group4.operAi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.group4.Map;

public class GridNode {
    Map map;
    public int x;
    public int y;
    int index;
    private Set<Pos> coords;
    private boolean calced = false;
    
    public GridNode(Map map,int x, int y){
        this.map = map;
        this.x = x;
        this.y = y;
    }
    public void setIndex(int index){
        this.index = index;
    }
    public Set<Pos> ConnectingCoords(){
        if (!calced){
            return coords;
        }
        return _ConnectingCoords(1);
    }
    class Pos{
        int x;
        int y;
        Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Pos){
                Pos other = (Pos) obj;
                return x == other.x && y == other.y;
            }
            return false;
        }
    }
    private Set<Pos> Adjacent(int x, int y){
        HashSet<Pos> coords = new HashSet<Pos>();
        coords.add(new Pos(x + 1,y));
        coords.add(new Pos(x - 1,y));
        coords.add(new Pos(x,y + 1));
        coords.add(new Pos(x,y - 1));
        return coords;
    }
    private Set<Pos> _ConnectingCoords(int antiSquareness){//antiSquareness is how not on a grid the AI will move (higher = less = more intensive)
        calced = true;
        if (antiSquareness < 1){
            throw new IllegalArgumentException("antiSquareness must be atleast 1");
        }
        coords = new HashSet<Pos>();
        ArrayList<Pos> blottPool = new ArrayList<Pos>();
        for (Pos pos : Adjacent(x,y)) {
            int nx = pos.x + x;
            int ny = pos.y + y;
            if (map.Empty(nx,ny)){
                coords.add(new Pos(nx,ny));
                for (Pos pos2 : Adjacent(nx,ny)) {
                    if ((!(coords.contains(pos2) && blottPool.contains(pos2))) && map.Empty(pos2.x,pos2.y)){
                        blottPool.add(pos2);
                    }
                }
            }
        }
        if (antiSquareness == 1) {return coords;}//end early
        for (Pos pos : coords) {//remove weirds
            blottPool.remove(pos);
        }
        for (int i = 0; i < 4*(antiSquareness); i++) {//perimeter based on antiSquareness
            Pos coord = blottPool.get(0);
            blottPool.remove(0);
            if (coord.y > y & coord.x > x){//NE
                if (map.Empty(coord.x - 1,coord.y - 1) && map.Empty(coord.x - 1,coord.y) && map.Empty(coord.x,coord.y - 1)){
                    coords.add(coord);
                    for (Pos pos2 : Adjacent(coord.x,coord.y)) {
                        if ((!(coords.contains(pos2) && blottPool.contains(pos2))) && map.Empty(pos2.x,pos2.y)){
                            blottPool.add(pos2);
                        }
                    }
                }
            } else if (coord.y < y & coord.x > x){//SE
                if (map.Empty(coord.x - 1,coord.y + 1) && map.Empty(coord.x - 1,coord.y) && map.Empty(coord.x,coord.y + 1)){
                    coords.add(coord);
                    for (Pos pos2 : Adjacent(coord.x,coord.y)) {
                        if ((!(coords.contains(pos2) && blottPool.contains(pos2))) && map.Empty(pos2.x,pos2.y)){
                            blottPool.add(pos2);
                        }
                    }
                }
            } else if (coord.y < y & coord.x < x){//SW
                if (map.Empty(coord.x + 1,coord.y + 1) && map.Empty(coord.x + 1,coord.y) && map.Empty(coord.x,coord.y + 1)){
                    coords.add(coord);
                    for (Pos pos2 : Adjacent(coord.x,coord.y)) {
                        if ((!(coords.contains(pos2) && blottPool.contains(pos2))) && map.Empty(pos2.x,pos2.y)){
                            blottPool.add(pos2);
                        }
                    }
                }
            } else if(coord.y > y & coord.x < x){//NW
                if (map.Empty(coord.x + 1,coord.y - 1) && map.Empty(coord.x + 1,coord.y) && map.Empty(coord.x,coord.y - 1)){
                    coords.add(coord);
                    for (Pos pos2 : Adjacent(coord.x,coord.y)) {
                        if ((!(coords.contains(pos2) && blottPool.contains(pos2))) && map.Empty(pos2.x,pos2.y)){
                            blottPool.add(pos2);
                        }
                    }
                }
            }
        }
        return coords;
    }
}
