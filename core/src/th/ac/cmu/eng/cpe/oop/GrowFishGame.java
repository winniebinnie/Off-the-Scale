package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

	private Array<Fish> raindrops;
	private long lastDropTime;

	private int countRaindrops = 0;
	private long diff = 1000000000;
	private int spawn = MathUtils.random(0,720-64);
	private Array<Fish> raindrops2;
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

		raindrops = new Array<Fish>();
		raindrops2  = new Array<Fish>();


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
			spawnRaindrop();
//			if (MathUtils.random(0,1) < 0.5){
//				spawnRaindrop();
//			} else{
//				spawnRaindrop1();
//			}
		}
		for (Iterator<Fish> iter = raindrops.iterator(); iter.hasNext(); ) {
			Fish raindrop = iter.next();
			raindrop.getRectangle().x += raindrop.getDirection() * Gdx.graphics.getDeltaTime();
//			if(raindrop.getDirection() == -200){
//				raindrop.getRectangle().x += raindrop.getDirection() * Gdx.graphics.getDeltaTime();
//			}
//			if(raindrop.getDirection() == 200){
//				raindrop.getRectangle().x += raindrop.getDirection() * Gdx.graphics.getDeltaTime();
//			}
//			raindrop.y = 2;
			//	raindrop.y += MathUtils.random(-200, 0) * Gdx.graphics.getDeltaTime();
			if(raindrop.getRectangle().y + 64 < 0)
				iter.remove();
			if(raindrop.getRectangle().overlaps(bucket.getRectangle())) {
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
		for(Fish raindrop: raindrops) {
			batch.draw(dropImage, raindrop.getRectangle().x, raindrop.getRectangle().y);
		}
		for(Fish raindrop2: raindrops2) {
			batch.draw(dropImage, raindrop2.getRectangle().x, raindrop2.getRectangle().y);
		}
		batch.draw(bucket.getTexture(), bucket.getRectangle().x, bucket.getRectangle().y);
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
	private void spawnRaindrop1() {
		spawn = MathUtils.random(0,720-64);
		Fish raindrop = new Fish(0,spawn,64,64,"droplet.png", 200);
		raindrops2.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
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