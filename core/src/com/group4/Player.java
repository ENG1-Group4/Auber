package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The player sprite. Extends the {@link com.badlogic.gdx.scenes.scene2d.Actor} class.
 * Handel's key presses for play moment, as wll as drawing the player each frame.
 * The key press are polled rather than using events so that the player can move diagonally.
 *
 * @author Robert Watts
 */
public class Player extends Actor {
    private Texture image = new Texture(Gdx.files.internal("img/player.png"));
    private float playerSpeed = 5;
    private Map map;

    public Player(Map map){
        this.map = map;
        setPosition(50, 50);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //Move the player by a set amount if the keys are pressed.
        float deltaX = 0;
        float deltaY = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            deltaY += playerSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            deltaY -= playerSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            deltaX -= playerSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            deltaX += playerSpeed;
        }

        //Check the space is empty before moving into it
        if (map.Empty(getX() + deltaX, getY(), image.getHeight(), image.getHeight())){
            moveBy(deltaX, 0);
        }
        if (map.Empty(getX(), getY() + deltaY, image.getHeight(), image.getHeight())){
            moveBy(0, deltaY);
        }

        //Draw the image
        batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
    }


}
