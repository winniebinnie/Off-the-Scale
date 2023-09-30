package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class OffTheScale extends ApplicationAdapter {
	private Texture dropImage;
	private Sound eatingSound;
	private Music underwaterAmbienece;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private PlayerFish playerFish;
	private Array<Fish> raindrops;
	private long lastDropTime;
	private int spawn;

	private int score;
	private String yourScoreName;
	BitmapFont myBitMap;


	@Override
	public void create () {
		batch = new SpriteBatch();

		playerFish = new PlayerFish(368, 20, 180, 123, "fish2.png");

		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		eatingSound = Gdx.audio.newSound(Gdx.files.internal("eating.mp3"));
		underwaterAmbienece = Gdx.audio.newMusic(Gdx.files.internal("underwater.mp3"));
		underwaterAmbienece.setLooping(true);
		underwaterAmbienece.play();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		raindrops = new Array<Fish>();


		score = 0;
		yourScoreName = "score: 0";
		myBitMap = new BitmapFont();

	}

	@Override
	public void render () {
		boolean isFacingLeft = playerFish.isFacingLeft();

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playerFish.getRectangle().x -= 500 * Gdx.graphics.getDeltaTime();
			if (!isFacingLeft) {
				playerFish.getSprite().flip(true, false);
				playerFish.setFacingLeft(true);  // Update facing direction
				System.out.println("Facing Left");
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playerFish.getRectangle().x += 500 * Gdx.graphics.getDeltaTime();
			if (isFacingLeft) {
				playerFish.getSprite().flip(true, false);
				playerFish.setFacingLeft(false);  // Update facing direction
				System.out.println("Facing Right");
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerFish.getRectangle().y += 500 * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			playerFish.getRectangle().y -= 500 * Gdx.graphics.getDeltaTime();
		}



		if (playerFish.getRectangle().x < 0)
			playerFish.getRectangle().x = 0;
		if (playerFish.getRectangle().x > 1280 - playerFish.getRectangle().width)
			playerFish.getRectangle().x = 1280 - playerFish.getRectangle().width;
		if (playerFish.getRectangle().y < 0)
			playerFish.getRectangle().y = 0;
		if (playerFish.getRectangle().y > 720 - playerFish.getRectangle().height)
			playerFish.getRectangle().y = 720 - playerFish.getRectangle().height;


		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) {
			spawnRaindrop();
		}
		for (Iterator<Fish> iter = raindrops.iterator(); iter.hasNext(); ) {
			Fish raindrop = iter.next();
			raindrop.getRectangle().x += raindrop.getDirection() * Gdx.graphics.getDeltaTime();
			if(raindrop.getRectangle().y + 64 < 0)
				iter.remove();
			if(raindrop.getRectangle().overlaps(playerFish.getRectangle())) {
				eatingSound.play();
				score++;
				playerFish.increaseSize(20,20);
				yourScoreName = "score: " + score;
				iter.remove();
			}
		}
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Fish raindrop: raindrops) {
			batch.draw(dropImage, raindrop.getRectangle().x, raindrop.getRectangle().y);
		}
		batch.draw(playerFish.getSprite(), playerFish.getRectangle().x, playerFish.getRectangle().y,
				playerFish.getRectangle().width, playerFish.getRectangle().height);

		myBitMap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		myBitMap.draw(batch, yourScoreName, 25, 100);
		myBitMap.getData().setScale(3);
		batch.end();
	}

	private void spawnRaindrop() {

		spawn = MathUtils.random(0,720-64);
		Fish raindrop;
		if (MathUtils.random(0,1) < 0.5)
			raindrop = new Fish(1280,spawn,64,64,"droplet.png", -200);
		else
			raindrop = new Fish(0,spawn,64,64,"droplet.png", 200);
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void dispose () {
//		img.dispose();
		dropImage.dispose();
		playerFish.getTexture().dispose();
		eatingSound.dispose();
		underwaterAmbienece.dispose();
		batch.dispose();
	}
}