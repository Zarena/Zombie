//Project 3

package com.badlogic.zombiearena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.zombiearena.MainMenuScreen;


public class ZombieArena extends Game
{

	public SpriteBatch batch;
	public BitmapFont font;

	public void create()
	{
		//Creates and calls the main menu screen from the appropriately titled MainMenuScreen java file.
		batch = new SpriteBatch();

		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render()
	{
		super.render(); //important!
	}

	public void dispose()
	{
	}

}
