package com.group4.Auber.OperativeAI;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;

public class GridPath implements Connection<GridNode>{
    GridNode from;
    GridNode to;
    float cost;
    public GridPath(GridNode from, GridNode to){
        this.from = from;
        this.to = to;
        cost = Vector2.dst(from.x, from.y, to.x, to.y);
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public GridNode getFromNode() {
        return from;
    }

    @Override
    public GridNode getToNode() {
        return to;
    }
    
}
