package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy
{
    //Zombie is 100 x 156

    protected boolean right;
    protected boolean alive;
    protected boolean attacking;
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
        avatar.flip(true, false);

        for(int i=0; i<maxFrames; i++)
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

        if(right)
            x = x + 150 * Gdx.graphics.getDeltaTime();
        else
            x = x - 150 * Gdx.graphics.getDeltaTime();
    }

    public boolean isAttacking()
    {
        return attacking;
    }


    //Scaffold - #STUB - finish me later
    public boolean playerInRange()
    {
        return false;
    }


    public void startAttacking()
    {
        attacking = true;
    }


    public void stopAttacking()
    {
        attacking = false;
    }

    //Scaffold - #STUB - finish me later
    public void attack()
    {

    }



    public void facePlayer(float playX)
    {
        if(playX < x && right)
            flip();

        if(playX > x && !right)
            flip();
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
