package th.ac.cmu.eng.cpe.oop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class PlayerFish {
    private final Rectangle rectangle;
    private final Texture texture;
    private Sprite sprite;
    private boolean isFacingLeft;

    public PlayerFish(int x, int y, int width, int height, String path) {
        this.rectangle = new Rectangle();
        this.rectangle.x = x;
        this.rectangle.y = y;
        this.rectangle.width = width;
        this.rectangle.height = height;
        this.texture = new Texture(Gdx.files.internal(path));
        this.sprite = new Sprite(this.texture);
        this.isFacingLeft = false;
    }
    public Rectangle getRectangle() {
        return this.rectangle;
    }
    public Texture getTexture() {
        return this.texture;
    }
    public Sprite getSprite() { return this.sprite; }
    public boolean isFacingLeft() { return isFacingLeft; }
    public void setFacingLeft(boolean facingLeft) { isFacingLeft = facingLeft; }

    public void increaseSize(double widthIncrement, double heightIncrement){
        this.rectangle.width += widthIncrement;
        this.rectangle.height += heightIncrement;
        this.sprite.setSize(this.rectangle.width, this.rectangle.height);
    }
}