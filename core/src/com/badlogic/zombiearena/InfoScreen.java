//Project 3

package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class InfoScreen implements Screen
{
    final ZombieArena game;
    OrthographicCamera camera;
    private Texture bg, shield;
    float swap;
    boolean swapped;

    public InfoScreen(final ZombieArena gam)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        bg = new Texture(Gdx.files.internal("infoBG.png"));
        shield = new Texture(Gdx.files.internal("buff_hp.png"));
        swap = TimeUtils.nanoTime();
        swapped = false;
    }

    @Override
    public void render(float delta)
    {
        // Below, our text is displayed and sets the condition
        // that if any button is pressed while on the screen
        // it will start the game.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(bg, 0, 0);
        game.batch.draw(shield, 1100, 420);

        if(TimeUtils.nanoTime() - swap > 500000000)
        {
            if(swapped)
            {
                swapped = false;
                game.font.setColor(Color.WHITE);
                swap = TimeUtils.nanoTime();
            }
            else
            {
                swapped = true;
                game.font.setColor(Color.FOREST);
                swap = TimeUtils.nanoTime();
            }
        }

        game.font.draw(game.batch, "Press Space To Return To Main Menu", 550, 100);
        game.font.draw(game.batch, "Press Right Arrow to go to the next page", 540, 80);

        game.batch.end();


        //The arrow keys here will swap between the info screens that are in the game.
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
        {
            game.setScreen(new InfoScreen2(game));
            dispose();
        }
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose()
    {
        game.dispose();
        bg.dispose();
        shield.dispose();
    }


}
