package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;

import java.util.Iterator;

public class OffTheScale extends ApplicationAdapter {
	private Texture redFish;
	private Texture yellowFish;
	private Texture pinkFish;
	private Texture skyblueFish;
	private Array<Texture> textures;
	private Sound eatingSound;
	private Music underwaterAmbience;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private PlayerFish playerFish;
	private Array<Fish> fishes;
	private long lastSpawnTime;
	private int spawn;
	private int score;
	private String fishEaten;
	private BitmapFont myBitmap;
	private int gameOver = 0;  // Flag to track if the game is over

	private Texture backgroundTexture;
	private Texture farBackgroundTexture;
	private Texture foregroundBackgroundTexture;
	private Texture sandBackgroundTexture;
	private Sprite backgroundSprite;

	private Sprite deadPlayerSprite;
	private Texture deadPlayerTexture;

	private boolean gameStarted = false;  // Flag to track if the game has started


	@Override
	public void create() {
		batch = new SpriteBatch();
		gameStarted = false; // Initially, the game has not started

		backgroundTexture = new Texture(Gdx.files.internal("backgroundOcean.jpg"));
		farBackgroundTexture = new Texture(Gdx.files.internal("far.png"));
		foregroundBackgroundTexture = new Texture(Gdx.files.internal("foregound-merged.png"));
		sandBackgroundTexture = new Texture(Gdx.files.internal("sand.png"));

		backgroundSprite = new Sprite(backgroundTexture);

		playerFish = new PlayerFish(720, 20, 100, 100, "fish2.png");

//		deadPlayerTexture = new Texture(Gdx.files.internal("deadFish.png"));
		deadPlayerTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
		deadPlayerSprite = new Sprite(deadPlayerTexture);

		redFish = new Texture(Gdx.files.internal("redFish.png"));
		yellowFish = new Texture(Gdx.files.internal("yellowFish.png"));
		pinkFish = new Texture(Gdx.files.internal("pinkFish.png"));
		skyblueFish = new Texture(Gdx.files.internal("skyblueFish.png"));

		eatingSound = Gdx.audio.newSound(Gdx.files.internal("eating.mp3"));
		underwaterAmbience = Gdx.audio.newMusic(Gdx.files.internal("underwater.mp3"));
		underwaterAmbience.setLooping(true);
		underwaterAmbience.play();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		fishes = new Array<Fish>();

		score = 0;
		fishEaten = "score: 0";
		myBitmap = new BitmapFont();
	}

	@Override
	public void render() {
		if (!gameStarted) {
			renderStartScreen();
		} else {
			updateGameState();
			renderGame();
		}
	}

	private void renderStartScreen() {
		// Render the start screen
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.gl.glClearColor(62 / 255f, 121 / 255f, 221 / 255f, 1); // Convert color values to the range [0, 1]
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.draw(farBackgroundTexture, 0, -150, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(sandBackgroundTexture, 0, -200, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		batch.draw(foregroundBackgroundTexture, 0, 0, 1400, 450);

		// You can add text or graphics for your start screen here
		myBitmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		myBitmap.getData().setScale(3);
		myBitmap.draw(batch, "Press any key to start", 400, 360);

		batch.end();

		// Check for user input to start the game
		if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			gameStarted = true;  // Start the game when any key is pressed
		}
	}

	private void updateGameState() {
		if (gameOver == 0) {
			// Update game state logic here
			// This includes updating fish positions, checking collisions

			boolean isFacingLeft = playerFish.isFacingLeft();

			// Player Movement Controls
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				playerFish.getRectangle().x -= 500 * Gdx.graphics.getDeltaTime();
				if (!isFacingLeft) {
					playerFish.getSprite().flip(true, false);
					playerFish.setFacingLeft(true);  // Update facing direction
				}
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				playerFish.getRectangle().x += 500 * Gdx.graphics.getDeltaTime();
				if (isFacingLeft) {
					playerFish.getSprite().flip(true, false);
					playerFish.setFacingLeft(false);  // Update facing direction
				}
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				playerFish.getRectangle().y += 500 * Gdx.graphics.getDeltaTime();
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				playerFish.getRectangle().y -= 500 * Gdx.graphics.getDeltaTime();
			}
			// PlayerFish boundary
			if (playerFish.getRectangle().x < 0)
				playerFish.getRectangle().x = 0;
			if (playerFish.getRectangle().x > 1280 - playerFish.getRectangle().width)
				playerFish.getRectangle().x = 1280 - playerFish.getRectangle().width;
			if (playerFish.getRectangle().y < 0)
				playerFish.getRectangle().y = 0;
			if (playerFish.getRectangle().y > 720 - playerFish.getRectangle().height)
				playerFish.getRectangle().y = 720 - playerFish.getRectangle().height;

			if (TimeUtils.nanoTime() - lastSpawnTime > 1000000000) {
				spawnFish();
			}

			for (Iterator<Fish> iter = fishes.iterator(); iter.hasNext();) {
				Fish objFish = iter.next();
				objFish.getRectangle().x += objFish.getDirection() * Gdx.graphics.getDeltaTime();
				if (objFish.getRectangle().x + 100 < 0 || objFish.getRectangle().x > 1280)
					iter.remove();

				// If playerFish rectangle overlaps with fish
				// playerFish becomes larger and removes that fish
				if (objFish.getRectangle().overlaps(playerFish.getRectangle())) {

					if(objFish.getRectangle().width * objFish.getRectangle().height <
							playerFish.getRectangle().width * playerFish.getRectangle().height) {
						eatingSound.play();
						score++;
						playerFish.increaseSize(5, 5);
						fishEaten = "score: " + score;
						iter.remove();
					}
					else {
						gameOver = 2;
					}
				}



			}

			if (score >= 25) {
				gameOver = 1;
			}
		}
	}

	private void renderGame() {
		// Render the game based on the current state (whether it's game play or game over)

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
//		batch.draw(backgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(62 / 255f, 121 / 255f, 221 / 255f, 1); // Convert color values to the range [0, 1]
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.draw(farBackgroundTexture, 0, -150, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(sandBackgroundTexture, 0, -200, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		batch.draw(foregroundBackgroundTexture, 0, 0, 1400, 450);

		for (Fish objFish : fishes) {
			if (objFish.getDirection() == -200 && !objFish.isFacingLeft()) {
				objFish.getSprite().flip(true, false);
				objFish.setFacingLeft(true);
			}
			batch.draw(objFish.getSprite(), objFish.getRectangle().x, objFish.getRectangle().y, objFish.getRectangle().width, objFish.getRectangle().height);
		}

		batch.draw(playerFish.getSprite(), playerFish.getRectangle().x, playerFish.getRectangle().y,
				playerFish.getRectangle().width, playerFish.getRectangle().height);

		myBitmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		myBitmap.draw(batch, fishEaten, 25, 100);
		myBitmap.getData().setScale(3);

		if (gameOver == 1) {
			// Render the game over screen
//			batch.draw(backgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // PLACE a game overScreen
			myBitmap.draw(batch, "Game Over. Your final score is: " + score, 320, 360);

		} else if(gameOver == 2)
		{
			myBitmap.draw(batch, "Game Over. Your final score is: " + score, 320, 360);
			// DeadFish
			batch.draw(deadPlayerSprite, playerFish.getRectangle().x, playerFish.getRectangle().y,
					playerFish.getRectangle().width, playerFish.getRectangle().height);
		}

		batch.end();
	}

	private void spawnFish() {
		spawn = MathUtils.random(0, 720 - 64);
		Fish objFish;
		Texture tempTexture;
		float size;
		float randomNumber = MathUtils.random(0, 100);
		if (randomNumber <= 50) {
			tempTexture = pinkFish;
			size = 1;
		}else if(randomNumber <= 75 && randomNumber > 50){
			tempTexture = yellowFish;
			size = 1;
		}else if(randomNumber <= 90 && randomNumber > 75){
			tempTexture = redFish;
			size = 2;
		} else {
			tempTexture = skyblueFish;
			size = 3;
		}


		if (MathUtils.random(0, 1) < 0.5)
			objFish = new Fish(1280, spawn, 64, 64, tempTexture, -200, size);
		else
			objFish = new Fish(-100, spawn, 64, 64, tempTexture, 200, size) ;
		fishes.add(objFish);
		lastSpawnTime = TimeUtils.nanoTime();
	}

	@Override
	public void dispose() {
		backgroundTexture.dispose();
		redFish.dispose();
		yellowFish.dispose();
		playerFish.getTexture().dispose();
		eatingSound.dispose();
		underwaterAmbience.dispose();
		batch.dispose();
	}
}
