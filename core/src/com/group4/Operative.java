package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.group4.operAi.GridGraph;
import com.group4.operAi.GridNode;
import java.lang.Math;

public class Operative extends Actor {
  private Texture image = new Texture(Gdx.files.internal("img/player.png"));
  private int moveSpeed = 5;
  private Map map;
  private int health = 100;
  private GSystem target;
  private boolean isHacking = false;
  private boolean combat = false;
  private static int remainingOpers = 0;
  private int delay = 0;
  public static GridGraph pathfinder;
  private GraphPath<GridNode> currentPath;
  private int nodeNum;

  public Operative(Map map,int x, int y) {
    this.map = map;
    remainingOpers += 1;
    this.chooseTarget();
    setPosition(map.worldPos(x), map.worldPos(y));
    if (pathfinder == null){pathfinder = new GridGraph(map,x,y);}
  }

  public void chooseTarget() {
    if (GSystem.systemsRemaining.size() == 0)
      return;
    target = GSystem.systemsRemaining.get((int) Math.round(Math.random() * (GSystem.systemsRemaining.size() - 1)));
    currentPath = pathfinder.findPath(pathfinder.GridMap[map.gridPos(getX())][map.gridPos(getY())], pathfinder.GridMap[target.gridX][target.gridY]);
    nodeNum = 0;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (isHacking){
      if (delay == 9 - 1){//delay == A - 1, A is the number of frames an oponent must spend hacking to damage the system
        target.onHit(this, 1);//damage dealt per A frames
        if (target.health <= 0){//they dead
          isHacking = false;
          chooseTarget();
        }
        delay = 0;
      } else{
        delay += 1;
      }
    } else if (combat){
      //TODO
    } else{ //moving
      GridNode curNode = currentPath.get(nodeNum);
      float xdif = curNode.x - getX();
      float ydif = curNode.y - getY();
      float deltaX;
      float deltaY;
      if (xdif >= 0){
        deltaX = Math.min(moveSpeed,xdif);
      } else{
        deltaX = Math.max(-moveSpeed,xdif);
      }
      if (ydif >= 0){
        deltaY = Math.min(moveSpeed,ydif);
      } else{
        deltaY = Math.max(-moveSpeed,ydif);
      }

      // Check the space is empty before moving into it
      if (map.Empty(getX() + deltaX, getY(), image.getWidth(), image.getHeight())) {
        map.autoEnter(this,getX(),getY(), image.getWidth(), image.getHeight());
        moveBy(deltaX, deltaY);
        map.autoLeave(this,getX(),getY(), image.getWidth(), image.getHeight());
      } else {
        throw new RuntimeException("Path finding error");
      }
      
      //Check if we should start hacking
      for (Object ent : map.GetEnts(getX(), getY(), image.getWidth(), image.getHeight())){
        if (ent == target){
          isHacking = true;
          break;
        }
      }
      if (getX() == curNode.x && getY() == curNode.y){//next node
        nodeNum += 1;
      }
    }
    // Draw the image
    batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
  }
  public void onHit(Object by,int amount) {
    if (by instanceof Player){
      isHacking = false;
      delay = 0;
      combat = true;
      health -= amount;
      if (health <= 0) {
        onDeath();
      }
    }
  }
  public void onDeath(){
    map.autoLeave(this);
    remainingOpers -= 1;
    remove();//so its .draw() isn't called
    if (remainingOpers == 0){
       //game won man
    }
  }
}