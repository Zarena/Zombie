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
    final int MENU_SIZE=3;
    final ZombieArena game;
    OrthographicCamera camera;
    private Texture splashScreen;
    private Texture logoSplash;
    private Color defCol, selCol;

    public MainMenuScreen(final ZombieArena gam)
    {
        // The following sets the screen size and loads the pictures onto our start screen.
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);
        splashScreen = new Texture(Gdx.files.internal("HDVikingSplash.png"));
        logoSplash = new Texture(Gdx.files.internal("zombieArena.png"));
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

        game.font.setColor(defCol);
        game.font.draw(game.batch, "Use arrow keys (UP / DOWN) to select an item.", 583, 475);
        game.font.draw(game.batch, "Press enter to make your selection.", 600, 450);


        if(selection == 0)
            game.font.setColor(selCol);
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Play", 600, 375);


        if(selection == 1)
            game.font.setColor(selCol);
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Instructions / Info", 600, 350);


        if(selection == 2)
            game.font.setColor(selCol);
        else
            game.font.setColor(defCol);
        game.font.draw(game.batch, "Quit", 600, 325);


        game.batch.draw(splashScreen,523 ,25,320,200);
        game.batch.draw(logoSplash, 383,568);
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
    public void dispose() {
    }


}
