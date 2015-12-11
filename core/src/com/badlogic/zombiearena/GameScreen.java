package com.badlogic.zombiearena;



import java.util.Iterator;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    //Health Bars
    private Texture[] eHP;
    private Texture[] pHP;



    //Shop Variables
    private Texture shopFrame;
    private boolean shopVis = false;
    private int strPrice;
    private int asPrice;
    private int msPrice;



    //Game Variables
    final ZombieArena game;
    BitmapFont font;
    private int round;



    //Pause Variables
    //Project 3
    private enum State
    {
        PAUSE, RESUME
    }
    private State state;



    //Dodge Cooldown Timer
    private float dodgeCD;




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

    private boolean facingRight;
    private boolean moveOK;

    private Texture avatarTexture;
    private Texture dodgeTexture;

    private Sprite standing;
    private Sprite avatar;
    private Sprite dodgeSprite;



    //Walk variables
    private Sprite[] walk;
    private int walkCounter;



    //"Slash" Variables
    private Texture attackSheet;
    private Sprite[] attackSprite;
    private float contactP;



    //Slam Variables
    private Texture vSlamTexture;
    private Sprite[] vSlamSprite;



    //Utilities
    public SpriteBatch batch;
    private OrthographicCamera camera;
    private float lastUpdate;

    private boolean firedRight;
    private boolean dodging;
    private boolean halfSwing;
    private boolean spawnOK;



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
    private String currentRound;



    //Round Variables
    private int maxEnemies;
    private int spawned;
    private int badOnScreen;
    private String roundFinished;
    private boolean roundInProg;



    //Player Stats
    private int playerStr; //player damage
    private double playerMS;    //player move speed
    private double playerAS;    //player attack speed




    public GameScreen(final ZombieArena gam)
    {
        this.game = gam;
        background = new Texture(Gdx.files.internal("bg1.png"));


        eHP = new Texture[20];
        pHP = new Texture[20];

        //Initialize Health Bars
        for(int i=0; i<20; i++)
        {
            eHP[i] = new Texture(Gdx.files.internal("e"+(i+1)*5)+".png");
            pHP[i] = new Texture(Gdx.files.internal("p"+(i+1)*5)+".png");
        }





        strPrice=5;
        asPrice=5;
        msPrice=5;


        shopFrame = new Texture(Gdx.files.internal("shop.png"));
        shopVis = false;



        dodgeCD = TimeUtils.nanoTime();



        //Configure Player Starting Stats
        playerStr = 2;
        playerMS = 1;
        playerAS = 1;



        //Configure First Round
        round = 1;
        maxEnemies = (int)(round * 1.5);
        spawned = 0;
        badOnScreen = 0;
        roundFinished = "Round " + round + " Complete!";
        roundInProg = true;
        currentRound = "Round:  "+round;
        spawnOK = true;



        //Score
        //Project 3
        font = new BitmapFont();
        score = 0;
        yourScoreIs = "Score:  " + score;



        //Pause State
        state = State.RESUME;



        //Project 3
        //Initialize sounds
        hammerSlam = Gdx.audio.newSound(Gdx.files.internal("HammerSlam.mp3"));
        hammerSwing1 = Gdx.audio.newSound(Gdx.files.internal("HammerSwing1.mp3"));
        hammerThrow = Gdx.audio.newSound(Gdx.files.internal("HammerThrow.mp3"));
        enemyHiss = Gdx.audio.newSound(Gdx.files.internal("EnemyHiss.mp3"));
        dodge = Gdx.audio.newSound(Gdx.files.internal("HeroDodge.mp3"));



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



        // The for loop is what plays the attack animation sprite sheet
        for (int i = 0; i < 12; i++)
        {
            attackSprite[i] = new Sprite(attackSheet, (i * 344), 0, 344, 210);
        }



        //Initialize hammer
        //hammer sprite dimensions = 156 x 152
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
        playPos = new Rectangle((screenX/2 - 220/2), 40, 220, 222);



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
    }





    //RoundChecker
    public void roundCheck()
    {
        if(badOnScreen == 0 && spawned == maxEnemies && roundInProg)
        {
            roundInProg = false;
            roundFinished = "Round " + round + " Complete!";
            round++;
        }
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
        if (TimeUtils.nanoTime() - lastUpdate > (90000000 / playerMS))
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
        if (TimeUtils.nanoTime() - throwUpdate > (22500000 / playerAS))
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
        if (TimeUtils.nanoTime() - slamUpdate > (45000000 / playerAS))
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
        if (TimeUtils.nanoTime() - slashUpdate > (45000000 / playerAS))
        {
            slashUpdate = TimeUtils.nanoTime();
            avatar.set(attackSprite[slashCounter]);



            if(!halfSwing)
            {
                slashCounter++;
            }



            if(slashCounter == 6 || slashCounter ==7)
            {
                Iterator<Enemy> iter = enemies.iterator();
                while(iter.hasNext())
                {
                    Enemy enemy = iter.next();

                    if (enemy.type == 1)
                    {
                        if (facingRight && enemy.x > playPos.getX())
                        {
                            float temp = (enemy.x - contactP);
                            if (temp < 0 && temp > -122)
                            {
                                if(enemy.isAlive());
                                    enemy.takeDamage(random((playerStr/2), playerStr));
                                //badOnScreen --;
                            }
                        } else
                        {
                            float temp = (contactP - (enemy.x + 100));
                            if (temp < 0 && temp > -122)
                            {
                                if(enemy.isAlive());
                                    enemy.takeDamage(random((int)(playerStr/2), playerStr));
                                //badOnScreen --;
                            }
                        }
                    }
                }

            }



            if(slashCounter==2)
                hammerSwing1.play();



            if (slashCounter == 12)
            {
                halfSwing = true;
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
            }
        }
    }





    public void hammerHit()
    {
        Iterator<Enemy> iter = enemies.iterator();
        while(iter.hasNext())
        {
            Enemy enemy = iter.next();
            if(enemy.type == 2)
            {
                float tempY = enemy.y - hammerY;
                if(tempY < 104 && tempY > -32)
                {
                    if(hammerX < enemy.x)
                    {
                        if(enemy.x - hammerX <= 156)
                        {
                            enemy.takeDamage(1);
                        }
                    }
                    else
                    {
                        if(hammerX - enemy.x <= 188)
                        {
                            enemy.takeDamage(1);
                        }

                    }
                }
            }
        }
    }





    @Override
    public void render (float delta)
    {
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
                System.exit(0);


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
                font.setColor(Color.WHITE);
                font.draw(batch, yourScoreIs, screenX - (float)(yourScoreIs.length() * 8.75), screenY - 20);
                font.setColor(Color.FOREST);
                font.draw(batch, currentRound, 5, screenY-20);



                //Draw and update hammer if hammer was fired
                if (flying)
                {
                    if (firedRight)
                    {
                        batch.draw(hammer, hammerX + 140, hammerY + 110);
                        hammerX += (600 * Gdx.graphics.getDeltaTime()) * playerAS;
                        hammerY += (600 * Gdx.graphics.getDeltaTime()) * playerAS;
                    } else
                    {
                        batch.draw(hammer, hammerX - 80, hammerY + 120);
                        hammerX -= (600 * Gdx.graphics.getDeltaTime()) * playerAS;
                        hammerY += (600 * Gdx.graphics.getDeltaTime()) * playerAS;
                    }

                    //check for hammer collisions
                    hammerHit();
                }



                for(Enemy enemy:  enemies)
                {
                    batch.draw(enemy.avatar, enemy.x, enemy.y);
                }



                roundCheck();
                if(!roundInProg)
                {
                    batch.draw(shopFrame, 341, 192);
                    font.setColor(Color.WHITE);
                    font.draw(batch, "Time to Level Up!", 600, 550);
                    font.draw(batch, "Costs " + strPrice +" for +2 damage!", 478, 452);
                    font.draw(batch, "Costs " + asPrice +" for +5% attack speed!", 478, 364);
                    font.draw(batch, "Costs " + msPrice +" for +5% movement speed & dodge cooldown reduction!", 478, 276);



                    font.draw(batch, "Press SPACE to start the next round!",580, 60);


                    font.setColor(Color.RED);
                    font.draw(batch, roundFinished, 600, 30);
                }

                    //DRAW HEALTHBARS
                    for(Enemy enemy: enemies)
                    {
                        if(enemy.type==1)
                        {
                            double val1;
                            double val2;
                            double val3;
                            int val4;

                            val1 = enemy.hp/round;
                            System.out.println("Val1:  "+val1);

                            val2 = val1/5;
                            System.out.println("Val2:  "+val2);

                            val3 = Math.round(val2);
                            System.out.println("Val3:  "+val3);

                            if(val1 != 1)
                                val4 = (int)(val3 * 5);
                            else
                                val4 = (int)val1;
                            System.out.println("Val4:  "+val4);


                            //val = Math.round(((enemy.hp/round)/5))*5;
                            //System.out.println(val);

                            switch(val4)
                            {
                                case 5:
                                    batch.draw(eHP[0], enemy.x, enemy.y+166);

                                    break;

                                case 10:
                                    batch.draw(eHP[1], enemy.x, enemy.y+166);

                                    break;

                                case 15:
                                    batch.draw(eHP[2], enemy.x, enemy.y+166);

                                    break;

                                case 20:
                                    batch.draw(eHP[3], enemy.x, enemy.y+166);

                                    break;

                                case 25:
                                    batch.draw(eHP[4], enemy.x, enemy.y+166);

                                    break;

                                case 30:
                                    batch.draw(eHP[5], enemy.x, enemy.y+166);

                                    break;

                                case 35:
                                    batch.draw(eHP[6], enemy.x, enemy.y+166);

                                    break;

                                case 40:
                                    batch.draw(eHP[7], enemy.x, enemy.y+166);

                                    break;

                                case 45:
                                    batch.draw(eHP[8], enemy.x, enemy.y+166);

                                    break;

                                case 50:
                                    batch.draw(eHP[9], enemy.x, enemy.y+166);

                                    break;

                                case 55:
                                    batch.draw(eHP[10], enemy.x, enemy.y+166);

                                    break;

                                case 60:
                                    batch.draw(eHP[11], enemy.x, enemy.y+166);

                                    break;

                                case 65:
                                    batch.draw(eHP[12], enemy.x, enemy.y+166);

                                    break;

                                case 70:
                                    batch.draw(eHP[13], enemy.x, enemy.y+166);

                                    break;

                                case 75:
                                    batch.draw(eHP[14], enemy.x, enemy.y+166);

                                    break;

                                case 80:
                                    batch.draw(eHP[15], enemy.x, enemy.y+166);

                                    break;

                                case 85:
                                    batch.draw(eHP[16], enemy.x, enemy.y+166);

                                    break;

                                case 90:
                                    batch.draw(eHP[17], enemy.x, enemy.y+166);

                                    break;

                                case 95:
                                    batch.draw(eHP[18], enemy.x, enemy.y+166);
                                    break;

                                case 100:
                                    batch.draw(eHP[19], enemy.x, enemy.y+166);
                                    break;

                            }



                        }
                    }



                    batch.end();

                if(TimeUtils.nanoTime() - lastEnemy > 2000000000 -  (round * (1000000000/15)) )
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





                ////////////////////////////////////////////////////////////
                ///////////////////////// INPUT ////////////////////////////
                ////////////////////////////////////////////////////////////



                //////////////  SPACE - DODGE  ////////////////////////////
                if (Gdx.input.isKeyJustPressed(Keys.SPACE) && roundInProg && moveOK && !dodging && (TimeUtils.nanoTime() - dodgeCD >= (1500000000 / playerMS)))
                {
                    dodgeCD = TimeUtils.nanoTime();
                    dodge.play();
                    dodging = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                    avatar.set(dodgeSprite);
                }



                //////////////////  SPACE - CONTINUE ROUND  ////////////////////
                if(Gdx.input.isKeyJustPressed(Keys.SPACE) && !roundInProg)
                {
                    roundInProg = true;
                    spawnOK = true;
                    spawned = 0;
                    maxEnemies = (int)(round * 1.5);
                    currentRound = "Round:  "+round;
                }


                //////////////////////  CLICK EVENTS FOR SHOP  ///////////////////////
                if(Gdx.input.justTouched() && !roundInProg)
                {
                    Vector3 touchPos = new Vector3();
                    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(touchPos);


                    if(touchPos.x >= (478) && touchPos.x <= (575) && touchPos.y >= (462) && touchPos.y <= (502) && score >= strPrice)
                    {
                        score = score - strPrice;
                        strPrice = strPrice + 5;
                        playerStr = playerStr + 2;
                        yourScoreIs = "Score:  " + score;
                        System.out.println("STR:  " + playerStr);

                    }



                    if(touchPos.x >= (341 + 136) && touchPos.x <= (341 + 136 + 100) && touchPos.y >= (374) && touchPos.y <= (413) && score >= asPrice)
                    {
                        score = score - asPrice;
                        asPrice = asPrice + 5;
                        playerAS = playerAS + 0.05;
                        yourScoreIs = "Score:  " + score;
                        System.out.println("AS:  " + playerAS);
                    }



                    if(touchPos.x >= (477) && touchPos.x <= (574) && touchPos.y >= (286) && touchPos.y <= (324) && score >= msPrice)
                    {
                        score = score - msPrice;
                        msPrice = msPrice + 5;
                        playerMS = playerMS + 0.05;
                        yourScoreIs = "Score:  " + score;
                        System.out.println("MS:  " + playerMS);
                    }
                }





                ///////////////  UP  ////////////////////
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



                ///////////  DOWN  ///////////////////////////////
                if (Gdx.input.isKeyJustPressed(Keys.DOWN) && moveOK)
                {
                    slamming = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                }



                ///////////////////  RIGHT  /////////////////////
                //Code for slashing to the right
                if (Gdx.input.isKeyJustPressed(Keys.RIGHT) && moveOK)
                {
                    contactP = playPos.getX() + 344 - 15;
                    if (!facingRight)
                    {
                        flip();
                        facingRight = true;
                    }

                    slashing = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                }



                ////////////////////// LEFT /////////////////////
                //code for slashing to the left
                if (Gdx.input.isKeyJustPressed(Keys.LEFT) && moveOK)
                {
                    contactP = playPos.getX() + 15;
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



                //////////////////////  P  ////////////////////////
                //// PAUSE
                if (Gdx.input.isKeyJustPressed(Keys.P))
                    state = State.PAUSE;



                //////////////////////  A & D  ///////////////////////////////
                //// Move LEFT and RIGHT and STAND
                if (Gdx.input.isKeyPressed(Keys.A) && moveOK)
                {
                    step();

                    playPos.setX(playPos.getX() - ((275 * Gdx.graphics.getDeltaTime()) * (float)playerMS));

                    if (facingRight)
                    {
                        flip();
                        facingRight = false;
                    }
                } else if (Gdx.input.isKeyPressed(Keys.D) && moveOK)
                {
                    step();
                    playPos.setX(playPos.getX() + ((275 * Gdx.graphics.getDeltaTime()) * (float)playerMS));

                    if (!facingRight)
                    {
                        flip();
                        facingRight = true;
                    }
                } else if (moveOK)
                {
                    avatar.set(standing);
                }
                ///////////////////////////////////////////////////////////////
                ////////////////////////  END INPUT  //////////////////////////
                ///////////////////////////////////////////////////////////////



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
            if(!enemy.isAttacking() && enemy.isAlive())
            {
                if(enemy.playerInRange())
                    enemy.startAttacking();
                else
                {
                      enemy.step();
                }
            }
            else if(enemy.isAlive())
            {
                enemy.attack();
            }
            else
            {
                boolean check = enemy.playDead();
                if (check)
                {
                    score = score + enemy.getValue();
                    enemies.removeValue(enemy, false);
                    badOnScreen --;
                }
            }
        }
    }




    //Project 3
    public void spawnEnemy()
    {
        int temp = random(0,1);
        int side = random(0,1);
        int sideX;

        spawned++;
        badOnScreen++;
        if(spawned >= maxEnemies)
            spawnOK = false;

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

        //TURN ON BY MAKING switch(temp) not switch(1);
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