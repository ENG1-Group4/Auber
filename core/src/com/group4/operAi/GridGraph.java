package com.group4.operAi;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.group4.Map;

public class GridGraph implements IndexedGraph<GridNode>{//this is the main pathfinding one
    GridHeur gridHeuristic = new GridHeur();
    Array<Connection<GridNode>>[][] paths;
    public GridNode[][] GridMap;
    Map map;
    private int lastNodeIndex = 0;

    public GridGraph(Map map,int x, int y){//x and y of an internal position
        this.map = map;
        GridMap = new GridNode[map.intMap.length][map.intMap[0].length];
        paths = new Array[map.intMap[0].length][];
        for (int i = 0; i < map.intMap.length; i++) {
            paths[i] = new Array[map.intMap[0].length];
            for (int j = 0; j < map.intMap.length ; j++) {
                paths[i][j] = new Array<Connection<GridNode>>();
            }
        }
        addNode(x,y);
        Tree(x,y);
    }
    private void Tree(int x, int y){
        for (GridNode.Pos coord : GridMap[x][y].ConnectingCoords()) {
            if (GridMap[coord.x][coord.y] == null){
                addNode(coord.x,coord.y);
                paths[x][y].add(new GridPath(GridMap[x][y],GridMap[coord.x][coord.y]));
                Tree(coord.x,coord.y);
            } else {
                paths[x][y].add(new GridPath(GridMap[x][y],GridMap[coord.x][coord.y]));
            }
        }
    }
    public void addNode(int x, int y){
        GridNode node = new GridNode(map,x,y);
        node.index = lastNodeIndex;
        lastNodeIndex++;
        GridMap[x][y] = node;
  }
  public GraphPath<GridNode> findPath(GridNode startNode, GridNode goalNode){
    GraphPath<GridNode> path = new DefaultGraphPath<>();
    new IndexedAStarPathFinder<>(this).searchNodePath(startNode, goalNode, gridHeuristic, path);
    return path;
  }

  @Override
  public Array<Connection<GridNode>> getConnections(GridNode fromNode) {
      return paths[fromNode.x][fromNode.y];
  }

  @Override
  public int getIndex(GridNode node) {
      return node.index;
  }

  @Override
  public int getNodeCount() {
      return lastNodeIndex;
  }
}
