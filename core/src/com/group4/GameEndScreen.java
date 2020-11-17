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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class GameEndScreen extends ScreenAdapter {
    public AuberGame game;
    private boolean playerWon;
    private Stage stage;
    private TextureRegion backgroundTexture = new TextureRegion(new Texture("Nebula Aqua-Pink.png"), 0, 0, 1920, 1080);
    private Sound menuSelect = Gdx.audio.newSound(Gdx.files.internal("menu/menuSelect.ogg"));
    private SpriteBatch batch = new SpriteBatch();
    private static Music winMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menuMusic.mp3"));
    private static Music lossMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menuMusic.mp3"));

    public GameEndScreen (AuberGame game, boolean playerWon){
        this.game = game;
        this.playerWon = playerWon;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        
        Table table = new Table();
        table.setFillParent(true);

        if(playerWon == true){
            winMusic.play();
            winMusic.setVolume(0.5f);
            Texture gameWin = new Texture(Gdx.files.internal("gameWin.png"));
            Image win = new Image(gameWin);
            table.add(win).width(win.getWidth()*2.3f).height(win.getHeight()*2.3f).pad(40).align(Align.center);
            table.row();
        } else {
            lossMusic.play();
            lossMusic.setVolume(0.5f);
            Texture gameEnd = new Texture(Gdx.files.internal("gameOver.png"));
            Image end = new Image(gameEnd);
            table.add(end).width(end.getWidth()*2.5f).height(end.getHeight()*2.5f).pad(40).align(Align.center);
            table.row();
        }

        ImageButton.ImageButtonStyle menuStyle =  new ImageButton.ImageButtonStyle();
        menuStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/menuButtonInactive.png"))));
        menuStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/menuButtonActive.png"))));
        menuStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/menuButtonActive.png"))));
        ImageButton menuButton = new ImageButton(menuStyle);
        table.add(menuButton).pad(30).align(Align.center);

        menuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //As per libGDX docs this is needed to return true for the touchup event to trigger
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                winMusic.stop();
                lossMusic.stop();
                menuSelect.play(0.2f);
                game.setScreen(new TitleScreen(game, false));

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
    }
    
    @Override
    public void dispose () {
        menuSelect.dispose();
        batch.dispose();
        stage.dispose();
    }
    
}