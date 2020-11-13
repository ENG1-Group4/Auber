package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * GameScreen is an extension of {@link com.badlogic.gdx.ScreenAdapter} to create and render the game.
 *
 * @author Robert Watts
 */
public class GameScreen extends ScreenAdapter {

    public AuberGame game;
    private Stage stage;
    private Player player;
    private Map map;
    private OrthographicCamera camera;
    private final float CameraLerp =1f;
    private HUD HUD;
    public GameScreen (AuberGame game){
        this.game = game;
    }

    @Override
    public void show() {
        //Create the camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();

        //Create the stage and allow it to process inputs. Using an Extend Viewport for scalability of the product
        stage = new Stage(new ExtendViewport(w,h,camera));
        Gdx.input.setInputProcessor(stage);

        //Load the map and create it
        TiledMap tiledMap = new TmxMapLoader().load("auber_map_4.0_base.tmx");
        map = new Map(tiledMap, Gdx.files.internal("map").readString());

        //Create the player and add it to the stage
        player = new Player(map);
        stage.addActor(player);

        //Create the Heads up display
        HUD = new HUD(player, tiledMap);
    }

    @Override
    public void render(float delta) {
        //Set the background colour & draw the stage
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        //Move the camera to follow the player
        Vector3 position = camera.position;
        camera.position.x += (player.getX() + delta - camera.position.x) * CameraLerp * delta;
        camera.position.y += (player.getY() + delta - camera.position.y) * CameraLerp * delta;
        camera.update();
        map.setView(camera);

        //Render the objects. Rander the bg layers, then the player, then the foreground layers to give the effect of
        //3d (as the player can go behind certain objects)
        map.render(new int[]{0,1,2,3,4,5});
        stage.draw();
        map.render(new int[]{6,7});

        //Draw the HUD
        HUD.draw();
    }



    @Override
    public void resize(int width, int height) {
        //Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

}
