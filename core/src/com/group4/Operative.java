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

  public Operative(Map map) {
    this.map = map;
    this.chooseTarget();
    setPosition(50, 50);
  }

  public void chooseTarget() {
    if (GSystem.systemsRemaining.size() == 0)
      return;
    this.target = GSystem.systemsRemaining.get((int) Math.round(Math.random() * GSystem.systemsRemaining.size()));
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {

    // TODO Implement A* search.
    float deltaX = 0;
    float deltaY = 0;

    // if ()

    // Check the space is empty before moving into it
    if (map.Empty(getX() + deltaX, getY(), image.getHeight(), image.getHeight())) {
      moveBy(deltaX, 0);
    }
    if (map.Empty(getX(), getY() + deltaY, image.getHeight(), image.getHeight())) {
      moveBy(0, deltaY);
    }

    // Draw the image
    batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
  }
}