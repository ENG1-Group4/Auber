package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Keys;

/**
 * TitleScreen is an extension of {@link com.badlogic.gdx.ScreenAdapter} to create and render the title screen.
 *
 * @author Robert Watts
 */
public class TitleScreen extends ScreenAdapter {

    public AuberGame game;
    private Stage stage;
    private TextureRegion backgroundTexture = new TextureRegion(new Texture("Nebula Aqua-Pink.png"), 0, 0, 1920, 1080);
    private SpriteBatch batch = new SpriteBatch();
    private Sound menuSelect = Gdx.audio.newSound(Gdx.files.internal("menu/menuSelect.ogg"));
    public static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menuMusic.mp3"));
    private boolean isMusicPlaying;

    public TitleScreen (AuberGame game, boolean isMusicPlaying){
        this.game = game;
        this.isMusicPlaying = isMusicPlaying;
        Operative.remainingOpers = 0;
        GSystem.systemsRemaining.clear();
    }

    @Override
    public void show() {

        if(isMusicPlaying == false){
            menuMusic.play();
            menuMusic.setVolume(0.1f);
            menuMusic.setLooping(true);
        }
        
        //Create the stage and allow it to process inputs. Using an Extend Viewport for scalablity of the product
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        //Create the table and expand it to fill the window
        Table table = new Table();
        table.setFillParent(true);

        //Create the logo and add it to the table
        Texture logoTexture = new Texture(Gdx.files.internal("menu/auberLogo.png"));
        Image logo = new Image(logoTexture);
        table.add(logo).pad(10).fillY().align(Align.center);
        table.row();

        //Create the start game button, add it to the table with its click event
        ImageButton.ImageButtonStyle playStyle =  new ImageButton.ImageButtonStyle();
        playStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/playButtonInactive.png"))));
        playStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/playButtonActive.png"))));
        playStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/playButtonActive.png"))));
        ImageButton playButton = new ImageButton(playStyle);
        table.add(playButton).center().pad(5);
        table.row();

        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //As per libGDX docs this is needed to return true for the touchup event to trigger
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                menuMusic.stop();
                GameEndScreen.menuMusic.stop();
                menuSelect.play(0.2f);
                game.setScreen(new GameScreen(game));
            }
        });

        //Create the instructions button, add it to the table with its click event
        ImageButton.ImageButtonStyle instructionsStyle =  new ImageButton.ImageButtonStyle();
        instructionsStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/instructionsButtonInactive.png"))));
        instructionsStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/instructionsButtonActive.png"))));
        instructionsStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/instructionsButtonActive.png"))));
        ImageButton instructionsButton = new ImageButton(instructionsStyle);
        table.add(instructionsButton).center().pad(5);
        table.row();

        instructionsButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //As per libGDX docs this is needed to return true for the touchup event to trigger
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                menuSelect.play(0.2f);
                game.setScreen(new Instructions(game));
            }
        });

        //Create the quit game button, add it to the table with its click event
        ImageButton.ImageButtonStyle quitStyle =  new ImageButton.ImageButtonStyle();
        quitStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/quitButtonInactive.png"))));
        quitStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/quitButtonActive.png"))));
        quitStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/quitButtonActive.png"))));
        ImageButton quitButton = new ImageButton(quitStyle);
        table.add(quitButton).center().pad(5);
        table.row();

        quitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //As per libGDX docs this is needed to return true for the touchup event to trigger
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                menuSelect.play(0.2f);
                Gdx.app.exit();
            }
        });

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        //Set the background colour & draw the stage
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        //Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void dispose () {
        menuSelect.dispose();
        menuMusic.stop();
        menuMusic.dispose();
        batch.dispose();
        stage.dispose();
	}

}