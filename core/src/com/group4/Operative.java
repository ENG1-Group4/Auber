package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.group4.operAi.GridGraph;
import com.group4.operAi.GridNode;
import java.lang.Math;
import java.util.ArrayList;

public class Operative extends Actor {
  public static AuberGame game;
  private Texture image = new Texture(Gdx.files.internal("img/operative.png"));
  private final Texture imageAttack = new Texture(Gdx.files.internal("img/operative_attack.png"));
  private float offset = 6;//used to make the hitbox center on the image
  private float moveSpeed = 1.2f;
  private Map map;
  private int health = 100;
  private GSystem target;
  private static int remainingOpers = 0;
  private boolean isHacking = false;
  private boolean combat = false;
  private static ArrayList<GSystem> untargetedSystems = new ArrayList<GSystem>();;
  private int delay = 0;
  public static GridGraph pathfinder;
  private GraphPath<GridNode> currentPath;
  private int nodeNum;

  public Operative(int x, int y,Map map) {
    this.map = map;
    remainingOpers += 1;
    setBounds(map.worldPos(x), map.worldPos(y),20f,20f);
    if (pathfinder == null){pathfinder = new GridGraph(map,x,y);}
    if (untargetedSystems.size() == 0){untargetedSystems.addAll(GSystem.systemsRemaining);}
    chooseTarget();
  }

  public void chooseTarget() {
    if (untargetedSystems.size() == 0){
      if (GSystem.systemsRemaining.size() == 0){//SHOULDN'T BE POSSIBLE
        throw new RuntimeException("no remaining targets");
      } else{
        target = GSystem.systemsRemaining.get((int) Math.round(Math.random() * (GSystem.systemsRemaining.size() - 1)));
      }
    } else{
      target = untargetedSystems.get((int) Math.round(Math.random() * (untargetedSystems.size() - 1)));
      untargetedSystems.remove(target);
    }
    currentPath = pathfinder.findPath(map.gridPos(getX()),map.gridPos(getY()), target.gridX,target.gridY);
    nodeNum = 0;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (isHacking){
      if (delay == 18 - 1){//delay == A - 1, A is the number of frames an oponent must spend hacking to damage the system
        target.onHit(this, 1);//damage dealt per A frames
        batch.draw(imageAttack,getX() - offset,getY() - offset,32,32);
        if (target.health <= 0){//they dead
          isHacking = false;
          chooseTarget();
        }
        delay = 0;
      } else{
        delay += 1;
      }
    } else if (combat){
      //attack?
      Player player = null;
      for (Actor thing : map.InArea(getX() - offset,getY() - offset,31,31)) {
        if (thing instanceof Player && delay == 0){
          player = (Player) thing;
          player.onHit(this,20);
          batch.draw(imageAttack,getX() - offset,getY() - offset,32,32);
          delay = 60;
          break;//only one player
        }
      }
      if (delay > 0){delay -= 1;}
      if (player == null){
        //check if player still nearby
        float size = 32*10;
        for (Actor thing : map.InArea(getX() + getWidth()/2 - size/2 - offset,getY() + getHeight()/2 - size/2 - offset,size,size)) {
          if (thing instanceof Player){
            player = (Player) thing;
            break;//only one player
          }
        }
        if (player == null){//end combat
          combat = false;
          delay = 0;
          chooseTarget(); 
        } else {//player still nearby
          //if (true){return;} //uncomment to kneecap them
          //move
          if (nodeNum >= currentPath.getCount()){
            currentPath = pathfinder.findPath(map.gridPos(getX()),map.gridPos(getY()), map.gridPos(player.getX()),map.gridPos(player.getY()));
            nodeNum = 0;
          }
          move();
        }
      }
    } else{ //moving
      move();
      
      //Check if we should start hacking
      if (getX() - offset == target.getX() && getY() - offset == target.getY()){
        isHacking = true;
        batch.draw(image, getX() -offset, getY() -offset, image.getWidth(), image.getHeight());
        return;
      }
      
    }

    // Draw the image
    batch.draw(image, getX() -offset, getY() -offset, image.getWidth(), image.getHeight());
  }

  private void move(){
    GridNode curNode = currentPath.get(nodeNum);
    float xdif = map.worldPos(curNode.x) - getX() + offset;
    float ydif = map.worldPos(curNode.y) - getY() + offset;
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
    if (map.Empty(getX() + deltaX, getY() + deltaY, getWidth(), getHeight())) {
      map.autoLeave(this,getX(),getY(), getWidth(), getHeight());
      moveBy(deltaX, deltaY);
      map.autoEnter(this,getX(),getY(), getWidth(), getHeight());
    } else {
      throw new RuntimeException("Path finding error");
    }
    if (getX() - offset == map.worldPos(curNode.x) && getY() - offset == map.worldPos(curNode.y)){//next node
      nodeNum += 1;
    }
  }
  public void onHit(Actor by,int amount) {
    if (by instanceof Player){
      isHacking = false;
      untargetedSystems.add(target);
      delay = 0;
      combat = true;
      currentPath = pathfinder.findPath(map.gridPos(getX()),map.gridPos(getY()), map.gridPos(by.getX()),map.gridPos(by.getY()));
      nodeNum = 0;
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
      game.setScreen(new GameEndScreen(game, true));
    }
  }
}