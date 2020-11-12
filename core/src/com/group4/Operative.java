package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Operative extends Actor {
  private Texture image = new Texture(Gdx.files.internal("img/player.png"));
  private int moveSpeed = 5;
  private Map map;
  private int health = 100;
  private GSystem target;
  private boolean isHacking = false;

  public Operative(Map map) {
    this.map = map;
    this.chooseTarget();
    setPosition(map.worldPos(this.target.x), map.worldPos(this.target.y));

  }

  public void chooseTarget() {
    if (GSystem.systemsRemaining.size() == 0)
      return;
    this.target = GSystem.systemsRemaining.get((int) Math.round(Math.random() * (GSystem.systemsRemaining.size() - 1)));
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {

    if (!this.isHacking && getX() == this.target.x && getY() == this.target.y) {
      this.hackSystem();
    }

    // TODO Implement A* search to find a path to the system.

    /*
    float deltaX = 0;
    float deltaY = 0;

    // Check the space is empty before moving into it
    if (map.Empty(getX() + deltaX, getY(), image.getHeight(), image.getHeight())) {
      moveBy(deltaX, 0);
    }
    if (map.Empty(getX(), getY() + deltaY, image.getHeight(), image.getHeight())) {
      moveBy(0, deltaY);
    }
    */

    // Draw the image
    batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
  }

  public void hackSystem() {
    this.isHacking = true;
    // TODO wait between each instance of damage to the system.
    while (this.target.health > 0) {
      this.target.onHit(this, 5);
    }
    this.target = GSystem.systemsRemaining.get((int) Math.round(Math.random() * (GSystem.systemsRemaining.size() - 1)));
  }
}