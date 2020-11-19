package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.lang.Math;

/**
 * The player sprite. Extends the {@link com.badlogic.gdx.scenes.scene2d.Actor} class.
 * Handles key presses for play moment, as wll as drawing the player each frame.
 * The key press are polled rather than using events so that the player can move diagonally.
 *
 * @author Robert Watts, Adam Wiegand
 */

public class Player extends Actor {
    public static AuberGame game;
    private final Texture imageDown = new Texture(Gdx.files.internal("img/player.png"));
    private final Texture imageUp = new Texture(Gdx.files.internal("img/player_up.png"));
    private final Texture imageLeft = new Texture(Gdx.files.internal("img/player_left.png"));
    private final Texture imageRight = new Texture(Gdx.files.internal("img/player_right.png"));
    private final Texture imageAttack = new Texture(Gdx.files.internal("img/player_attack.png"));
    private final Texture imageTarget = new Texture(Gdx.files.internal("img/player_target.png"));
    private Texture currentImage = imageDown;
    private Sound step = Gdx.audio.newSound(Gdx.files.internal("audio/footstep.mp3"));
    private Sound swing = Gdx.audio.newSound(Gdx.files.internal("audio/swing.mp3"));
    private Sound punch1 = Gdx.audio.newSound(Gdx.files.internal("audio/punch1.mp3"));
    private Sound punch2 = Gdx.audio.newSound(Gdx.files.internal("audio/punch2.mp3"));

    private float playerSpeed = 1.5f;
    private Map map;
    private int health = 100;
    private float healthTimer = 0;
    private long audioStart = 0;
    private int attackDelay = 0;

    public Player(Map map,int x, int y){
        this.map = map;
        setBounds(map.worldPos(x), map.worldPos(y), 20f, 20f);
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
        map.autoLeave(this,getX(),getY(), getWidth(), getHeight());
        if (map.Empty(getX() + deltaX, getY(), getWidth(), getHeight())){
            moveBy(deltaX, 0);
        }
        if (map.Empty(getX(), getY() + deltaY, getWidth(), getHeight())){
            moveBy(0, deltaY);
        }
        map.autoEnter(this,getX(),getY(), getWidth(), getHeight());
        //See if the player has moved
        if (Math.abs(deltaX) > 0 || Math.abs(deltaY) > 0){
            
            //Sets the footstep sound effect to play at 0.32 sec intervals when the player is moving
            if (TimeUtils.timeSinceNanos(audioStart) > 320000000) {
                step.play(0.3f);
                audioStart = TimeUtils.nanoTime();
            }

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

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){//attack
            float xAtt = getX() - 12f;
            float yAtt = getY() - 6f;
            float wAtt = imageAttack.getWidth();//assuming square
            //attack direction
            if(currentImage == imageRight){//attack right
                xAtt += 32;
            }else if (currentImage == imageLeft) { //attack left
                xAtt -= 32;
            }else if(currentImage == imageUp){//attack up
                yAtt  += 32;
            }else if (currentImage == imageDown) {//attack down
                yAtt  -= 32;
            }

            //do attack
            if (attackDelay == 0){
                Operative target = null;
                for (Actor thing : map.InArea(xAtt, yAtt, wAtt, wAtt)) {
                    if (thing instanceof Operative){
                        target = (Operative) thing;
                        target.onHit(this, 20);
                        punch1.play(0.20f);
                    }
                }
                if (target == null) {
                    swing.play(0.45f);
                }
                attackDelay = 61;
                //display attack
                batch.draw(imageAttack, xAtt, yAtt, wAtt, wAtt);
            } else {
                //display uncharged attack
                batch.draw(imageTarget, xAtt, yAtt, wAtt, wAtt);
            }
        }

        //attack delay
        if (attackDelay > 0){
            attackDelay -= 1;
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
        batch.draw(currentImage, getX() - 6, getY(), currentImage.getWidth(), currentImage.getHeight());
    }

    public void onHit(Actor by,int amount) {
        if (by instanceof Operative){
            punch2.play(0.30f);
            health -= amount;
            if (health <= 0) {
            onDeath();
            }
        }
    }

    public void onDeath(){
        map.autoLeave(this);
        game.setScreen(new GameEndScreen(game, false));
      }
    public int getHealth(){
        return health;
    }
}