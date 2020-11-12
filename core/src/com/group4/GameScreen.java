package com.group4;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * GameScreen is an extension of {@link com.badlogic.gdx.ScreenAdapter} to
 * create and render the game.
 *
 * @author Robert Watts
 */
public class GameScreen extends ScreenAdapter {

    public AuberGame game;
    private Stage stage;
    private Player player;
    private Map map;
    private ArrayList<Operative> operatives = new ArrayList<Operative>();

    public GameScreen(AuberGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Create the stage and allow it to process inputs. Using an Extend Viewport for
        // scalability of the product
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Load the map
        map = new Map(Gdx.files.internal("map").readString());

        // Create the player and add it to the stage
        player = new Player(map);
        stage.addActor(player);

        // Create operatives
        operatives.add(new Operative(map));
        stage.addActor(operatives.get(0));

    }

    @Override
    public void render(float delta) {
        // Set the background colour & draw the stage
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

}
