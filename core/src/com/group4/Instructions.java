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
import com.badlogic.gdx.Input.Keys;


public class Instructions extends ScreenAdapter {

    public AuberGame game;
    private Stage stage;
    private TextureRegion backgroundTexture = new TextureRegion(new Texture("Nebula Aqua-Pink.png"), 0, 0, 1920, 1080);
    private SpriteBatch batch = new SpriteBatch();
    private Sound menuSelect = Gdx.audio.newSound(Gdx.files.internal("menu/menuSelect.ogg"));

    public Instructions (AuberGame game){
        this.game = game;
    }

    @Override
    public void show() {
        //Create the stage and allow it to process inputs. Using an Extend Viewport for scalablity of the product
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Texture logoTexture = new Texture(Gdx.files.internal("menu/auberLogo.png"));
        Image logo = new Image(logoTexture);
        table.add(logo).width(914.9f).height(270.9f).pad(20).align(Align.top);
        table.row();

        Texture instructionsTexture = new Texture(Gdx.files.internal("menu/instructions.png"));
        Image instructions = new Image(instructionsTexture);
        table.add(instructions).align(Align.top);
        table.row();

        ImageButton.ImageButtonStyle backStyle =  new ImageButton.ImageButtonStyle();
        backStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/backButtonInactive.png"))));
        backStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/backButtonActive.png"))));
        backStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menu/backButtonActive.png"))));
        ImageButton backButton = new ImageButton(backStyle);
        backButton.setPosition(20,20);

        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                //As per libGDX docs this is needed to return true for the touchup event to trigger
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                menuSelect.play(0.2f);
                game.setScreen(new TitleScreen(game, true));

            }
        });
        
        stage.addActor(backButton);
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
            game.setScreen(new TitleScreen(game, false));
        }
    }

    @Override
    public void resize(int width, int height) {
        //Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

}