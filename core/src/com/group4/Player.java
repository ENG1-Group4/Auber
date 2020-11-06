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

    public Player(){
        setPosition(50, 50);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //Move the player by a set amount if the keys are pressed.
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            moveBy(0,playerSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            moveBy(0,-playerSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            moveBy(-playerSpeed,0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            moveBy(playerSpeed,0);
        }

        //Draw the image
        batch.draw(image, getX(), getY(), image.getWidth(), image.getHeight());
    }


}