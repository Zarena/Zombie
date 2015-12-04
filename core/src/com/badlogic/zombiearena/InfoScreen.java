//Project 3

package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class InfoScreen implements Screen
{
    final ZombieArena game;
    OrthographicCamera camera;
    private Texture logoSplash;

    public InfoScreen(final ZombieArena gam)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
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
        {

            game.font.setColor(Color.GREEN);
            game.font.draw(game.batch, "Click anywhere (or press space) to return to the main menu.", 500, 50);

            game.font.setColor(Color.FOREST);
            game.font.draw(game.batch, "Story", 50, 600);

            game.font.setColor(Color.WHITE);
            game.font.draw(game.batch, "     You are a viking warrior! For decades your people have been tormented", 75, 575);
            game.font.draw(game.batch, "by an evil lich, a necromancer who commands an army of foul creatures.", 75, 550);
            game.font.draw(game.batch, "Today you have taken matters into your own hands and brought the fight to", 75, 525);
            game.font.draw(game.batch, "his doorstep! Furious, the necromancer has trapped you in his arena of", 75, 500);
            game.font.draw(game.batch, "death! Undead monsters and even dragons descend upon you! Fight for your", 75, 475);
            game.font.draw(game.batch, "life, hero, fight for your people!", 75, 450);


            game.font.setColor(Color.FOREST);
            game.font.draw(game.batch, "Goal", 50, 400);

            game.font.setColor(Color.WHITE);
            game.font.draw(game.batch, "     Enemies will come at you continuously. Survive by destroying enemies.", 75, 375);
            game.font.draw(game.batch, "You will be awarded points for defeating enemies. Get the highest score!", 75, 350);

            game.font.setColor(Color.FOREST);
            game.font.draw(game.batch, "Controls", 50, 300);

            game.font.setColor(Color.WHITE);
            game.font.draw(game.batch, "Arrow Keys - Attacks", 75, 275);
            game.font.draw(game.batch, "UP - Hurl your hammer skyward (used for killing dragons)", 100, 250);
            game.font.draw(game.batch, "LEFT / RIGHT - Swing your hammer in the respective direction (used for damaging zombies)", 100, 225);
            game.font.draw(game.batch, "DOWN - Slam your hammer into the ground (used for killing skeletons in the direction you are facing)", 100, 200);
            game.font.draw(game.batch, "P - Pause the game (used for when you need to use the restroom or be social)", 100, 175);

            game.font.setColor(Color.FOREST);
            game.font.draw(game.batch, "Ending The Game", 50, 125);

            game.font.setColor(Color.WHITE);
            game.font.draw(game.batch, "If you lose all of your health points the game is over! Good luck!", 75, 100);

            game.batch.draw(logoSplash, 383, 568);
        }
        game.batch.end();



        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            game.setScreen(new MainMenuScreen(game));
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
