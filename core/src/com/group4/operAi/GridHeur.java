package com.group4.operAi;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class GridHeur implements Heuristic<GridNode>{
    @Override
    public float estimate(GridNode node, GridNode endNode) {
        return Vector2.dst(node.x, node.y, endNode.x, endNode.y);
    }
}
