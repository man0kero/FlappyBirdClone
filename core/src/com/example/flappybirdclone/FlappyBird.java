package com.example.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;

	Texture[] bird;

	int birdStateFlag = 0;
	float flyHeight;
	float fallingSpeed = 10;
	int gameStateFlag = 0;

	Texture topTube;
	Texture bottomTube;
	int space = 500;
	Random random;
	int tubeSpeed = 5;
	int tubesNumber = 150;
	float tubeX[] = new float[tubesNumber];
	float tubeShift[] = new float[tubesNumber];
	float distance;

	Circle birdCircle;
	//ShapeRenderer shapeRenderer;

	int gameScore = 0;

	Rectangle[] topRec;
	Rectangle[] bottomRec;

	int pasteTube = 0;


	BitmapFont scoreFont;

	Texture gameOver;
	Texture retry;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		topRec = new Rectangle[tubesNumber];
		bottomRec = new Rectangle[tubesNumber];

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");

		topTube = new Texture("top_tube.png");
		bottomTube = new Texture("bottom_tube.png");
		random = new Random();
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(10);

		gameOver = new Texture("game_over.png");
		retry = new Texture("retry.png");

		distance = Gdx.graphics.getWidth() / 2;

		init();

		birdCircle = new Circle();
	}

	public void init() {
		flyHeight = Gdx.graphics.getHeight()/2 - bird[0].getHeight()/2;

		for (int i = 0; i< tubesNumber; i++) {
			tubeX[i] = Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() - topTube.getWidth() / 2 + i*distance*1.2F;
			tubeShift[i] = (random.nextFloat() - 0.5f) *
					(Gdx.graphics.getHeight() - space - 500);
			topRec[i] = new Rectangle();
			bottomRec[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameStateFlag == 1) {

			Gdx.app.log("Game Score", String.valueOf(gameScore));

			if (tubeX[pasteTube] < Gdx.graphics.getWidth() / 2) {
				gameScore++;

				if (pasteTube <tubesNumber -1) {
					pasteTube++;
				} else {
					pasteTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				fallingSpeed = -20;
			}

			for (int i = 0; i< tubesNumber; i++) {

				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] = tubesNumber * distance;
				} else {
					tubeX[i] = tubeX[i] - tubeSpeed;
				}

				batch.draw(topTube, tubeX[i],
						Gdx.graphics.getHeight()/2 + space/2 + tubeShift[i]);

				batch.draw(bottomTube, tubeX[i],
						Gdx.graphics.getHeight()/2 - space/2 - bottomTube.getHeight() + tubeShift[i]);
				topRec[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight()/2 + space/2 + tubeShift[i], topTube.getWidth(), topTube.getHeight());
				bottomRec[i] =
						new Rectangle(tubeX[i],
								Gdx.graphics.getHeight()/2 - space/2 - bottomTube.getHeight() +
										tubeShift[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (flyHeight > 0) {
				fallingSpeed++;
				flyHeight -= fallingSpeed;
			} else {
				gameStateFlag =2;
			}


		} else if (gameStateFlag == 0) {
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Tap","Oops!");
				gameStateFlag = 1;
			}
		} else if (gameStateFlag ==2) {
			batch.draw(gameOver, Gdx.graphics.getWidth() /2 -gameOver.getWidth() /2,
					1300);
			batch.draw(retry, Gdx.graphics.getWidth() /2 -retry.getWidth() /2,
					200);
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Tap","Oops!");
				gameStateFlag = 1;
				init();
				gameScore = 0;
				pasteTube = 0;
				fallingSpeed = 0;
			}

		}




		if (birdStateFlag == 0) {
			birdStateFlag = 1;
		} else {
			birdStateFlag = 0;
		}

		batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth()/2 - bird[birdStateFlag].getWidth()/2,
				flyHeight);
		scoreFont.draw(batch, String.valueOf(gameScore), Gdx.graphics.getWidth()/2 - scoreFont.getXHeight()/2, 2100);
		batch.end();


		birdCircle.set(Gdx.graphics.getWidth()/2, flyHeight + bird[birdStateFlag].getHeight() / 2,
				bird[birdStateFlag].getWidth() / 2 );

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.CYAN);
		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);*/

		for (int i = 0; i< tubesNumber; i++) {
			/*shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight()/2 + space/2 + tubeShift[i], topTube.getWidth(), topTube.getHeight());
			shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight()/2 - space/2 - bottomTube.getHeight() +
							tubeShift[i], bottomTube.getWidth(), bottomTube.getHeight());*/

			if(Intersector.overlaps(birdCircle, topRec[i]) || Intersector.overlaps(birdCircle, bottomRec[i]) ) {
				Gdx.app.log("Inter", "Bumb");
				gameStateFlag =2;
			}
		}
		//shapeRenderer.end();
	}
}
