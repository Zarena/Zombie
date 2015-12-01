package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

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
    protected float lastUpdate;
    public int type;

    //str refers to the strength of the enemy and will be used to determine how much damage it deals
    //hp is how much damage the enemy can take before it is destroyed
    protected int str, hp;

    public Enemy(int round)
    {
        hp = round;
        str = round;
        frameCounter = 0;
        alive = true;
        lastUpdate = TimeUtils.nanoTime();
    }



    public void flip()
    {
        // This is the animations to match the direction the enemy is actually moving towards.
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
        // This small section is to make sure that the enemies are always facing towards the player.
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
