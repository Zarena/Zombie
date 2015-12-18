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
    private int ptValue;
    public float lastAttack;
    public boolean grabbing;
    public boolean counted;
    public boolean bump;
    int kb; //Number of times knocked back





    //hp is how much damage the enemy can take before it is destroyed
    protected int hp;

    public Enemy(int round)
    {
        hp = round * 2;
        frameCounter = 0;
        alive = true;
        lastUpdate = TimeUtils.nanoTime();
        grabbing = false;
        counted = false;
        bump = false;
        kb=0;
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


    public boolean grab()
    {
        System.out.println("You should not be here.");
        return false;
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

    public Fireball attack()
    {
        Fireball fake = new Fireball(false, 0, 0);
        System.out.println("Accidental Call"); //Should never print
        return fake;
    }



    public void facePlayer(float playX)
    {
        // This small section is to make sure that the enemies are always facing towards the player.
        if(playX < x && right)
            flip();

        if(playX > x && !right)
            flip();
    }

    public void takeDamage(int damage)
    {
        hp = hp - damage;
        if(hp <= 0)
            alive = false;
    }

    public void setValue(int in)
    {
        ptValue = in;
    }



    public boolean playDead()
    {
        //If end of death frame, return true;

        if(TimeUtils.nanoTime() - lastUpdate > 500000000)
        {
            lastUpdate = TimeUtils.nanoTime();

        }


        //If end of death frame, return true;
        if(1==1)
            return true;
        return false;
    }

    public void bump(int inx, float iny)
    {

    }



    public int getValue()
    {
        return ptValue;
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
