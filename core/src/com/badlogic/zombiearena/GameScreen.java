package com.badlogic.zombiearena;



import java.util.Iterator;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


//DECLARE
public class GameScreen implements Screen
{
    //Survival Bonus Timer
    float lastHit;
    int winCounter;
    float lastWin;



    //Tutorial Text
    String tut1, tut2;


    private Sprite[] winning;



    //Items
    Array<Item> items;
    int shield;
    boolean isStanding;
    Texture shieldT;
    Sprite shieldSprite;



    //More Sounds
    private Sound death, fball, victory, broke, growl;
    private Sound[] injury;
    private Sound[] pickup;
    private Music bgMusic;



    //Skeleton Grab
    int grabbed;



    private Sprite paused;


    //Health Bars
    private Texture[] eHP;
    private Texture[] pHP;
    private int playerHealth;
    private boolean vuln;
    private float vulnTimer;


    //Shop Variables
    private Texture shopFrame;
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
    private Sound hammerSlam;
    private Sound hammerSwing1;
    private Sound hammerThrow;
    private Sound enemyHiss;
    private Sound dodge;



    //Fireballs
    //maxFB is the maximum number of fireballs allowed in the screen at a single time. This is to prevent an overload of
    //projectiles making the game too difficult.
    //fbOnScreen tracks the number of fireballs currently on the screen
    private Array<Fireball> fireballs;
    private int maxFB;
    private int fbOnScreen;




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
    private int totalScore;
    private String yourScoreIs;
    private String yourTotalIs;
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




    //INITIALIZE
    public GameScreen(final ZombieArena gam)
    {
        //Survival Bonus timer
        lastHit = TimeUtils.nanoTime();
        winCounter=0;
        lastWin=TimeUtils.nanoTime();



        Texture winningT = new Texture(Gdx.files.internal("winning.png"));
        winning = new Sprite[16];
        for(int i=0; i<16; i++)
        {
            winning[i] = new Sprite(winningT, (i*72),0, 72, 72);
        }



        tut1 = "Use 'A' and 'D' to move left and right respectively. Attack the Zombie by pressing the left or right arrow key!";
        tut2 = "To attack dragons press the up arrow key to throw your hammer. You can attack burrowing skeletons by pressing the down arrow key.";


        this.game = gam;
        background = new Texture(Gdx.files.internal("bg1.png"));



        Texture pausedT = new Texture(Gdx.files.internal("paused.png"));
        paused = new Sprite(pausedT);



        growl = Gdx.audio.newSound(Gdx.files.internal("EnemyGrowl.mp3"));
        broke = Gdx.audio.newSound(Gdx.files.internal("s_broke.wav"));
        death = Gdx.audio.newSound(Gdx.files.internal("s_death.wav"));
        fball = Gdx.audio.newSound(Gdx.files.internal("s_fball.wav"));
        victory = Gdx.audio.newSound(Gdx.files.internal("s_victory.wav"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bgmusic.mp3"));


        injury = new Sound[2];
        injury[0] = Gdx.audio.newSound(Gdx.files.internal("s_ooh.wav"));
        injury[1] = Gdx.audio.newSound(Gdx.files.internal("s_ow.wav"));


        pickup = new Sound[3];
        pickup[0] = Gdx.audio.newSound(Gdx.files.internal("s_mine.wav"));
        pickup[1] = Gdx.audio.newSound(Gdx.files.internal("s_takethat.wav"));
        pickup[2] = Gdx.audio.newSound(Gdx.files.internal("s_thanks.wav"));



        shieldT = new Texture(Gdx.files.internal("buff_hp.png"));
        shieldSprite = new Sprite(shieldT);



        eHP = new Texture[20];
        pHP = new Texture[20];
        playerHealth = 1;
        vuln = true;
        vulnTimer = TimeUtils.nanoTime();

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



        dodgeCD = TimeUtils.nanoTime();







        items = new Array<Item>();
        shield = 1;
        isStanding = true;


        grabbed = 0;



        eHP = new Texture[20];
        pHP = new Texture[20];
        playerHealth = 100;
        vuln = true;
        vulnTimer = TimeUtils.nanoTime();

        //Initialize Health Bars
        for(int i=0; i<20; i++)
        {
            eHP[i] = new Texture(Gdx.files.internal("e"+(i+1)*5)+".png");
            pHP[i] = new Texture(Gdx.files.internal("p"+(i+1)*5)+".png");
        }




        shopFrame = new Texture(Gdx.files.internal("shop.png"));



        dodgeCD = TimeUtils.nanoTime();



        //Configure Player Starting Stats
        playerStr = 4;
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
        totalScore =0;
        yourScoreIs = "Points:  " + score;
        yourTotalIs = "Score:  " + totalScore;




        //Pause State
        state = State.RESUME;



        //Project 3
        //Initialize sounds
        hammerSlam = Gdx.audio.newSound(Gdx.files.internal("HammerSlam.mp3"));
        hammerSwing1 = Gdx.audio.newSound(Gdx.files.internal("HammerSwing1.mp3"));
        hammerThrow = Gdx.audio.newSound(Gdx.files.internal("HammerThrow.mp3"));
        enemyHiss = Gdx.audio.newSound(Gdx.files.internal("EnemyHiss.mp3"));
        dodge = Gdx.audio.newSound(Gdx.files.internal("HeroDodge.mp3"));



        //Fireballs
        fireballs = new Array<Fireball>();
        fbOnScreen =0;
        maxFB = 1;



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
            {
                long id = hammerThrow.play();
                hammerThrow.setVolume(id, (float).15);
            }



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
            {
                long id =  hammerSlam.play();
                hammerSlam.setVolume(id, (float).01);
            }

            if(slamCounter >= 3)
            {
               slamDamage();
            }



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
        if (TimeUtils.nanoTime() - slashUpdate > (40000000 / playerAS))
        {
            slashUpdate = TimeUtils.nanoTime();
            avatar.set(attackSprite[slashCounter]);



            if(!halfSwing)
            {
                slashCounter++;
            }



            if(slashCounter == 6 || slashCounter ==7)
            {
                slashDamage();
            }



            if(slashCounter==2 && !halfSwing)
            {
                long id = hammerSwing1.play();
                hammerSwing1.setVolume(id, (float).01);
            }



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




    private void slashDamage()
    {
       for(Enemy e:enemies)
       {
            if (e.type == 1)
            {
                if (facingRight && e.x > playPos.getX())
                {
                    float temp = (e.x - contactP);
                    if (temp < 0 && temp > -122)
                    {
                        if(e.isAlive())
                        {
                            int damage = random(playerStr, playerStr + (playerStr/10)) * ((TimeUtils.nanoTime() - lastHit > 2000000000)? 2:1);
                            e.takeDamage(damage); //cast Int

                            int x = random(140, 160);
                            float y = (float)(random(90, 100));
                            e.bump(x, y);
                        }
                    }
                } else
                {
                    float temp = (contactP - (e.x + 100));
                    if (temp < 0 && temp > -122)
                    {
                        if(e.isAlive())
                        {
                            int damage = random(playerStr, playerStr + (playerStr/10)) * ((TimeUtils.nanoTime() - lastHit > 2000000000)? 2:1);
                            e.takeDamage(damage); //cast Int

                            int x = random(140, 160);
                            float y = (float)(random(90, 100));
                            e.bump(x,y);
                        }

                    }
                }
            }
        }
    }









    public void hammerHit()
    {
        for(Enemy e : enemies)
        {
            if(e.type == 2)
            {
                float tempY = e.y - hammerY;
                if(tempY < 104 && tempY > -32)
                {
                    if(hammerX < e.x)
                    {
                        if(e.x - hammerX <= 156)
                        {
                            e.takeDamage(1);
                        }
                    }
                    else
                    {
                        if(hammerX - e.x <= 188)
                        {
                            e.takeDamage(1);
                        }

                    }
                }
            }
        }
    }





    @Override
    public void render(float delta)
    {

        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
                System.exit(0);





        switch (state)
        {
            case PAUSE:
                batch.begin();

                camera.update();
                batch.setProjectionMatrix((camera.combined));

                batch.draw(paused, 0, 0);

                batch.end();

                if (Gdx.input.isKeyJustPressed(Keys.P))
                    state = State.RESUME;
                break;
            case RESUME:


                Gdx.gl.glClearColor(0, 0, 0.35f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



                camera.update();



                batch.setProjectionMatrix(camera.combined);


                if(TimeUtils.nanoTime() - lastWin > 45000000)
                {
                    lastWin = TimeUtils.nanoTime();
                    winCounter ++;
                }

                if(winCounter == 16)
                    winCounter =0;




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


                //fffffffe - default color
                batch.begin();

                batch.draw(background, 0, 0);

                if(round == 1)
                {
                    font.setColor(Color.WHITE);
                    font.draw(batch, tut1, 300, 15);
                }
                if(round == 2)
                {
                    font.setColor(Color.WHITE);
                    font.draw(batch, tut2, 300, 15);
                }
                if(round == 3)
                {
                    font.setColor(Color.WHITE);
                    font.draw(batch, "Don't forget to spend points on upgrades between rounds! Do so by clicking the colored buttons labled \"Damage\", \"Atk Speed\" and \"Agility\"",300, 15);
                }


                for(Item i : items)
                {
                    batch.draw(i.image, i.x, i.y);


                    if(i.up && i.y < 100)
                        i.y +=2.0;
                    else if (i.y >= 100)
                        i.up = false;


                    if(!i.up && i.y > 50)
                        i.y -= 2.0;
                    else if(i.y <= 50)
                        i.up = true;

                    if(((i.x >= playPos.x && i.x <= playPos.x +220) || (i.x+96 <= playPos.x + 220 && i.x+96 >= playPos.x)) && (i.y <= 260))
                    {
                        switch(i.type)
                        {
                            case 1:
                                playerAS += 0.05;
                                break;
                            case 2:
                                playerStr += 2;
                                break;
                            case 3:
                                playerHealth += 65;
                                if(playerHealth > 100)
                                    playerHealth = 100;
                                break;
                            case 4:
                                shield ++;
                                break;
                            default:
                                playerMS += 0.05;
                                break;
                        }
                        items.removeValue(i, false);
                        pickup[random(0,2)].play();
                    }
                }



                //Draw player sprite
                if(!vuln)
                {
                    batch.setColor(Color.LIGHT_GRAY);
                    batch.draw(avatar,playPos.getX(), playPos.getY());
                    batch.setColor(Color.WHITE);
                }
                else
                    batch.draw(avatar, playPos.getX(), playPos.getY());


                if(shield >0 && isStanding)
                {
                    if(facingRight)
                    {
                        batch.draw(shieldSprite, playPos.x, playPos.y + 67, 80, 80);
                    }
                    else
                    {
                        batch.draw(shieldSprite, playPos.x+210, playPos.y + 67, 80, 80);
                    }
                }




                //Draw Enemies
                for(Enemy enemy:  enemies)
                {
                    batch.draw(enemy.getAvatar(), enemy.x, enemy.y);
                }

                for(Fireball fb : fireballs)
                {
                    batch.draw(fb.skin, fb.x, fb.y);
                }




                //Draw Score
                font.setColor(Color.WHITE);
                font.draw(batch, yourScoreIs, screenX - (float)(yourScoreIs.length() * 8.75), screenY - 20);

                font.setColor(Color.WHITE);
                font.draw(batch, yourTotalIs, screenX - (float)(yourTotalIs.length() * 8.75), screenY - 40);


                font.setColor(Color.FOREST);
                font.draw(batch, currentRound, 5, screenY-20);

                font.setColor(Color.FIREBRICK);
                font.draw(batch, "Strength:  " + playerStr, 5, screenY - 40);

                font.setColor(Color.GREEN);
                font.draw(batch, "Attack Speed:  " +(int)(100 * playerAS)+"%", 5, screenY-60);

                font.setColor(Color.BLUE);
                font.draw(batch, "Movement Speed:  " + (int)(100 * playerMS)+"%", 5, screenY-80);

                font.setColor(Color.BROWN);
                font.draw(batch, "Shields:  " + shield, 5, screenY-100);







                font.setColor(Color.FOREST);
                font.draw(batch, currentRound, 5, screenY-20);





                //Draw and update hammer if hammer was fired
                if (flying)
                {
                    if (firedRight)
                    {
                        batch.draw(hammer, hammerX + 140, hammerY + 110);
                        hammerX += (600 * Gdx.graphics.getDeltaTime());
                        hammerY += (600 * Gdx.graphics.getDeltaTime());
                    } else
                    {
                        batch.draw(hammer, hammerX - 80, hammerY + 120);
                        hammerX -= (600 * Gdx.graphics.getDeltaTime());
                        hammerY += (600 * Gdx.graphics.getDeltaTime());
                    }

                    //check for hammer collisions
                    hammerHit();
                }



                for(Enemy enemy:  enemies)
                {
                    batch.draw(enemy.avatar, enemy.x, enemy.y);
                }


                roundCheck();




                    //DRAW HEALTHBARS
                    for(Enemy enemy: enemies)
                    {
                        if(enemy.type==1)
                        {
                            int val = round5(enemy.hp, (round*2));

                            switch(val)
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

                switch(playerHealth)
                {
                    case 5:
                        batch.draw(pHP[0], playPos.x + 75, playPos.y+220);

                        break;

                    case 10:
                        batch.draw(pHP[1], playPos.x + 75, playPos.y+220);

                        break;

                    case 15:
                        batch.draw(pHP[2], playPos.x + 75, playPos.y+220);

                        break;

                    case 20:
                        batch.draw(pHP[3], playPos.x + 75, playPos.y+220);

                        break;

                    case 25:
                        batch.draw(pHP[4], playPos.x + 75, playPos.y+220);

                        break;

                    case 30:
                        batch.draw(pHP[5], playPos.x + 75, playPos.y+220);

                        break;

                    case 35:
                        batch.draw(pHP[6], playPos.x + 75, playPos.y+220);

                        break;

                    case 40:
                        batch.draw(pHP[7], playPos.x + 75, playPos.y+220);

                        break;

                    case 45:
                        batch.draw(pHP[8], playPos.x + 75, playPos.y+220);

                        break;

                    case 50:
                        batch.draw(pHP[9], playPos.x + 75, playPos.y+220);

                        break;

                    case 55:
                        batch.draw(pHP[10], playPos.x + 75, playPos.y+220);

                        break;

                    case 60:
                        batch.draw(pHP[11], playPos.x + 75, playPos.y+220);

                        break;

                    case 65:
                        batch.draw(pHP[12], playPos.x + 75, playPos.y+220);

                        break;

                    case 70:
                        batch.draw(pHP[13], playPos.x + 75, playPos.y+220);

                        break;

                    case 75:
                        batch.draw(pHP[14], playPos.x + 75, playPos.y+220);

                        break;

                    case 80:
                        batch.draw(pHP[15], playPos.x + 75, playPos.y+220);

                        break;

                    case 85:
                        batch.draw(pHP[16], playPos.x + 75, playPos.y+220);

                        break;

                    case 90:
                        batch.draw(pHP[17], playPos.x + 75, playPos.y+220);

                        break;

                    case 95:
                        batch.draw(pHP[18], playPos.x + 75, playPos.y+220);
                        break;

                    case 100:
                        batch.draw(pHP[19], playPos.x + 75, playPos.y+220);
                        break;
                    }



                if(TimeUtils.nanoTime() - lastHit > 2000000000)
                    batch.draw(winning[winCounter], playPos.x+80,playPos.y+235);


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
                        playPos.setX(playPos.getX() + 1000 * Gdx.graphics.getDeltaTime());
                    else
                        playPos.setX(playPos.getX() - 1000 * Gdx.graphics.getDeltaTime());
                }


                getHit();
                if(!vuln)
                    checkVuln();


                if (playerHealth <= 0)
                {
                    death.play();
                    game.setScreen(new gameOverScreen(game, totalScore, round));
                    dispose();
                    break;
                }



                ////////////////////////////////////////////////////////////
                ///////////////////////// INPUT ////////////////////////////
                ////////////////////////////////////////////////////////////



                //////////////  SPACE - DODGE  ////////////////////////////
                if (grabbed == 0 && Gdx.input.isKeyJustPressed(Keys.SPACE) && roundInProg && moveOK && !dodging && (TimeUtils.nanoTime() - dodgeCD >= (1500000000 / playerMS)))
                {
                    isStanding = false;
                    dodgeCD = TimeUtils.nanoTime();
                    long id = dodge.play();
                    dodge.setVolume(id, (float).05);
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
                        yourScoreIs = "Points:  " + score;

                    }



                    if(touchPos.x >= (341 + 136) && touchPos.x <= (341 + 136 + 100) && touchPos.y >= (374) && touchPos.y <= (413) && score >= asPrice)
                    {
                        score = score - asPrice;
                        asPrice = asPrice + 5;
                        playerAS = playerAS + 0.05;
                        yourScoreIs = "Points:  " + score;
                    }



                    if(touchPos.x >= (477) && touchPos.x <= (574) && touchPos.y >= (286) && touchPos.y <= (324) && score >= msPrice)
                    {
                        score = score - msPrice;
                        msPrice = msPrice + 5;
                        playerMS = playerMS + 0.05;
                        yourScoreIs = "Points:  " + score;
                    }
                }





                ///////////////  UP  ////////////////////
                if (Gdx.input.isKeyJustPressed(Keys.UP) && moveOK && !flying)
                {
                    isStanding = false;
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
                    isStanding = false;
                    slamming = true;
                    moveOK = false;
                    lastUpdate = TimeUtils.nanoTime();
                }



                ///////////////////  RIGHT  /////////////////////
                //Code for slashing to the right
                if (Gdx.input.isKeyJustPressed(Keys.RIGHT) && moveOK)
                {
                    isStanding = false;

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

                    isStanding = false;

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
                {
                    state = State.PAUSE;
                }



                //////////////////////  A & D  ///////////////////////////////
                //// Move LEFT and RIGHT and STAND
                if (Gdx.input.isKeyPressed(Keys.A) && moveOK)
                {
                    if(grabbed == 0)
                    {
                        isStanding = false;
                        step();

                        playPos.setX(playPos.getX() - ((275 * Gdx.graphics.getDeltaTime()) * (float)playerMS));
                    }

                    if (facingRight)
                    {
                        flip();
                        facingRight = false;
                    }
                } else if (Gdx.input.isKeyPressed(Keys.D) && moveOK)
                {
                    if(grabbed == 0)
                    {
                        isStanding = false;
                        step();
                        playPos.setX(playPos.getX() + ((275 * Gdx.graphics.getDeltaTime()) * (float)playerMS));
                    }

                    if (!facingRight)
                    {
                        flip();
                        facingRight = true;
                    }
                } else if (moveOK)
                {
                    isStanding = true;
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
                        if(!enemy.bump)
                            enemy.facePlayer(playPos.getX() + 110);
                    break;
                case 2:
                    if (enemy.x > 1366 - 188)
                        enemy.facePlayer(0);
                    if (enemy.x < 0)
                        enemy.facePlayer(1366);
                    break;
                case 3:
                        enemy.facePlayer(playPos.getX() + 110);
                    break;
                default:
                    break;
            }

            if(enemy.type != 3)
                enemy.step();
            else
            {
                    boolean inRange = false;

                    if(!enemy.counted)
                    {
                        float pX = playPos.x;
                        float eX;


                        if(enemy.isFacingRight())
                            eX = enemy.x + 141 - 13;
                        else
                            eX = enemy.x + 13;

                        if(facingRight)
                        {

                            if(eX >= playPos.x + 125 && eX <= playPos.x + 166)
                                inRange = true;
                            if(eX >= pX + 48 && eX <= pX + 91)
                                inRange = true;
                        }
                        else
                        {

                            if(eX <= pX + 280 -48 && eX >= pX + 280 - 91)
                                inRange = true;
                            if(eX <= pX + 280 - 125 && eX >= pX + 280 - 166)
                                inRange = true;
                        }
                    }
                    else
                        inRange = true;

                    if(inRange)
                        enemy.grabbing = true;
                    else if(enemy.grabbing)
                    {
                        enemy.grabbing = false;
                        if(enemy.counted)
                        {
                            grabbed --;
                            enemy.counted = false;
                        }

                    }

                    if(enemy.grabbing)
                    {
                        if(!enemy.counted && enemy.grab())
                            grabbed ++;
                    }
                    else
                        enemy.step();
                }



            if(enemy.isAlive())
            {
                if(enemy.type==2)
                    if(TimeUtils.nanoTime() - enemy.lastAttack > 2147483647 && fbOnScreen <= maxFB && enemy.x + 88 > 0 && enemy.x < 1266)
                    {
                        enemy.lastAttack = TimeUtils.nanoTime();
                        fireballs.add(enemy.attack());
                        fball.stop();
                        fball.play();
                        fbOnScreen ++;
                    }
            }
            else
            {
                boolean check = enemy.playDead();
                if (check)
                {
                    int bonus = (enemy.getValue() * ((TimeUtils.nanoTime() - lastHit > 2000000000) ? 2:1));
                    score = score + bonus;
                    totalScore += bonus;

                    //Project 3
                    yourScoreIs = "Points:  " + score;
                    yourTotalIs = "Score:  " + totalScore;


                    spawnItem(enemy.x, enemy.y);
                    enemies.removeValue(enemy, false);
                    badOnScreen --;
                    if(spawned == maxEnemies && badOnScreen == 0)
                        victory.play();
                }
            }
        }

        for(Fireball fb : fireballs)
        {
            fb.step();
        }
    }


    private void checkVuln()
    {
        if(TimeUtils.nanoTime() - vulnTimer > 1000000000)
            vuln = true;
    }




    public void getHit()
    {
        for (Enemy enemy : enemies)
        {
            if (enemy.type == 1)
            {
                if (facingRight)
                {
                    if (enemy.x < playPos.x)
                    {
                        if (enemy.x + 100 >= playPos.x)
                        {
                            if(!dodging && vuln)
                             damagePlayer(1);

                           int x = random(140, 160); 
                            float y = (float)(random(90, 100));
                            enemy.bump(x,y);
                        }
                    } else
                    {
                        if (playPos.x + 164 >= enemy.x)
                        {
                            if(!dodging && vuln)
                                 damagePlayer(1);

                           int x = random(140, 160); 
                            float y = (float)(random(90, 100));
                            enemy.bump(x,y);
                        }
                    }
                } else
                {
                    if (enemy.x < playPos.x)
                    {
                        if (enemy.x + 100 >= playPos.x + 112)
                        {
                            if(!dodging && vuln)
                            damagePlayer(1);

                           int x = random(140, 160); 
                            float y = (float)(random(90, 100));
                            enemy.bump(x,y);
                        }
                    } else
                    {
                        if (playPos.x + 280 >= enemy.x)
                        {
                            if(!dodging && vuln)
                            damagePlayer(1);

                           int x = random(140, 160); 
                            float y = (float)(random(90, 100));
                            enemy.bump(x,y);
                        }
                    }
                }
            }
        }

        Iterator<Fireball> it = fireballs.iterator();
        while(it.hasNext())
        {
            boolean rem =false;
            Fireball fb = it.next();
            if(fb.y+96 < 0)
                rem=true;
            if(fb.x+96 < 0)
                rem=true;
            if(fb.x > 1366)
                rem=true;

            if(isStanding  && !dodging && vuln)
            {
                if((fb.y <= playPos.y + 126) && (fb.x >= playPos.x+10) && (fb.x <= playPos.x + 150))
                {
                    rem=true;
                    damagePlayer(2);
                }
            }
            else if(!isStanding && !dodging && vuln)
            {
                if((fb.y <= playPos.y + 177) && (fb.y >= 0) && (fb.x >= playPos.x+100) && (fb.x <= playPos.x + 167))
                {
                    rem=true;
                    damagePlayer(2);
                }

            }
            if(rem)
            {
                fbOnScreen --;
                it.remove();
            }
        }
    }






    //Deal damage to the player
    //Called by getHit()
    //The parameter dType is an integer that indicated which type of damage is being dealt.
    //Zombie damage is called with a 1
    //Fireball damage is called with a 2
    private void damagePlayer(int dType)
    {
        lastHit = TimeUtils.nanoTime();
        int damage;

        switch(dType)
        {
            case 2:
                damage = random(3,5) * 5;
                vuln = false;
                vulnTimer = TimeUtils.nanoTime();
                break;

            default:
                damage = random(1, 4) * 5;
                vuln = false;
                vulnTimer = TimeUtils.nanoTime();
                break;
        }

        if(shield > 0)
        {
            long id = broke.play();
            broke.setVolume(id, 1);
            shield --;
        }
        else
        {
            playerHealth -= damage;
            if(playerHealth > 0)
            {
                int x = random(0,1);
                long id = injury[x].play();
                injury[x].setVolume(id, (float)1);

            }
        }

    }







    //Project 3
    public void spawnEnemy()
    {
        int temp = random(1,6);
        if(round == 1)
            temp = 3;

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
            case 1:
            case 2:
            case 3:
                Ground g = new Ground(round);
                g.x = sideX;
                g.y = playPos.y + 25;
                enemies.add(g);
                lastEnemy = TimeUtils.nanoTime();
                {
                    long id = enemyHiss.play();
                    enemyHiss.setVolume(id, (float).05);
                }
                break;

            case 4:
            case 5:
                Air a = new Air(round);
                a.x = sideX;
                a.y = playPos.y + random(350, screenY - 40 - 136);
                enemies.add(a);
                lastEnemy = TimeUtils.nanoTime();
                {
                    long id = growl.play();
                    enemyHiss.setVolume(id, (float).05);
                }
                break;


            case 6:
                Under u = new Under(round);
                u.x = sideX;
                u.y = playPos.y + 25;
                enemies.add(u);
                lastEnemy = TimeUtils.nanoTime();
                break;

            default:
                break;
        }
    }





    public int round5(int current, int max)
    {
        if(current <= 0)
            return 0;

        float a = (float) current;
        float b = (float) max;

        double val1 = (a / b);

        double val2 = val1*100;

        double val3 = Math.round(val2);

        double val4 = val3;


        int retval = (int)val3;



        if((int)val2 != 100 && (int)val2 % 5 != 0)
        {
            while (val4 > 5)
                val4 -= 5;

            switch ((int) val4)
            {
                case 3:
                case 4:
                    retval = (int) (val3 + 5 - val4);
                    break;
                default:
                    retval = (int) (val3 - val4);
            }
        }
        return retval;
    }



    private void spawnItem(float inx, float iny)
    {

        if(random(1,10) >= 8)
        {
            Item i = new Item(random(1,5), inx, iny);
            items.add(i);
        }
    }








    //RoundChecker
    public void roundCheck()
    {
        if(badOnScreen == 0 && spawned == maxEnemies && roundInProg)
        {
            roundInProg = false;
            roundFinished = "Round " + round + " Complete!";
            round++;
            maxFB = maxEnemies / 4 +1;
        }
    }





    //To be Used later
    public static int random(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }





    private void slamDamage()
    {
        for(Enemy e: enemies)
        {
            if(e.type==1 && (e.x >= playPos.x - 50 && e.x <= playPos.x + 270) && e.kb <= 3)
            {
                e.hp --;
                e.kb ++;
                int x = random(100, 120);
                float y = (float)(random(80, 90));
                e.bump(x, y);
            }

            if(e.type == 3)
            {
                if(facingRight)
                {
                    if(e.x <= playPos.x + 276 && e.x >= playPos.x)
                    {
                        if(e.counted)
                            grabbed --;

                        badOnScreen --;
                        int bonus = (e.getValue() * ((TimeUtils.nanoTime() - lastHit > 2000000000) ? 2:1));
                        score += bonus;
                        totalScore += bonus;
                        spawnItem(e.x, e.y);
                        enemies.removeValue(e, false);
                        if(spawned == maxEnemies && badOnScreen == 0)
                            victory.play();
                    }
                }
                else
                {
                    if(e.x + 141 >= playPos.x + 10 && e.x + 141 <=  playPos.x + 276)
                    {
                        if(e.counted)
                            grabbed --;
                        badOnScreen --;
                        int bonus = (e.getValue() * ((TimeUtils.nanoTime() - lastHit > 2000000000) ? 2:1));
                        score += bonus;
                        totalScore += bonus;
                        spawnItem(e.x, e.y);
                        enemies.removeValue(e, false);
                        if(spawned == maxEnemies && badOnScreen == 0)
                            victory.play();
                    }
                }
            }
        }
    }





    @Override
    public void show()
    {
        //start the playback of the background music when screen is shown
        bgMusic.setLooping(true);
        bgMusic.play();
        bgMusic.setVolume((float)0.05);
    }



    @Override
    public void hide()
    {

    }



    @Override
    public void dispose()
    {
        throwStrip.dispose();
        hammerT.dispose();
        batch.dispose();
        vSlamTexture.dispose();
        attackSheet.dispose();
        avatarTexture.dispose();
        dodgeTexture.dispose();
        background.dispose();
        dodge.dispose();
        enemyHiss.dispose();
        hammerThrow.dispose();
        hammerSwing1.dispose();
        hammerSlam.dispose();
        shopFrame.dispose();
        for(int i=0; i<20; i++)
        {
            eHP[i].dispose();
            pHP[i].dispose();
        }
        bgMusic.dispose();
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
    
    
 /*   public void bumpValue()
    {
       int x = random(140, 160); 
        float y = (float)(random(90, 100));
        enemy.bump(x,y);
    }
    */
    
    
}
