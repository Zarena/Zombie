package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy
{
    //Zombie is 100 x 156

    protected boolean right;
    public boolean alive;
    protected Texture sheet;
    protected Sprite[] frame;
    protected Sprite avatar;
    protected int frameCounter;
    public int x,y;

    public Enemy()
    {
        frameCounter = 0;
        alive = true;
        right = false;
        sheet = new Texture(Gdx.files.internal("zombieWalk.png"));
        frame = new Sprite[10];
        for(int i = 0; i < 10; i++)
        {
            frame[i] = new Sprite(sheet, (i*100), 0, 100, 156);
        }
        avatar = new Sprite(frame[0]);
    }


    public void flip()
    {
        for(int i=0; i<10; i++)
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


    public boolean isFacingRight()
    {
        return right;
    }
}
