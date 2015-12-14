//Project 3

package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen
{
    int selection;
    private Texture hand;
    final int MENU_SIZE=3;
    final ZombieArena game;
    OrthographicCamera camera;
    private Texture mainBG;
    private Color defCol, selCol;

    public MainMenuScreen(final ZombieArena gam)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        mainBG = new Texture(Gdx.files.internal("mainBG.png"));
        defCol = Color.FOREST;
        selCol = Color.WHITE;
        selection=0;
        hand = new Texture(Gdx.files.internal("zombie_hand_use.png"));

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
        game.batch.draw(mainBG, 0, 0);


        game.font.setColor(defCol);


        if(selection == 0)
        {
            game.batch.draw(hand, 450, 340 -100);
            game.font.setColor(selCol);
        }
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Play", 650, 375 -100);


        if(selection == 1)
        {
            game.batch.draw(hand, 450, 315 -100);
            game.font.setColor(selCol);
        }
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Instructions / Info", 615, 350 -100);


        if(selection == 2)
        {
            game.batch.draw(hand, 450, 290 -100);
            game.font.setColor(selCol);
        }
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Quit", 650, 325 -100);


        game.batch.end();


        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
        {
            selection++;
            if(selection == MENU_SIZE)
                selection = 0;
        }



        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            if(selection > 0)
                selection--;
            else
                selection = MENU_SIZE -1;
        }



        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            switch(selection)
            {
                case 0:
                    game.setScreen(new GameScreen(game));
                    dispose();
                    break;
                case 1:
                    game.setScreen(new InfoScreen(game));
                    dispose();

                    break;
                case 2:
                    dispose();
                    System.exit(0);
                    break;
                default:
                    System.out.println("ERROR - HOW DID YOU MANAGE TO GET HERE!?");
            }
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
        hand.dispose();
        game.dispose();
        mainBG.dispose();
    }


}
