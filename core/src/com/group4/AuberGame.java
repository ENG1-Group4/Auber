package com.group4;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * AuberGame is responsible for handling multiple screens and provides some
 * helper methods for this purpose ({@link com.badlogic.gdx.Game#setScreen}),
 * alongside an implementation of {@link com.badlogic.gdx.Game} for use.
 *
 * @author Robert Watts
 */
public class AuberGame extends Game {

	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
