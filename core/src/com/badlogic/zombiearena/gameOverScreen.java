//Project 3

package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class gameOverScreen implements Screen
{
    int selection;
    final int MENU_SIZE=2;
    OrthographicCamera camera;
    private Texture splashScreen;
    private Texture hand;
    private Color defCol, selCol;
    private String yourScoreIs;
    final ZombieArena game;

    public gameOverScreen(final ZombieArena gam, int scoreIn)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        yourScoreIs = "Total Score Was:          " + scoreIn;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        splashScreen = new Texture(Gdx.files.internal("gameOver.png"));
        hand = new Texture(Gdx.files.internal("zombie_hand_use.png"));
        defCol = Color.FOREST;
        selCol = Color.WHITE;
        selection=0;
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

        game.batch.draw(splashScreen,0 ,0);


        game.font.setColor(defCol);
        game.font.draw(game.batch, "Use arrow keys (UP / DOWN) to select an item.", 583, 475);
        game.font.draw(game.batch, "Press enter to make your selection.", 600, 450);


        game.font.draw(game.batch, yourScoreIs, 550, 350);


        if(selection == 0)
        {
            game.batch.draw(hand, 480, 265);
            game.font.setColor(selCol);
        }
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Main Menu", 600, 300);



        if(selection == 1)
        {

            game.batch.draw(hand, 480, 240);
            game.font.setColor(selCol);
        }
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Quit", 625, 275);


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
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                    break;
                case 1:
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
        splashScreen.dispose();
        hand.dispose();
        game.dispose();
    }


}
