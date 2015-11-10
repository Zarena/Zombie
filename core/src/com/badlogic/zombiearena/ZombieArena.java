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

public class ZombieArena implements ApplicationListener
{
	private static int screenX=1366;
	private static int screenY=768;

	private boolean facingRight, moveOK, slashing, shooting, stomping, firedRight, dodging;

	private Sprite standing;
	private Sprite[] walk;
	private int walkCounter;
	private Sprite playerChar;
	private Texture playerT;
	private Sprite playerS;
	private Texture attackSheet;
	private Sprite[] attackSprite;

	private Rectangle playPos;

	private Texture slashSheet;
	private Sprite slash;
	private Sprite stomp;

	private Texture arrowT;
	private Sprite arrow, arrowL, arrowR;
	float arrowX, arrowY;

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private float lastUpdate, arrowUpdate, lastEnemy;

	private int slashCounter;

	@Override
	public void create ()
	{
		slashCounter=0;


		//playerT is a sprite sheet for the  character texture
		playerT = new Texture(Gdx.files.internal("viking.png"));
		playerS = new Sprite(playerT);


		//AttackSheet is a sprite sheet for the "slash" animations
		attackSheet = new Texture(Gdx.files.internal("vAttack.png"));
		attackSprite = new Sprite[12];
		for (int i=0; i<12; i++)
		{
			attackSprite[i] = new Sprite(attackSheet, (0 + (i * 344)), (0 + (i * 210)), 344, 210);
		}


		//Initialize arrow
		arrowT = new Texture(Gdx.files.internal("arrow.png"));
		arrowL = new Sprite(arrowT);
		arrowL.flip(true, false);
		arrowR = new Sprite(arrowT);
		arrow = new Sprite(arrowT);


		//Booleans for tracking facing, whether or not movement is allowed, whether
		//or not the player is currently attacking
		facingRight = true;
		moveOK = true;
		slashing = false;
		shooting = false;
		stomping = false;
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
			walk[i] = new Sprite(playerT, (440 + (i*220)), 0, 220, 222);
		}
		walkCounter = 0;


		//Setup slash animations
		slashSheet = new Texture(Gdx.files.internal("slash.png"));
		slash = new Sprite(slashSheet, 0, 228, 132, (312-212));


		//Initialize stomp animation
		stomp = new Sprite(slashSheet, 36, 588, (156-36), (684-588));


		//Setup basic character (standing)
		playerChar = new Sprite(playerT, 0 , 0, 280, 222);
		standing = new Sprite(playerT, 0 , 0, 280, 222);


		//Tracking animation times and enemy spawn times
		lastUpdate = TimeUtils.nanoTime();
		arrowUpdate = lastUpdate;
		lastEnemy = lastUpdate;


		//Cam variables
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenX, screenY);
	}


	//Method for flipping sprites/textures
	public void flip()
	{
		playerChar.flip(true, false);
		standing.flip(true, false);
		slash.flip(true, false);
		stomp.flip(true, false);

		if(!shooting)
		{
			arrow.flip(true, false);
		}

		for(int i=0; i<9; i++)
		{
			walk[i].flip(true, false);
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
			playerChar.set(walk[walkCounter]);
		}
	}

	public void slash()
	{
		if (TimeUtils.nanoTime() - lastUpdate > 90000000)
		{
			lastUpdate = TimeUtils.nanoTime();
			slashCounter++;
			if(slashCounter == 12)
			{
				slashCounter = 0;
				slashing = false;
				moveOK = true;
			}
			playerChar.set(attackSprite[slashCounter]);
		}
	}


	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0, 0, 0.35f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		camera.update();


		batch.setProjectionMatrix(camera.combined);


		batch.begin();

		//Draw player sprite
		batch.draw(playerChar, playPos.getX(), playPos.getY());


		//Draw slash animation if slashing
		if(slashing && facingRight)
		{
			batch.draw(slash, playPos.getX()+ 8, playPos.getY() -16 );
		}
		else if(slashing)
		{
			batch.draw(slash, playPos.getX() - 96, playPos.getY() - 16);
		}



		//Draw and update arrow if arrow was fired
		if(shooting && firedRight)
		{
			batch.draw(arrow, arrowX, arrowY);
			arrowX += 400 * Gdx.graphics.getDeltaTime();
			arrowY += 400 * Gdx.graphics.getDeltaTime();
		}
		else if(shooting)
		{
			batch.draw(arrow, arrowX, arrowY);
			arrowX -= 400 * Gdx.graphics.getDeltaTime();
			arrowY += 400 * Gdx.graphics.getDeltaTime();
		}


		//Draw stomp animation if stomping
		if(stomping && facingRight)
		{
			batch.draw(stomp, playPos.getX()+35, playPos.getY()-20, 50, 50);
		}
		else if (stomping)
		{
			batch.draw(stomp, playPos.getX() - 35, playPos.getY()-20, 50, 50);
		}

		batch.end();


		if(dodging && TimeUtils.nanoTime() - lastUpdate > 500000000)
		{
			playerChar.set(standing);
			moveOK=true;
			dodging=false;
		}


		if(Gdx.input.isKeyPressed(Keys.SPACE) && moveOK && !dodging)
		{
			dodging = true;
			moveOK=false;
			lastUpdate = TimeUtils.nanoTime();
			playerChar.set(playerS);
		}

		if(dodging)
		{
			if(facingRight)
				playPos.setX(playPos.getX() + 400 * Gdx.graphics.getDeltaTime());
			else
				playPos.setX(playPos.getX() - 400 * Gdx.graphics.getDeltaTime());
		}


		//If up is pressed, code for firing bow
		if(Gdx.input.isKeyPressed(Keys.UP) && moveOK && !shooting)
		{
			firedRight = facingRight;
			if(firedRight)
				arrow.set(arrowR);
			else
				arrow.set(arrowL);

			shooting=true;
			moveOK=false;
			arrowX = playPos.getX();
			arrowY = playPos.getY();
			arrowUpdate = TimeUtils.nanoTime();
		}


		//Code for stomp attack if down is pressed
		if(Gdx.input.isKeyPressed(Keys.DOWN) && moveOK)
		{
			moveOK=false;
			stomping=true;
			lastUpdate = TimeUtils.nanoTime();
		}


		//Code for slashing to the right
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && moveOK)
		{

			if(!facingRight)
			{
				flip();
				facingRight = true;
			}

			slashing = true;
			moveOK = false;
			lastUpdate = TimeUtils.nanoTime();
		}



		//code for slashing to the left
		if(Gdx.input.isKeyPressed(Keys.LEFT) && moveOK)
		{

			if(facingRight)
			{
				flip();
				facingRight = false;
			}

			slashing = true;
			moveOK = false;
			lastUpdate = TimeUtils.nanoTime();
		}

		if(slashing)
		{
			slash();
		}


		//End Slashing Animation after set time
		if(slashing && TimeUtils.nanoTime() - lastUpdate > 125000000)
		{
		//	moveOK = true;
		//	slashing = false;
		}


		//End stomping animation
		if(stomping && TimeUtils.nanoTime() - lastUpdate > 125000000)
		{
			moveOK=true;
			stomping = false;
		}


		//End shooting/arrow animation and allow movement after arrow has been fired
		if(!slashing && !stomping && !dodging && TimeUtils.nanoTime() - arrowUpdate > 100000000)
		{
				moveOK = true;
		}

		if(arrowX < 1) shooting=false;
		if(arrowX > screenX - 65) shooting=false;
		if(arrowY > screenY) shooting=false;



		//// Move LEFT and RIGHT and STAND
		if(Gdx.input.isKeyPressed(Keys.A) && moveOK)
		{
			step();

			playPos.setX(playPos.getX() - 275 * Gdx.graphics.getDeltaTime());

			if(facingRight)
			{
				flip();
				facingRight = false;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.D) && moveOK)
		{
			step();
			playPos.setX(playPos.getX() + 275 * Gdx.graphics.getDeltaTime());

			if(!facingRight)
			{
				flip();
				facingRight = true;
			}
		}
		else if(moveOK)
		{
			playerChar.set(standing);
		}

		if(playPos.getX() < 0) playPos.setX(0);
		if(playPos.getX() > screenX - 220) playPos.setX(screenX - 220);


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
