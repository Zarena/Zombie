package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Kain on 12/12/2015.
 */
public class Fireball
{
    private Texture skin1;
    private Texture skin2;
    private Sprite[] frame;
    public Sprite skin;
    private float lastUpdate;
    private int frameCounter;
    public float x, y;
    boolean right;


    public Fireball(boolean flip, float inX, float inY)
    {
        skin1 = new Texture(Gdx.files.internal("fireball1.png"));
        skin2 = new Texture(Gdx.files.internal("fireball2.png"));
        frame = new Sprite[2];
        frame[0] = new Sprite(skin1);
        frame[1] = new Sprite(skin2);

        if(flip)
        {
            frame[0].flip(true, false);
            frame[1].flip(true,false);
            x = inX + 188;
        }
        else
        {
            x = inX - 80;
        }
        lastUpdate = TimeUtils.nanoTime();
        frameCounter = 0;
        y = inY;
        right = flip;
        skin = new Sprite(frame[0]);
    }

    public void step()
    {
        if(TimeUtils.nanoTime() - lastUpdate > 250000000)
        {
            lastUpdate = TimeUtils.nanoTime();
            frameCounter ++;
        }

        if(frameCounter == 2)
            frameCounter = 0;

        if(right)
        {
            x += (550 * Gdx.graphics.getDeltaTime());
            y -= (550 * Gdx.graphics.getDeltaTime());
        }
        else
        {
            x -= (550 * Gdx.graphics.getDeltaTime());
            y -= (550 * Gdx.graphics.getDeltaTime());
        }

        skin.set(frame[frameCounter]);
    }


}
