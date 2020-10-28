package com.group4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * TitleScreen is an extension of {@link com.badlogic.gdx.ScreenAdapter} to create and render the title screen.
 *
 * @author Robert Watts
 */
public class TitleScreen extends ScreenAdapter {

    public AuberGame game;
    private Stage stage;

    public TitleScreen (AuberGame game){
        this.game = game;
    }

    @Override
    public void show() {
        //Create the stage and allow it to process inputs. Using an Extend Viewport for scalablity of the product
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        //Create the table and expand it to fill the window
        Table table = new Table();
        table.setFillParent(true);


        //Create the logo and add it to the table
        Texture logoTexture = new Texture(Gdx.files.internal("img/logo.png"));
        Image logo = new Image(logoTexture);
        table.add(logo).pad(10).fillY().align(Align.center);
        table.row();

        //Create the start game button. and add it to the table
        Button startButton = new TextButton("Start Game", game.skin);
        table.add(startButton).pad(50).fillX().fillY();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        //Set the background colour & draw the stage
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //Update the viewport side, and recenter it.
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

}
