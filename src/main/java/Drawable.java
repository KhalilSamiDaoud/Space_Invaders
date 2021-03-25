import java.awt.Graphics;

/**
 * Objects drawn on the panel (except the score) are subclasses of this.
 * 
 * This class defines fields keeping the x and y position of the object, as well
 * as its width and height. This class defines getter and setter methods, and
 * abstract method for getting painted given a Graphics object, and concrete
 * methods for moving the object a number of pixels in a certain direction
 * (either up, down, left, or right).
 * 
 * @author Kenneth Sheppard
 * @version 11-24-19
 */
public abstract class Drawable {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private int x, y, width, height;

    /**
     * Get the current value of x.
     * 
     * @return The value of x for this object.
     */
    public int getX() {
        return x;
    }

    /**
     * Set the value of x for this object.
     * 
     * @param x
     *            The new value for x.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the current value of y.
     * 
     * @return The value of y for this object.
     */
    public int getY() {
        return y;
    }

    /**
     * Set the value of y for this object.
     * 
     * @param y
     *            The new value for y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the current value of width.
     * 
     * @return The value of width for this object.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the value of width for this object.
     * 
     * @param width
     *            The new value for width.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get the current value of height.
     * 
     * @return The value of height for this object.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the value of height for this object.
     * 
     * @param height
     *            The new value for height.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Abstract method for drawing the object to be implemented
     * 
     * @param g
     *            The Graphics object to be passed in
     */
    public abstract void draw(Graphics g);

    /**
     * Move the object a given number of pixels in a Direction
     * 
     * @param dir
     *            The Direction to move the object as specified by the
     *            enumerated type
     * 
     * @param dis
     *            the number of pixels the object should be moved
     */
    public void move(Direction dir, int dis) {
        switch (dir) {
            case UP:
                setY(getY() - dis);
                break;
            case DOWN:
                setY(getY() + dis);
                break;
            case LEFT:
                setX(getX() - dis);
                break;
            case RIGHT:
                setX(getX() + dis);
                break;
        }
    }

}
