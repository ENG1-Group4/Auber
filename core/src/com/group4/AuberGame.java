package com.group4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


/**
 * AuberGame is responsible for handling multiple screens and provides some
 * helper methods for this purpose ({@link com.badlogic.gdx.Game#setScreen}),
 * alongside an implementation of {@link com.badlogic.gdx.Game} for use.
 *
 * @author Robert Watts
 */
public class AuberGame extends Game {

	SpriteBatch batch;
	Skin skin;

	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
