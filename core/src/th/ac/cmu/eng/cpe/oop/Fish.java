package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Fish {
    private final Rectangle rectangle;
    private final Texture texture;
    private final int direction;

    public Fish(int x, int y, int width, int height, String path, int d) {
        this.rectangle = new Rectangle();
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;
        this.texture = new Texture(Gdx.files.internal(path));
        this.direction = d;
    }
    public Rectangle getRectangle() {
        return this.rectangle;
    }
    public Texture getTexture() {
        return this.texture;
    }
    public int getDirection() { return this.direction; }

}