//Project 3

package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen
{
    final ZombieArena game;
    OrthographicCamera camera;
    private Texture splashScreen;
    private Texture logoSplash;

    public MainMenuScreen(final ZombieArena gam)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        splashScreen = new Texture(Gdx.files.internal("HDVikingSplash.png"));
        logoSplash = new Texture(Gdx.files.internal("zombieArena.png"));
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
        game.font.draw(game.batch, "Welcome to Zombie Arena!!! ", 583, 475);
        game.font.draw(game.batch, "Click anywhere to begin!", 600, 450);
        game.batch.draw(splashScreen,523 ,25,320,200);
        game.batch.draw(logoSplash, 383,500);
        game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            game.setScreen(new GameScreen(game));
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
    public void dispose() {
    }


}
