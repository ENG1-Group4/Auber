package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;

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

    private HUD HUD;
    private final float CameraLerp = 2f;
    private SpriteBatch batch = new SpriteBatch();
    private Music ambience = Gdx.audio.newMusic(Gdx.files.internal("audio/ambience.mp3"));
    private TextureRegion backgroundTexture = new TextureRegion(new Texture("Nebula Aqua-pink.png"), 0, 0, 1920, 1080);


    public GameScreen (AuberGame game){
        this.game = game;
        ambience.play();
        ambience.setLooping(true);
        ambience.setVolume(0.6f);
    }
    
    @Override
    public void show() {
        //Create the camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        //Create the stage and allow it to process inputs. Using an Extend Viewport for scalability of the product
        stage = new Stage(new ExtendViewport(w/3f, h/3f, camera));
        Gdx.input.setInputProcessor(stage);

        //Load the map and create it
        TiledMap tiledMap = new TmxMapLoader().load("auber_map_4.0_base.tmx");
        map = new Map(tiledMap, Gdx.files.internal("map").readString());
        String[] datas = Gdx.files.internal("mapdata").readString().split("\\r?\\n");
        //for game end stuff
        Player.game = game;
        GSystem.game = game;
        Operative.game = game;
        //Create the player and add it to the stage
        String[] coords = datas[0].split(",");
        player = new Player(map,Integer.parseInt(coords[0]),Integer.parseInt(coords[1]));
        stage.addActor(player);
        
        //String[] coords = datas[0].split(",");
        //stage.addActor(new Player(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]), map));
        //create systems + add them to the stage
        for (String coord : datas[1].split(":")) {
            coords = coord.split(",");
            stage.addActor(new GSystem(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]), map, 1));
        }
        //create operatives + add them to the stage
        for (String coord : datas[2].split(":")) {
            coords = coord.split(",");
            stage.addActor(new Operative(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]),map));
        }

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

        //Render the objects. Render the bg layers, then the player, then the foreground layers to give the effect of
        //3d (as the player can go behind certain objects)
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.end();
        map.render(new int[]{0,1,2,3,4,5});
        stage.draw();
        map.render(new int[]{6,7});

        //Draw the HUD
        HUD.draw();

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
            ambience.stop();
            game.setScreen(new TitleScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        //Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void dispose(){
        batch.dispose();
        map.dispose();
        stage.dispose();
    }
}
