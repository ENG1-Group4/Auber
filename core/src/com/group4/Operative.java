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
  private Texture image = new Texture(Gdx.files.internal("img/operative.png"));
  private int moveSpeed = 1;
  private Map map;
  private int health = 100;
  private float w = 20f;
  private float h = 20f;
  private GSystem target;
  private boolean isHacking = false;
  private boolean combat = false;
  private static int remainingOpers = 0;
  private int delay = 0;
  public static GridGraph pathfinder;
  private GraphPath<GridNode> currentPath;
  private int nodeNum;

  public Operative(int x, int y,Map map) {
    this.map = map;
    remainingOpers += 1;
    setPosition(map.worldPos(x), map.worldPos(y));
    if (pathfinder == null){pathfinder = new GridGraph(map,x,y);}
    chooseTarget();
  }

  public void chooseTarget() {
    if (GSystem.systemsRemaining.size() == 0)//SHOULDN'T BE POSSIBLE
      throw new RuntimeException("no remaining targets");
    target = GSystem.systemsRemaining.get((int) Math.round(Math.random() * (GSystem.systemsRemaining.size() - 1)));
    currentPath = pathfinder.findPath(pathfinder.GridMap[map.gridPos(getX())][map.gridPos(getY())], pathfinder.GridMap[target.gridX][target.gridY]);
    nodeNum = 0;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (isHacking){
      if (delay == 18 - 1){//delay == A - 1, A is the number of frames an oponent must spend hacking to damage the system
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
      float xdif = map.worldPos(curNode.x) - getX();
      float ydif = map.worldPos(curNode.y) - getY();
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
      if (map.Empty(getX() + deltaX, getY(), w, h)) {
        map.autoEnter(this,getX(),getY(), w, h);
        moveBy(deltaX, deltaY);
        map.autoLeave(this,getX(),getY(), w, h);
      } else {
        throw new RuntimeException("Path finding error");
      }
      
      //Check if we should start hacking
      if (getX() == target.getX() && getY() == target.getY()){
        isHacking = true;
        batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
        return;
      }
      // for (Object ent : map.GetEnts(getX(), getY(), w, h)){
      //   if (ent == target){
      //     isHacking = true;
      //     batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
      //     return;
      //   }
      // }
      else if (getX() == map.worldPos(curNode.x) && getY() == map.worldPos(curNode.y)){//next node
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