// Air enemies
package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

public class Air extends Enemy
{
    boolean reverse;

    public Air(int round)
    {
        // This draws the dragon sprite sheet into the game.
        super(round);
        maxFrames = 4;

        sheet = new Texture(Gdx.files.internal("dragon.png"));
        frame = new Sprite[maxFrames];
        for(int i=0; i<maxFrames; i++)
            frame[i] = new Sprite(sheet, (i*188), 0, 188, 136);

        avatar = new Sprite(frame[0]);
        reverse = false;
        right = true;
        frameCounter=0;
        type = 2;
    }

    public void step()
    {
        //Section sets the location and speed as well as, direction to where the dragon flies
        if(TimeUtils.nanoTime() - lastUpdate > 50000000)
        {
            lastUpdate = TimeUtils.nanoTime();

            if (reverse)
                frameCounter--;
            else
                frameCounter++;

            if (frameCounter == maxFrames)
            {
                frameCounter = maxFrames - 2;
                reverse = true;
            }

            if (frameCounter < 0)
            {
                frameCounter = 1;
                reverse = false;
            }

            avatar.set(frame[frameCounter]);
        }

        if(right)
            x = x + 200 * Gdx.graphics.getDeltaTime();
        else
            x = x - 200 * Gdx.graphics.getDeltaTime();
    }

}