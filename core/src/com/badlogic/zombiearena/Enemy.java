package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy
{
    //Zombie is 100 x 156

    protected boolean right;
    protected boolean alive;
    protected Texture sheet;
    protected Sprite[] frame;
    protected Sprite avatar;
    protected int frameCounter;
    protected int maxFrames;
    public float x,y;

    public Enemy()
    {
        frameCounter = 0;
        alive = true;
        right = false;
    }


    public void flip()
    {
        for(int i=maxFrames; i<maxFrames; i++)
        {
            frame[i].flip(true, false);
        }

        if(right)
            right = false;
        else
            right = true;
    }


    public Sprite getAvatar()
    {
        return avatar;
    }

    public void step()
    {
        frameCounter ++;

        if(frameCounter == 10)
            frameCounter = 0;

        avatar.set(frame[frameCounter]);
    }

    public boolean isAlive()
    {
        return alive;
    }


    public boolean isFacingRight()
    {
        return right;
    }
}
