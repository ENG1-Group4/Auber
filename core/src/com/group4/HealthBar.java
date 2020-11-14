package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Draws the health bar of the game
 *
 * @author Robert Watts
 */
public class HealthBar extends Actor {

    final private ShapeRenderer shapeRenderer = new ShapeRenderer();
    float size;
    Player player;
    BitmapFont font = new BitmapFont();

    /**
     *
     * @param player the player
     * @param size the size of the circle
     */
    public HealthBar(Player player,float size){
        this.size = size;
        this.player = player;
        shapeRenderer.setAutoShapeType(true);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        float radius = size /2;
        float healthPercentage = (float) player.getHealth() / 100f;
        float circleCenterX = getX() + radius;
        float circleCenterY = getY() + radius;

        //Allow colours to blend
        batch.end();
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin();

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        //Draw bg
        shapeRenderer.setColor(0,0,0,0.4f);
        shapeRenderer.circle(circleCenterX,circleCenterY,radius);

        //Draw health bar
        shapeRenderer.setColor(new Color().set(1,0,0,1).lerp(0,1,0,1, healthPercentage));
        shapeRenderer.arc(circleCenterX,circleCenterY, radius,270,361 - (360 * (1-healthPercentage)), 400);

        //Draw center circle
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.circle(circleCenterX,circleCenterY,radius * 0.75f);

        //Stop colours blending for the next set of actors in the stage
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();

        //Draw the health bar text
        font.setColor(1,1,1,1f);
        GlyphLayout healthText = new GlyphLayout(font, String.valueOf(player.getHealth()));
        font.draw(batch, healthText, circleCenterX - healthText.width / 2, circleCenterY + font.getXHeight()/2);
    }
}
