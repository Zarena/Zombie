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


public class ZombieArena implements ApplicationListener
{
	BitmapFont font;

	//Pause Variables
	private enum State
	{
		PAUSE, RESUME;
	}
	private State state;


	//Enemies
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
	private int score;
	private String yourScoreIs;


	@Override
	public void create ()
	{
		background = new Texture(Gdx.files.internal("bg1.png"));


		//Score
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


	public void slam()
	{
		if (TimeUtils.nanoTime() - slamUpdate > 45000000)
		{
			slamUpdate = TimeUtils.nanoTime();
			avatar.set(vSlamSprite[slamCounter]);
			slamCounter++;

			if(slamCounter == 9)
			{
				slamCounter = 0;
				slamming = false;
				moveOK = true;
			}
		}
	}


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


	@Override
	public void render ()
	{
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


				//Draw Score
				font.draw(batch, yourScoreIs, screenX - (float)(yourScoreIs.length() * 8.75), screenY - 20);


				//Draw and update hammer if hammer was fired
				if (flying && firedRight)
				{
					batch.draw(hammer, hammerX, hammerY);
					hammerX += 600 * Gdx.graphics.getDeltaTime();
					hammerY += 600 * Gdx.graphics.getDeltaTime();
				} else if (flying)
				{
					batch.draw(hammer, hammerX, hammerY);
					hammerX -= 600 * Gdx.graphics.getDeltaTime();
					hammerY += 600 * Gdx.graphics.getDeltaTime();
				}

				for(Enemy enemy:  enemies)
				{
					batch.draw(enemy.avatar, enemy.x, enemy.y);
				}

				batch.end();

				if(TimeUtils.nanoTime() - lastEnemy > 1000000000)
					spawnEnemy();


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


				//End Slashing Animation after set time
		/*
		if(slashing && TimeUtils.nanoTime() - lastUpdate > 125000000)
		{
			moveOK = true;
			slashing = false;
		}
		*/


				//End throwing/hammer animation and allow movement after hammer has been fired
/*		if(!slashing && !slamming && !dodging && TimeUtils.nanoTime() - hammerUpdate > 100000000)
		{
				moveOK = true;
		}
*/

				if (hammerX < 1 - 156) flying = false;
				if (hammerX > screenX) flying = false;
				if (hammerY > screenY) flying = false;


				//// PAUSE
				if (Gdx.input.isKeyJustPressed(Keys.P))
					state = State.PAUSE;


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


	public void spawnEnemy()
	{
		Ground g = new Ground();
		g.x = 0;
		g.y = playPos.y + 25;
		enemies.add(g);
		lastEnemy = TimeUtils.nanoTime();
	}


	public static int random(int min, int max)
	{
		int range = Math.abs(max - min) + 1;
		return (int)(Math.random() * range) + (min <= max ? min : max);
	}


	@Override
	public void dispose()
	{

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
