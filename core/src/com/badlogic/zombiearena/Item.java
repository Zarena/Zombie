package com.badlogic.zombiearena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Item
{
    public int type;
    public Texture tex;
    public Sprite image;
    public float x,y;
    public boolean up;

    public Item(int t, float inx, float iny)
    {
        type = t;
        x=inx;
        y=iny;

        switch(type)
        {
            case 1:
                tex = new Texture(Gdx.files.internal("buff_as.png"));
                image = new Sprite(tex);
                break;

            case 2:
                tex = new Texture(Gdx.files.internal("buff_dmg.png"));
                image = new Sprite(tex);
                break;

            case 3:
                tex = new Texture(Gdx.files.internal("buff_heal.png"));
                image = new Sprite(tex);
                break;

            case 4:
                tex = new Texture(Gdx.files.internal("buff_hp.png"));
                image = new Sprite(tex);
                break;

            case 5:
                tex = new Texture(Gdx.files.internal("buff_ms.png"));
                image = new Sprite(tex);
                break;
            default:
                break;
        }
    }

}
