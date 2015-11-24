package com.badlogic.zombiearena;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class GameScreen implements Screen
{
    final ZombieArena game;

    BitmapFont font;

    //Pause Variables
    //Project 3
    private enum State
    {
        PAUSE, RESUME;
    }
    private State state;

    private int round;


    private boolean hit;
    private boolean spawnOK;


    //Project 3
    //Sounds
    private Sound hammerSlam;
    private Sound hammerSwing1;
    private Sound hammerThrow;
    private Sound enemyHiss;
    private Sound dodge;

    //Project 3
    //Enemies are kept in an array just like raindrops from the drop toy
    private Array<Enemy> enemies;
    private float lastEnemy;
    // 1000000000 Nanoseconds = 1 second
    // 1,000,000,000



    //Game Window Variables
    private static int screenX=1366;
    private static int screenY=768;
    private Texture background;


    //Avatar variables
    private Rectangle playPos;
    private boolean facingRight, moveOK;

    private Texture avatarTexture;
    private Sprite standing;
    private Sprite avatar;

    private Texture dodgeTexture;
    private Sprite dodgeSprite;


    //Walk variables
    private Sprite[] walk;
    private int walkCounter;


    //"Slash" Variables
    private Texture attackSheet;
    private Sprite[] attackSprite;


    //Slam Variables
    private Texture vSlamTexture;
    private Sprite[] vSlamSprite;


    //Utilities
    public SpriteBatch batch;
    private OrthographicCamera camera;
    private float lastUpdate;
    private boolean firedRight, dodging, halfSwing;


    //Throw Trackers
    private Texture throwStrip;
    private Sprite[] throwSprite;
    private int throwCounter;
    private float throwUpdate;
    private boolean throwing, flying;
    private Texture hammerT;
    private Sprite hammer, hammerL, hammerR;
    float hammerX, hammerY;


    //"Slash" Trackers
    private int slashCounter;
    private float slashUpdate;
    private boolean slashing;


    //Slam Trackers
    private int slamCounter;
    private float slamUpdate;
    private boolean slamming;

    //Score Variables
    //Project 3
    private int score;
    private String yourScoreIs;


    public GameScreen(final ZombieArena gam)
    {

        this.game = gam;

        background = new Texture(Gdx.files.internal("bg1.png"));


        round = 1;

        hit = false;
        spawnOK = true;

        //Project 3
        //Initialize sounds
        hammerSlam = Gdx.audio.newSound(Gdx.files.internal("HammerSlam.mp3"));
        hammerSwing1 = Gdx.audio.newSound(Gdx.files.internal("HammerSwing1.mp3"));
        hammerThrow = Gdx.audio.newSound(Gdx.files.internal("HammerThrow.mp3"));
        enemyHiss = Gdx.audio.newSound(Gdx.files.internal("EnemyHiss.mp3"));
        dodge = Gdx.audio.newSound(Gdx.files.internal("HeroDodge.mp3"));

        //Score
        //Project 3
        font = new BitmapFont();
        score = 0;
        yourScoreIs = "Score:  " + score;


        //Pause State
        state = State.RESUME;


        //Enemies
        enemies = new Array<Enemy>();


        //Initialize Throw Trackers
        throwCounter = 0;
        throwUpdate =TimeUtils.nanoTime();
        throwing = false;
        flying = false;
        throwStrip = new Texture(Gdx.files.internal("vThrow.png"));
        throwSprite = new Sprite[18];
        for(int i=0; i<18; i++)
        {
            throwSprite[i] = new Sprite(throwStrip, (i * 356), 0, 356, 256);
        }


        //Initialize Slam Trackers
        slamCounter = 0;
        slamUpdate = TimeUtils.nanoTime();
        slamming = false;


        //Initialize "Slash" Trackers
        slashCounter=0;
        halfSwing=false;
        slashing = false;
        slashUpdate = slamUpdate;


        //Initialize viking slam animation
        vSlamTexture = new Texture(Gdx.files.internal("vSlam.png"));
        vSlamSprite = new Sprite[9];
        for (int i=0; i<9; i++)
        {
            vSlamSprite[i] = new Sprite(vSlamTexture, (i * 286 ),0,286,254);
        }


        //avatarTexture is a sprite sheet for the  character texture
        avatarTexture = new Texture(Gdx.files.internal("viking.png"));
        dodgeTexture = new Texture(Gdx.files.internal("dodge.png"));
        dodgeSprite = new Sprite(dodgeTexture);


        //AttackSheet is a sprite sheet for the "slash" animations
        attackSheet = new Texture(Gdx.files.internal("vAttack.png"));
        attackSprite = new Sprite[12];

        for (int i = 0; i < 12; i++)
        {
            attackSprite[i] = new Sprite(attackSheet, (i * 344), 0, 344, 210);
        }


        //Initialize hammer
        hammerT = new Texture(Gdx.files.internal("hammer.png"));
        hammerL = new Sprite(hammerT);
        hammerL.flip(true, false);
        hammerR = new Sprite(hammerT);
        hammer = new Sprite(hammerT);


        //Booleans for tracking facing, whether or not movement is allowed, whether
        //or not the player is currently attacking, etc.
        facingRight = true;
        moveOK = true;
        slashing = false;
        throwing = false;
        firedRight = true;
        dodging = false;


        //Sprite batch
        batch = new SpriteBatch();


        //playPos is a rectangle for tracking the player's current location
        //playPos = new Rectangle((800 /2 - 64/2),20, 48, 64);
        playPos = new Rectangle((screenX/2 - 220/2), 40, 55, 56);


        //Setup walk animation
        //220 x 222
        walk = new Sprite[9];
        for(int i=0; i<9; i++)
        {
            walk[i] = new Sprite(avatarTexture, (440 + (i*220)), 0, 220, 222);
        }
        walkCounter = 0;



        //Setup basic character (standing)
        standing = new Sprite(avatarTexture, 0 , 0, 280, 222);
        avatar = new Sprite(standing);

        //Tracking animation times and enemy spawn times
        lastUpdate = TimeUtils.nanoTime();
        lastEnemy = lastUpdate;
        slashUpdate = lastUpdate;


        //Cam variables
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenX, screenY);

        //Spawn First Enemy
        spawnEnemy();
    }



    //Method for flipping sprites/textures
    public void flip()
    {
        avatar.flip(true, false);
        standing.flip(true, false);
        dodgeSprite.flip(true, false);


        if(!flying)
        {
            hammer.flip(true, false);
        }

        for(int i=0; i<9; i++)
        {
            walk[i].flip(true, false);
        }

        for(int i=0; i<12; i++)
        {
            attackSprite[i].flip(true, false);
        }

        for(int i=0; i<9; i++)
        {
            vSlamSprite[i].flip(true, false);
        }

        for(int i=0; i<18; i++)
        {
            throwSprite[i].flip(true, false);
        }
    }


    //Method used to update walk animation
    public void step()
    {
        if (TimeUtils.nanoTime() - lastUpdate > 90000000)
        {
            lastUpdate = TimeUtils.nanoTime();
            walkCounter++;
            if (walkCounter == 9)
                walkCounter = 0;
            avatar.set(walk[walkCounter]);
        }
    }


    //Throw is a reserved word.
    public void toss()
    {
        if (TimeUtils.nanoTime() - throwUpdate > 22500000)
        {
            throwUpdate = TimeUtils.nanoTime();
            avatar.set(throwSprite[throwCounter]);
            throwCounter++;

            if(throwCounter==6)
                hammerThrow.play();

            if(throwCounter==9)
                flying = true;

            if(throwCounter == 18)
            {
                throwCounter = 0;
                throwing = false;
                moveOK = true;
            }
        }
    }


    //Method controls the slam attack (down key)
    public void slam()
    {
        if (TimeUtils.nanoTime() - slamUpdate > 45000000)
        {
            slamUpdate = TimeUtils.nanoTime();
            avatar.set(vSlamSprite[slamCounter]);
            slamCounter++;

            if(slamCounter == 4)
                hammerSlam.play();

            if(slamCounter == 9)
            {
                slamCounter = 0;
                slamming = false;
                moveOK = true;
            }
        }
    }


    //Method controls the attacks triggered by left and right arrow keys
    public void slash()
    {
        if (TimeUtils.nanoTime() - slashUpdate > 45000000)
        {
            slashUpdate = TimeUtils.nanoTime();
            avatar.set(attackSprite[slashCounter]);


            if(!halfSwing)
            {
                slashCounter++;
            }
            if(slashCounter==2)
                hammerSwing1.play();
            if (slashCounter == 12)
            {
                halfSwing = true;
            }

            if(halfSwing && !hit)
            {
                Iterator<Enemy> iter = enemies.iterator();
                while(iter.hasNext())
                {
                    Enemy enemy = iter.next();

                    if(enemy.x <= playPos.getX() && !facingRight)
                    {
                        iter.remove();
                        score++;
                    }

                    if(enemy.x >= playPos.getX() && facingRight)
                    {
                        iter.remove();
                        score++;
                    }
                }
                hit = true;
            }

            if(halfSwing)
            {
                slashCounter--;
            }
            if(halfSwing && slashCounter < 0)
            {

                slashCounter =0;
                halfSwing=false;
                slashing = false;
                moveOK = true;
                hit = false;
            }
        }
    }


    @Override
    public void render (float delta)
    {

        //Project 3
        yourScoreIs = "Score:  " + score;

        switch (state)
        {
            case PAUSE:
                if (Gdx.input.isKeyJustPressed(Keys.P))
                    state = State.RESUME;
                break;
            case RESUME:


                Gdx.gl.glClearColor(0, 0, 0.35f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


                camera.update();


                batch.setProjectionMatrix(camera.combined);

                if (throwing)
                {
                    toss();
                }

                if (slashing)
                {
                    slash();
                }

                if (slamming)
                {
                    slam();
                }

                batch.begin();

                batch.draw(background, 0, 0);


                //Draw player sprite
                batch.draw(avatar, playPos.getX(), playPos.getY());


                //Draw Enemies
                for(Enemy enemy:  enemies)
                {
                    batch.draw(enemy.getAvatar(), enemy.x, enemy.y);
                }


                //Draw Score
                font.draw(batch, yourScoreIs, screenX - (float)(yourScoreIs.length() * 8.75), screenY - 20);


                //Draw and update hammer if hammer was fired
                if (flying && firedRight)
                {
                    batch.draw(hammer, hammerX+140, hammerY+110);
                    hammerX += 600 * Gdx.graphics.getDeltaTime();
                    hammerY += 600 * Gdx.graphics.getDeltaTime();
                } else if (flying)
                {
                    batch.draw(hammer, hammerX-80, hammerY+120);
                    hammerX -= 600 * Gdx.graphics.getDeltaTime();
                    hammerY += 600 * Gdx.graphics.getDeltaTime();
                }

                for(Enemy enemy:  enemies)
                {
                    batch.draw(enemy.avatar, enemy.x, enemy.y);
                }

                batch.end();

                if(TimeUtils.nanoTime() - lastEnemy > 2000000000)
                    if(spawnOK)
                        spawnEnemy();
                runEnemies();


                if (dodging && TimeUtils.nanoTime() - lastUpdate > 500000000)
                {
                    avatar.set(standing);
                    moveOK = true;
                    dodging = false;
                }


                if (dodging)
                {
                    if (facingRight)
                        playPos.setX(playPos.getX() + 800 * Gdx.graphics.getDeltaTime());
                    else
                        playPos.setX(playPos.getX() - 800 * Gdx.graphics.getDeltaTime());
                }

                ///////////////////////// INPUT ////////////////////////////
                // - SPACE
                if (Gdx.input.isKeyJustPressed(Keys.SPACE) && moveOK && !dodging)
                {
                    dodge.play();
                    dodging = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                    avatar.set(dodgeSprite);
                }


                // - UP
                if (Gdx.input.isKeyJustPressed(Keys.UP) && moveOK && !flying)
                {
                    firedRight = facingRight;
                    if (firedRight)
                        hammer.set(hammerR);
                    else
                        hammer.set(hammerL);

                    throwing = true;
                    moveOK = false;
                    hammerX = playPos.getX();
                    hammerY = playPos.getY();
                }


                // - DOWN
                if (Gdx.input.isKeyJustPressed(Keys.DOWN) && moveOK)
                {
                    slamming = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                }


                //Code for slashing to the right
                if (Gdx.input.isKeyJustPressed(Keys.RIGHT) && moveOK)
                {

                    if (!facingRight)
                    {
                        flip();
                        facingRight = true;
                    }

                    slashing = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                }


                //code for slashing to the left
                if (Gdx.input.isKeyJustPressed(Keys.LEFT) && moveOK)
                {

                    if (facingRight)
                    {
                        flip();
                        facingRight = false;
                    }

                    slashing = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                }


                if (hammerX < 1 - 156) flying = false;
                if (hammerX > screenX) flying = false;
                if (hammerY > screenY) flying = false;


                //// PAUSE
                if (Gdx.input.isKeyJustPressed(Keys.P))
                    state = State.PAUSE;


                if(Gdx.input.isKeyJustPressed(Keys.E))
                    if(spawnOK)
                        spawnOK = false;
                    else
                    spawnOK = true;


                //// Move LEFT and RIGHT and STAND
                if (Gdx.input.isKeyPressed(Keys.A) && moveOK)
                {
                    step();

                    playPos.setX(playPos.getX() - 275 * Gdx.graphics.getDeltaTime());

                    if (facingRight)
                    {
                        flip();
                        facingRight = false;
                    }
                } else if (Gdx.input.isKeyPressed(Keys.D) && moveOK)
                {
                    step();
                    playPos.setX(playPos.getX() + 275 * Gdx.graphics.getDeltaTime());

                    if (!facingRight)
                    {
                        flip();
                        facingRight = true;
                    }
                } else if (moveOK)
                {
                    avatar.set(standing);
                }

                if (playPos.getX() < 0) playPos.setX(0);
                if (playPos.getX() > screenX - 220) playPos.setX(screenX - 220);

                break;
        }
    }


    //Handles Enemy Actions (AI)
    public void runEnemies()
    {
        for(Enemy enemy:  enemies)
        {
            switch(enemy.type)
            {
                case 1:
                        enemy.facePlayer(playPos.getX() + 110);
                    break;
                case 2:
                    if (enemy.x > 1366 - 188)
                        enemy.facePlayer(0);
                    if (enemy.x < 0)
                        enemy.facePlayer(1366);
                    break;
                default:
                    break;
            }
            if(!enemy.isAttacking())
            {
                if(enemy.playerInRange())
                    enemy.startAttacking();
                else
                {
                      enemy.step();
                }
            }
            else
            {
                enemy.attack();
            }
        }
    }




    //Project 3
    public void spawnEnemy()
    {
        int temp = random(0,1);
        int side = random(0,1);
        int sideX =0;

        switch(side)
        {
            case 0:
                sideX = -200;
                break;
            case 1:
                sideX = 1566;
                break;
            default:
                sideX = -200;
                break;
        }

        switch(temp)
        {
            case 0:
                Ground g = new Ground(round);
                g.x = sideX;
                g.y = playPos.y + 25;
                enemies.add(g);
                lastEnemy = TimeUtils.nanoTime();
                enemyHiss.play();
                break;
            case 1:
                Air a = new Air(round);
                a.x = sideX;
                a.y = playPos.y + random(350, screenY - 40 - 136);
                enemies.add(a);
                lastEnemy = TimeUtils.nanoTime();
                break;
            default:
                System.out.println("default");
                break;
        }
    }


    //To be Used later
    public static int random(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    @Override
    public void show()
    {
        //start the playback of the background music when screen is shown
    }


    @Override
    public void hide()
    {

    }


    @Override
    public void dispose()
    {
        background.dispose();
        avatarTexture.dispose();
        dodgeTexture.dispose();
        attackSheet.dispose();
        vSlamTexture.dispose();
        throwStrip.dispose();
        hammerT.dispose();
        hammerThrow.dispose();
        hammerSwing1.dispose();
        hammerSlam.dispose();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }
}
