package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.lang.Math;

/**
 * The player sprite. Extends the {@link com.badlogic.gdx.scenes.scene2d.Actor} class.
 * Handles key presses for play moment, as wll as drawing the player each frame.
 * The key press are polled rather than using events so that the player can move diagonally.
 *
 * @author Robert Watts
 */
public class Player extends Actor {
    private final Texture imageDown = new Texture(Gdx.files.internal("img/player.png"));
    private final Texture imageUp = new Texture(Gdx.files.internal("img/player_up.png"));
    private final Texture imageLeft = new Texture(Gdx.files.internal("img/player_left.png"));
    private final Texture imageRight = new Texture(Gdx.files.internal("img/player_right.png"));
    private Texture currentImage = imageDown;

    private float playerSpeed = 5f;
    private Map map;
    private int health = 100;
    private float healthTimer = 0;

    public Player(Map map){
        this.map = map;
        setPosition(map.worldPos(39), map.worldPos(40));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //Move the player by a set amount if the keys are pressed.
        float deltaX = 0;
        float deltaY = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)  && health >= 0){
            health -= 1;
        }

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
        if (map.Empty(getX() + deltaX, getY(), currentImage.getWidth(), currentImage.getHeight())){
            moveBy(deltaX, 0);

        }
        if (map.Empty(getX(), getY() + deltaY, currentImage.getWidth(), currentImage.getHeight())){
            moveBy(0, deltaY);
        }

        //See if the player has moved
        if (Math.abs(deltaX) > 0 || Math.abs(deltaY) > 0){

            //Change the image
            if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                if(deltaX > 0){
                    currentImage = imageRight;
                } else {
                    currentImage = imageLeft;
                }
            } else {
                if(deltaY > 0){
                    currentImage = imageUp;
                } else {
                    currentImage = imageDown;
                }
            }

        }

        //Player Health
        if (map.Effect(2,this)){
            healthTimer += Gdx.graphics.getDeltaTime();
            if(healthTimer >= 0.1f && health < 100) {
                health += 1;
                healthTimer = 0f;
            }
        }


        //Draw the image
        batch.draw(currentImage, getX(), getY(), currentImage.getWidth(), currentImage.getHeight());
    }

    public int getHealth(){
        return health;
    }
}
