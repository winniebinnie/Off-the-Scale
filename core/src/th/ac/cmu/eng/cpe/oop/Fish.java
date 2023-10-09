package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Fish {
    private final Rectangle rectangle;
    private final Texture texture;
    private final int direction;
    private Sprite sprite;
    private boolean isFacingLeft;

    public Fish(int x, int y, int width, int height, Texture texture, int d, float size) {
        this.rectangle = new Rectangle();
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width * size;
        this.rectangle.height = height * size;
        this.texture = texture;
        this.direction = d;
        this.sprite = new Sprite(this.texture); // Depending on the fish size input from parameter
        this.isFacingLeft = false;

    }
    public Rectangle getRectangle() {
        return this.rectangle;
    }
    public Texture getTexture() {
        return this.texture;
    }
    public int getDirection() { return this.direction; }
    public Sprite getSprite() { return this.sprite; }
    public boolean isFacingLeft() { return isFacingLeft; }
    public void setFacingLeft(boolean facingLeft) { isFacingLeft = facingLeft; }

}