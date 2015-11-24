package com.badlogic.zombiearena;

//Class for G type enemies

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

public class Ground extends Enemy
{

    public Ground(int round)
    {
        super(round);
        maxFrames = 10;

        sheet = new Texture(Gdx.files.internal("zombieWalk.png"));
        frame = new Sprite[maxFrames];
        for(int i = 0; i < maxFrames; i++)
        {
            frame[i] = new Sprite(sheet, (i*100), 0, 100, 156);
        }
        avatar = new Sprite(frame[0]);
        right = false;
    }

    public void step()
    {
        if(TimeUtils.nanoTime() - lastUpdate > 100000000 )
        {
            lastUpdate = TimeUtils.nanoTime();
            frameCounter++;

            if (frameCounter == maxFrames)
                frameCounter = 0;

            avatar.set(frame[frameCounter]);

        }

        if (right)
            x = x + 150 * Gdx.graphics.getDeltaTime();
        else
            x = x - 150 * Gdx.graphics.getDeltaTime();
    }

}