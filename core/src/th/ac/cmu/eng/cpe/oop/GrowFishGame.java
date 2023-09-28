package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GrowFishGame extends ApplicationAdapter {
	Texture img;
	private Texture dropImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Bucket bucket;

	private Array<Rectangle> raindrops;
	private long lastDropTime;

	private int countRaindrops = 0;
	private long diff = 1000000000;
	private int spawn = MathUtils.random(0,720-64);
	private Array<Rectangle> raindrops2;
	private int direction = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");

		bucket = new Bucket(368, 20, 180, 123, "fish2.png");

		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);
		rainMusic.play();
		//TODO MAKE width and height a vairable
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		raindrops = new Array<Rectangle>();
		raindrops2  = new Array<Rectangle>();
		spawnRaindrop();


	}

	@Override
	public void render () {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
			bucket.getRectangle().x -= 500 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
			bucket.getRectangle().x += 500 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.UP) )
			bucket.getRectangle().y += 500 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) )
			bucket.getRectangle().y -= 500 * Gdx.graphics.getDeltaTime();


		if(bucket.getRectangle().x < 0)
			bucket.getRectangle().x = 0;
		if(bucket.getRectangle().x > 1280 - 64)
			bucket.getRectangle().x = 1280 - 64;
		if(bucket.getRectangle().y < 0)
			bucket.getRectangle().y = 0;
		if(bucket.getRectangle().y > 720 - 64)
			bucket.getRectangle().y = 720 - 64;


		if(TimeUtils.nanoTime() - lastDropTime > diff) {
//			int rand =
			if (MathUtils.random(0,1) < 0.5){
				spawnRaindrop();
			} else{
				spawnRaindrop1();
			}
		}
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			if(direction == -200){
				raindrop.x += direction * Gdx.graphics.getDeltaTime();
			}
			if(direction == 200){
				raindrop.x += direction * Gdx.graphics.getDeltaTime();
			}
//			raindrop.y = 2;
			//	raindrop.y += MathUtils.random(-200, 0) * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0)
				iter.remove();
			if(raindrop.overlaps(bucket.getRectangle())) {
				dropSound.play();
				countRaindrops++;
				System.out.println("P1: "+countRaindrops);
				iter.remove();
			}
		}
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		for(Rectangle raindrop2: raindrops2) {
			batch.draw(dropImage, raindrop2.x, raindrop2.y);
		}
		batch.draw(bucket.getTexture(), bucket.getRectangle().x, bucket.getRectangle().y);
		batch.end();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		spawn = MathUtils.random(0,720-64);
		raindrop.x = 1280;
		raindrop.y = spawn;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
		direction = -200;
	}
	private void spawnRaindrop1() {
		Rectangle raindrop = new Rectangle();
		spawn = MathUtils.random(0,720-64);
		raindrop.x = 0;
		raindrop.y = spawn;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
		direction = 200;
	}

	@Override
	public void dispose () {
//		img.dispose();
		dropImage.dispose();
		bucket.getTexture().dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}