package com.group4.Auber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.group4.Auber.HUD.HUD;
import org.json.*;


/**
 * GameScreen is an extension of {@link com.badlogic.gdx.ScreenAdapter} to create and render the game.
 *
 * @author Robert Watts
 */
public class GameScreen extends ScreenAdapter {

    public AuberGame game;
    private Stage stage;
    private Player player;
    private MapRenderer map;
    private OrthographicCamera camera;
    private com.group4.Auber.HUD.HUD HUD;
    private final float CameraLerp = 2f;
    private SpriteBatch batch = new SpriteBatch();
    private SpriteBatch batch2 = new SpriteBatch();
    private Music ambience = Gdx.audio.newMusic(Gdx.files.internal("audio/ambience.mp3"));
    private TextureRegion backgroundTexture = new TextureRegion(new Texture("img/tilesets/Nebula-Aqua-Pink.png"), 0, 0, 1920, 1080);
    private TextureRegion mapTexture = new TextureRegion(new Texture("img/mapScreen.png"), 0, 0, 1920, 1080);
    private TiledMap tiledMap = new TmxMapLoader().load("auber_map.tmx");
    JSONObject gameData = new JSONObject(Gdx.files.internal("mapdata.json").readString());

    public GameScreen (AuberGame game){
        this.game = game;
        ambience.play();
        ambience.setLooping(true);
        ambience.setVolume(0.7f);
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


        //Load the map and create it
        map = new MapRenderer(tiledMap, Gdx.files.internal("walkable_map.txt").readString());

        //for game end stuff
        Player.game = game;
        Systems.game = game;
        Operative.game = game;
        //Create the player and add it to the stage
        player = new Player(map, gameData.getJSONArray("playerStartCoords").getInt(0), gameData.getJSONArray("playerStartCoords").getInt(1));



        stage.addActor(player);

        //Create the Heads up display
        HUD = new HUD(player, gameData);
        Gdx.input.setInputProcessor(HUD);
        HUD.infoNotification("System Log started...");

        //String[] coords = datas[0].split(",");
        //stage.addActor(new Player(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]), map));
        //create systems + add them to the stage

        for (int i = 0; i < gameData.getJSONArray("rooms").length(); i++) {
            if (!gameData.getJSONArray("rooms").getJSONObject(i).isNull("systemCoords")) {
                stage.addActor(new Systems(
                        gameData.getJSONArray("rooms").getJSONObject(i).getJSONArray("systemCoords").getInt(0),
                        gameData.getJSONArray("rooms").getJSONObject(i).getJSONArray("systemCoords").getInt(1),
                        map,
                        this.HUD,
                        gameData.getJSONArray("rooms").getJSONObject(i).getString("name")));
            }
        }

        //create operatives + add them to the stage
        for (int i = 0; i < gameData.getJSONArray("operativeStartCoords").length(); i++) {
            stage.addActor(
                    new Operative(
                            gameData.getJSONArray("operativeStartCoords").getJSONArray(i).getInt(0),
                            gameData.getJSONArray("operativeStartCoords").getJSONArray(i).getInt(1),
                            map,
                            this.HUD
                        ));
        }

        HUD.setValues(Operative.remainingOpers, Systems.systemsRemaining.size());
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
            game.setScreen(new TitleScreen(game, false));
        }

        if (Gdx.input.isKeyPressed(Keys.M)){
            batch2.begin();
            batch2.draw(mapTexture, 0, 0);
            batch2.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        //Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    @Override
    public void dispose(){
        ambience.dispose();
        batch.dispose();
        batch2.dispose();
        map.dispose();
        stage.dispose();
        HUD.dispose();
    }


}