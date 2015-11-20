package com.badlogic.zombiearena;

//Class for G type enemies

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ground extends Enemy
{

    public Ground()
    {
        super();
        maxFrames = 10;

        sheet = new Texture(Gdx.files.internal("zombieWalk.png"));
        frame = new Sprite[maxFrames];
        for(int i = 0; i < maxFrames; i++)
        {
            frame[i] = new Sprite(sheet, (i*100), 0, 100, 156);
        }
        avatar = new Sprite(frame[0]);
    }

}