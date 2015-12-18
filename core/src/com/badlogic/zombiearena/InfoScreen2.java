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

public class InfoScreen2 implements Screen
{
    final ZombieArena game;
    OrthographicCamera camera;
    private Texture bg;

    public InfoScreen2(final ZombieArena gam)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        bg = new Texture(Gdx.files.internal("info2BG.png"));
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

        game.batch.end();



        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        if(Gdx.input.isKeyJustPressed((Input.Keys.LEFT)))
        {
            game.setScreen(new InfoScreen(game));
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
    }


}
