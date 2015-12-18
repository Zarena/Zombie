package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Kain on 12/12/2015.
 */
public class Under extends Enemy
{
    Sprite attack[];

    public Under(int round)
    {
        super(round);

        maxFrames = 4;
        frame = new Sprite[maxFrames];

        for(int i= 0; i < maxFrames; i++)
        {
            Texture tex = new Texture(Gdx.files.internal("ugDig"+(i+1)+".png"));
            frame[i] = new Sprite(tex);
        }

        avatar = new Sprite(frame[0]);
        right = false;
        type = 3;
        attack = new Sprite[3];
        for(int i =0; i<3; i++)
        {
            Texture tex = new Texture(Gdx.files.internal("ugGrab"+(i+1)+".png"));
            attack[i] = new Sprite(tex);
        }
        grabbing = false;
        setValue(round +1);
        hp = 1;
    }

    public void step()
    {
        if(grabbing)
            grabbing = false;

        if(maxFrames != 4)
        {
            frameCounter = 0;
            maxFrames = 4;
        }

        if(TimeUtils.nanoTime() - lastUpdate > 400000000)
        {
            lastUpdate = TimeUtils.nanoTime();
            frameCounter ++;

            if (frameCounter == maxFrames)
                frameCounter =0;

            avatar.set(frame[frameCounter]);
        }

        if(right)
            x = x + 125 * Gdx.graphics.getDeltaTime();
        else
            x = x - 125 * Gdx.graphics.getDeltaTime();
    }

    public boolean grab()
    {
        if(maxFrames!=3)
        {
            maxFrames = 3;
            frameCounter=0;
        }

        if(TimeUtils.nanoTime() - lastUpdate > 100000000)
        {
            lastUpdate = TimeUtils.nanoTime();

            if(frameCounter != 2)
            {
                frameCounter ++;
                avatar.set(attack[frameCounter]);
            }

        }

        if(frameCounter == 2)
        {
            counted = true;
            return true;
        }

        return false;

    }

    public void flip()
    {
        // This is the animations to match the direction the enemy is actually moving towards.
        avatar.flip(true, false);

        for(int i=0; i<4; i++)
        {
            frame[i].flip(true, false);
        }

        for(int i=0; i<3; i++)
        {
            attack[i].flip(true, false);
        }

        if(right)
            right = false;
        else
            right = true;
    }


}
