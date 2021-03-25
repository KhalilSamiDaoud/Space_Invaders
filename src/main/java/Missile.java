import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Missles are the simplest displayable objects in the game.
 * 
 * They are implemented with this class and are drawn as white-filled rectangles
 * of dimension 2-by-10 pixels
 * 
 * @author Kenneth Sheppard
 * @version 11-24-19
 */
public class Missile extends Drawable {

    /**
     * Create a new Missile object.
     * 
     * @param x
     *            The initial x position of the missile
     * @param y
     *            The initial y position of the missile
     */
    public Missile(int x, int y) {
        setX(x);
        setY(y);
        setWidth(2);
        setHeight(10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(getX(), getY(), getWidth(), getHeight());
    }

}
